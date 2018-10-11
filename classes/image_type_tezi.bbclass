inherit image_types

# This class implements Toradex Easy Installer image type
# It allows to use OpenEmbedded to build images which can be consumed
# by the Toradex Easy Installer.
# Since it also generates the image.json description file it is rather
# interwind with the boot flow which is U-Boot target specific.
#
# Currently there are two image types implemented:
# teziimg: Default Toradex boot flow
# teziimg-distro: Distro boot boot flow

do_image_teziimg[depends] += "tezi-metadata:do_deploy virtual/bootloader:do_deploy"
do_image_teziimg_distro[depends] += "tezi-metadata:do_deploy virtual/bootloader:do_deploy"

TEZI_ROOT_FSTYPE ??= "ext4"
TEZI_ROOT_LABEL ??= "RFS"
TEZI_ROOT_SUFFIX ??= "tar.xz"
UBOOT_BINARY ??= "u-boot.${UBOOT_SUFFIX}"
UBOOT_BINARY_TEZI_EMMC ?= "${UBOOT_BINARY}"
UBOOT_BINARY_TEZI_EMMC_apalis-t30 = "apalis_t30.img"
UBOOT_BINARY_TEZI_EMMC_apalis-tk1 = "apalis-tk1.img"
UBOOT_BINARY_TEZI_EMMC_apalis-tk1-mainline = "apalis-tk1.img"
UBOOT_BINARY_TEZI_RAWNAND ?= "${UBOOT_BINARY}"
UBOOT_ENV_TEZI_EMMC ?= "uEnv.txt"
UBOOT_ENV_TEZI_RAWNAND ?= "uEnv.txt"

# for generic images this is not yet defined
TDX_VERDATE ?= "-${DATE}"
TDX_VERDATE[vardepsexclude] = "DATE"

def rootfs_get_size(d):
    import subprocess

    # Calculate size of rootfs in kilobytes...
    output = subprocess.check_output(['du', '-ks',
                                      d.getVar('IMAGE_ROOTFS', True)])
    return int(output.split()[0])

def bootfs_get_size(d):
    import subprocess

    kernel = d.getVar('KERNEL_IMAGETYPE', True)
    deploydir = d.getVar('DEPLOY_DIR_IMAGE', True)

    # Calculate size of bootfs...
    bootfiles = [ os.path.join(deploydir, kernel) ]

    has_devicetree = d.getVar('KERNEL_DEVICETREE', True)
    if has_devicetree:
        for dtb in d.getVar('KERNEL_DEVICETREE', True).split():
            bootfiles.append(os.path.join(deploydir, dtb))

    args = ['du', '-kLc']
    args.extend(bootfiles)
    output = subprocess.check_output(args)
    return int(output.splitlines()[-1].split()[0])

def rootfs_tezi_emmc(d):
    from collections import OrderedDict
    offset_bootrom = d.getVar('OFFSET_BOOTROM_PAYLOAD', True)
    offset_spl = d.getVar('OFFSET_SPL_PAYLOAD', True)
    imagename = d.getVar('IMAGE_NAME', True)
    imagename_suffix = d.getVar('IMAGE_NAME_SUFFIX', True)
    imagetype_suffix = d.getVar('TEZI_ROOT_SUFFIX', True)

    bootpart_rawfiles = []

    has_spl = d.getVar('SPL_BINARY', True)
    if has_spl:
        bootpart_rawfiles.append(
              {
                "filename": d.getVar('SPL_BINARY', True),
                "dd_options": "seek=" + offset_bootrom
              })
    bootpart_rawfiles.append(
              {
                "filename": d.getVar('UBOOT_BINARY_TEZI_EMMC', True),
                "dd_options": "seek=" + (offset_spl if has_spl else offset_bootrom)
              })

    return [
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
                "uncompressed_size": bootfs_get_size(d) / 1024
              }
            },
            {
              "partition_size_nominal": 512,
              "want_maximised": True,
              "content": {
                "label": d.getVar('TEZI_ROOT_LABEL', True),
                "filesystem_type": d.getVar('TEZI_ROOT_FSTYPE', True),
                "mkfs_options": "-E nodiscard",
                "filename": imagename + imagename_suffix + "." + imagetype_suffix,
                "uncompressed_size": rootfs_get_size(d) / 1024
              }
            }
          ]
        }),
        OrderedDict({
          "name": "mmcblk0boot0",
          "content": {
            "filesystem_type": "raw",
            "rawfiles": bootpart_rawfiles
          }
        })]


def rootfs_tezi_rawnand(d):
    from collections import OrderedDict
    imagename = d.getVar('IMAGE_NAME', True)
    imagename_suffix = d.getVar('IMAGE_NAME_SUFFIX', True)
    imagetype_suffix = d.getVar('TEZI_ROOT_SUFFIX', True)

    # Use device tree mapping to create product id <-> device tree relationship
    dtmapping = d.getVarFlags('TORADEX_PRODUCT_IDS')
    dtfiles = []
    for f, v in dtmapping.items():
        dtfiles.append({ "filename": v, "product_ids": f })

    return [
        OrderedDict({
          "name": "u-boot1",
          "content": {
            "rawfile": {
              "filename": d.getVar('UBOOT_BINARY_TEZI_RAWNAND', True),
              "size": 1
            }
          },
        }),
        OrderedDict({
          "name": "u-boot2",
          "content": {
            "rawfile": {
              "filename": d.getVar('UBOOT_BINARY_TEZI_RAWNAND', True),
              "size": 1
            }
          }
        }),
        OrderedDict({
          "name": "ubi",
          "ubivolumes": [
            {
              "name": "kernel",
              "size_kib": 8192,
              "type": "static",
              "content": {
                "rawfile": {
                  "filename": d.getVar('KERNEL_IMAGETYPE', True),
                  "size": 5
                }
              }
            },
            {
              "name": "dtb",
              "content": {
                "rawfiles": dtfiles
              },
              "size_kib": 128,
              "type": "static"
            },
            {
              "name": "m4firmware",
              "size_kib": 896,
              "type": "static"
            },
            {
              "name": "rootfs",
              "content": {
                "filesystem_type": "ubifs",
                "filename": imagename + imagename_suffix + "." + imagetype_suffix,
                "uncompressed_size": rootfs_get_size(d) / 1024
              }
            }
          ]
        })]

def rootfs_tezi_json(d, flash_type, flash_data, json_file, uenv_file):
    import json
    from collections import OrderedDict
    from datetime import datetime

    deploydir = d.getVar('DEPLOY_DIR_IMAGE', True)
    # patched in IMAGE_CMD_teziimg() below
    release_date = "%release_date%"

    data = OrderedDict({ "config_format": 2, "autoinstall": False })

    # Use image recipes SUMMARY/DESCRIPTION/PV...
    data["name"] = d.getVar('SUMMARY', True)
    data["description"] = d.getVar('DESCRIPTION', True)
    data["version"] = d.getVar('PV', True)
    data["release_date"] = release_date
    data["u_boot_env"] = uenv_file
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

    dtmapping = d.getVarFlags('TORADEX_PRODUCT_IDS')
    data["supported_product_ids"] = []

    # If no varflags are set, we assume all product ids supported with single image/U-Boot
    if dtmapping is not None:
        for f, v in dtmapping.items():
            dtbflashtypearr = v.split(',')
            if len(dtbflashtypearr) < 2 or dtbflashtypearr[1] == flash_type:
                data["supported_product_ids"].append(f)
    else:
        data["supported_product_ids"].extend(product_ids.split())

    if flash_type == "rawnand":
        data["mtddevs"] = flash_data
    elif flash_type == "emmc":
        data["blockdevs"] = flash_data

    with open(os.path.join(deploydir, json_file), 'w') as outfile:
        json.dump(data, outfile, indent=4)
    bb.note("Toradex Easy Installer metadata file {0} written.".format(json_file))

python rootfs_tezi_run_json() {
    flash_type = d.getVar('TORADEX_FLASH_TYPE', True)
    if flash_type is None:
        bb.fatal("Toradex flash type not specified")

    if len(flash_type.split()) > 1:
        bb.fatal("This class only supports a single flash type.")

    if flash_type == "rawnand":
        flash_data = rootfs_tezi_rawnand(d)
        uenv_file = d.getVar('UBOOT_ENV_TEZI_RAWNAND', True)
    elif flash_type == "emmc":
        flash_data = rootfs_tezi_emmc(d)
        uenv_file = d.getVar('UBOOT_ENV_TEZI_EMMC', True)
    else:
        bb.fatal("Toradex flash type unknown")

    rootfs_tezi_json(d, flash_type, flash_data, "image.json", uenv_file)
}

do_image_teziimg[prefuncs] += "rootfs_tezi_run_json"

IMAGE_CMD_teziimg () {
	bbnote "Create Toradex Easy Installer tarball"

	# Fixup release_date in image.json, convert ${TDX_VERDATE} to isoformat
	# This works around the non fatal ERRORS: "the basehash value changed" when DATE is referenced
	# in a python prefunction to do_image
	ISODATE=$(echo ${TDX_VERDATE} | sed 's/.\(....\)\(..\)\(..\).*/\1-\2-\3/')
	sed -i "s/%release_date%/$ISODATE/" ${DEPLOY_DIR_IMAGE}/image.json

	cd ${DEPLOY_DIR_IMAGE}

	case "${TORADEX_FLASH_TYPE}" in
		rawnand)
		# The first transform strips all folders from the files to tar, the
		# second transform "moves" them in a subfolder ${IMAGE_NAME}_${PV}.
		${IMAGE_CMD_TAR} \
			--transform='s/.*\///' \
			--transform 's,^,${IMAGE_NAME}-Tezi_${PV}/,' \
			-chf ${IMGDEPLOYDIR}/${IMAGE_NAME}-Tezi_${PV}${TDX_VERDATE}.tar \
			image.json toradexlinux.png marketing.tar prepare.sh wrapup.sh \
			${SPL_BINARY} ${UBOOT_BINARY_TEZI_RAWNAND} ${UBOOT_ENV_TEZI_RAWNAND} ${KERNEL_IMAGETYPE} ${KERNEL_DEVICETREE} \
			${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tar.xz
		;;
		*)
		# Create bootfs...
		${IMAGE_CMD_TAR} \
			-chf ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar -C ${DEPLOY_DIR_IMAGE} \
			${KERNEL_IMAGETYPE} ${KERNEL_DEVICETREE}
		xz -f -k -c ${XZ_COMPRESSION_LEVEL} ${XZ_THREADS} --check=${XZ_INTEGRITY_CHECK} ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar > ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar.xz

		# The first transform strips all folders from the files to tar, the
		# second transform "moves" them in a subfolder ${IMAGE_NAME}-Tezi_${PV}.
		${IMAGE_CMD_TAR} \
			--transform='s/.*\///' \
			--transform 's,^,${IMAGE_NAME}-Tezi_${PV}/,' \
			-chf ${IMGDEPLOYDIR}/${IMAGE_NAME}-Tezi_${PV}${TDX_VERDATE}.tar \
			image.json toradexlinux.png marketing.tar prepare.sh wrapup.sh \
			${SPL_BINARY} ${UBOOT_BINARY_TEZI_EMMC} ${UBOOT_ENV_TEZI_EMMC} ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar.xz \
			${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tar.xz
		;;
	esac
}

IMAGE_TYPEDEP_teziimg += "${TEZI_ROOT_SUFFIX}"

def rootfs_tezi_distro_rawnand(d):
    from collections import OrderedDict
    imagename = d.getVar('IMAGE_NAME', True)
    imagename_suffix = d.getVar('IMAGE_NAME_SUFFIX', True)
    imagetype_suffix = d.getVar('TEZI_ROOT_SUFFIX', True)

    return [
        OrderedDict({
          "name": "u-boot1",
          "content": {
            "rawfile": {
              "filename": d.getVar('UBOOT_BINARY_TEZI_RAWNAND', True),
              "size": 1
            }
          },
        }),
        OrderedDict({
          "name": "u-boot2",
          "content": {
            "rawfile": {
              "filename": d.getVar('UBOOT_BINARY_TEZI_RAWNAND', True),
              "size": 1
            }
          }
        }),
        OrderedDict({
          "name": "ubi",
          "ubivolumes": [
            {
              "name": "boot",
              "size_kib": 16384,
              "content": {
                "filesystem_type": "ubifs",
                "filename": imagename + ".bootfs.tar.xz",
                "uncompressed_size": bootfs_get_size(d) / 1024
              }
            },
            {
              "name": "rootfs",
              "content": {
                "filesystem_type": "ubifs",
                "filename": imagename + imagename_suffix + "." + imagetype_suffix,
                "uncompressed_size": rootfs_get_size(d) / 1024
              }
            }
          ]
        })]

python rootfs_tezi_run_distro_json() {
    flash_types = d.getVar('TORADEX_FLASH_TYPE', True)
    if flash_types is None:
        bb.fatal("Toradex flash type not specified")

    flash_types_list = flash_types.split()
    for flash_type in flash_types_list:
        if flash_type == "rawnand":
            flash_data = rootfs_tezi_distro_rawnand(d)
            uenv_file = d.getVar('UBOOT_ENV_TEZI_RAWNAND', True)
            uboot_file = d.getVar('UBOOT_BINARY_TEZI_RAWNAND', True)
        elif flash_type == "emmc":
            flash_data = rootfs_tezi_emmc(d)
            uenv_file = d.getVar('UBOOT_ENV_TEZI_EMMC', True)
            uboot_file = d.getVar('UBOOT_BINARY_TEZI_EMMC', True)
            # TODO: Multi image/raw NAND with SPL currently not supported
            if d.getVar('SPL_BINARY', True):
                uboot_file += " " + d.getVar('SPL_BINARY', True)
        else:
            bb.fatal("Toradex flash type unknown")

        if len(flash_types_list) > 1:
            json_file = "image-{0}.json".format(flash_type)
        else:
            json_file = "image.json"

        rootfs_tezi_json(d, flash_type, flash_data, json_file, uenv_file)
        d.appendVar("TEZI_IMAGE_JSON_FILES", json_file + " ")
        d.appendVar("TEZI_IMAGE_UBOOT_FILES", uenv_file + " " + uboot_file + " ")
}

do_image_teziimg_distro[prefuncs] += "rootfs_tezi_run_distro_json"

IMAGE_CMD_teziimg-distro () {
	bbnote "Create Toradex Easy Installer tarball"

	# Fixup release_date in image.json, convert ${DATE} to isoformat
	# This works around the non fatal ERRORS: "the basehash value changed" when DATE is referenced
	# in a python prefunction to do_image
	ISODATE=$(echo ${DATE} | sed 's/\(....\)\(..\)\(..\)/\1-\2-\3/')
	for TEZI_IMAGE_JSON in ${TEZI_IMAGE_JSON_FILES}; do
		sed -i "s/%release_date%/$ISODATE/" ${DEPLOY_DIR_IMAGE}/${TEZI_IMAGE_JSON}
	done

	cd ${DEPLOY_DIR_IMAGE}

	# Create bootfs...
	${IMAGE_CMD_TAR} \
		-chf ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar -C ${DEPLOY_DIR_IMAGE} \
		${KERNEL_IMAGETYPE} ${KERNEL_DEVICETREE} boot.scr
	xz -f -k -c ${XZ_COMPRESSION_LEVEL} ${XZ_THREADS} --check=${XZ_INTEGRITY_CHECK} ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar > ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar.xz

	# The first transform strips all folders from the files to tar, the
	# second transform "moves" them in a subfolder ${IMAGE_NAME}-Tezi_${PV}.
	${IMAGE_CMD_TAR} \
		--transform='s/.*\///' \
		--transform 's,^,${IMAGE_NAME}-Tezi_${PV}/,' \
		-chf ${IMGDEPLOYDIR}/${IMAGE_NAME}-Tezi_${PV}${TDX_VERDATE}.tar \
		${TEZI_IMAGE_JSON_FILES} toradexlinux.png marketing.tar prepare.sh wrapup.sh \
                ${TEZI_IMAGE_UBOOT_FILES} ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar.xz \
		${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${TEZI_ROOT_SUFFIX}
}

IMAGE_TYPEDEP_teziimg-distro += "${TEZI_ROOT_SUFFIX}"
