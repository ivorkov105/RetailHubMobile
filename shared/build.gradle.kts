plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidLibrary {
        namespace = "studying.diplom.retailhub"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
	    experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.compose.uiTooling)
            implementation(libs.sqldelight.android.driver)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.websockets)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.bottom.sheet.navigator)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)
            
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            
            implementation(libs.multiplatform.settings)
            implementation(libs.sqldelight.coroutines)

            implementation(libs.vico.core)
            implementation(libs.vico.compose)
            implementation(libs.vico.compose.m3)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

sqldelight {
    databases {
        create("RetailHubDatabase") {
            packageName.set("studying.diplom.retailhub.database")
        }
    }
}

compose.resources {
    publicResClass = true
	packageOfResClass = "studying.diplom.retailhub.resources"
}
