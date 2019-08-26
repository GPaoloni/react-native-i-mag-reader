
package com.sppmagreaderlibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;

public class RNSppMagReaderPackage implements ReactPackage {
    static final String TAG = "SppModule";
    //static final String TAG = "BluetoothSerial";

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
            new RNSppMagReaderModule(reactContext),
            new RCTBluetoothSerialModule(reactContext),
            new SppBluetoothModule(reactContext)
        );
        // List<NativeModule> modules = new ArrayList<>();
        // modules.add(new RNSppMagReaderModule(reactContext));
        // modules.add(new RCTBluetoothSerialModule(reactContext));
        // modules.add(new SppBluetoothModule(reactContext));
        // return modules;
    }

    // Deprecated from RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
    }
}