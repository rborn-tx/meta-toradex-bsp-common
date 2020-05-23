do_shared_workdir_append () {
    if [ -f include/config/auto.conf ]; then
        mkdir -p $kerneldir/include/config/
        cp -f include/config/auto.conf $kerneldir/include/config/
    fi

    ln -sf ${STAGING_KERNEL_DIR} ${kerneldir}/source

    if [ ! -f $kerneldir/Makefile ]; then
        VERSION=$(grep "^VERSION =" Makefile | sed s/.*=\ *//)
        PATCHLEVEL=$(grep "^PATCHLEVEL =" Makefile | sed s/.*=\ *//)
        bash ${S}/scripts/mkmakefile $(pwd) $kerneldir $VERSION $PATCHLEVEL
    fi
}

