package com.zp.mobile91q.tool;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

public class MyGlideModule implements GlideModule {

    /*3.8.0*/

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, 250 * 1024 * 1024));//250 is the max
        //Memory Cache
        builder.setMemoryCache(new LruResourceCache(200 * 1024 * 1024));
        //Bitmap Pool ;Glide的位图池用于允许重复使用各种不同大小的位图，这可以大大减少在解码图像时由于像素数组分配而导致的jank垃圾收集。
        builder.setBitmapPool(new LruBitmapPool(200 * 1024 * 1024));
        //Bitmap Format
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }



    /*4.9.0*/

    /*private String cacheLocation;
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, 250 * 1024 * 1024));//250 is the max
        //Memory caches and pools
//        Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
        //Memory Cache
        builder.setMemoryCache(new LruResourceCache(100 * 1024 * 1024));
        //Bitmap Pool ;Glide的位图池用于允许重复使用各种不同大小的位图，这可以大大减少在解码图像时由于像素数组分配而导致的jank垃圾收集。
        builder.setBitmapPool(new LruBitmapPool(100 * 1024 * 1024));
        //Bitmap Format
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }*/




}
