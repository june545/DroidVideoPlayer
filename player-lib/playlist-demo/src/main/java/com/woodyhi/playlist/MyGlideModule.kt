package com.woodyhi.playlist

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class MyGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
//        var options = RequestOptions()
//        options.placeholder(R.drawable.placeholder)
//        options.centerCrop()
//        builder.setDefaultRequestOptions(options)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
    }
}