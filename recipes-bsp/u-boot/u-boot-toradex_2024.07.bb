require u-boot-toradex-common.inc

SRCREV = "3f772959501c99fbe5aa0b22a36efe3478d1ae1c"

TDX_PATCHES = "\
    file://0001-board-toradex-verdin-imx8mm-add-4-GB-lpddr4-memory-s.patch \
    file://0002-board-toradex-verdin-imx8mm-increase-maximum-address.patch \
    file://0003-toradex-tdx-cfg-block-add-aquila-am69-sku-0088-pid4.patch \
    file://0004-toradex-tdx-cfg-block-add-verdin-imx95-sku-0089-pid4.patch \
    file://0005-toradex-tdx-cfg-block-add-verdin-i.mx8m-mini-0090-pi.patch \
    file://0006-ARM-imx-verdin-imx8mm-Set-CAN-oscillator-frequency-b.patch \
    file://0001-configs-verdin-imx8m-mp-set-CONFIG_SPL_LOAD_FIT_ADDR.patch \
"
