#!/usr/bin/env python

import os
import sys

"""
To be sure that we use the Python modules included in '_scripts' folder,
and no other, we clear this process's PYTHONPATH environment variable
and set it to include the '_scripts' folder.
"""

os.environ ["PYTHONPATH"] = ""
sys.path.append (os.path.join (os.getcwd(), "_scripts"))

from scripting_commons import append_to_file, ask_if_continue, concat,\
                              file_contains, make_path, replace_all_in
from shell import *




class Data:
    AndroidLibraryEntry = concat ("androidLibrary = { ",
                                  "id = \"com.android.library\"",
                                  ", version.ref = \"agp\"",
                                  " }")
    
    ModuleEntry = concat ("\n", "include(\":$[module_name]\")", "\n")

    def __init__(self) -> None:
        pass


class Placeholders:
    Package = "me.android.modules"
    NativePackage = "me_android_modules"

    def __init__(self) -> None:
        pass


# class Files:
#     AndroidManifest = "AndroidManifest.xml"

#     MainActivity = "MainActivity.kt"
#     AndroidTest = "ExampleInstrumentedTest.kt"
#     Test = "ExampleUnitTest.kt"
    
#     GradleSettings = "settings.gradle.kts"
#     GradleBuild = "build.gradle.kts"

#     LibsVersions = "libs.versions.toml"

#     CMakeLists = "CMakeLists.txt"
#     NativeLib = "native-lib.cpp"

#     Strings = "strings.xml"
#     Themes = "themes.xml"

#     def __init__(self) -> None:
#         pass


class Files:
    MainActivity = "NativeLib.kt"
    AndroidTest = "ExampleInstrumentedTest.kt"
    Test = "ExampleUnitTest.kt"

    NativeLib = "nativelib.cpp"

    GradleBuild = "build.gradle.kts"
    GradleSettings = "settings.gradle.kts"
    LibsVersions = "libs.versions.toml"

    def __init__(self) -> None:
        pass


class ModulePaths:
    Root = ""
    SrcMain = ""
    SrcAndroidTest = ""
    SrcTest = ""
    SrcNative = ""

    def __init__ () -> None:
        pass

    @staticmethod
    def Init (module_name):
        ModulePaths.Root = make_path (".", module_name)

        ModulePaths.SrcMain = make_path (ModulePaths.Root, 
                                         "/src/main/java")
        
        ModulePaths.SrcAndroidTest = make_path (ModulePaths.Root, 
                                                "/src/androidTest/java")
        
        ModulePaths.SrcTest = make_path (ModulePaths.Root, 
                                         "/src/test/java")
        
        ModulePaths.SrcNative = make_path (ModulePaths.Root,
                                           "/src/main/cpp")


class Paths:
    BaseModule = make_path ("_BaseModule", "Native")
    BaseModuleSrcMain = make_path (BaseModule, "/src/main/java")
    BaseModuleSrcAndroidTest = make_path (BaseModule, "/src/androidTest/java")
    BaseModuleSrcTest = make_path (BaseModule, "/src/test/java")

    Gradle = "gradle"

    def __init__(self) -> None:
        pass


class Messages:
    Aborted = "Aborted!"
    Done = "Done!"
    Failed = "Failed!"
    Finished = "Finished!"
    InitializationFailed = "Module initialization failed."
    ModuleAlreadyExists = "A module named $[0] already exists!"

    def __init__(self) -> None:
        pass




def abort ():
    echo (Messages.Aborted)
    echo ("")
    exit (1)


def usage ():
    echo ("Creates a new Android native module in this project.")
    echo ("Usage: add_native_module <module name> <project package>")
    echo ("")


def init_module (module_name, package_name):
    module_entry = Data.ModuleEntry.replace("$[module_name]", module_name)
    project_settings = Files.GradleSettings

    if file_contains (project_settings, module_entry):
        echo_va (Messages.ModuleAlreadyExists, module_name)
        echo (Messages.InitializationFailed)
        abort ()

    libversions_toml = make_path (Paths.Gradle, 
                                  Files.LibsVersions)
    
    module_package_dirtree = package_name.replace (".", "/")
    module_native_package = package_name.replace (".", "_")

    ModulePaths.Init (module_name)

    mainactivity_src = make_path (ModulePaths.SrcMain,
                                  module_package_dirtree,
                                  Files.MainActivity)

    androidtest_src = make_path (ModulePaths.SrcAndroidTest, 
                                 module_package_dirtree, 
                                 Files.AndroidTest)
    
    test_src = make_path (ModulePaths.SrcTest, 
                          module_package_dirtree, 
                          Files.Test)

    nativelib_src = make_path (ModulePaths.SrcNative, 
                               Files.NativeLib)
    
    module_build_settings = make_path (ModulePaths.Root, Files.GradleBuild)


    echo ("Ready for creating Android native module")
    echo ("----------------------------------------")
    echo_va ("Module name: $[0]", module_name)
    echo_va ("Package: $[0]", package_name)
    echo ("----------------------------------------")

    proceed = ask_if_continue ()

    if not proceed:
        abort ()


    # First, copy the module template to the new module
    # destination.
    echo ("Creating module tree...")

    if not cp (Paths.BaseModule, ModulePaths.Root):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    # Next, create the package dir tree below of each one of the source trees.
    if not mkdir (make_path (ModulePaths.SrcMain, 
                             module_package_dirtree)) \
    or not mkdir (make_path (ModulePaths.SrcAndroidTest, 
                             module_package_dirtree)) \
    or not mkdir (make_path (ModulePaths.SrcTest, 
                             module_package_dirtree)):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    # Now is time to move the starting source files to their final locations.
    if not mv (make_path (ModulePaths.SrcMain, Files.MainActivity),
               mainactivity_src) \
    or not mv (make_path (ModulePaths.SrcAndroidTest, Files.AndroidTest), 
               androidtest_src) \
    or not mv (make_path (ModulePaths.SrcTest, Files.Test), 
               test_src):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    # We have to start updating the source and config files with the
    # new module data.

    # Update the .kt files with the new module's package
    echo_va ("Updating $[0], $[1] and $[2] ...", 
             Files.MainActivity, Files.AndroidTest, Files.Test)

    if not replace_all_in (Placeholders.Package,
                           package_name,
                           mainactivity_src) \
    or not replace_all_in (Placeholders.Package, 
                           package_name, 
                           androidtest_src) \
    or not replace_all_in (Placeholders.Package, 
                           package_name, 
                           test_src):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    # Update the CPP library
    echo_va ("Updating $[0] ...", Files.NativeLib)

    if not replace_all_in (Placeholders.NativePackage, 
                           module_native_package, 
                           nativelib_src):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    # Update the module's build config file
    echo_va ("Updating $[0] ...", Files.GradleBuild)

    if not replace_all_in (Placeholders.Package, 
                           package_name, 
                           module_build_settings):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    # Update the project's settings
    echo_va ("Updating $[0] ...", Files.GradleSettings)

    if not append_to_file (project_settings, module_entry):
        echo_va ("ERROR: Failed updating $[0].", project_settings)
        echo_va ("Append manually the module entry: $[0]", module_entry) 

    echo (Messages.Done)


    # Update the project's libs settings
    echo_va ("Updating $[0] ...", Files.LibsVersions)

    if not file_contains (libversions_toml, Data.AndroidLibraryEntry):
        if not append_to_file (libversions_toml, Data.AndroidLibraryEntry):
            echo (Messages.Failed)
            echo_va ("You'll need to append $[0] yourself to $[1].",
                     Data.AndroidLibraryEntry, libversions_toml)
        echo (Messages.Done)


    echo (Messages.Finished)




DEBUGGING = False

if __name__ == "__main__":
    if not DEBUGGING:
        argv = sys.argv
    else:
        argv = (None, "TestModule", "me.arithesage.kotlin.android.testapp.modules")

    argc = len (argv[1:])

    if argc != 2:
        usage ()
        exit (1)

    init_module (argv[1], argv[2])
