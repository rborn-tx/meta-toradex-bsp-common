inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "653cb21b42a735b8dca99489837c0baa61ec1503"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
