inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "6e4765109a648b641da2916e415eeedcd58ca36b"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
