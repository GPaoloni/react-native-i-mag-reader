
# react-native-spp-mag-reader

## Getting started

`$ npm install react-native-spp-mag-reader --save`

### Mostly automatic installation

`$ react-native link react-native-spp-mag-reader`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-spp-mag-reader` and add `RNSppMagReader.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNSppMagReader.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNSppMagReaderPackage;` to the imports at the top of the file
  - Add `new RNSppMagReaderPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-spp-mag-reader'
  	project(':react-native-spp-mag-reader').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-spp-mag-reader/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-spp-mag-reader')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNSppMagReader.sln` in `node_modules/react-native-spp-mag-reader/windows/RNSppMagReader.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Spp.Mag.Reader.RNSppMagReader;` to the usings at the top of the file
  - Add `new RNSppMagReaderPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNSppMagReader from 'react-native-spp-mag-reader';

// TODO: What to do with the module?
RNSppMagReader;
```
  