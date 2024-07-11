require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
SOC_HANDLING = ""
SOC_HANDLING:imx-generic-bsp = "recipes-bsp/u-boot/u-boot-toradex-imx.inc"
SOC_HANDLING:ti-soc = "recipes-bsp/u-boot/u-boot-toradex-ti.inc"
require ${SOC_HANDLING}

LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"
DEPENDS += " \
    bc-native \
    dtc-native \
    gnutls-native \
    python3-jsonschema-native \
    python3-pyelftools-native \
    python3-pyyaml-native \
    python3-yamllint-native \
"
PROVIDES += "u-boot"

SRC_URI = " \
    git://source.denx.de/u-boot/u-boot.git;protocol=https;branch=master \
    ${TDX_PATCHES} \
"

inherit toradex-u-boot-localversion

UBOOT_INITIAL_ENV = "u-boot-initial-env"
