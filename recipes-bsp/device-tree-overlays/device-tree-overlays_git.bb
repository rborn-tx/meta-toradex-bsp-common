SUMMARY = "Toradex BSP device tree overlays"
DESCRIPTION = "Toradex BSP device tree overlays from within layer."

SRC_URI = "git://git.toradex.com/device-tree-overlays.git;branch=${SRCBRANCH};protocol=https"

SRCBRANCH = "toradex_4.14-2.3.x-imx"
SRCBRANCH_use-mainline-bsp = "toradex_5.4.y"

SRCREV = "fc95c1137968488a172c45dc66813fd3e79286e8"
SRCREV_use-mainline-bsp = "f73d8b1caf5b6b837ed39e2bdd60040163d43c56"
SRCREV_use-head-next = "${AUTOREV}"

PV = "1.0.0+git${SRCPV}"

inherit devicetree

S = "${WORKDIR}/git/overlays"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
