inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "3b9521e502143c15963baf27d5e0e46edb5c08ee"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
