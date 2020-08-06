#!/usr/bin/env sh
#
# Called from udev. Compatible to colibri-imx6.
#
# Create/remove symlinks to/from the files with raw ain data.

# Map the ADC lines:
# colibri-ain{0..3} -> STMPE811 ADC{4,5,6,7} in terms of the driver.
LINES=4
START=4

if [ "$ACTION" = "add" ]; then
    for idx in `seq 0 $((LINES-1))`; do
        ln -s "/sys$DEVPATH/in_voltage$((START+idx))_raw" /dev/colibri-ain$idx
    done
elif [ "$ACTION" = "remove" ]; then
    for idx in `seq 0 $((LINES-1))`; do
        rm -f /dev/colibri-ain$idx
    done
fi

