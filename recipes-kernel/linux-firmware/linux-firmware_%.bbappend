IMX_FIRMWARE_SRC ?= "git://github.com/NXP/imx-firmware.git;protocol=https"
SRCBRANCH_imx-firmware = "lf-6.6.3_1.0.0"
SRC_URI += " \
    ${IMX_FIRMWARE_SRC};branch=${SRCBRANCH_imx-firmware};destsuffix=${UNPACKDIR}/imx-firmware;name=imx-firmware \
"

SRCREV_imx-firmware = "2afa15e77f0b58eade42b4f59c9215339efcca66"

SRCREV_FORMAT = "default_imx-firmware"

do_patch:append() {
    src = oe.path.join(d.getVar("UNPACKDIR"), "/imx-firmware")
    dest = oe.path.join(d.getVar("S"), "/imx-firmware")
    oe.path.copyhardlinktree(src, dest)
}

do_install:append () {
    # Install IMX Firmware EULA/LICENSE license
    install -d ${D}${nonarch_base_libdir}/firmware
    install -m 0644 imx-firmware/LICENSE.txt ${D}${nonarch_base_libdir}/firmware/LICENSE.imx

    # Install common IMX firmware files
    install -d ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 imx-firmware/nxp/wifi_mod_para.conf                        ${D}${nonarch_base_libdir}/firmware/nxp

    # Install NXP Connectivity SDIO8997 firmware
    install -m 0644 imx-firmware/nxp/FwImage_8997_SD/ed_mac_ctrl_V3_8997.conf  ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 imx-firmware/nxp/FwImage_8997_SD/sdiouart8997_combo_v4.bin ${D}${nonarch_base_libdir}/firmware/nxp
    install -m 0644 imx-firmware/nxp/FwImage_8997_SD/txpwrlimit_cfg_8997.conf  ${D}${nonarch_base_libdir}/firmware/nxp

    # Upstream SDIO8997 driver firmware is located elsewhere
    install -d ${D}${nonarch_base_libdir}/firmware/mrvl
    ln -frs ${D}${nonarch_base_libdir}/firmware/nxp/sdiouart8997_combo_v4.bin ${D}${nonarch_base_libdir}/firmware/mrvl/sdiouart8997_combo_v4.bin

    # Install IW416 firmware
    install -m 0644 imx-firmware/nxp/FwImage_IW416_SD/sdiouartiw416_combo_v0.bin ${D}${nonarch_base_libdir}/firmware/nxp
    ln -frs ${D}${nonarch_base_libdir}/firmware/nxp/sdiouartiw416_combo_v0.bin ${D}${nonarch_base_libdir}/firmware/mrvl/sdiouartiw416_combo_v0.bin
}

LICENSE += " \
    & firmware-imx \
"
LIC_FILES_CHKSUM += " \
    file://imx-firmware/LICENSE.txt;md5=44a8052c384584ba09077e85a3d1654f \
"
NO_GENERIC_LICENSE[firmware-imx] = "imx-firmware/LICENSE.txt"

PACKAGES =+ " \
    ${PN}-imx-common \
    ${PN}-imx-license \
    ${PN}-iw416 \
    ${PN}-nxp89xx \
"

FILES:${PN}-imx-common = "${nonarch_base_libdir}/firmware/nxp/wifi_mod_para.conf"

FILES:${PN}-imx-license = "${nonarch_base_libdir}/firmware/LICENSE.imx"

FILES:${PN}-iw416 = " \
       ${nonarch_base_libdir}/firmware/mrvl/sdiouartiw416_combo_v0.bin \
       ${nonarch_base_libdir}/firmware/nxp/sdiouartiw416_combo_v0.bin \
"

LICENSE:${PN}-iw416 = "firmware-imx"

RDEPENDS:${PN}-iw416 += "${PN}-imx-common ${PN}-imx-license"

FILES:${PN}-nxp89xx = " \
       ${nonarch_base_libdir}/firmware/mrvl/sdiouart8997_combo_v4.bin \
       ${nonarch_base_libdir}/firmware/nxp/ed_mac_ctrl_V3_8997.conf \
       ${nonarch_base_libdir}/firmware/nxp/sdiouart8997_combo_v4.bin \
       ${nonarch_base_libdir}/firmware/nxp/txpwrlimit_cfg_8997.conf \
"

LICENSE:${PN}-nxp89xx = "firmware-imx"

RDEPENDS:${PN}-nxp89xx += "${PN}-imx-common ${PN}-imx-license"
