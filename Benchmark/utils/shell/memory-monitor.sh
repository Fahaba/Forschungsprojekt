#! /bin/bash

OUT_FILE=$1

memory_stamp() {
    MEMORY=`free | grep -oP "[\s]+[0-9]+" | grep -oP "[0-9]+" | sed -n 2p`
    TIME=`date "+%s:%N"`
    echo -e "$TIME\t$MEMORY" >> "$OUT_FILE"
}

while true; do
  sleep 0.1
  memory_stamp
done