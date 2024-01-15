object Dependencies {
    object Versions {
        const val adapterDelegate = "4.3.2"
        const val appCompat = "1.6.1"
        const val coreKtx = "1.12.0"
        const val dataBase = "2.6.1"
        const val dateTime = "0.4.1"
        const val espresso = "3.5.1"
        const val json = "2.10.1"
        const val jUnit = "4.13.2"
        const val jUnitTest = "1.1.5"
        const val koin = "3.4.1"
        const val layout = "2.1.4"
        const val material = "1.11.0"
        const val moshi = "1.14.0"
        const val moshiConverter = "2.9.0"
        const val navigation = "2.7.6"
        const val viewBinding = "1.5.9"
    }

    object AndroidX {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.layout}"
        const val jUnit = "junit:junit:${Versions.jUnit}"
        const val jUnitTest = "androidx.test.ext:junit:${Versions.jUnitTest}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
        const val navGraph = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    }

    object Delegate {
        const val adapterDelegate = "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:${Versions.adapterDelegate}"
        const val viewBinding = "com.github.kirich1409:viewbindingpropertydelegate-full:${Versions.viewBinding}"
    }

    object Network {
        const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
        const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.moshiConverter}"
        const val json = "com.google.code.gson:gson:${Versions.json}"
    }

    object Extra {
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.dateTime}"
        const val koin = "io.insert-koin:koin-android:${Versions.koin}"
    }

    object DataBase {
        const val room = "androidx.room:room-runtime:${Versions.dataBase}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.dataBase}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.dataBase}"
        const val ksp = "androidx.room:room-compiler:${Versions.dataBase}"
    }
}