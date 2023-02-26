LINUX_VERSION ?= "6.2-rc"
require recipes-kernel/linux/linux-toradex-mainline_git.bb
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-git:"

SUMMARY = "Toradex mainline real-time Linux kernel"
# To build the RT kernel we use the RT kernel git repo rather than applying
# the RT patch on top of a vanilla kernel.

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/rt/linux-rt-devel.git"
KBRANCH = "linux-6.2.y-rt"
SRCREV_machine = "bee0efc1fe1add1df7d677cee5a9c4c325196403"
SRCREV_machine:use-head-next = "${AUTOREV}"

SRC_URI:append = " \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
