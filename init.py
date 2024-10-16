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

from scripting_commons import ask_if_continue, make_path, replace_all_in
from shell import *




class Files:
    AndroidManifest = "AndroidManifest.xml"

    MainActivity = "MainActivity.kt"
    AndroidTest = "ExampleInstrumentedTest.kt"
    Test = "ExampleUnitTest.kt"
    
    GradleSettings = "settings.gradle.kts"
    GradleBuild = "build.gradle.kts"

    Strings = "strings.xml"
    Themes = "themes.xml"

    def __init__(self) -> None:
        pass


class Paths:
    APP = "app"
    Main = make_path (APP, "src/main")
    SRC_Main = make_path (Main, "java")
    SRC_AndroidTest =  make_path (APP, "src/androidTest/java")
    SRC_Test = make_path (APP, "src/test/java")
    Values = make_path (Main, "res/values")
    ValuesNight = make_path (Main, "res/values-night")

    def __init__(self) -> None:
        pass


class Messages:
    Done = "Done!"
    Failed = "Failed!"
    Finished = "Finished! Happy programming!"
    InitializationFailed = "Project initialization failed."

    def __init__(self) -> None:
        pass


class Placeholders:
    ProjectName = "BaseApp"
    Package = "me.android.baseapp"

    def __init__(self) -> None:
        pass




def abort ():
    echo ("Aborted.")
    echo ("")
    exit (1)


def check_if_already_initialized () -> None:
    if file_exists (make_path (Paths.SRC_Main, Files.MainActivity)):
        echo ("Android Project already initialized.")
        echo ()
        exit (1)


def usage ():
    echo ("Initializes this Android project.")
    echo ("Usage: init <project package>")
    echo ("")


def init_project (project_package: str):
    project_name = basename (realpath ("."))
    project_package_dirtree = project_package.replace (".", "/")

    main_src = make_path (Paths.SRC_Main,
                          project_package_dirtree, 
                          Files.MainActivity)
    
    androidtest_src = make_path (Paths.SRC_AndroidTest, 
                                 project_package_dirtree, 
                                 Files.AndroidTest)
    
    test_src = make_path (Paths.SRC_Test, 
                          project_package_dirtree, 
                          Files.Test)
    
    app_build_settings = make_path (Paths.APP, Files.GradleBuild)
    
    project_manifest = make_path (Paths.Main, Files.AndroidManifest)
    project_settings = Files.GradleSettings

    strings = make_path (Paths.Values, Files.Strings)
    themes_xml = make_path (Paths.Values, Files.Themes)
    themes_night_xml = make_path (Paths.ValuesNight, Files.Themes)
    

    echo ("Ready for initialize Android project")
    echo ("------------------------------------")
    echo_va ("Project name: $[0]", project_name)
    echo_va ("Package: $[0]", project_package)
    echo ("------------------------------------")

    proceed = ask_if_continue ()

    if not proceed:
        abort ()

    
    echo ("Creating project tree...")

    if not mkdir (make_path (Paths.SRC_Main, 
                             project_package_dirtree)) \
    or not mkdir (make_path (Paths.SRC_AndroidTest, 
                             project_package_dirtree)) \
    or not mkdir (make_path (Paths.SRC_Test, 
                             project_package_dirtree)):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    if not mv (make_path (Paths.SRC_Main, Files.MainActivity), 
               main_src) \
    or not mv (make_path (Paths.SRC_AndroidTest, Files.AndroidTest), 
               androidtest_src) \
    or not mv (make_path (Paths.SRC_Test, Files.Test), 
               test_src):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    echo_va ("Updating $[0], $[1] and $[2] ...", 
             Files.MainActivity, 
             Files.AndroidTest, 
             Files.Test)

    if not replace_all_in (Placeholders.Package, project_package, 
                           main_src) \
    or not replace_all_in (Placeholders.Package, project_package, 
                           androidtest_src) \
    or not replace_all_in (Placeholders.Package, project_package,
                           test_src):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    echo_va ("Updating $[0] and $[1] ...", 
             Files.GradleSettings, 
             Files.GradleBuild)

    if not replace_all_in (Placeholders.ProjectName, 
                           project_name, 
                           project_settings) \
    or not replace_all_in (Placeholders.Package, 
                           project_package, 
                           app_build_settings):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    echo_va ("Updating $[0] ...", Files.AndroidManifest)

    if not replace_all_in (Placeholders.ProjectName, 
                           project_name, 
                           project_manifest):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    echo_va ("Updating $[0] ...", "themes.xml")

    if not replace_all_in (Placeholders.ProjectName, 
                           project_name, 
                           themes_xml) \
    or not replace_all_in (Placeholders.ProjectName, 
                           project_name, 
                           themes_night_xml):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)

    
    echo_va ("Updating $[0] ...", Files.Strings)

    if not replace_all_in (Placeholders.ProjectName, 
                           project_name, 
                           strings):
        echo (Messages.Failed)
        echo (Messages.InitializationFailed)
        abort ()

    echo (Messages.Done)


    echo (Messages.Finished)



DEBUGGING = False

if __name__ == "__main__":
    if not DEBUGGING:
        argv = sys.argv
    else:
        argv = (None, "me.arithesage.kotlin.android.testapp")

    argc = len (argv[1:])

    if argc != 1:
        usage ()
        exit (1)

    init_project (argv[1])
    
