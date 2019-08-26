package com.sppmagreaderlibrary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.imagpay.Settings;
import com.imagpay.enums.CardDetected;
import com.imagpay.enums.PinPadEvent;
import com.imagpay.spp.SppHandler;
import com.imagpay.spp.SppListener;

import static com.sppmagreaderlibrary.RNSppMagReaderPackage.TAG;


public class SppBluetoothModule extends ReactContextBaseJavaModule implements SppListener {
    private SppHandler _handler;
    private Settings _settings;

    private String magName = "";
    private String magPan = "";
    private String magExpDate = "";

    private BluetoothAdapter mBluetoothAdapter;
    private ReactApplicationContext mReactContext;

    public SppBluetoothModule(ReactApplicationContext reactContext) {
        super(reactContext); //required by React Native

        mReactContext = reactContext;

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
    }

    @Override
    //getName is required to define the name of the module represented in JavaScript
    public String getName() {
        return "SppBluetooth";
    }

    @ReactMethod
    public void sayHi(Callback errorCallback, Callback successCallback) {
        try {
            System.out.println("Greetings from Java");
            successCallback.invoke("Callback: Greetings from SPP");
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    public boolean creteSppListenerOnConection(BluetoothDevice device) {
        Log.d(TAG, "Function called");

        try {
            Log.d(TAG, "Inside try block");
            _handler = new SppHandler(mReactContext.getApplicationContext());
            _settings = new Settings(_handler);
            _handler.setShowAPDU(true);
            _handler.setBlueTooth(true);// set bluetooth flag
            _handler.setDebug(true);
            _handler.addSppListener(this);

            String name = device.getName();

            if (_handler.connect(device)) {
                Log.d(TAG, "Connected to " + name);
            } else {
                Log.d(TAG, "Failed");
            }
        } catch (IllegalViewOperationException e) {
            Log.e(TAG, "Error creating SPP handler");
            return false;
        }

        Log.d(TAG, "Module created");
        return true;
    }


    @ReactMethod
    public void createSppListener (Callback errorCallback, Callback successCallback) {
        Log.d(TAG, "Function called");

        try {
            Log.d(TAG, "Inside try block");
            _handler = new SppHandler(mReactContext.getApplicationContext());
            _settings = new Settings(_handler);
            _handler.setShowAPDU(true);
            _handler.setBlueTooth(true);// set bluetooth flag
            _handler.setDebug(true);
            _handler.addSppListener(this);



            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("60:96:81:10:94:7D");
            String name = device.getName();

            if (_handler.connect(device)) {
                Log.d(TAG, "Connected to " + name);
            } else {
                Log.d(TAG, "Failed");
            }

            successCallback.invoke("Spp handler created. Device name is " + name);
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }

        Log.d(TAG, "Module created");
    }

    @ReactMethod
    public void readMagAllDatas (Callback errorCallback, Callback successCallback) {
        try {
            String magAllDatas = _handler.getMagAllData();
            Log.d(TAG, "MagAllData: " + magAllDatas);
            successCallback.invoke("Callback: " + magAllDatas);
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void destroySppListener (Callback errorCallback, Callback successCallback) {
        try {
            _handler.onDestroy();
            successCallback.invoke("Callback: spp handler destroyed");
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
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
//            Log.d(TAG, "Mag PAN:" + magPan);
//            Log.d(TAG, "Track1:" + _handler.getTrack1Data());
//            Log.d(TAG, "Track2:" + _handler.getTrack2Data());
//            Log.d(TAG, "Track3:" + _handler.getTrack3Data());
//            Log.d(TAG, "ExpDate:" + magExpDate);
//            Log.d(TAG, magPan);
//            Log.d(TAG, magName);
//            Log.d(TAG, magExpDate);
            Log.d(TAG, "magPan: " + magPan);
            Log.d(TAG, "magName: " + magName);
            Log.d(TAG, "magExpDate: " + magExpDate);
        }

    }

    @Override
    public void onConnected() {
        Log.d(TAG, "connect successful......");
    }

    @Override
    public void onDisconnect() {
        Log.d(TAG, "disconnect......");
        try {
            _handler.onDestroy();
         Log.d(TAG, "Callback: spp handler destroyed");
        } catch (IllegalViewOperationException e) {
            Log.e(TAG, "Error destroying SPP handler");
        }
    }

    @Override
    public boolean onFindReader(final BluetoothDevice device) {
        String deviceName = device.getName();
        Log.d("BTDeviceName", deviceName);
        return false;
    }

    @Override
    public void onParseData(String data) {
        Log.d(TAG, "parseData(16) <== " + data);
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
}