plugins {
    id("com.android.application")
}




android {
    namespace = "com.example.myapplication"
    compileSdk = 34


    buildFeatures {
        buildConfig = true
        // ...
    }

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.2")//--heat maps test
    implementation ("com.google.android.gms:play-services-location:17.0.0")// get current location
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.robolectric:robolectric:4.7.3")
    testImplementation ("org.mockito:mockito-inline:4.5.1")
//    testImplementation ("org.mockito:mockito-core:4.5.1")
    androidTestImplementation ("androidx.test:core:1.2.0")
    androidTestImplementation ("org.mockito:mockito-android:4.5.1")
    androidTestImplementation ("androidx.test:core:1.4.0")
    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.3.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")

}