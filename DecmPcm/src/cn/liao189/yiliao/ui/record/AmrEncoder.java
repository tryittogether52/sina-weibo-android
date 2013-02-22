package cn.liao189.yiliao.ui.record;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class AmrEncoder {
	
	public static final String TAG = "AmrEncoder";
	static {
		System.loadLibrary("voice");
	}
	
	private AmrEncoder() {
		mGae = AmrEncoderInitialize();
		Log.d(TAG, "AmrEncoderInitialize");
	};

	private static AmrEncoder singleInstance = null;

	static public AmrEncoder getSingleInstance() {
		if (singleInstance != null) {
			return singleInstance;
		} else {
			return new AmrEncoder();
		}
	}
	
	public void reset(){
		in = null;
		reserve = null;
	}
	
	public byte[] encode(byte[] input) {
		if(mGae == 0) return null;
		if(input.length == 0) return null;
		List<Byte> outBytes = new ArrayList<Byte>();
		if(reserve != null) {
			in = new byte[input.length + reserve.length];
			System.arraycopy(reserve, 0, in, 0, reserve.length);
			System.arraycopy(input, 0, in, reserve.length, input.length);
			reserve = null;
		} else {
			in = input;
		}
		int count = in.length / (SAMPLES_PER_FRAME * 2);
		int i = 0;
		for(; i < count ; ++i){
			System.arraycopy(in, i*(SAMPLES_PER_FRAME * 2), mBuf, 0, SAMPLES_PER_FRAME * 2);
			mBufIn = AmrEncoderEncode(mGae, mBuf, 0, mBuf, 0);
			for (int j = 0; j < mBufIn; j++) {
				outBytes.add(mBuf[j]);
			}
		}
		byte[] keep = new byte[in.length - (SAMPLES_PER_FRAME * 2 * count)];
		System.arraycopy(in, SAMPLES_PER_FRAME * 2 * count, keep, 0, keep.length);
		reserve = keep;
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
	
	public void cleanup() {
		if(mGae != 0){
			AmrEncoderCleanup(mGae);
			mGae = 0;
		}
	}
	
	// frame is 20 msec at 8.000 khz
    private final static int SAMPLES_PER_FRAME = 8000 * 20 / 1000;
    
    // native handle
    private int mGae;
    
    // result amr stream
    private final byte[] mBuf = new byte[SAMPLES_PER_FRAME * 2];
    private int mBufIn = 0;
    private int mBufOut = 0;
    
    //input pcm
    private byte[] in;
    //out of one frame,reserved,wait for next input
    private byte[] reserve;
    
    
    public native int AmrEncoderInitialize();
    public native void AmrEncoderCleanup(int state);
    public native int AmrEncoderEncode(int state, byte[] pcm, int pcmOffset, byte[] amr, int amrOffset);
	
	
}
