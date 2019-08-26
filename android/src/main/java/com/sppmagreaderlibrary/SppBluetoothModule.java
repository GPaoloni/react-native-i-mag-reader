package com.sppmagreaderlibrary;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.IllegalViewOperationException;

import com.imagpay.Settings;
import com.imagpay.enums.CardDetected;
import com.imagpay.enums.PinPadEvent;
import com.imagpay.spp.SppHandler;
import com.imagpay.spp.SppListener;

import javax.annotation.Nullable;

import static com.sppmagreaderlibrary.RNSppMagReaderPackage.TAG;


public class SppBluetoothModule implements SppListener {
    private SppHandler _handler;
    private Settings _settings;

    private String magName = "";
    private String magPan = "";
    private String magExpDate = "";

    private ReactApplicationContext mReactContext;

    private static final String CONN_SUCCESS = "connectionSuccess";
    private static final String CONN_FAILED = "connectionFailed";
    private static final String CONN_LOST = "connectionLost";
    private static final String CARD_READ = "cardRead";

    public SppBluetoothModule(ReactApplicationContext reactContext) {
        mReactContext = reactContext;
    }

    public boolean creteSppListener(Context context) {
        try {
            _handler = new SppHandler(context);
            _settings = new Settings(_handler);
            _handler.setShowAPDU(true);
            _handler.setBlueTooth(true);// set bluetooth flag
            _handler.setDebug(true);
            _handler.addSppListener(this);
        } catch (IllegalViewOperationException e) {
            Log.e(TAG, "Error creating SPP handler");
            return false;
        }

        return true;
    }

    public boolean connectSppListener(BluetoothDevice device) {
        if (_handler.isConnected()) {
            disconnectSppListener();
            Log.d(TAG, "Already connected");
            // _handler.close();
        } //else {
        if (_handler.connect(device)) {
            String name = device.getName();
            Log.d(TAG, "Connected to " + name);
        } else {
            sendEvent(CONN_FAILED, null);
            return false;
        }

        return true;
    }

    public boolean disconnectSppListener() {
        try {
            if (_handler.isConnected()) {
                _handler.close();
            }
        } catch (IllegalViewOperationException e) {
            Log.e(TAG, "Error closing SPP handler");
            return false;
        }

        return true;
    }

    public boolean isHandlerConnected() {
        return _handler.isConnected();
    }

    public void destroySppListener () {
        try {
            _handler.onDestroy();
            Log.d(TAG, "Spp handler destroyed");
        } catch (IllegalViewOperationException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onCardDetected(CardDetected status) {
        if (status == CardDetected.SWIPED) {
            Log.d(TAG, "Brush a magnetic stripe card detected...");
            Log.d(TAG, "----------------Mag card-----------------");
            String magAllDatas = _handler.getMagAllData();
            String holderName = "";
            /**
             * only read Mag card mode is "Settings.TYPE_PLAINTEXT" , The following interface method return value is not
             * empty
             */
            if (magAllDatas != null && magAllDatas.contains("^")) {
                holderName = magAllDatas.substring(magAllDatas.indexOf("^") + 1, magAllDatas.lastIndexOf("^"));
            }
            magName = holderName;
            magPan = _handler.getMagPan();
            magExpDate = _handler.getMagExpDate();
            WritableMap params = Arguments.createMap();
            params.putString("message", magPan + '^' + magName + '^' + magExpDate);
            sendEvent(CARD_READ, params);
        }

    }

    @Override
    public void onConnected() {
        sendEvent(CONN_SUCCESS, null);
        Log.d(TAG, "connect successful......");
    }

    @Override
    public void onDisconnect() {
        sendEvent(CONN_LOST, null);
        Log.d(TAG, "disconnect......");
    }

    @Override
    public boolean onFindReader(final BluetoothDevice device) {
        String deviceName = device.getName();
        Log.d("BTDeviceName", deviceName);
        return false;
    }

    @Override
    public void onParseData(String data) {
        Log.d(TAG, "parseData(16)");// <== " + data);
    }

    @Override
    public void onPinPad(PinPadEvent event) {
        Log.e(TAG, event.toString());
//        if (event == PinPadEvent.ENTER) {
//            Log.e(TAG, _handler.getPINBlock());// ANSI X9.8 Format
//        }
    }

    @Override
    public void onStartedDiscovery() {
        Log.d(TAG, "Started Discovery.....");
    }

    @Override
    public void onFinishedDiscovery() {
        Log.d(TAG, "Finished Discovery.....");
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        if (mReactContext.hasActiveCatalystInstance()) {
            Log.d(TAG, "Sending event: " + eventName);
            mReactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }
}