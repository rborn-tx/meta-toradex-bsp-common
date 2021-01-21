SUMMARY = "Toradex BSP device tree overlays"
DESCRIPTION = "Toradex BSP device tree overlays from within layer."

SRC_URI = "git://git.toradex.com/device-tree-overlays.git;branch=${SRCBRANCH};protocol=https"

SRCBRANCH = "toradex_5.4.y"

SRCREV = "d9a72edca69215f9b836283be70288b371fd3899"
SRCREV_use-head-next = "${AUTOREV}"

PV = "${SRCBRANCH}+git${SRCPV}"

inherit devicetree

S = "${WORKDIR}/git/overlays"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
