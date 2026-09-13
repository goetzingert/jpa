#!/bin/sh

set -eu

WRAPPER_VERSION=3.9.11
CACHE_DIR="${MAVEN_USER_HOME:-${HOME}/.m2}/wrapper/dists/apache-maven-${WRAPPER_VERSION}"
MAVEN_HOME="${CACHE_DIR}/apache-maven-${WRAPPER_VERSION}"
ARCHIVE="${CACHE_DIR}/apache-maven-${WRAPPER_VERSION}-bin.zip"
URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${WRAPPER_VERSION}/apache-maven-${WRAPPER_VERSION}-bin.zip"

if [ ! -x "${MAVEN_HOME}/bin/mvn" ]; then
    mkdir -p "${CACHE_DIR}"
    if [ ! -f "${ARCHIVE}" ]; then
        curl -fL --retry 3 "${URL}" -o "${ARCHIVE}"
    fi
    rm -rf "${MAVEN_HOME}"
    unzip -q "${ARCHIVE}" -d "${CACHE_DIR}"
fi

exec "${MAVEN_HOME}/bin/mvn" "$@"
