
package com.sppmagreaderlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class RNSppMagReaderModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNSppMagReaderModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNSppMagReader";
  }

}