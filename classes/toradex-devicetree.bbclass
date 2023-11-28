# Toradex devicetree.bbclass extension
#
# This bbclass extends OE's devicetree.bbclass by implementing devicetree
# overlays compilation for Toradex BSPs.
#
# A overlays.txt file is generated in ${DEPLOY_DIR_IMAGE}, with a uboot
# environment variable 'fdt_overlays' containing the devicetree overlays
# to be applied to the boot devicetree at runtime.
#
# The following options are supported:
#
# TEZI_EXTERNAL_KERNEL_DEVICETREE = "a-overlay.dtbo another_overlay.dtbo"
#
#                                      The devicetree overlays to be deployed
#                                      to ${DEPLOY_DIR_IMAGE}/overlays, if not
#                                      set, all machine related overlays would
#                                      be deployed.
#
# TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT = "a-overlay.dtbo"
#
#                                      The devicetree overlays to be applied at
#                                      runtime, will be written to:
#                                      ${DEPLOY_DIR_IMAGE}/overlays.txt, if not
#                                      set, no overlays would be applied.
#
# Copyright 2021 (C) Toradex AG

TEZI_EXTERNAL_KERNEL_DEVICETREE ??= ""
TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT ??= ""

SUMMARY = "Toradex BSP device tree overlays"

SRC_URI = "git://git.toradex.com/device-tree-overlays.git;branch=${SRCBRANCH};protocol=https"

PV = "${SRCBRANCH}+git${SRCPV}"

inherit devicetree

S = "${WORKDIR}/git/overlays"
DT_FILES_PATH = "${WORKDIR}/machine-overlays"
DT_INCLUDE:append = " ${S}"
KERNEL_DTB_PREFIX ??= ""
KERNEL_INCLUDE:append = " ${STAGING_KERNEL_DIR}/arch/${ARCH}/boot/dts/${KERNEL_DTB_PREFIX}"

# The machine specific recipes start with MACHINE_PREFIX}[_-]
MACHINE_PREFIX = "${MACHINE}"
MACHINE_PREFIX:colibri-imx6ull-emmc = "colibri-imx6ull"
MACHINE_PREFIX:colibri-imx7-emmc = "colibri-imx7"

do_collect_overlays () {
    if [ -z "${TEZI_EXTERNAL_KERNEL_DEVICETREE}" ] ; then
        machine_dts=`cd ${S} && ls ${MACHINE_PREFIX}[_-]*.dts 2>/dev/null || true`
        all_dts="$machine_dts"
    else
        for dtbo in ${TEZI_EXTERNAL_KERNEL_DEVICETREE}; do
            dtbo_ext=${dtbo##*.}
            dts="`basename $dtbo .$dtbo_ext`.dts"
            all_dts="$all_dts $dts"
        done
    fi

    for dts in $all_dts; do
        cp ${S}/$dts ${DT_FILES_PATH}
    done
}
do_collect_overlays[dirs] = "${DT_FILES_PATH}"
do_collect_overlays[cleandirs] = "${DT_FILES_PATH}"

addtask collect_overlays after do_patch before do_configure

do_deploy:append () {
    install -d ${DEPLOYDIR}/overlays
    if [ -d ${DEPLOYDIR}/devicetree ]; then
        cp ${DEPLOYDIR}/devicetree/* ${DEPLOYDIR}/overlays
    else
        touch ${DEPLOYDIR}/overlays/none_deployed
    fi

    # overlays that we want to be applied during boot time
    overlays=
    for dtbo in ${TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT}; do
        if [ ! -e ${DEPLOYDIR}/overlays/$dtbo ]; then
            bbfatal "$dtbo is not installed in your boot filesystem, please make sure it's in TEZI_EXTERNAL_KERNEL_DEVICETREE or being provided by virtual/dtb."
        fi
        overlays="$overlays $dtbo"
    done

    echo "fdt_overlays=$(echo $overlays)" > ${DEPLOYDIR}/overlays.txt
}
