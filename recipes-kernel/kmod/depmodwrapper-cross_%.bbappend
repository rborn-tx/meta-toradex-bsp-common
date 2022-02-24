do_install() {
	install -d ${D}${bindir_crossscripts}/

	cat > ${D}${bindir_crossscripts}/depmodwrapper << EOF
#!/bin/sh
# Expected to be called as: depmodwrapper -a KERNEL_VERSION
if [ "\$1" != "-a" -o "\$2" != "-b" ]; then
    echo "Usage: depmodwrapper -a -b rootfs KERNEL_VERSION" >&2
    exit 1
fi

kernelabi=""
if [ -r "${PKGDATA_DIR}/kernel-depmod/kernel-abiversion" ]; then
    kernelabi=\$(cat "${PKGDATA_DIR}/kernel-depmod/kernel-abiversion")
fi

if [ ! -r ${PKGDATA_DIR}/kernel-depmod/System.map-\$4 ] || [ "\$kernelabi" != "\$4" ]; then
    echo "Unable to read: ${PKGDATA_DIR}/kernel-depmod/System.map-\$4" >&2
    FILESYMS=
else
    FILESYMS=-F "${PKGDATA_DIR}/kernel-depmod/System.map-\$4"
fi

exec env depmod -C "\$3${sysconfdir}/depmod.d" "\$1" "\$2" "\$3" ${FILESYMS} "\$4"

EOF
	chmod +x ${D}${bindir_crossscripts}/depmodwrapper
}
