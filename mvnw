#!/bin/sh
set -eu

BASE_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_PROPERTIES="$BASE_DIR/.mvn/wrapper/maven-wrapper.properties"

if [ ! -f "$WRAPPER_PROPERTIES" ]; then
  echo "No se encontró $WRAPPER_PROPERTIES" >&2
  exit 1
fi

DISTRIBUTION_URL=$(sed -n 's/^distributionUrl=//p' "$WRAPPER_PROPERTIES")

if [ -z "$DISTRIBUTION_URL" ]; then
  echo "No se encontró distributionUrl en $WRAPPER_PROPERTIES" >&2
  exit 1
fi

ARCHIVE_NAME=$(basename "$DISTRIBUTION_URL")
MAVEN_BASE_DIR="${HOME}/.m2/wrapper/dists"
ARCHIVE_PATH="${MAVEN_BASE_DIR}/${ARCHIVE_NAME}"
UNPACK_DIR="${MAVEN_BASE_DIR}/${ARCHIVE_NAME%.zip}"

mkdir -p "$MAVEN_BASE_DIR"

download_file() {
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$1" -o "$2"
    return
  fi

  if command -v wget >/dev/null 2>&1; then
    wget -q "$1" -O "$2"
    return
  fi

  echo "Se requiere curl o wget para descargar Maven." >&2
  exit 1
}

if [ ! -f "$ARCHIVE_PATH" ]; then
  echo "Descargando Maven desde $DISTRIBUTION_URL" >&2
  download_file "$DISTRIBUTION_URL" "$ARCHIVE_PATH"
fi

if [ ! -d "$UNPACK_DIR" ]; then
  mkdir -p "$UNPACK_DIR"
  unzip -q "$ARCHIVE_PATH" -d "$UNPACK_DIR"
fi

MAVEN_HOME=$(find "$UNPACK_DIR" -mindepth 1 -maxdepth 1 -type d -name 'apache-maven-*' | head -n 1)

if [ -z "$MAVEN_HOME" ]; then
  echo "No se pudo localizar la distribución de Maven desempaquetada." >&2
  exit 1
fi

exec "$MAVEN_HOME/bin/mvn" "$@"
