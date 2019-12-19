package com.khb.mvvmlibrary.img

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.util.LruCache

/**
 *创建时间：2019/12/11
 *编写人：kanghb
 *功能描述：
 */
@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(
                context, diskCacheFolderName(), diskCacheSize()
            )
        ).setMemoryCache(LruResourceCache(memoryCacheSize()))
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    private fun memoryCacheSize(): Long = 20 * 1024 * 1024

    private fun diskCacheSize(): Long = 512 * 1024 * 1024

    private fun diskCacheFolderName(): String = "mvvm-github"

}