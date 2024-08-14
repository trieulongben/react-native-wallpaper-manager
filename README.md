
# react-native-device-wallpaper-manager
React Native New Architecture - device wallpaper setter library (Android only), backed with Coil and Coroutine.
+ 🐎 New Architecture
+ ⚡ Using light-weight thread
+ 🏎 Kotlin implement
+ 💥 Support from Android 7.0 (API 24)

## Installation

### Adding the package

#### npm

```bash
$ npm install react-native-device-wallpaper-manager
```

#### yarn

```bash
$ yarn add react-native-device-wallpaper-manager
```


### .setWallpaper Props

|Prop|Type|Description|Note|
|-|-|-|-|
|**destination**|string| type of wallpaper|"system", "both", "lock"|
|**imageUri**|string|path to image (remote or local uri are acceptable)|"http://", "https://", "file://"|


### Example

```typescript
import RTNDeviceWallpaper from 'react-native-device-wallpaper-manager/js/NativeDeviceWallpaper'

const setWallpaper=async()=>{
    await RTNDeviceWallpaper?.setWallpaper("https://example_website/example_image.png","both")
}
```

### Demo

![Alt Text](https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExanF5YzVpZG5ncmNqanp2aW81eW14aTcwdmNyMzBlcmhlcjVjNHduMyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/FpMJDFkj6mNzu600eN/giphy.gif)



