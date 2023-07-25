IMX_FIRMWARE_SRC ?= "git://github.com/NXP/imx-firmware.git;protocol=https"
SRCBRANCH_imx-firmware = "lf-6.1.22_2.0.0"
SRC_URI += " \
    ${IMX_FIRMWARE_SRC};branch=${SRCBRANCH_imx-firmware};destsuffix=imx-firmware;name=imx-firmware \
"

SRCREV_imx-firmware = "f775d53ca3a478c85e8c8a880e44cc269bd14db0"

SRCREV_FORMAT = "default_imx-firmware"

do_install:append () {
    # Install IMX Firmware EULA license
    install -d ${D}${nonarch_base_libdir}/firmware
    install -m 0644 ${WORKDIR}/imx-firmware/EULA.txt ${D}${nonarch_base_libdir}/firmware/LICENSE.imx

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

LICENSE += " \
    & firmware-imx \
"
LIC_FILES_CHKSUM += " \
    file://${WORKDIR}/imx-firmware/EULA.txt;md5=673fa34349fa40f59e0713cb0ac22b1f \
"
NO_GENERIC_LICENSE[firmware-imx] = "${WORKDIR}/imx-firmware/EULA.txt"

PACKAGES =+ " \
    ${PN}-imx-license \
    ${PN}-iw416 \
    ${PN}-nxp89xx \
"

FILES:${PN}-imx-license = "${nonarch_base_libdir}/firmware/LICENSE.imx"

FILES:${PN}-iw416 = " \
       ${nonarch_base_libdir}/firmware/mrvl/sdiouartiw416_combo_v0.bin \
       ${nonarch_base_libdir}/firmware/nxp/sdiouartiw416_combo_v0.bin \
       ${nonarch_base_libdir}/firmware/nxp/wifi_mod_para.conf \
"

LICENSE:${PN}-iw416 = "firmware-imx"

RDEPENDS:${PN}-iw416 += "${PN}-imx-license"

FILES:${PN}-nxp89xx = " \
       ${nonarch_base_libdir}/firmware/mrvl/sdiouart8997_combo_v4.bin \
       ${nonarch_base_libdir}/firmware/nxp/ed_mac_ctrl_V3_8997.conf \
       ${nonarch_base_libdir}/firmware/nxp/sdiouart8997_combo_v4.bin \
       ${nonarch_base_libdir}/firmware/nxp/txpwrlimit_cfg_8997.conf \
       ${nonarch_base_libdir}/firmware/nxp/wifi_mod_para.conf \
"

LICENSE:${PN}-nxp89xx = "firmware-imx"

RDEPENDS:${PN}-nxp89xx += "${PN}-imx-license"
