#!/bin/sh

if [ -f /proc/device-tree/toradex,product-id ]; then
    product_id=$(printf "0x%X\n" $((0x4000 + 0x$(tr -d "\0" < /proc/device-tree/toradex,product-id))))
    echo $product_id > /sys/kernel/config/usb_gadget/g1/idProduct
fi

if [ -f /proc/device-tree/serial-number ]; then
    serial_number=$(tr -d "\0" < /proc/device-tree/serial-number)
    echo $serial_number > /sys/kernel/config/usb_gadget/g1/strings/0x409/serialnumber
fi
