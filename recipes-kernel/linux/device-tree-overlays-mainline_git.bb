inherit toradex-devicetree

SRCBRANCH = "toradex_5.4.y"
SRCREV = "653cb21b42a735b8dca99489837c0baa61ec1503"
SRCREV_use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
