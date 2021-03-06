inherit toradex-devicetree

SRCBRANCH = "toradex_5.4.y"
SRCREV = "ea060d31211b2977bd12cabc886c151433e11c31"
SRCREV_use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
