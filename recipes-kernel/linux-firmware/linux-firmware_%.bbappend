# Do this in python to stay backward/forward compatible with
# openembedded-core, morty.

python __anonymous () {
    pks = (d.getVar("PACKAGES", False) or "").split()

    if ("${PN}-rtl8188eu" not in pks):
        d.prependVar("PACKAGES", "${PN}-rtl8188eu ")
        d.setVar("FILES_${PN}-rtl8188eu", "/lib/firmware/rtlwifi/rtl8188eufw.bin")
        d.setVar("LICENSE_${PN}-rtl8188eu", "Firmware-rtlwifi_firmware")
        d.appendVar("RDEPENDS_${PN}-rtl8188eu", " ${PN}-rtl-license")

    if ("${PN}-sd8887" not in pks):
        d.prependVar("PACKAGES", "${PN}-sd8887 ")
        d.setVar("FILES_${PN}-sd8887", "/lib/firmware/mrvl/sd8887_uapsta.bin")
        d.setVar("LICENSE_${PN}-sd8887", "Firmware-Marvell")
        d.appendVar("RDEPENDS_${PN}-sd8887", " ${PN}-marvell-license")
}
