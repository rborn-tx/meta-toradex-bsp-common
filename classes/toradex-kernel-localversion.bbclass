# Toradex Kernel LOCALVERSION extension
#
# This allow to easy reuse of code between different kernel recipes
#
# The following options are supported:
#
#  SCMVERSION        Puts the Git hash in kernel local version
#  LOCALVERSION      Value used in LOCALVERSION
#
# Copyright 2014, 2015 (C) O.S. Systems Software LTDA.
# Copyright 2019 (C) Toradex AG

SCMVERSION ??= "y"
LOCALVERSION ?= "-${TDX_VERSION}"

kernel_do_configure_append() {
	sed -i -e /CONFIG_LOCALVERSION/d ${B}/.config
	echo "CONFIG_LOCALVERSION=\"${LOCALVERSION}\"" >> ${B}/.config

	if [ "${SCMVERSION}" = "y" ]; then
		# Add GIT revision to the local version
		head=`git --git-dir=${S}/.git rev-parse --verify --short HEAD 2> /dev/null`
		printf "+git.%s" $head > ${S}/.scmversion
	fi
}
