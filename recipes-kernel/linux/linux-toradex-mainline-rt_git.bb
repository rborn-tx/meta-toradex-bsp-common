LINUX_VERSION ?= "v6.1-rc7-rt5"
require recipes-kernel/linux/linux-toradex-mainline_git.bb

SUMMARY = "Toradex mainline real-time Linux kernel"
# To build the RT kernel we use the RT kernel git repo rather than applying
# the RT patch on top of a vanilla kernel.

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/rt/linux-rt-devel.git"
KBRANCH = "linux-6.1.y-rt"
SRCREV_machine = "ef46357401222b4f1daded0b9992b788351b7550"
SRCREV_machine:use-head-next = "${AUTOREV}"

SRC_URI:append = " \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
    file://0001-Revert-ARM-dts-imx7-Fix-NAND-controller-size-cells.patch \
"
