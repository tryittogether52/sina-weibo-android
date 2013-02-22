package com.example.decmpcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.liao189.yiliao.ui.record.Aac2Wav;
import cn.liao189.yiliao.ui.record.AacEncoder;
import cn.liao189.yiliao.ui.record.AmrEncoder;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// 音频获取源  
    private int audioSource = MediaRecorder.AudioSource.MIC;
	
	// 缓冲区字节大小  
    private int bufferSizeInBytes = 0; 
 // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025  
    private static int sampleRateInHz = 44100;  
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道  
    private static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;  
 // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。  
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT; 
    
    private AudioRecord audioRecord; 
    
    private boolean isRecord = false;// 设置正在录制的状态
    private boolean isTestRecord = false;
    
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	
	private String AudioName;
	private String WavName;
	
	private String TestAudioName;
	private String TestAacName;
	private String TestWavName;
	private int testTime = 30;
	private TextView m_testText;
	private long testpcm2aactime = 0;
	private boolean testpcm2aac = false;
	
	AacEncoder mAacEncoder;
	
	private TextView m_textview;
	
	private MediaPlayer m_MediaPlayer;
	private boolean m_bIsPlayMusic = false;
	
	private Handler m_handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
//				if(m_bIsPlayMusic)
//					((Button)findViewById(R.id.music)).setText("stop");
//				else
//					((Button)findViewById(R.id.music)).setText("play");
				m_testText.setText("剩余 : " + testTime + " 秒");
				break;
				
			case 1:
				stopTestRecord();
				break;
				
			case 2:
				testobj obj = (testobj) msg.obj;
				StringBuffer strb = new StringBuffer();
				strb.append(m_testText.getText() + "\n");
				strb.append("生成文件 " + TestAacName + " 文件大小: " + obj.size + " M" + "\n" + " 耗时: " + obj.l + " 毫秒");
		        m_testText.setText(strb);
				break;
			}
		}
	};
	
	public void clear(View view){
		File dir = new File(SDCARD_PATH +"/DemoPcm/");
		if(dir.exists()){
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				File file = new File(dir,children[i]);
				file.delete();
			}
			dir.delete();
		}
		m_textview.setText("");
		m_testText.setText("");
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    
    private void init(){
    	m_textview = (TextView) findViewById(R.id.audioname);
    	m_testText = (TextView) findViewById(R.id.testText);
    	
    	
    	 bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,  
                 channelConfig, audioFormat);  
         // 创建AudioRecord对象  
         audioRecord = new AudioRecord(audioSource, sampleRateInHz,  
                 channelConfig, audioFormat, bufferSizeInBytes);  
         
         File dir = new File(SDCARD_PATH +"/DemoPcm/");
         if(!dir.exists())
        	 dir.mkdirs();
         
         
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void start(View view){
    	int nId = view.getId();
    	if(nId == R.id.testpcm2aac2){
    		testpcm2aactime = 0;
    		testpcm2aac = true;
    	}
    	if(!isRecord)
    	{
    		if(audioRecord == null)
    	         audioRecord = new AudioRecord(audioSource, sampleRateInHz,  
    	                 channelConfig, audioFormat, bufferSizeInBytes); 
	    	if (audioRecord != null) {
		    	audioRecord.startRecording();  
		        // 让录制状态为true  
		        isRecord = true;  
		        // 开启音频文件写入线程  
		        new Thread(new AudioRecordThread()).start(); 
	    	}
	    	if(testpcm2aac){
	    		((Button)findViewById(R.id.testpcm2aac2)).setText("停止录音");
	    	}
	    	else
	    		((Button)findViewById(R.id.start)).setText("停止录音");
	    }
    	else{
        	if (audioRecord != null) {  
                isRecord = false;//停止文件写入  
                audioRecord.stop();  
                audioRecord.release();//释放资源  
                audioRecord = null;
            }
        	if(testpcm2aac){
        		m_testText.setText("生成文件 " + AudioName + "\n" + "耗时: " + testpcm2aactime + " 毫秒");
        		((Button)findViewById(R.id.testpcm2aac2)).setText("边录边转aac");
	    		testpcm2aactime = 0;
	    		testpcm2aac = false;
	    	}
        	else{
        		m_textview.setText("生成文件 " + AudioName);
        		((Button)findViewById(R.id.start)).setText("开始录音");
        	}
    	}
    }
    
    public void play(View view){
    	String playName = "";
    	String playText = "";
    	String stopText = "";
    	
    	int nId = view.getId();
    	switch(nId){
    	case R.id.music: //aac
    		playName = AudioName;
    		playText = "播放aac";
    		stopText = "停止aac";
    		break;
    	case R.id.wav: //wav
    		playName = WavName;
    		playText = "播放wav";
    		stopText = "停止wav";
    		break;
    	case R.id.testaac:
    		playName = TestAacName;
    		playText = "播放aac";
    		stopText = "停止aac";
    		break;
    	case R.id.testwav:
    		playName = TestWavName;
    		playText = "播放aac";
    		stopText = "停止aac";
    		break;
    	}
    	if(playName == null || playName.equals("")){
    		Toast t = Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG);
    		t.show();
    		return;
    	}
    	if(!m_bIsPlayMusic){
    		m_MediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(playName)));
    		if(m_MediaPlayer == null)
    			return;
    		m_MediaPlayer.setLooping(true);
    		m_MediaPlayer.start();
    		m_bIsPlayMusic = true;
    		((Button)findViewById(nId)).setText(stopText);
    	}
    	else{
    		stopMusic();
    		m_bIsPlayMusic = false;
    		((Button)findViewById(nId)).setText(playText);
    	}
    	
//    	Message m = m_handler.obtainMessage(0);
//    	m_handler.sendMessage(m);
    }
    
    private void stopMusic(){
		if (m_MediaPlayer != null) {
			m_MediaPlayer.stop();
			m_MediaPlayer.release();
			m_MediaPlayer = null;
		}
    }
    
    

    public void aac2wav(View view){
    	Aac2Wav aac2wav = Aac2Wav.getSingleInstance();
    	aac2wav.setInfile(AudioName);
    	int ret = aac2wav.convert();
    	if(ret == 0){
    		 WavName = aac2wav.getOufile();
    		 StringBuffer strb = new StringBuffer();
    		 strb.append(m_textview.getText().toString());
    		 strb.append("\n");
    		 strb.append("生成文件 " + WavName);
    		 m_textview.setText(strb);
    	}
    }
    
    private void pcm2aac(){
    	mAacEncoder = AacEncoder.getSingleInstance();
    	mAacEncoder.reset();
    	mAacEncoder.init(sampleRateInHz,channelConfig,audioFormat);
    	
    	byte[] audiodata = new byte[bufferSizeInBytes];  
    	byte[] output = new byte[bufferSizeInBytes];
        FileOutputStream fos = null;  
        int readsize = 0; 
        AudioName = SDCARD_PATH +"/DemoPcm/"+ System.currentTimeMillis() +".aac";
        File file = new File(AudioName);  
        if (file.exists()) {  
            file.delete(); 
        }
        try {
			fos = new FileOutputStream(file);// 建立一个可存取字节的文件  
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if(testpcm2aac)
        {
        	long l;
	        while (isRecord == true) {
	            readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);  
	            if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
	            	l = System.currentTimeMillis();
	            	output = mAacEncoder.encode(audiodata);
	            	testpcm2aactime += System.currentTimeMillis() - l;
	                try {  
	                    fos.write(output);
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
        }else{
        	while (isRecord == true) {
                readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);  
                if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
                	output = mAacEncoder.encode(audiodata);
                    try {  
                        fos.write(output);
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }
        try {  
            fos.close();// 关闭写入流  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        mAacEncoder.cleanup();
    }
    
    class AudioRecordThread implements Runnable {  
        @Override  
        public void run() {
//            writeDateTOFile();//往文件中写入裸数据  
//            copyWaveFile(AudioName, NewAudioName);//给裸数据加上头文件  
        	pcm2aac();
        }  
    }
    
    
    public void record(View view){
    	if(!isTestRecord)
    	{
    		String time = ((TextView)findViewById(R.id.num)).getText().toString();
    		if(time == null || time.equals("")){
    			testTime = 30;
    		}else{
    			testTime = Integer.parseInt(time);
    		}
    		
    		new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(testTime > 0){
						Message m = m_handler.obtainMessage(0);
						m_handler.sendMessage(m);
						--testTime;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Message m = m_handler.obtainMessage(0);
					m.what = 1;
					m_handler.sendMessage(m);
				}
			}).start();
    			
	    	if (audioRecord != null) {
		    	audioRecord.startRecording();  
		        // 让录制状态为true  
		    	isTestRecord = true;  
		        // 开启音频文件写入线程  
		        new Thread(new Runnable() {
					@Override
					public void run() {
						writeDateTOFile();
					}
				}).start();
	    	}
	    	((Button)findViewById(R.id.recordbtn)).setText("停止录音");
    	}
    	else{
    		testTime = 0;
    		stopTestRecord();
    		((Button)findViewById(R.id.start)).setText("开始录音");
    	}
    }
    
    public void stopTestRecord(){
    	if (audioRecord != null) {  
    		isTestRecord = false;//停止文件写入  
            audioRecord.stop();  
            audioRecord.release();//释放资源  
            audioRecord = null;  
            File file = new File(TestAudioName);
            float size = file.length()/1024/1024f;
            m_testText.setText("生成文件 " + TestAudioName + " 文件大小: " + size + " M");
        }
    }
    
    public void testpcm2aac(View view){
    	new Thread(new Runnable() {
			@Override
			public void run() {
				byte[] audiodata = new byte[bufferSizeInBytes];  
		    	byte[] output = new byte[bufferSizeInBytes];
		        FileOutputStream fos = null;  
		        int readsize = 0;
		        
				int index = TestAudioName.lastIndexOf(".");
				TestAacName = TestAudioName.substring(0, index) + ".aac";
		        File testaacfile = new File(TestAacName);  
		    	
		    	long l = System.currentTimeMillis();
		    	mAacEncoder = AacEncoder.getSingleInstance();
		    	mAacEncoder.reset();
		    	mAacEncoder.init(sampleRateInHz,channelConfig,audioFormat);

		        try {
					fos = new FileOutputStream(testaacfile);// 建立一个可存取字节的文件  
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		        File testfile = new File(TestAudioName);
		        FileInputStream fin = null;
				try {
					fin = new FileInputStream(testfile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {  
			        while ((readsize = fin.read(audiodata)) != -1) {
			        	output = mAacEncoder.encode(audiodata);
			            fos.write(output);
			        }
		        fos.close();// 关闭写入流  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
		        mAacEncoder.cleanup();
		        l = System.currentTimeMillis() - l;
		        float size = testaacfile.length()/1024/1024f;
		        Message m = m_handler.obtainMessage(2);
		        m.obj = new testobj(size, l);
		        m_handler.sendMessage(m);
			}
		}).start();
    	
    	
    }
    
    class testobj{
    	float size;
    	long l;
    	public testobj(float size, long l){
    		this.size = size;
    		this.l = l;
    	}
    };
    
    public void testaac2wac(View view){
    	if(TestAacName == null || TestAacName.equals(""))
    		return;
    	File file = new File(TestAacName);
    	float size = file.length()/1024/1024f;
    	long l = System.currentTimeMillis();
    	Aac2Wav aac2wav = Aac2Wav.getSingleInstance();
    	aac2wav.setInfile(TestAacName);
    	int ret = aac2wav.convert();
    	float f = (System.currentTimeMillis() - l)/1024f;
    	if(ret == 0){
    		 TestWavName = aac2wav.getOufile();
    		 StringBuffer strb = new StringBuffer();
    		 strb.append(m_testText.getText().toString());
    		 strb.append("\n");
    		 strb.append("生成文件 " + TestWavName);
    		 strb.append(" 文件大小: " + size + " M");
    		 strb.append("\n" + "耗时: " + f + " 毫秒");
    		 m_testText.setText(strb);
    	}
    }
    
    /** 
     * 这里将数据写入文件，但是并不能播放，因为AudioRecord获得的音频是原始的裸音频， 
     * 如果需要播放就必须加入一些格式或者编码的头信息。但是这样的好处就是你可以对音频的 裸数据进行处理，比如你要做一个爱说话的TOM 
     * 猫在这里就进行音频的处理，然后重新封装 所以说这样得到的音频比较容易做一些音频的处理。 
     */  
    private void writeDateTOFile() {  
        // new一个byte数组用来存一些字节数据，大小为缓冲区大小  
        byte[] audiodata = new byte[bufferSizeInBytes];  
        FileOutputStream fos = null;  
        int readsize = 0;  
        try {
        	TestAudioName = SDCARD_PATH +"/DemoPcm/"+ System.currentTimeMillis() +".pcm";
            File file = new File(TestAudioName);  
            if (file.exists()) {  
                file.delete();  
            } 
            fos = new FileOutputStream(file);// 建立一个可存取字节的文件  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        while (isTestRecord == true) {  
            readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);  
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {  
                try {  
                    fos.write(audiodata);  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        try {  
            fos.close();// 关闭写入流  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
