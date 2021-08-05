#!/usr/bin/env sh
#
# Called from udev. Compatible to colibri-imx6ull.
#
# Create/remove symlinks to/from the files with raw ain data.

# Map the ADC lines:
# colibri-ain{0..3} -> ADCx_IN{0,1,8,9}

if [ "$ACTION" = "add" ]; then
    ln -s "/sys$DEVPATH/in_voltage0_raw" /dev/colibri-ain0
    ln -s "/sys$DEVPATH/in_voltage1_raw" /dev/colibri-ain1
    ln -s "/sys$DEVPATH/in_voltage8_raw" /dev/colibri-ain2
    ln -s "/sys$DEVPATH/in_voltage9_raw" /dev/colibri-ain3
elif [ "$ACTION" = "remove" ]; then
    rm -f /dev/colibri-ain0
    rm -f /dev/colibri-ain1
    rm -f /dev/colibri-ain2
    rm -f /dev/colibri-ain3
fi

