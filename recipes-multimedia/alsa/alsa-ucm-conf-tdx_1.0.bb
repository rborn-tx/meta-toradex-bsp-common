SUMMARY = "ALSA Use Case Manager configuration for Toradex Hardware"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

RDEPENDS:${PN} = "alsa-ucm-conf"

SRC_URI = "\
        file://aquila-wm8904-HiFi.conf \
        file://aquila-wm8904.conf \
        file://verdin-nau8822-HiFi.conf \
        file://verdin-nau8822.conf \
        file://verdin-wm8904-HiFi.conf \
        file://verdin-wm8904.conf \
"

do_install () {
        wm8904_dir="${D}${datadir}/alsa/ucm2/Toradex/wm8904"
        nau8822_dir="${D}${datadir}/alsa/ucm2/Toradex/nau8822"

        install -d $wm8904_dir
        install -m 0644 ${WORKDIR}/aquila-wm8904-HiFi.conf $wm8904_dir
        install -m 0644 ${WORKDIR}/aquila-wm8904.conf $wm8904_dir
        install -m 0644 ${WORKDIR}/verdin-wm8904-HiFi.conf $wm8904_dir
        install -m 0644 ${WORKDIR}/verdin-wm8904.conf $wm8904_dir

        install -d $nau8822_dir
        install -m 0644 ${WORKDIR}/verdin-nau8822-HiFi.conf $nau8822_dir
        install -m 0644 ${WORKDIR}/verdin-nau8822.conf $nau8822_dir

        install -d "${D}${datadir}/alsa/ucm2/conf.d/simple-card"
        ln -fsr ${wm8904_dir}/aquila-wm8904.conf \
                "${D}${datadir}/alsa/ucm2/conf.d/simple-card"
        ln -fsr ${wm8904_dir}/verdin-wm8904.conf \
                "${D}${datadir}/alsa/ucm2/conf.d/simple-card"
        ln -fsr ${nau8822_dir}/verdin-nau8822.conf \
                "${D}${datadir}/alsa/ucm2/conf.d/simple-card"
}

FILES:${PN} = "${datadir}"
