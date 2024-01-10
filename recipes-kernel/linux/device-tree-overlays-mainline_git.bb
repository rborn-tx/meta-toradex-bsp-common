inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "7f35a26c74084d29244a032bce9a420b1564c116"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
