plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.frontendextraexpenses"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.frontendextraexpenses"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.android.volley:volley:1.2.1")
   // implementation(fileTree(mapOf(
       // "dir" to "C:\\Users\\Prakeerth Regunath\\AppData\\Local\\Android\\Sdk\\platforms\\android-34",
       // "include" to listOf("*.aar", "*.jar"),
       // "exclude" to listOf()
   // )))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.android.volley:volley:1.2.1")




}