// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.application) version Plugins.Versions.application apply false
    id(Plugins.jetBrains) version Plugins.Versions.jetBrains apply false
    id(Plugins.navSaveArgs) version Plugins.Versions.navSaveArgs apply false
    id(Plugins.ksp) version Plugins.Versions.ksp apply false
}
