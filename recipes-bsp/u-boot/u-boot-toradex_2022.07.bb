require u-boot-toradex-common.inc

# hash of release v2022.07"
SRCREV = "e092e3250270a1016c877da7bdd9384f14b1321e"
# patches which are not in the used stable version
TDX_PATCHES = " \
    file://0001-toradex-tdx-cfg-block-use-only-snprintf.patch \
    file://0002-toradex-tdx-cfg-block-use-defines-for-string-length.patch \
    file://0003-toradex-tdx-cfg-block-extend-assembly-version.patch \
    file://0004-toradex-tdx-cfg-block-add-new-toradex-oui-range.patch \
    file://0005-toradex-tdx-cfg-block-add-0068-i.mx-8m-mini-sku.patch \
    file://0006-toradex-common-Remove-stale-comments-about-modules-a.patch \
    file://0007-toradex-common-Use-ARRAY_SIZE-macro.patch \
    file://0008-toradex-tdx-cfg-block-Cleanup-interactive-cfg-block-.patch \
    file://0009-toradex-common-Remove-stale-function-declaration.patch \
    file://0010-toradex-common-Remove-ifdef-usage-for-2nd-ethaddr.patch \
    file://0011-toradex-tdx-cfg-block-Use-official-SKU-names.patch \
    file://0012-toradex-common-Improve-product-serial-print-during-b.patch \
    file://0013-configs-colibri-imx7-Enable-bootd-command.patch \
    file://0001-ARM-imx8mp-verdin-imx8mp-Add-memory-size-detection.patch \
    file://0001-apalis-colibri_imx6-imx6ull-_imx7-update-env-memory-.patch \
    file://0001-configs-colibri-imx7-Fix-bad-block-table-in-flash-co.patch \
    file://0001-colibri_imx6-fix-RALAT-and-WALAT-values.patch \
"
