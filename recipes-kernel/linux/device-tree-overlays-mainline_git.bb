inherit toradex-devicetree

SRCBRANCH = "toradex_5.4.y"
SRCREV = "f4b74d8a48e76a626d66cdd9d5e358f630940005"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
