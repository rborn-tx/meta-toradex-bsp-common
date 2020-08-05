FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_append = " \
    file://0001-Dont-store-to-device-if-no-value-changes.patch \
    file://0001-uboot_env-Use-canonicalized-pathname-when-reading-de.patch \
"

RRECOMMENDS_${PN}-bin_class-target += "u-boot-default-env"
