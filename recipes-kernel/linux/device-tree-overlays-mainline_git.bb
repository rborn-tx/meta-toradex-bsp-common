inherit toradex-devicetree

SRCBRANCH = "toradex_5.4.y"
SRCREV = "b4bc095e8b8c2613c8c9a10c1895b544652f8cf4"
SRCREV_use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE = ".*(mx[678]|tegra124).*"
