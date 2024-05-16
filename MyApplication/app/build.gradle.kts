plugins {
    id("com.android.application")
    jacoco
    id("jacoco")
    id("kotlin-android")
//    id("org.jetbrains.kotlin.android")
}


jacoco {
    toolVersion = "0.8.7"
}


android {
    namespace = "com.example.myapplication"

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    buildFeatures {
        buildConfig = true
    }


    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["coverage"] = "true"
        testInstrumentationRunnerArguments["coverageFilePath"] = "$buildDir/outputs/code-coverage/connected/coverage.ec"
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
    compileSdk = 33
}
tasks.withType<Test> {
    extensions.configure(JacocoTaskExtension::class.java) {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.material:material:1.8.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.2")//--heat maps test
    implementation ("com.google.android.gms:play-services-location:17.0.0")// get current location
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
//    testImplementation ("org.robolectric:robolectric:4.7.3")
    testImplementation ("org.mockito:mockito-inline:4.5.1")
//    testImplementation ("org.mockito:mockito-core:4.5.1")
    androidTestImplementation("org.jacoco:org.jacoco.agent:0.8.7")
    androidTestImplementation("org.jacoco:org.jacoco.core:0.8.7")
    testImplementation ("org.powermock:powermock-module-junit4:2.0.9")
    testImplementation ("org.powermock:powermock-api-mockito2:2.0.9")
    androidTestImplementation ("androidx.test:core:1.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation ("org.mockito:mockito-android:4.5.1")
//    androidTestImplementation ("androidx.test:core:1.4.0")
    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")

}

tasks.withType<Test> {
    environment("PATH", System.getenv("PATH"))
}
tasks.register<Exec>("pullCoverageFile") {
    group = "verification"
    description = "Pulls the coverage file from the device"
    commandLine("adb", "pull", "/sdcard/coverage.ec", "build/outputs/code_coverage/connected/")
}

tasks.register<JacocoReport>("jacocoInstrumentedTestReport") {
    dependsOn("connectedDebugAndroidTest", "pullCoverageFile")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
            fileTree(mapOf(
                    "dir" to "$buildDir/tmp/kotlin-classes/debug",
                    "excludes" to listOf(
                            "**/R.class",
                            "**/R$*.class",
                            "**/BuildConfig.*",
                            "**/Manifest*.*",
                            "**/*\$ViewInjector*.*",
                            "**/*\$ViewBinder*.*",
                            "**/Dagger*.*",
                            "**/*MembersInjector*.*",
                            "**/*_Provide*Factory*.*",
                            "**/*_Factory*.*",
                            "**/*\$JsonAdapter*.*"
                    )
            ))
    )

    sourceDirectories.setFrom(files("src/main/java"))
    executionData.setFrom(files("build/outputs/code_coverage/connected/coverage.ec"))
}


//plugins {
//    id("com.android.application")
//    jacoco
//}
//
//
//jacoco {
//    toolVersion = "0.8.8"
//}
//
//android {
//    namespace = "com.example.myapplication"
//    compileSdk = 33
//
//    testOptions {
//        unitTests.isIncludeAndroidResources = true
//    }
//    buildFeatures {
//        buildConfig = true
//        // ...
//    }
//
//
//    defaultConfig {
//        applicationId = "com.example.myapplication"
//        minSdk = 29
//        targetSdk = 33
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//}
//
//dependencies {
//
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.gms:play-services-maps:18.1.0")
//    implementation("com.google.android.material:material:1.8.0")
//    implementation("com.google.maps.android:android-maps-utils:3.8.2")//--heat maps test
//    implementation ("com.google.android.gms:play-services-location:17.0.0")// get current location
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    testImplementation("junit:junit:4.13.2")
////    testImplementation ("org.robolectric:robolectric:4.7.3")
//    testImplementation ("org.mockito:mockito-inline:4.5.1")
////    testImplementation ("org.mockito:mockito-core:4.5.1")
//    androidTestImplementation ("androidx.test:core:1.5.0")
//    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
//    androidTestImplementation ("org.mockito:mockito-android:4.5.1")
////    androidTestImplementation ("androidx.test:core:1.4.0")
//    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.2.0")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation ("androidx.test:runner:1.5.2")
//    androidTestImplementation ("androidx.test:rules:1.5.0")
//
//}
//
//
//tasks.register<JacocoReport>("jacocoTestReport") {
//    dependsOn("testDebugUnitTest")
//
//    reports {
//        xml.required.set(true)
//        html.required.set(true)
//    }
//
//    val fileFilter = listOf("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*")
//
//    val debugTree = fileTree(mapOf("dir" to "$buildDir/intermediates/javac/debug", "excludes" to fileFilter))
//    val mainSrc = "$projectDir/src/main/java"
//
//    sourceDirectories.setFrom(files(listOf(mainSrc)))
//    classDirectories.setFrom(files(listOf(debugTree)))
//    executionData.setFrom(files(listOf("$buildDir/jacoco/testDebugUnitTest.exec")))
//}
