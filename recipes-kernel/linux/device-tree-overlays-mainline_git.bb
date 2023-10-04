inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "3dfb87f1966ee63be3c00268eaf7797f4d2bc1d5"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
