#!/usr/bin/env bash
set -euo pipefail

PNG_PATH="$1"
ICNS_PATH="$2"
ICONSET_DIR="target/jpackage/icon.iconset"

mkdir -p "$ICONSET_DIR"

sips -z 16 16 "$PNG_PATH" --out "$ICONSET_DIR/icon_16x16.png"
sips -z 32 32 "$PNG_PATH" --out "$ICONSET_DIR/icon_16x16@2x.png"
sips -z 32 32 "$PNG_PATH" --out "$ICONSET_DIR/icon_32x32.png"
sips -z 64 64 "$PNG_PATH" --out "$ICONSET_DIR/icon_32x32@2x.png"
sips -z 128 128 "$PNG_PATH" --out "$ICONSET_DIR/icon_128x128.png"
sips -z 256 256 "$PNG_PATH" --out "$ICONSET_DIR/icon_128x128@2x.png"
sips -z 256 256 "$PNG_PATH" --out "$ICONSET_DIR/icon_256x256.png"
sips -z 512 512 "$PNG_PATH" --out "$ICONSET_DIR/icon_256x256@2x.png"
sips -z 512 512 "$PNG_PATH" --out "$ICONSET_DIR/icon_512x512.png"
sips -z 1024 1024 "$PNG_PATH" --out "$ICONSET_DIR/icon_512x512@2x.png"
iconutil -c icns "$ICONSET_DIR" -o "$ICNS_PATH"
