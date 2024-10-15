#!/usr/bin/env python

from scripting_commons import *

avd_path = make_path (user_home (), ".android", "avd")
avd = read_file (make_path (cwd (), "debug_device")).strip()
emulator = make_path (env ("ANDROID_HOME"), "emulator", "emulator")

device_path = make_path (avd_path, (avd + ".avd"))

if (avd == None) or (str_empty (avd)):
    print ("ERROR: 'debug_device' file is empty or non-existant.")
    print ()
    exit (1)

if not dir_exists (device_path):
    print_va ("ERROR: Cannot locate '$[0]'", (avd + ".avd"))
    print ()
    exit (1)

exec ([emulator, "-avd", avd])

