LICENSE_${PN}-rtl8188eu = "Firmware-rtlwifi_firmware"
LICENSE_${PN}-sd8997 = "Firmware-Marvell"

FILESEXTRAPATHS_append := "${THISDIR}/files:"

PACKAGES_prepend = "\
                     ${PN}-rtl8188eu \
                     ${PN}-sd8997 \
                   "
do_install_append() {
    ln -fs sdsd8997_combo_v4.bin ${D}${nonarch_base_libdir}/firmware/mrvl/sd8997_uapsta.bin
}

FILES_${PN}-rtl8188eu = " \
  ${nonarch_base_libdir}/firmware/rtlwifi/rtl8188eufw.bin \
"
FILES_${PN}-sd8997 = " \
  ${nonarch_base_libdir}/firmware/mrvl/sd8997_uapsta.bin \
  ${nonarch_base_libdir}/firmware/mrvl/sdsd8997_combo_v4.bin \
"
RDEPENDS_${PN}-rtl8188eu += "${PN}-rtl-license"
RDEPENDS_${PN}-sd8997 += "${PN}-marvell-license"
