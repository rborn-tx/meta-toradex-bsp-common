LINUX_VERSION ?= "6.0.5-rt14"
require recipes-kernel/linux/linux-toradex-mainline_git.bb

SUMMARY = "Toradex mainline real-time Linux kernel"
# To build the RT kernel we use the RT kernel git repo rather than applying
# the RT patch on top of a vanilla kernel.

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/rt/linux-rt-devel.git"
KBRANCH = "linux-6.0.y-rt"
SRCREV_machine = "040e3123e9d93721de64a3df726871b57af204e9"
SRCREV_machine:use-head-next = "${AUTOREV}"

SRC_URI:append = " \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
