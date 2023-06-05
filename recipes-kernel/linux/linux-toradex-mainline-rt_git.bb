LINUX_VERSION ?= "6.5-rc"
require recipes-kernel/linux/linux-toradex-mainline_git.bb
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-git:"

SUMMARY = "Toradex mainline real-time Linux kernel"
# To build the RT kernel we use the RT kernel git repo rather than applying
# the RT patch on top of a vanilla kernel.

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/rt/linux-rt-devel.git"
KBRANCH = "linux-6.5.y-rt"
SRCREV_machine = "16ae0de52ef9f7e5c2cd7e8443f5195c176353ea"
SRCREV_machine:use-head-next = "${AUTOREV}"

SRC_URI:append = " \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
