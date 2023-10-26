require recipes-kernel/linux-firmware/linux-firmware_20230804.bb

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git;protocol=https;branch=main"
SRCREV = "8b855f3797e6b1d207b7a2b8dae0e9913f907e5b"
S = "${WORKDIR}/git"
WHENCE_CHKSUM = "18ad0db97d180df261ba964945e736ff"
