#!/usr/bin/env bash

adb reverse --remove-all

adb reverse tcp:8000 tcp:8000
adb reverse tcp:8001 tcp:8001

python main.py --address=0.0.0.0
