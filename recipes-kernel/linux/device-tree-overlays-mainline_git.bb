inherit toradex-devicetree

SRCBRANCH = "toradex_5.4.y"
SRCREV = "8f727c038e267c4388b1a27d8e5a460b130d36a6"
SRCREV_use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
