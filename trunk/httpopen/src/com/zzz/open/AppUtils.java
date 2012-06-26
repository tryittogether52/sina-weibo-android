package com.zzz.open;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AppUtils {
	
	public final static String ICON_URL= "";

	public static boolean isBlank(String str) {
		int strLen;

		if ((str == null) || ((strLen = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查对象是否为数字型字符串。
	 */
	public static boolean isNumeric(Object obj) {
		if (obj == null) {
			return false;
		}
		String str = obj.toString();
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查指定的字符串列表是否不为空。 by:Jerry
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isBlank(value);
			}
		}
		return result;
	}

	/**
	 * 把通用字符编码的字符串转化为汉字编码。 by:Jerry
	 */
	public static String unicodeToChinese(String unicode) {
		StringBuilder out = new StringBuilder();
		if (!isBlank(unicode)) {
			for (int i = 0; i < unicode.length(); i++) {
				out.append(unicode.charAt(i));
			}
		}
		return out.toString();
	}

	/**
	 * 过滤不可见字符 by:Jerry
	 */
	public static String stripNonValidXMLCharacters(String input) {
		if (input == null || ("".equals(input)))
			return "";
		StringBuilder out = new StringBuilder();
		char current;
		for (int i = 0; i < input.length(); i++) {
			current = input.charAt(i);
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

	/**
	 * 检测SD卡剩余容量
	 * 
	 * @return
	 */
	public static int getSDCardRemainingSpace() {
		String sdcard = Environment.getExternalStorageDirectory().getPath();
		File file = new File(sdcard);
		StatFs statFs = new StatFs(file.getPath());
		int remainingSpace = (int) (statFs.getBlockSize() * ((long) statFs
				.getAvailableBlocks() - 4));
		return remainingSpace;
	}

	
	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean isExistSDCard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 读取远程图片数据
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static byte[] downloadImage(String imageUrl) {
		if (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".bmp")
				|| imageUrl.endsWith(".png") || imageUrl.endsWith(".gif")) {
			try {
				URL url = new URL(imageUrl);
				URLConnection urlConn = url.openConnection();
				InputStream is = urlConn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;

				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}

				return baf.toByteArray();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static boolean isExistFile(String fullName) {
		File file = new File(fullName);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static void deleteFile(String fullName) {
		File file = new File(fullName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * byte[]转Bitmap
	 * 
	 * @param bytes
	 *            图片源
	 * @param maxSize
	 *            最大尺寸
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] bytes, int maxSize) {
		try {
			if (bytes == null) {
				return null;
			}

			if (bytes.length != 0) {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);

				int scale = 1;

				if ((opt.outHeight > maxSize) || (opt.outWidth > maxSize)) {
					scale = (int) Math.pow(
							2,
							(int) Math.round(Math.log(maxSize
									/ (double) Math.max(opt.outHeight,
											opt.outWidth))
									/ Math.log(0.5)));
				}

				BitmapFactory.Options newOpt = new BitmapFactory.Options();
				newOpt.inSampleSize = scale;

				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						newOpt);
			} else {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

	public static String getDateStringFromDate(Date date) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				java.util.Locale.CHINA);
		String dateString = formatter.format(date);
		return dateString.trim();
	}

	public static Date getDateFromDateString(String dateStr) {
		SimpleDateFormat formatter;
		Date date = null;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				java.util.Locale.CHINA);
		try {
			date = (Date) formatter.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// "yyyy-MM-dd HH:mm"
	public static Date getDateFromDateStringWithoutss(String dateStr,
			String template) {
		SimpleDateFormat formatter;
		Date date = null;
		formatter = new SimpleDateFormat(template, java.util.Locale.CHINA);
		try {
			date = (Date) formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static boolean convertBytes2Bitmap(String imageName, byte[] byt) {
		if (byt.length == 0)
			return false;
		boolean success = true;
		Bitmap bmp = BitmapFactory.decodeByteArray(byt, 0, byt.length);
		File file = new File("/sdcard/" + imageName + ".png");

		try {
			file.createNewFile();
		} catch (IOException e) {
			success = false;
		}

		FileOutputStream out = null;

		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			success = false;
		}

		bmp.compress(Bitmap.CompressFormat.PNG, 100, out);

		try {
			out.flush();
		} catch (IOException e) {
			success = false;
		}

		try {
			out.close();
		} catch (IOException e) {
		}

		return success;
	}

	/**
	 * drawable转Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						(drawable.getOpacity() != PixelFormat.OPAQUE) ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * 
	 * @param imgURL
	 * @return
	 */
	public static BitmapDrawable downloadBitmapDrawable(String imgURL) {
		BitmapDrawable icon = null;
		try {
			URL url = new URL(imgURL);
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
			icon = new BitmapDrawable(hc.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return icon;
	}

	/**
	 * 计算 ListView 的高度
	 * 
	 * @param lv
	 *            需要计算高度的ListView
	 */
	public static void setListViewHeight(ListView lv, int height) {
		ListAdapter la = lv.getAdapter();
		if (null == la) {
			return;
		}
		// 计算所有item的高度
		int h = 0;
		final int cnt = la.getCount();
		for (int i = 0; i < cnt; i++) {
			View item = la.getView(i, null, lv);
			item.measure(0, 0);
			h += item.getMeasuredHeight();
		}
		// 重置ListView高度
		ViewGroup.LayoutParams lp = lv.getLayoutParams();
		lp.height = h + (lv.getDividerHeight() * (cnt - 1));
		lp.height += height;
		lv.setLayoutParams(lp);
	}

	/**
	 * 以私有文件保存内容
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public static void save(String filename, String content, Context context)
			throws Exception {
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public static String readFile(String filename, Context context)
			throws Exception {
		FileInputStream inStream = context.openFileInput(filename);
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 得到文件的二进制数据
		outStream.close();
		inStream.close();
		return new String(data);
	}	
}