package cn.liao189.yiliao.ui.record;

public class Aac2Wav {
	private static Aac2Wav singleInstance = null;
	
	static public Aac2Wav getSingleInstance() {
		if (singleInstance != null) {
			return singleInstance;
		} else {
			return new Aac2Wav();
		}
	}
	
	private Aac2Wav(){
	}
	
	static {
		System.loadLibrary("voice");
	}
	
	private String mInfile ;
	private String mOufile;
	
	public void setInfile(String infile){
		mInfile = infile;
	}
	
	public String getOufile(){
		return mOufile;
	}
	
	//失败返回-1,成功返回0.有可能存在转换失败的问题.
	public int convert() {
		int index = mInfile.lastIndexOf(".");
		String string = mInfile.substring(0, index);
		mOufile = string + ".wav";
		return Convert(mInfile, mOufile);
	}
	
	public native int Convert(String infile,String outfile);
}
