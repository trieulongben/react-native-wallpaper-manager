
# react-native-device-wallpaper-manager
React Native New Architecture - device wallpaper setter library (Android only), backed with Coil and Coroutine.
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

