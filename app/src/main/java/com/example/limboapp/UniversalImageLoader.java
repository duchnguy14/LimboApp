package com.example.limboapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class UniversalImageLoader
{
    private static final int defaultImage = R.drawable.ic_user_dark;
    //    private final Context ACTIVITY_CONTEXT = UniversalImageLoader.this;
    private Context ACTIVITY_CONTEXT;

    static final String TAG = "(Debug) UniversalImageLoader";


    public UniversalImageLoader(Context context) {
        this.ACTIVITY_CONTEXT = context;
    }


    /*
        Notes: This is where we are going to set all the settings
                    for the Universal Image Loader because it works
                    on an instance basis, we need to set the setting once
                    and it will be set for the whole time the app is running
     */
    public ImageLoaderConfiguration getConfig()
    {
         /*Notes: This is the default image that will show
                        if for whatever reason the image couldnt load.
                  Universal Image Loader can handle null inputs, if we pass a
                        null input or query a database and nothing is found, a
                        default image will be loaded.

          */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(ACTIVITY_CONTEXT)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        return configuration;
    }

    /*
        Notes:
            Setting a single image on a layout, not a grid or list of image.
            String append is for all the image file extensions Universal Image Loader can take.
            This method can be used to set images that are static.
            This method can't be used if the images are being changed in the fragment/activity
                or if they're being set in a List or a grid.
     */
    public static void setImage(String image_url_string, ImageView image, final ProgressBar progressBar, String append)
    {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + image_url_string, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(progressBar != null)
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason)
            {
                if(progressBar != null)
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                if(progressBar != null)
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view)
            {
                if(progressBar != null)
                {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


}