// Every code sample on https://paywall.sanikant.dev lives in this module between
// `// region <name>` and `// endregion` markers. The site reads the regions at build time,
// so `./gradlew :docs-snippets:assembleDebug` proves the published code compiles.
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "dev.sanikant.m3.paywall.snippets"
    compileSdk {
        version = release(37)
    }
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":paywall"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.androidx.activity.compose)
    implementation(libs.play.billing.ktx)
    implementation(libs.revenuecat.purchases)
}
