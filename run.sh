#!/usr/bin/env bash

set -e

mvn package
docker build -t dynamicip-chrome-java .
docker run -v /dev/shm:/dev/shm dynamicip-chrome-java