inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "7fab0e4d7309bfbe1f6c04949e84ac212241e627"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
