# RoundTrip_(with_retrofit2_and_h2)

### What is this example

A Retrofit2 example alongside a Springboot backend for roundtrip demostration


### Important Notes

- Use `10.0.2.2` instead of `localhost` IF the server program is running on the same host as the Android Enmulator

- AndroidManifest.xml
    - add `<uses-permission android:name="android.permission.INTERNET" />` before `<application>`
    - add `android:usesCleartextTraffic="true"` inside `<application>`

- build.gradle (Module :app)
    - add `implementation 'com.squareup.retrofit2:retrofit:2.9.0'` and 
    `implementation 'com.squareup.retrofit2:converter-gson:2.6.0'` inside `dependencies{}`, then sync gradle.


### Version Tested
|Android Studio            | Android SDK | Gradle | Gradle Plugin | Gradle JDK | Emulator |
|--------------------------|-------------|--------|---------------|------------|----------|
|Giraffe 2022.3.1 Patch 2  |     33      | 7.2    |    7.0.1      |    11      | Pixel 3a |
