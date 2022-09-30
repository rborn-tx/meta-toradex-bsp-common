SUMMARY = "Script to get useful information about Toradex Hardware."
DESCRIPTION = "This script can be used by Toradex customers in userspace to grab information from the module and share it with Toradex Support Team"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c38c9404df9658111a87d5090719fb44"

SRC_URI = "git://github.com/toradex/tdx-info;protocol=https;branch=v${PV}"
SRCREV = "80840350f00965e9862a354a1bd870954f8af18e"

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/tdx-info ${D}${bindir}
}
