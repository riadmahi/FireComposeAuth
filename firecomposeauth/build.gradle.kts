import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.vanniktech.maven.publish)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "FireComposeAuth: Firebase Auth for Compose Multiplatform"
        homepage = "https://github.com/riadmahi/FireComposeAuth"
        ios.deploymentTarget = "15.0"
        pod("FirebaseAuth") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("FirebaseCore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        framework {
            baseName = "FireComposeAuth"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.firebase.auth)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.riadmahi.firecomposeauth"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("com.riadmahi", "firecomposeauth")

    pom {
        name.set("FireComposeAuth")
        description.set("Compose Multiplatform library for Firebase Authentication integration")
        inceptionYear.set("2024")
        url.set("https://github.com/riadmahi/FireComposeAuth")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("riadmahi")
                name.set("Riad Mahi")
                email.set("contact@riadmahi.com")
                url.set("https://github.com/riadmahi")
            }
        }

        scm {
            url.set("https://github.com/riadmahi/FireComposeAuth")
            connection.set("scm:git:git://github.com/riadmahi/FireComposeAuth.git")
            developerConnection.set("scm:git:ssh://git@github.com/riadmahi/FireComposeAuth.git")
        }
    }
}
