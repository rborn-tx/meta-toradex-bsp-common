SUMMARY = "Toradex BSP device tree overlays"
DESCRIPTION = "Toradex BSP device tree overlays from within layer."

SRC_URI = "git://git.toradex.com/device-tree-overlays.git;branch=${SRCBRANCH};protocol=https"

SRCBRANCH = "toradex_5.4.y"

SRCREV = "e1d0ef7ecc9503c3cfb8300ad33f372fc78094cb"
SRCREV_use-head-next = "${AUTOREV}"

PV = "${SRCBRANCH}+git${SRCPV}"

inherit devicetree

S = "${WORKDIR}/git/overlays"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
