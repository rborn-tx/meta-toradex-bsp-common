inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "1e210e85bc2fc462d874412a855b10d9f86b31fd"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE:tdx = ".*"
COMPATIBLE_MACHINE = "^$"
