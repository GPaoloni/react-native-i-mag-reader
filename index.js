
import { DeviceEventEmitter, NativeModules } from 'react-native';
import { Buffer } from 'buffer';
//global.Buffer = Buffer;
const { RNSppMagReader } = NativeModules;

const RNSppIMagReader = NativeModules.RNSppIMagReader

/**
 * Listen for available events
 * @param  {String} eventName Name of event one of connectionSuccess, connectionLost, data, rawData
 * @param  {Function} handler Event handler
 */
RNSppIMagReader.on = (eventName, handler) => {
  DeviceEventEmitter.addListener(eventName, handler)
}

/**
 * Stop listening for event
 * @param  {String} eventName Name of event one of connectionSuccess, connectionLost, data, rawData
 * @param  {Function} handler Event handler
 */
RNSppIMagReader.removeListener = (eventName, handler) => {
  DeviceEventEmitter.removeListener(eventName, handler)
}

/**
 * Write data to device, you can pass string or buffer,
 * We must convert to base64 in RN there is no way to pass buffer directly
 * @param  {Buffer|String} data
 * @return {Promise<Boolean>}
 */
RNSppIMagReader.write = (data) => {
  if (typeof data === 'string') {
    data = new Buffer(data)
  }
  return RNSppIMagReader.writeToDevice(data.toString('base64'))
}

export default RNSppMagReader;
//export default RNSppIMagReader;
