plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt") version "1.9.23"
}

android {
    namespace = "com.example.flashcardsystent"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.flashcardsystent"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "21"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

afterEvaluate {
    val androidExtension = project.extensions.findByName("android") as? com.android.build.gradle.AppExtension
    if (androidExtension != null) {
        tasks.register<org.gradle.api.tasks.javadoc.Javadoc>("generateJavadoc") {
            val variant = androidExtension.applicationVariants.first { it.name == "debug" }

            source = variant.javaCompileProvider.get().source
            classpath = files(variant.javaCompileProvider.get().classpath) + files(androidExtension.bootClasspath)

            (options as org.gradle.external.javadoc.StandardJavadocDocletOptions).apply {
                addStringOption("Xdoclint:none", "-quiet")
                encoding = "UTF-8"
                charSet = "UTF-8"
            }

            exclude("**/module-info.java")
            isFailOnError = false
        }
    } else {
        println("⚠️ Could not find android extension. Skipping javadoc task.")
    }
}

