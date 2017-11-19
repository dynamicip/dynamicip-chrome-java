#!/bin/bash
#
# ADAPTED FROM: https://github.com/SeleniumHQ/docker-selenium/blob/3.7.1-argon/StandaloneChrome/entry_point.sh
#
source /opt/bin/functions.sh

export GEOMETRY="$SCREEN_WIDTH""x""$SCREEN_HEIGHT""x""$SCREEN_DEPTH"

function shutdown {
  kill -s SIGTERM $NODE_PID
  wait $NODE_PID
}

SERVERNUM=$(get_server_num)

rm -f /tmp/.X*lock

xvfb-run -n $SERVERNUM --server-args="-screen 0 $GEOMETRY -ac +extension RANDR" \
  java -jar /opt/dynamicip/scraping-example/com.dynamicip.example.jar &
NODE_PID=$!

trap shutdown SIGTERM SIGINT
wait $NODE_PID