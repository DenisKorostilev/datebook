buildscript {
    dependencies {
        classpath(Plugins.navGradle)
    }
}

plugins {
    id(Plugins.application)
    id(Plugins.jetBrains)
    id(Plugins.navSaveArgs)
    id(Plugins.ktlint) version Plugins.Versions.ktlint
    id(Plugins.parcelize)
    id(Plugins.ksp)
}

android {
    namespace = "com.example.datebook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.datebook"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}
tasks.getByPath("preBuild").dependsOn("ktlintFormat")
ktlint {
    android
    ignoreFailures
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}
dependencies {
    implementation(Dependencies.Delegate.viewBinding)
    implementation(Dependencies.AndroidX.navGraph)
    implementation(Dependencies.Extra.koin)
    implementation(Dependencies.Delegate.adapterDelegate)
    implementation(Dependencies.Extra.dateTime)
    implementation(Dependencies.Network.retrofitMoshiConverter)
    implementation(Dependencies.Network.moshi)
    implementation(Dependencies.Network.json)
    implementation(Dependencies.DataBase.room)
    implementation(Dependencies.DataBase.roomKtx)
    annotationProcessor(Dependencies.DataBase.roomCompiler)
    ksp(Dependencies.DataBase.ksp)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.material)
    implementation(Dependencies.AndroidX.constraintLayout)
    testImplementation(Dependencies.AndroidX.jUnit)
    androidTestImplementation(Dependencies.AndroidX.jUnitTest)
    androidTestImplementation(Dependencies.AndroidX.espresso)
}
