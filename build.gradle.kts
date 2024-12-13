plugins {
    id("com.android.application") version "8.7.0"
    id("org.jetbrains.kotlin.android") version "2.0.21"
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" // Add the KSP plugin if needed
}