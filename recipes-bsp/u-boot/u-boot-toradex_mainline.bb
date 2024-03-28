# This builds latest head of U-Boot denx master
# Do not use this recipe for production, it is not reproducible
# and you don't know what git hash is built from just looking
# at the metadata

require u-boot-toradex-common.inc

SRCREV = "${AUTOREV}"
# patches which are not (yet) in the latest master
TDX_PATCHES = " \
"


DEFAULT_PREFERENCE = "-1"
