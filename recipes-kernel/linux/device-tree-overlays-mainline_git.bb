inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "72dec9868378bdf32c50a64236579164928b60bd"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]).*"
