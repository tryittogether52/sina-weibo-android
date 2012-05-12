package com.zzz.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.State;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;

public class ImageThreadLoader {
	private static final String LOG_TAG = ImageThreadLoader.class.getName();
	private final Cache cache;//图片缓存
	private Thread thread;
	 private final Handler handler = new Handler();//更新ui
	 private final QueueRunner runner = new QueueRunner();
	 private final class QueueItem {
		    public String url;
		    public ImageLoadedListener listener;
		  }

	private final ArrayList<QueueItem> queue;
	public ImageThreadLoader(Cache cache) {//保存url 和对于的图片
		// TODO Auto-generated constructor stub
		 thread = new Thread(runner);
		    this.cache = cache;
		    queue = new ArrayList<QueueItem>();
	}

	public static ImageThreadLoader getInMemoryInstance() {
		    return new ImageThreadLoader(new MemoryCache());
		  }
	 
	public static ImageThreadLoader getOnDiskInstance(Context context) {
	    return new ImageThreadLoader(new DiskCache(context));
	  }

	
	 private class QueueRunner implements Runnable {
		    @Override
		    public void run() {
		      synchronized (this) {
		        while (queue.size() > 0) {
		          final QueueItem item = queue.remove(0);

		          // If in the cache, return that copy and be done
		          if (cache.containsKey(item.url) && cache.get(item.url) != null) {
		            getCachedItem(item);
		          } else {
		            getRemoteItem(item);
		          }

		        }
		      }
		    }
		    
		    private void getRemoteItem(final QueueItem item) {// 下载图片更新ui
		        final Bitmap bmp = DownloadDrawable.createBitmapFromUrl(item.url);
		        if (bmp != null) {
		          cache.put(item.url, bmp);

		          // Use a handler to get back onto the UI thread for the update
		          handler.post(new Runnable() {
		            @Override
		            public void run() {
		              if (item.listener != null) {
		                item.listener.imageLoaded(new BitmapDrawable(bmp));
		              }
		            }
		          });
		        } else {
		          Log.e(LOG_TAG, "Image from <" + item.url + "> was null!");
		        }
		      }
		    
		    private void getCachedItem(final QueueItem item) {
		        // Use a handler to get back onto the UI thread for the update
		        handler.post(new Runnable() {
		          public void run() {
		            if (item.listener != null) {
		             
		              Bitmap ref = cache.get(item.url);
		              if (ref != null) {
		                item.listener.imageLoaded(new BitmapDrawable(ref));
		              } else {
		                Log.w(LOG_TAG, "Image loader lost the image to GC.");
		              }
		            }
		          }
		        });
		      }
	 }
	
	 public Drawable loadImage(final String uri,final ImageLoadedListener//加载图片
		      listener){
		 // 假如有存在图片直接去图片
		 if (cache.containsKey(uri)) {
		      Bitmap ref = cache.get(uri);
		      if (ref != null) {
		        return new BitmapDrawable(ref);
		      }
		    }
		 
		  QueueItem item = new QueueItem();
		    item.url = uri;
		    item.listener = listener;
		    queue.add(item);

		    // 启动线程加载图片
		    if (thread.getState() == State.NEW) {
		      thread.start();
		    } else if (thread.getState() == State.TERMINATED) {
		      thread = new Thread(runner);
		      thread.start();
		    }
		 return null;
	 }
	protected static interface Cache {

	    Bitmap get(String uri);

	    boolean containsKey(String uri);

	    void put(String uri, Bitmap image);
	  }
	
	 public interface ImageLoadedListener {
		    public void imageLoaded(Drawable imageBitmap);
	 }
	protected static class DiskCache implements Cache {
	    private final Context context;
	    
	    public DiskCache(Context context) {
	        this.context = context;
	        cleanCache();
	      }

	    @Override
	    public Bitmap get(String uri) {
	      if (uri == null) {
	        return null;
	      }
	      Bitmap value = null;
	      try {
	        FileInputStream stream =
	          new FileInputStream(
	              new File(getCachePath(context), makeCacheFileName(uri)))
	            ;
	        value = BitmapFactory.decodeStream(stream);
	        stream.close();
	        Log.d(LOG_TAG, "Cache hit: " + uri);
	      } catch (FileNotFoundException e) {
	        Log.e(LOG_TAG, "Error getting cache file.", e);
	      } catch (IOException e) {
	        Log.e(LOG_TAG, "Error closing cache file.", e);
	      }
	      return value;
	    }

	    @Override
	    public boolean containsKey(String uri) {
	      if (uri == null) {
	        return false;
	      }
	      File file = new File(getCachePath(context), makeCacheFileName(uri));
	      return file.exists();
	    }

	    @Override
	    public void put(String uri, Bitmap image) {
	      if (uri == null) {
	        return;
	      }
	      try {
	        Bitmap.CompressFormat compression = Bitmap.CompressFormat.JPEG;
	        if (uri.toLowerCase().endsWith("png")) {
	          compression = Bitmap.CompressFormat.PNG;
	        }
	        FileOutputStream stream =
	            new FileOutputStream(
	                new File(getCachePath(context), makeCacheFileName(uri))
	            );
	        image.compress(compression, 70, stream);
	        stream.flush();
	        stream.close();
	      } catch (FileNotFoundException e) {
	        Log.e(LOG_TAG, "Error writing cache file. Is the path wrong?", e);
	      } catch (IOException e) {
	        Log.e(LOG_TAG, "Error closing cache file.", e);
	      }
	    }
	    
	    public static String makeCacheFileName(String uri) {// 保存文件经过MD5 和base64 处理保存
	        if (uri == null) {
	          return null;
	        }
	        String key = null;
	        try {
	          MessageDigest digest = MessageDigest.getInstance("MD5");
	          digest.update(uri.getBytes("iso-8859-1"), 0, uri.length());
	          key = Base64.encodeBytes(digest.digest()).replace('/','-');
	        } catch (NoSuchAlgorithmException e) {
	          Log.e(LOG_TAG, "Error making image key name", e);
	        } catch (UnsupportedEncodingException e) {
	          Log.e(LOG_TAG, "Error making image key name", e);
	        }
	        return key;
	      }

	    public static String getCachePath(Context context) {
		      //根据是否存在sd 进行存储
	    	File path=null;
	    	if(isExistSDCard()&&getSDCardRemainingSpace()>10*1024){
	    		path = new File(Environment.getExternalStorageDirectory(),
	    				 ImageThreadLoader.class.getName());
	    	}else
	    	{
	    		path = new File(context.getCacheDir(),
	    				 ImageThreadLoader.class.getName());
	    	}
		         
		      if (!path.exists()) {
		        //判断是否存在对于的路径，不存在创建
		        path.mkdirs();
		      }
		      return path.getAbsolutePath();
		    }
	    
	    private void cleanCache() {// 清楚保存文件 根据时间清楚可修改为保存列表清除
	        new Thread(new Runnable(){
	          @Override
	          public void run() {
//	            String oldPath=getCachePath(context);
	            String oldPath = context.getFilesDir().getAbsolutePath();
	            removeFiles(oldPath, ".{22}==$", 0);
	            removeFiles(getCachePath(context), ".{22}==$", 7);
	          }

	          private void removeFiles(String path, String filePattern, int daysOld) {
	            final long oldFileDate = new Date().getTime() - (daysOld * 86400000);
	            File folder = new File(path);
	            if (folder.exists()) {
	              String[] filenames = folder.list();
	              for (String filename : filenames) {
	                if (filename.matches(filePattern)) {
	                  File file = new File(path, filename);
	                  if (file.lastModified() < oldFileDate) {
	                    Log.d(LOG_TAG, "Removing from cache: " + filename);
	                    //删除文件
	                    file.delete();
	                  }
	                }
	              }
	            }
	          }
	        }).start();
	      }

	}
	// 判断sd 是否存在
	public static boolean isExistSDCard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	// sd 卡存储空间
	public static int getSDCardRemainingSpace() {
		String sdcard = Environment.getExternalStorageDirectory().getPath();
		File file = new File(sdcard);
		StatFs statFs = new StatFs(file.getPath());
		int remainingSpace = (int) (statFs.getBlockSize() * ((long) statFs
				.getAvailableBlocks() - 4));
		return remainingSpace;
	}
	 protected static class MemoryCache implements Cache {// 使用SoftReference 清除文件
		   
		    private final HashMap<String, SoftReference<Bitmap>> cache;

		    public MemoryCache() {
		      cache = new HashMap<String, SoftReference<Bitmap>>();
		    }

		    
		    public Bitmap get(String uri) {
		      return cache.get(uri).get();
		    }

		    
		    public boolean containsKey(String uri) {
		      return cache.containsKey(uri);
		    }

		 
		    public void put(String uri, Bitmap image) {
		      cache.put(uri, new SoftReference<Bitmap>(image));
		    }
		  }
	 
}
