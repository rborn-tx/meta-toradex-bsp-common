inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "4d391402e393bbdd22e0989fc966502358514794"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
