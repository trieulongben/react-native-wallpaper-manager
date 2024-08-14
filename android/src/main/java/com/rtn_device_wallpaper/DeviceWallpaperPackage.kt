package com.rtn_device_wallpaper;

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val myPluginScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

enum class DESTINATION(val value:String){
  BOTH("both"),
  LOCK("lock"),
  SYSTEM("system")
}

class DeviceWallpaperPackage : TurboReactPackage() {

  override fun getModule(name: String, reactAppContext: ReactApplicationContext): NativeModule? {
    if(name==DeviceWallpaperModule.NAME){
      return DeviceWallpaperModule(reactAppContext)
    }
    else{
    return null

    }
  }
  override fun getReactModuleInfoProvider()=ReactModuleInfoProvider {
    mapOf(
      DeviceWallpaperModule.NAME to ReactModuleInfo(
        DeviceWallpaperModule.NAME,DeviceWallpaperModule.NAME,false,false,true,false,true
      )
    )
  }
}
