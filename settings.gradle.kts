pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "VPSERVIICE"
include(":app")

sourceControl {
    gitRepository(uri("https://github.com/schwabe/ics-openvpn")) {
        // این خط به Gradle می‌گوید چه ماژولی از این ریپو تولید می‌شود
        producesModule("de.blinkt.openvpn:openvpn")
    }
}
