# Toradex U-Boot LOCALVERSION extension
#
# This allow to easy reuse of code between different U-Boot recipes
#
# The following options are supported:
#
#  SCMVERSION        Puts the Git hash in U-Boot local version
#  LOCALVERSION      Value used in LOCALVERSION
#
# Copied from meta-freescale/classes/fsl-u-boot-localversion.bbclass
# Copyright 2014 (C) O.S. Systems Software LTDA.
# Copyright 2017-2019 (C) Toradex Inc.

TDX_VERSION ??= "0"
SCMVERSION ??= "y"
LOCALVERSION ??= "-${TDX_VERSION}"

UBOOT_LOCALVERSION = "${LOCALVERSION}"

do_compile:prepend() {
	if [ "${SCMVERSION}" = "y" ]; then
		head=`cd ${S} ; git rev-parse --verify --short=12 HEAD 2> /dev/null`
		printf "%s+git.%s" "${UBOOT_LOCALVERSION}" $head > ${S}/.scmversion
		printf "%s+git.%s" "${UBOOT_LOCALVERSION}" $head > ${B}/.scmversion
	fi
}
