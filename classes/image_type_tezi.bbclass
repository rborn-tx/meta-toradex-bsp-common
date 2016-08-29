inherit image_types

IMAGE_DEPENDS_tezi = "tezi-metadata:do_deploy"

python rootfs_tezi_json() {
    import json, subprocess
    from datetime import date
    from collections import OrderedDict

    # Calculate size of rootfs...
    output = subprocess.check_output(['du', '-ks',
                                      d.getVar('IMAGE_ROOTFS', True)])
    rootfssize_kb = int(output.split()[0])

    deploydir = d.getVar('DEPLOY_DIR_IMAGE', True)
    kernel = d.getVar('KERNEL_IMAGETYPE', True)

    # Calculate size of bootfs...
    bootfiles = [ os.path.join(deploydir, kernel) ]
    for dtb in d.getVar('KERNEL_DEVICETREE', True).split():
        bootfiles.append(os.path.join(deploydir, kernel + "-" + dtb))

    args = ['du', '-kLc']
    args.extend(bootfiles)
    output = subprocess.check_output(args)
    bootfssize_kb = int(output.splitlines()[-1].split()[0])

    data = OrderedDict({ "config_format": 1, "autoinstall": False })

    # Use image recipies SUMMARY/DESCRIPTION/PV...
    data["name"] = d.getVar('SUMMARY', True)
    data["description"] = d.getVar('DESCRIPTION', True)
    data["version"] = d.getVar('PV', True)
    data["release_date"] = date.isoformat(date.today())
    if os.path.exists(os.path.join(deploydir, "prepare.sh")):
        data["prepare_script"] = "prepare.sh"
    if os.path.exists(os.path.join(deploydir, "wrapup.sh")):
        data["wrapup_script"] = "wrapup.sh"
    if os.path.exists(os.path.join(deploydir, "marketing.tar")):
        data["marketing"] = "marketing.tar"
    if os.path.exists(os.path.join(deploydir, "toradexlinux.png")):
        data["icon"] = "toradexlinux.png"

    product_ids = d.getVar('TORADEX_PRODUCT_IDS', True)
    if product_ids is None:
        bb.fatal("Supported Toradex product ids missing, assign TORADEX_PRODUCT_IDS with a list of product ids.")

    data["supported_product_ids"] = d.getVar('TORADEX_PRODUCT_IDS', True).split()

    imagename = d.getVar('IMAGE_NAME', True)
    data["blockdevs"] = [
    OrderedDict({
      "name": "mmcblk0",
      "partitions": [
        {
          "partition_size_nominal": 16,
          "want_maximised": False,
          "content": {
            "label": "BOOT",
            "filesystem_type": "FAT",
            "mkfs_options": "",
            "filename": imagename + ".bootfs.tar.xz",
            "uncompressed_size": bootfssize_kb / 1024
          }
        },
        {
          "partition_size_nominal": 512,
          "want_maximised": True,
          "content": {
            "label": "RFS",
            "filesystem_type": "ext3",
            "mkfs_options": "",
            "filename": imagename + ".rootfs.tar.xz",
            "uncompressed_size": rootfssize_kb / 1024
          }
        }
      ]
    }),
    OrderedDict({
      "name": "mmcblk0boot0",
      "content": {
        "filesystem_type": "raw",
        "rawfiles": [
          {
            "filename": d.getVar('SPL_BINARY', True),
            "dd_options": "seek=2"
          },
          {
            "filename": d.getVar('U_BOOT_BINARY', True),
            "dd_options": "seek=138"
          }
        ]
      }
    })]
    deploy_dir = d.getVar('DEPLOY_DIR_IMAGE', True)
    with open(os.path.join(deploy_dir, 'image.json'), 'w') as outfile:
        json.dump(data, outfile, indent=4)
    bb.note("Toradex Easy Installer metadata file image.json written.")
}
do_rootfs[postfuncs] =+ "rootfs_tezi_json"

IMAGE_CMD_tezi () {
	bbnote "Create bootfs tarball"

	# Create list of device tree files
	if test -n "${KERNEL_DEVICETREE}"; then
		for DTS_FILE in ${KERNEL_DEVICETREE}; do
			DTS_BASE_NAME=`basename ${DTS_FILE} | awk -F "." '{print $1}'`
			if [ -e "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.dtb" ]; then
				KERNEL_DEVICETREE_FILES="${KERNEL_DEVICETREE_FILES} ${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.dtb"
			else
				bbfatal "${DTS_FILE} does not exist."
			fi
		done
	fi

	cd ${DEPLOY_DIR_IMAGE}
	${IMAGE_CMD_TAR} --transform="flags=r;s|${KERNEL_IMAGETYPE}-||" -chf ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar -C ${DEPLOY_DIR_IMAGE} ${KERNEL_IMAGETYPE} ${KERNEL_DEVICETREE_FILES}
	xz -f -k -c ${XZ_COMPRESSION_LEVEL} ${XZ_THREADS} --check=${XZ_INTEGRITY_CHECK} ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar > ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar.xz

	# The first transform strips all folders from the files to tar, the
	# second transform "moves" them in a subfolder ${IMAGE_NAME}_${PV}.
	${IMAGE_CMD_TAR} --transform='s/.*\///' --transform 's,^,${IMAGE_NAME}_${PV}/,' -chf ${IMGDEPLOYDIR}/${IMAGE_NAME}_${PV}.tar image.json toradexlinux.png marketing.tar prepare.sh wrapup.sh ${SPL_BINARY} ${U_BOOT_BINARY} ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar.xz ${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.tar.xz
}

IMAGE_TYPEDEP_tezi += "tar.xz"
