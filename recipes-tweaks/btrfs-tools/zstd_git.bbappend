# this is the hash of version tag v1.3.3
SRCREV = "f3a8bd553a865c59f1bd6e1f68bf182cf75a8f00"
SRC_URI = "git://github.com/facebook/zstd.git;protocol=https;nobranch=1"

EXTRA_OECMAKE_append = " -DTHREADS_PTHREAD_ARG=0"
