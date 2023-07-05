inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "95bde7f1893017b9de14e106c0830faecccda85d"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
