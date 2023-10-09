require linux-toradex-mainline-common.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-toradex-mainline-rt-git:"

SUMMARY = "Toradex mainline real-time Linux kernel"
# To build the RT kernel we use the RT kernel git repo rather than applying
# the RT patch on top of a vanilla kernel.

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/rt/linux-rt-devel.git"
TDX_PATCHES = " \
    file://0001-thermal-imx-Update-critical-temp-threshold.patch \
    file://0002-Revert-drm-panel-simple-drop-use-of-data-mapping-pro.patch \
    file://0003-drivers-chipidea-disable-runtime-pm-for-imx6ul.patch \
    file://0004-media-i2c-ov5640-Implement-get_mbus_config.patch \
"
SRC_URI:append = " \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"

LINUX_VERSION ?= "6.5-rc"
KBRANCH = "linux-6.5.y-rt"
SRCREV_machine = "16ae0de52ef9f7e5c2cd7e8443f5195c176353ea"
SRCREV_machine:use-head-next = "${AUTOREV}"
