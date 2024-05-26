package com.rtn_device_wallpaper

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.ThumbnailUtils
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.RequiresApi
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException


class DeviceWallpaperModule(reactContext: ReactApplicationContext) : NativeDeviceWallpaperSpec(
    reactContext
) {

     var context: Context= reactContext.applicationContext
    
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun setWallpaper(imgUri: String?, destination: String?, promise: Promise?) {
        if(imgUri.isNullOrBlank() || destination.isNullOrBlank() || promise==null) return
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

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getCenterCroppedBitmap(bitmap: Bitmap): Bitmap {
        val metrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        metrics.widthPixels=  windowManager.currentWindowMetrics.bounds.width()
        metrics.heightPixels= windowManager.currentWindowMetrics.bounds.height()

        return ThumbnailUtils.extractThumbnail(bitmap, metrics.widthPixels, metrics.heightPixels)
    }

    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    private fun setWallpaperBitmap(bitmap: Bitmap, destination:String){
        val wpManager= WallpaperManager.getInstance(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wpManager.setBitmap(bitmap, null, false, getWallpaperDestination(destination));
        } else {
            wpManager.setBitmap(bitmap);
        }
    }

    private fun getWallpaperDestination(destination: String): Int {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return 0

        return when(destination){
            DESTINATION.BOTH.value -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
            DESTINATION.LOCK.value -> WallpaperManager.FLAG_LOCK
            DESTINATION.SYSTEM.value -> WallpaperManager.FLAG_SYSTEM
            else -> {
                return 0
            }
        }

    }
}
