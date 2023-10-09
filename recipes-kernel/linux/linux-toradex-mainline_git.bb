require linux-toradex-mainline-common.inc

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git"
TDX_PATCHES = " \
    file://0001-thermal-imx-Update-critical-temp-threshold.patch \
    file://0002-Revert-drm-panel-simple-drop-use-of-data-mapping-pro.patch \
    file://0003-drivers-chipidea-disable-runtime-pm-for-imx6ul.patch \
    file://0004-media-i2c-ov5640-Implement-get_mbus_config.patch \
"

LINUX_VERSION ?= "6.6-rc"
KBRANCH = "master"
SRCREV_machine = "2dde18cd1d8fac735875f2e4987f11817cc0bc2c"
SRCREV_machine:use-head-next = "${AUTOREV}"
