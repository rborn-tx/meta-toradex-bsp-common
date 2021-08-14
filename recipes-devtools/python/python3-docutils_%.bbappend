FILESEXTRAPATHS:prepend := "${THISDIR}/python3-docutils/:"

SRC_URI:append = " \
file://rst2man_using_python3.patch \
"
 
