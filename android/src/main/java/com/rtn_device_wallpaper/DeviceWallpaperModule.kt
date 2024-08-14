package com.rtn_device_wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.ThumbnailUtils
import android.os.Build
import android.util.DisplayMetrics
import androidx.window.layout.WindowMetricsCalculator
import coil.ImageLoader
import coil.request.ImageRequest
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException


class DeviceWallpaperModule(reactContext: ReactApplicationContext) : NativeDeviceWallpaperSpec(
    reactContext
) {

     var context: Context= reactContext.applicationContext
    
    override fun setWallpaper(imgUri: String?, destination: String?, promise: Promise?) {
        if(imgUri.isNullOrBlank() || destination.isNullOrBlank() || promise==null) {
            return
        }
            myPluginScope.launch {
                try {
                    val bitmap = async {
                        getBitmapFromUri(imgUri);
                    }.await()
                    if (bitmap != null) {
                        setWallpaperBitmap(getCenterCroppedBitmap(bitmap), destination);
                        promise.resolve(true);
                    } else {
                        promise.reject(Exception("Cannot find bitmap!"));
                    }
                }catch (e:Exception){
                    promise.reject(e)
                }
            }
        }


    companion object{
        const val NAME="RTNDeviceWallpaper"
    }
    
    
    @Throws(IOException::class)
    private suspend fun getBitmapFromUri(imageUri: String): Bitmap? {
        var bitmap: Bitmap? = null
        if (imageUri.startsWith("http://") || imageUri.startsWith("https://")) {
                val loader = ImageLoader( context)
                val request = ImageRequest.Builder(context)
                    .data(imageUri)
                    .allowHardware(false) // Disable hardware bitmaps.
                    .build()
                val result = (loader.execute(request)).drawable
                if(result is BitmapDrawable){
                    bitmap = result.bitmap
                }
        } else if (imageUri.startsWith("file://")) {
            bitmap = BitmapFactory.decodeFile(imageUri.replace("file://", ""))
        }
        return bitmap
    }

    private fun getCenterCroppedBitmap(bitmap: Bitmap): Bitmap {
        val metrics = DisplayMetrics()
        val (width,height)= getWindowSize()
        metrics.widthPixels=width
        metrics.heightPixels=height
        return ThumbnailUtils.extractThumbnail(bitmap, metrics.widthPixels, metrics.heightPixels)
    }

    private fun getWindowSize(): Pair<Int, Int> {
        val windowMetrics = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(context)
        val width = windowMetrics.bounds.width()
        val height = windowMetrics.bounds.height()
        return Pair(width,height)
    }

    private fun setWallpaperBitmap(bitmap: Bitmap, destination:String){
        val wpManager= WallpaperManager.getInstance(context)
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.N){
            wpManager.setBitmap(bitmap, null, false, getWallpaperDestination(destination));
        }
        else{
            wpManager.setBitmap(bitmap)
        }
    }

    private fun getWallpaperDestination(destination: String): Int {
        
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            return when(destination){
                DESTINATION.BOTH.value -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                DESTINATION.LOCK.value -> WallpaperManager.FLAG_LOCK
                DESTINATION.SYSTEM.value -> WallpaperManager.FLAG_SYSTEM
                else -> {
                    return 0
                }
            }
        }
            return when(destination){
                DESTINATION.BOTH.value -> 1 or 2
                DESTINATION.LOCK.value -> 2
                DESTINATION.SYSTEM.value -> 1
                else -> {
                    return 0
                }
            }
    }
}
