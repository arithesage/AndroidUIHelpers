import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

pluginManagement {
    repositories {
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

val projects: Path = Path (
    System.getenv("HOME"),
    "Proyectos",
    "Propios",
    "Android"
)

val wipProjects: Path = Path (
    projects.toFile().absolutePath,
    "_WIP"
)

rootProject.name = "UIHelpers"
include(":app")
include(":lib")

include (":Helpers-Testing")
project (":Helpers-Testing").projectDir = File (
    wipProjects.toFile(),
    "Helpers/lib"
)

//include (":Helpers")
//project (":Helpers").projectDir = File (wipProjects.toFile(), "Helpers/lib")









