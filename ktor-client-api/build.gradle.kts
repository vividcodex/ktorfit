import com.vanniktech.maven.publish.SonatypeHost

plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.serialization)
	alias(libs.plugins.android.library)
	alias(libs.plugins.maven.publish)
}

val ktorClientKspVersion: String by project

group = "cn.vividcode.multiplatform.ktor.client.api"
version = ktorClientKspVersion

kotlin {
	androidTarget {
		compilations.all {
			kotlinOptions {
				jvmTarget = "17"
			}
		}
	}
	jvm("desktop") {
		compilations.all {
			kotlinOptions {
				jvmTarget = "17"
			}
		}
	}
	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64()
	).forEach {
		it.binaries.framework {
			baseName = "VividCodeKtorClientApi"
			isStatic = true
		}
	}
	sourceSets {
		commonMain.dependencies {
			implementation(libs.krypto)
			api(libs.ktor.client.core)
			api(libs.ktor.client.logging)
			api(libs.ktor.client.cio)
			api(libs.ktor.client.serialization)
			api(libs.ktor.client.content.negotiation)
			api(libs.ktor.serialization.kotlinx.json)
		}
	}
}

android {
	namespace = "cn.vividcode.multiplatform.ktor.client.api"
	compileSdk = 34
	
	sourceSets["main"].apply {
		manifest.srcFile("src/androidMain/AndroidManifest.xml")
		res.srcDirs("src/androidMain/res")
		resources.srcDirs("src/commonMain/resources")
	}
	
	defaultConfig {
		minSdk = 24
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
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.13"
	}
}

mavenPublishing {
	publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
	signAllPublications()
	
	coordinates("cn.vividcode.multiplatform", "ktor-client-api", ktorClientKspVersion)
	
	pom {
		name.set("ktor-client-api")
		description.set("这是一个 Kotlin Multiplatform 用于生成 Ktor 网络请求代码的框架")
		inceptionYear.set("2024")
		url.set("https://gitlab.com/vividcode/multiplatform-ktor-client")
		licenses {
			license {
				name.set("The Apache License, Version 2.0")
				url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
				distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
			}
		}
		developers {
			developer {
				id.set("li-jia-wei")
				name.set("li-jia-wei")
				url.set("https://gitlab.com/vividcode/multiplatform-ktor-client")
			}
		}
		
		scm {
			url.set("https://gitlab.com/vividcode/multiplatform-ktor-client")
			connection.set("scm:git:git://gitlab.com/vividcode/multiplatform-ktor-client.git")
			developerConnection.set("scm:git:ssh://git@gitlab.com:vividcode/multiplatform-ktor-client.git")
		}
	}
}