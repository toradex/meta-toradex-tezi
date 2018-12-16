HOMEPAGE = "https://github.com/andreaskoch/dcsg"
SUMMARY = "A systemd service generator for docker-compose"
DESCRIPTION = "dcsg is a command-line utility for Linux that generates systemd services for \
Docker Compose projects. If you have one or more docker compose projects running \
on your server you might want create a systemd service for each of them. And dcsg \
is here to help you with just that. Quickly create systemd services from docker-compose \
files."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=524fb62207fbe8d13cd295e14a43ba73"

SRC_URI = "git://github.com/andreaskoch/dcsg;branch=develop \
           file://0001-Add-root-dir-and-compose-dir-parameters.patch \
"

SRCREV = "708049ad082843b77d3698babfad3843ccefd111"
PV = "0.4.0+git${SRCREV}"

GO_IMPORT = "import"

S = "${WORKDIR}/git"

inherit goarch
inherit go

do_compile() {
    export GOARCH="${TARGET_GOARCH}"

    cd ${S}/src/${GO_IMPORT}
    oe_runmake
}

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${S}/src/${GO_IMPORT}/bin/dcsg ${D}/${bindir}/dcsg
}

BBCLASSEXTEND = "native"
