#!/usr/bin/env bash
COUNT=0

while true
do
    COUNT=$((COUNT+1))
    curl http://localhost:9091/displayMessage
    echo " ${COUNT}"
    sleep 1
done
