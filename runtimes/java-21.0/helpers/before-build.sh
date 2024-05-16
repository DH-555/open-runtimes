#!/bin/sh
# Fail build if any command fails
set -e

echo "Preparing for build ..."

# Copy from mounted volume to temporary folder
cp -R /mnt/code/* /usr/local/build

# Copy user code to server code
mkdir -p /usr/local/server/src/main/java/io/openruntimes/java
cp -a /usr/local/build/. /usr/local/server/src/main/java/io/openruntimes/java

# Link user's depenrencies
cd /usr/local/server/src/main/java/io/openruntimes/java
for filename in ./*.gradle*; do
    if [ ! -f "${filename}" ]; then
        continue;
    fi
    mv "${filename}" "/usr/local/server/${filename}"
    echo "apply from: \"${filename}\"" >> /usr/local/server/build.gradle
done

echo 'Building ...'

# Enter build folder
cd /usr/local/build