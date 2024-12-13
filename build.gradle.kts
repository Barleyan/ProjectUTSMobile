// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Android application plugin, version 8.7.0 for the entire project
    id("com.android.application") version "8.7.0" apply false

    // Kotlin plugin for Android, version 2.0.21 for the entire project
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false

    // Kotlin Symbol Processing plugin, version 2.0.21-1.0.25, not applied globally
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" apply false

    // Google services plugin (e.g., for Firebase), not applied globally
    alias(libs.plugins.google.gms.google.services) apply false
}