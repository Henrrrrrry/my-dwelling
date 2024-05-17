// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
}
buildscript{
    dependencies{
        classpath("org.jacoco:org.jacoco.core:0.8.8")
    }
}

