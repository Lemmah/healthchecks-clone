#!/usr/bin/env bash

set -x
set -o errexit
set -o pipefail

# Ensure that we're on the app directory
cd ~/healthchecksapp/healthchecks-clone

# Use example local settings for db configs
cp hc/local_settings.py.example hc/local_settings.py

cd ~/healthchecksapp/healthchecks-clone

# Run the migrate command
./manage.py makemigrations accounts admin api auth contenttypes payments sessions
./manage.py migrate

