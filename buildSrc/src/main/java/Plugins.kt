object Plugins {
    const val application = "com.android.application"
    const val jetBrains = "org.jetbrains.kotlin.android"
    const val navSaveArgs = "androidx.navigation.safeargs"
    const val ktlint = "org.jlleitschuh.gradle.ktlint"
    const val parcelize = "kotlin-parcelize"
    const val ksp = "com.google.devtools.ksp"
    const val navGradle = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navGradle}"

    object Versions {
        const val navGradle = "2.7.6"
        const val application = "8.2.0"
        const val jetBrains = "1.9.21"
        const val navSaveArgs = "2.5.3"
        const val ksp = "1.9.21-1.0.15"
        const val ktlint = "11.6.1"
    }
}