pluginManagement {
    repositories {
        // 阿里云Gradle插件镜像（优先使用）
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin/") }
        // 阿里云Google镜像
        maven { url = uri("https://maven.aliyun.com/repository/google/") }
        // 华为云Maven镜像
        maven { url = uri("https://mirrors.huaweicloud.com/repository/maven/") }
        // 阿里云Maven镜像
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        // 腾讯云Maven镜像
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
        // 保留原始仓库
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        // 阿里云Google镜像（优先使用）
        maven { url = uri("https://maven.aliyun.com/repository/google/") }
        // 华为云Maven镜像
        maven { url = uri("https://mirrors.huaweicloud.com/repository/maven/") }
        // 阿里云Maven镜像
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        // 腾讯云Maven镜像
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
        // 保留原始仓库
        google()
        mavenCentral()
    }
}

rootProject.name = "INgress"
include(":app")
