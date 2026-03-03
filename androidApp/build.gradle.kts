plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.composeCompiler)
}

android {
	namespace = "studying.diplom.retailhub.android"

	compileSdk = libs.versions.android.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "studying.diplom.retailhub"

		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()

		versionCode = 1
		versionName = "1.0"
	}

	buildFeatures {
		compose = true
	}

	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}

	sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")
}

dependencies {
	implementation(project(":shared"))

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.activity.compose)

	implementation(libs.compose.ui)
	implementation(libs.compose.material3)

	implementation(libs.koin.android)

	debugImplementation(libs.compose.uiTooling)
	debugImplementation(libs.compose.uiToolingPreview)
}