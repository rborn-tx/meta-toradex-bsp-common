LINUX_VERSION ?= "6.1.33-rt11"
require recipes-kernel/linux/linux-toradex-mainline_git.bb

SUMMARY = "Toradex mainline real-time Linux kernel"
# To build the RT kernel we use the RT kernel git repo rather than applying
# the RT patch on top of a vanilla kernel.

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/rt/linux-stable-rt.git"
KBRANCH = "v6.1-rt"
SRCREV_machine = "3f9ca5f166e5796bd21176f356a479e505ce7760"
SRCREV_machine:use-head-next = "${AUTOREV}"

SRC_URI:append = " \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
