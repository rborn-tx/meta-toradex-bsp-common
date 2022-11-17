# Sanity check the Toradex setup for common misconfigurations

TORADEX_SANITY_ABIFILE ?= "${DEPLOY_DIR}/abi_version"

#
# Check the 'ABI' of DEPLOY_DIR
#
def toradex_check_abichanges(status, d):
    current_abi = d.getVar('OELAYOUT_ABI')
    abifile = d.getVar('TORADEX_SANITY_ABIFILE')
    if os.path.exists(abifile):
        with open(abifile, "r") as f:
            abi = f.read().strip()
        if not abi.isdigit():
            with open(abifile, "w") as f:
                f.write(current_abi)
        elif (abi != current_abi):
            # Code to convert from one ABI to another could go here if possible.
            status.addresult("Error, DEPLOY_DIR has changed its layout version number (%s to %s) and you need to either rebuild, revert or adjust it at your own risk.\n" % (abi, current_abi))
    else:
        bb.utils.mkdirhier(os.path.dirname(abifile))
        with open(abifile, "w") as f:
            f.write(current_abi)

def toradex_raise_sanity_error(msg, d):
    if d.getVar("SANITY_USE_EVENTS") == "1":
        bb.event.fire(bb.event.SanityCheckFailed(msg), d)
        return

    bb.fatal("Toradex' config sanity checker detected a potential misconfiguration.\n"
             "Please fix the cause of this error then you can continue to build.\n"
             "Following is the list of potential problems / advisories:\n"
             "\n%s" % msg)

def toradex_check_sanity(sanity_data):
    class SanityStatus(object):
        def __init__(self):
            self.messages = ""
            self.reparse = False

        def addresult(self, message):
            if message:
                self.messages = self.messages + message

    status = SanityStatus()

    toradex_check_abichanges(status, sanity_data)

    if status.messages != "":
        toradex_raise_sanity_error(sanity_data.expand(status.messages), sanity_data)

addhandler toradex_check_sanity_eventhandler
toradex_check_sanity_eventhandler[eventmask] = "bb.event.SanityCheck"

python toradex_check_sanity_eventhandler() {
    if bb.event.getName(e) == "SanityCheck":
        sanity_data = bb.data.createCopy(e.data)
        if e.generateevents:
            sanity_data.setVar("SANITY_USE_EVENTS", "1")
        reparse = toradex_check_sanity(sanity_data)
        e.data.setVar("BB_INVALIDCONF", reparse)
        bb.event.fire(bb.event.SanityCheckPassed(), e.data)

    return
}
