LICENSE = "MIT"

inherit packagegroup

RDEPENDS_${PN} += " \
    libusbgx \
    rapidjson \
    qtzeroconf-dev \
"
