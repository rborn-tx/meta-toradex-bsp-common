#!/usr/bin/env sh
#
# Called from udev. Compatible to aquila-am69.
#
# Create/remove symlinks to/from the files with raw adc data.

# Map the ADC lines:
# aquila-adc{1,2,3,4} -> AM69 SoC MCU_ADC0_AIN{0,1,2,3}

if [ "$ACTION" = "add" ]; then
    ln -s "/sys$DEVPATH/in_voltage0_raw" /dev/aquila-adc1
    ln -s "/sys$DEVPATH/in_voltage1_raw" /dev/aquila-adc2
    ln -s "/sys$DEVPATH/in_voltage2_raw" /dev/aquila-adc3
    ln -s "/sys$DEVPATH/in_voltage3_raw" /dev/aquila-adc4
elif [ "$ACTION" = "remove" ]; then
    rm -f /dev/aquila-adc1
    rm -f /dev/aquila-adc2
    rm -f /dev/aquila-adc3
    rm -f /dev/aquila-adc4
fi
