package com.zzz.image;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;

public class ImageThreadLoader {
	
	private final Cache cache;//Í¼Æ¬»º´æ
	private Thread thread;
	 private final Handler handler = new Handler();//¸üÐÂui
	 private final QueueRunner runner = new QueueRunner();
	 private final class QueueItem {
		    public String url;
		    public ImageLoadedListener listener;
		  }

	private final ArrayList<QueueItem> queue;
	public ImageThreadLoader(Cache cache) {
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
	 }
	
	 public Drawable loadImage(final String uri,final ImageLoadedListener
		      listener){
		 
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
//	        cleanCache();
	      }

		@Override
		public Bitmap get(String uri) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean containsKey(String uri) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void put(String uri, Bitmap image) {
			// TODO Auto-generated method stub
			
		}
	}
	
	 protected static class MemoryCache implements Cache {
		    // Using SoftReference to allow garbage collector to clean cache if needed
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
