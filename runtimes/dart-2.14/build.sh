#!/bin/sh

# Copy User Code
cp -a /usr/code/* /usr/local/src/user_code

# make lib directory if not exist
mkdir -p /usr/local/src/user_code/lib

# Rename Main Function Dart
mv /usr/local/src/user_code/$INTERNAL_RUNTIME_ENTRYPOINT /usr/local/src/user_code/lib/main.dart

# Move all other user code into directory
mv /usr/local/src/user_code/* /usr/local/src/user_code/lib

cd /usr/local/src/user_code/

# Add a pubspec.yaml if one doesn't already exist.
if [ ! -f "pubspec.yaml" ]; then
    cp /usr/local/src/pubspec.yaml.fallback /usr/local/src/user_code/pubspec.yaml
fi

# Move to prepare_package script directory
cd /usr/local/src/prepare
dart pub get
dart prepare.dart

# Move to server directory
cd /usr/local/src
dart pub get

cd /usr/local/src/user_code
# Get user code dependencies
dart pub get

# Move back to server directory
cd /usr/local/src

# Compile the Code
dart compile exe server.dart -o runtime

# Finish build by preparing tar to use for starting the runtime
tar --exclude code.tar.gz -zcf /usr/code/code.tar.gz runtime