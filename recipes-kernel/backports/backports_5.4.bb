DESCRIPTION = "Linux Backports"
HOMEPAGE = "https://backports.wiki.kernel.org"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit module-base

# To generate the backports tree yourself, so you can make modifications:
# Generate the backport tree using:
#    git clone git://git.toradex.com/backports-sources-toradex.git -b linux-5.4.y
# add/commit the generated tree
# Change SRCREV = to point to your generated tree
# Under SRC_URI point this at your generated tree as such:
#    git:///home/user/linux-backports-generated;protocol=file

SRCREV = "e8ef623db4bd6fb85df341d583000e1b12a64a63"
SRCREV_use-head-next = "${AUTOREV}"
SRC_URI = " \
    git://git.toradex.com/backports-toradex.git;protocol=git;branch=toradex-${PV} \
    file://config \
    "

# Depend on virtual/kernel to ensure that the kernel is built before we try to
# build the backports
DEPENDS += "virtual/kernel"
DEPENDS += "bison-native coreutils-native flex-native"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = " \
    KLIB_BUILD=${KBUILD_OUTPUT} \
    KLIB=${B} \
    "

do_configure() {
    # Somehow lex does not automatically get linked to flex!
    ln -fs flex ../recipe-sysroot-native/usr/bin/lex
    make CFLAGS="" CPPFLAGS="" CXXFLAGS="" LDFLAGS="" CC="${BUILD_CC}" LD="${BUILD_LD}" \
        AR="${BUILD_AR}" -C ${S}/kconf O=${S}/kconf conf

    cp ${WORKDIR}/config ${S}/.config
    oe_runmake oldconfig
}

# LDFLAGS are not suitable for direct ld use as with the kernel build system
do_compile() {
    unset LDFLAGS
    oe_runmake
}

do_install() {
    install -d ${D}/lib/modules/${KERNEL_VERSION}/backports
    for ko in $(find ${S} -type f -name "*.ko")
    do
        install -m 0644 $ko ${D}/lib/modules/${KERNEL_VERSION}/backports/
    done
}

pkg_postinst_${PN} () {
#!/bin/sh
set -e
if [ -z "$D" ]; then
    CPWD=`pwd`
    depmod -a ${KERNEL_VERSION}
    cd /lib/modules/${KERNEL_VERSION}/
    cp modules.order modules.order.save
    ls -d backports/*.ko >modules.order
    cat modules.order.save >> modules.order
    cd $CPWD
else
        # image.bbclass will call depmodwrapper after everything is installed,
        # no need to do it here as well
        :
fi
}

pkg_postrm_${PN} () {
#!/bin/sh
set -e
if [ -z "$D" ]; then
    mv /lib/modules/${KERNEL_VERSION}/modules.order.save /lib/modules/${KERNEL_VERSION}/modules.order
    depmod -a ${KERNEL_VERSION}
fi
}

FILES_${PN} = " \
    /lib/modules/${KERNEL_VERSION}/backports/ \
    "
