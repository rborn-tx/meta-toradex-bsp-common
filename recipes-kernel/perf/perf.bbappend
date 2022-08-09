# python3-setuptools is required with 5.19-rc7
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'scripting', 'python3-setuptools-native', '', d)}"
