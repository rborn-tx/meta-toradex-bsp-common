# backport values from openembedded-core/hardknott 37c6df10e8d9b997b3631ee1e7b1cad39353d926

require recipes-kernel/linux-firmware/linux-firmware_20210511.bb

LIC_FILES_CHKSUM_remove = " \
                    file://LICENCE.iwlwifi_firmware;md5=3fd842911ea93c29cd32679aa23e1c88 \
                    file://WHENCE;md5=727d0d4e2d420f41d89d098f6322e779 \
"

LIC_FILES_CHKSUM_append = " \
                    file://LICENCE.iwlwifi_firmware;md5=2ce6786e0fc11ac6e36b54bb9b799f1b \
                    file://WHENCE;md5=15ad289bf2359e8ecf613f3b04f72fab \
"

SRC_URI[sha256sum] = "bef3d317c348d962b3a1b95cb4e19ea4f282e18112b2c669cff74f9267ce3893"
