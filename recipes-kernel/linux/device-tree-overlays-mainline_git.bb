SUMMARY = "Toradex BSP device tree overlays"
DESCRIPTION = "Toradex BSP device tree overlays from within layer."

SRC_URI = "git://git.toradex.com/device-tree-overlays.git;branch=${SRCBRANCH};protocol=https"

SRCBRANCH = "toradex_5.4.y"

SRCREV = "ce7532bf33e799571d04abc57cf9d73b86c37b02"
SRCREV_use-head-next = "${AUTOREV}"

PV = "${SRCBRANCH}+git${SRCPV}"

inherit devicetree

S = "${WORKDIR}/git/overlays"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
