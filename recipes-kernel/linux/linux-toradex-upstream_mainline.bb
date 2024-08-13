# This builds latest head of linux master
# Do not use this recipe for production, it is not reproducible
# and you don't know what git hash is built from just looking
# at the metadata

require linux-toradex-upstream.inc

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git"
TDX_PATCHES = " \
    file://0001-thermal-imx-Update-critical-temp-threshold.patch \
    file://0002-Revert-drm-panel-simple-drop-use-of-data-mapping-pro.patch \
    file://0004-media-i2c-ov5640-Implement-get_mbus_config.patch \
    file://0001-usb-gadget-f_ncm-Apply-workaround-for-packet-cloggin.patch \
"
# set PV manually, that way PREFERRED_VERSION can be set to a constant value
PV = "mainline"
LINUX_VERSION = "6.9-rc"
KBRANCH = "master"
SRCREV_machine = "${AUTOREV}"
