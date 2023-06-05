inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "b1ce42f23fac37e997d1e3b1070bd6bdc0f610d3"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
