LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} += " \
    libusbgx \
    rapidjson \
    qtzeroconf \
    qhttp \
"
