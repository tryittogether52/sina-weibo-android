package cn.liao189.yiliao.ui.record;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;


public class AacEncoder {
	
	public static final String TAG = "AacEncoder";
	
	private int mApiHandle;
	private int mEncoderHandle;
	private int mMemOperatorHandle;
	
	private byte[] mIn;
	private byte[] mReserve;
	
	private static AacEncoder singleInstance = null;
	private int mInputBufSize;
	private static final int AAC_LC_PCM_SAMPLES = 2*1024;
	private byte[] mBuf = new byte[20480];
	private int mBufIn = 0;
	private int mBufOut = 0;
	
	private int mChannel;
	
	
	static public AacEncoder getSingleInstance() {
		if (singleInstance != null) {
			return singleInstance;
		} else {
			return new AacEncoder();
		}
	}
	
	private AacEncoder(){
	}
	
	public void init(int sampleRate,int bitrate,int channels) {
		mChannel = channels;
		mInputBufSize = channels * AAC_LC_PCM_SAMPLES;
		int[] handle = new int[3];
		if(AacEncoderInitialize(sampleRate, bitrate, mChannel, handle) == true) {
			mApiHandle = handle[0];
			mEncoderHandle = handle[1];
			mMemOperatorHandle = handle[2];
		}
		Log.d(TAG, "AacEncoderInitialize");
	}
	
	public void reset() {
		mIn = null;
		mReserve = null;
	}
	
	public void cleanup(){
		if(mApiHandle != 0){
			AacEncoderCleanup(mApiHandle, mEncoderHandle, mMemOperatorHandle);
			mApiHandle = 0;
			mEncoderHandle = 0;
			mMemOperatorHandle = 0;
		}
	}
	
	
	public byte[] encode(byte[] input) {
		if(mApiHandle == 0) return null;
		if(input.length == 0) return null;
		List<Byte> outBytes = new ArrayList<Byte>();
		if(mReserve != null) {
			mIn = new byte[input.length + mReserve.length];
			System.arraycopy(mReserve, 0, mIn, 0, mReserve.length);
			System.arraycopy(input, 0, mIn, mReserve.length, input.length);
			mReserve = null;
		} else {
			mIn = input;
		}
		int count = mIn.length / mInputBufSize;
		int i = 0;
		for(; i < count; ++i) {
			System.arraycopy(mIn, i*mInputBufSize, mBuf, 0, mInputBufSize);
			mBufOut = AacEncoderEncode(mEncoderHandle, mApiHandle, mBuf, mBuf,mChannel);
			for (int j = 0; j < mBufOut; j++) {
				outBytes.add(mBuf[j]);
			}
		}
		
		byte[] keep = new byte[mIn.length - (mInputBufSize * count)];
		System.arraycopy(mIn, mInputBufSize * count, keep, 0, keep.length);
		mReserve = keep;
		byte[] out;
		if(outBytes.size() != 0) {
			 out = new byte[outBytes.size()];
			for(int m = 0; m < outBytes.size(); ++m){
				out[m] = outBytes.get(m).byteValue();
			}
		} else {
			out = null;
		}
		return out;
		
	}
	
	
	static {
		System.loadLibrary("voice");
	}
	
	///////////////////////////////////////////////
	public native boolean AacEncoderInitialize(int sampleRate,int bitrate,int channels,int[] handle);
	public native void AacEncoderCleanup(int apiHandle, int encoderHandle,int memOperator);
	public native int AacEncoderEncode(int encoderHandle, int apiHandle,byte[] pcm,byte[] aac ,int channel);
}
