IMX_FIRMWARE_SRC ?= "git://github.com/NXP/imx-firmware.git;protocol=https"
SRCBRANCH_imx-firmware = "lf-6.1.22_2.0.0"
SRC_URI += " \
    ${IMX_FIRMWARE_SRC};branch=${SRCBRANCH_imx-firmware};destsuffix=imx-firmware;name=imx-firmware \
"

SRCREV_imx-firmware = "b6f070e3d4cab23932d9e6bc29e3d884a7fd68f4"

SRCREV_FORMAT = "default_imx-firmware"

do_install:append () {
    # Install NXP Connectivity SDIO8997 firmware
    install -d ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/wifi_mod_para.conf                        ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997_SD/ed_mac_ctrl_V3_8997.conf  ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997_SD/sdiouart8997_combo_v4.bin ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_8997_SD/txpwrlimit_cfg_8997.conf  ${D}${nonarch_base_libdir}/firmware/nxp

    # Upstream SDIO8997 driver firmware is located elsewhere
    install -d ${D}${nonarch_base_libdir}/firmware/mrvl
    ln -frs ${D}${nonarch_base_libdir}/firmware/nxp/sdiouart8997_combo_v4.bin ${D}${nonarch_base_libdir}/firmware/mrvl/sdiouart8997_combo_v4.bin

    # Install IW416 firmware
    install -m 0644 ${WORKDIR}/imx-firmware/nxp/FwImage_IW416_SD/sdiouartiw416_combo_v0.bin ${D}${nonarch_base_libdir}/firmware/nxp
    ln -frs ${D}${nonarch_base_libdir}/firmware/nxp/sdiouartiw416_combo_v0.bin ${D}${nonarch_base_libdir}/firmware/mrvl/sdiouartiw416_combo_v0.bin
}

PACKAGES =+ " \
    ${PN}-iw416 \
    ${PN}-nxp89xx \
"

FILES:${PN}-iw416 = " \
       ${nonarch_base_libdir}/firmware/mrvl/sdiouartiw416_combo_v0.bin \
       ${nonarch_base_libdir}/firmware/nxp/sdiouartiw416_combo_v0.bin \
       ${nonarch_base_libdir}/firmware/nxp/wifi_mod_para.conf \
"

FILES:${PN}-nxp89xx = " \
       ${nonarch_base_libdir}/firmware/mrvl/sdiouart8997_combo_v4.bin \
       ${nonarch_base_libdir}/firmware/nxp/ed_mac_ctrl_V3_8997.conf \
       ${nonarch_base_libdir}/firmware/nxp/sdiouart8997_combo_v4.bin \
       ${nonarch_base_libdir}/firmware/nxp/txpwrlimit_cfg_8997.conf \
       ${nonarch_base_libdir}/firmware/nxp/wifi_mod_para.conf \
"
