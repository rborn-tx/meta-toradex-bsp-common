SUMMARY = "Filters device tree overlays based on machine into the deploy dir"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# This recipe deploys from the available device tree overlays those which
# are applicable for the current machine. Additionally it creates the file
# overlays.txt containing a list of overlays which should be started by default.
# The files to deploy in the final image are: "overlays.txt overlays/*"

do_deploy[depends] = "${@'virtual/dtb:do_deploy' if '${PREFERRED_PROVIDER_virtual/dtb}' else ''}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy nopackages

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"

# The machine specifc recipes start with MACHINE_PREFIX}[_-]
MACHINE_PREFIX = "${MACHINE}"
MACHINE_PREFIX_apalis-imx8x-v11a = "apalis-imx8x"
MACHINE_PREFIX_colibri-imx8x-v10b = "colibri-imx8x"
MACHINE_PREFIX_colibri-imx7-emmc = "colibri-imx7"

TEZI_EXTERNAL_KERNEL_DEVICETREE ??= ""
TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT ??= ""

do_deploy() {
    deploy_dt_dir=${DEPLOY_DIR_IMAGE}/devicetree/
    dtbos=
    if [ -z "${TEZI_EXTERNAL_KERNEL_DEVICETREE}" -a -d "$deploy_dt_dir" ] ; then
        machine_dtbos=`cd $deploy_dt_dir && ls ${MACHINE_PREFIX}[_-]*.dtbo 2>/dev/null || true`
        common_dtbos=`cd $deploy_dt_dir && ls *.dtbo 2>/dev/null | grep -v -e 'imx[6-8]' -e 'tk1' | xargs || true`
        dtbos="$machine_dtbos $common_dtbos"
    else
        dtbos="${TEZI_EXTERNAL_KERNEL_DEVICETREE}"
    fi

    mkdir -p ${DEPLOYDIR}/overlays/

    # copy overlays to overlays/ or create an empty file for deployment
    have_dtbos="n"
    for dtbo in $dtbos; do
        cp $deploy_dt_dir/$dtbo ${DEPLOYDIR}/overlays/
        have_dtbos="y"
    done
    if [ "$have_dtbos" = "n" ] ; then
        touch ${DEPLOYDIR}/overlays/none_deployed
    fi

    # overlays that we want to be applied during boot time
    overlays=
    for dtbo in ${TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT}; do
        if [ ! -e ${DEPLOYDIR}/overlays/$dtbo ]; then
            bbfatal "$dtbo is not installed in your boot filesystem, please make sure it's in TEZI_EXTERNAL_KERNEL_DEVICETREE or being provided by virtual/dtb. ${DEPLOYDIR}/overlays/"
        fi
        overlays="$overlays $dtbo"
    done

    echo "fdt_overlays=$(echo $overlays)" > ${DEPLOYDIR}/overlays.txt
}

addtask deploy after do_install before do_build
