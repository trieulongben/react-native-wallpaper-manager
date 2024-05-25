import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {TurboModuleRegistry} from 'react-native';


enum IDestination{
    BOTH='both',
    SYSTEM='system',
    LOCK='lock'
}

type TDestination='both'|'system'|'lock'

export interface Spec extends TurboModule {
  setWallpaper(imgUri: string, destination: string): Promise<any>;
}

export default TurboModuleRegistry.get<Spec>('RTNDeviceWallpaper') as Spec | null;