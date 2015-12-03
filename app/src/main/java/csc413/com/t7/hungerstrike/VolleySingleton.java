package csc413.com.t7.hungerstrike;

/**
 * Created by Mardan Anwar on 12/02/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mardan Anwar on 12/2/15.
 */
public class VolleySingleton
{
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

//    private static Context mCtx;

    private VolleySingleton(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);
//        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache()
        {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(5 * 1024 * 1024);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public void cancelAll(String tag){
        mRequestQueue.cancelAll(tag);
    }

    public static synchronized VolleySingleton getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
//        if (mRequestQueue == null) {
//            // getApplicationContext() is key, it keeps you from leaking the
//            // Activity or BroadcastReceiver if someone passes one in.
//            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
//        }
        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
//        req.setTag("tag");
        getRequestQueue().add(req);
    }
}
