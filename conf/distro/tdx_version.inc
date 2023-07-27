# These variables are used to set image, kernel and U-Boot versions

TDX_BUILDNBR ?= "0"
TDX_PURPOSE ?= "Testing"

def get_tdx_prerelease(purpose, datetime):
    if purpose == "Testing":
        return '-devel-%s' % (datetime)
    elif purpose == "Nightly":
        return '-devel-%s' % (datetime[:8])
    elif purpose == "Monthly":
        return '-devel-%s' % (datetime[:6])
    elif purpose == "Release":
        return ''
    else:
        return '-devel-%s' % (datetime)

TDX_BUILD ?= "+build.${TDX_BUILDNBR}"
TDX_MATRIX_BUILD_TIME ?= "${DATETIME}"
TDX_MATRIX_BUILD_TIME[vardepsexclude] = "DATETIME"
TDX_PRERELEASE ?= "${@get_tdx_prerelease(d.getVar('TDX_PURPOSE'), d.getVar('TDX_MATRIX_BUILD_TIME'))}"
TDX_MAJOR ?= "6"
TDX_MINOR ?= "6"
TDX_PATCH ?= "0"
TDX_RELEASE ?= "${TDX_MAJOR}.${TDX_MINOR}.${TDX_PATCH}"

# Toradex Version number without date for U-Boot/Kernel and other Toradex
# specific software artifacts.
TDX_VERSION ?= "${TDX_RELEASE}${@'' if d.getVar('TDX_PURPOSE') == 'Release' else '-devel'}"

# Complete image version with date and build number
DISTRO_VERSION = "${TDX_RELEASE}${TDX_PRERELEASE}${TDX_BUILD}"

TDX_VER_ID:apalis-imx6 = "Apalis-iMX6_ToradexEasyInstaller_${DISTRO_VERSION}"
TDX_VER_ID:apalis-imx8 = "Apalis-iMX8_ToradexEasyInstaller_${DISTRO_VERSION}"
TDX_VER_ID:colibri-imx6 = "Colibri-iMX6_ToradexEasyInstaller_${DISTRO_VERSION}"
TDX_VER_ID:colibri-imx6ull = "Colibri-iMX6ULL_ToradexEasyInstaller_${DISTRO_VERSION}"
TDX_VER_ID:colibri-imx7 = "Colibri-iMX7_ToradexEasyInstaller_${DISTRO_VERSION}"
TDX_VER_ID:colibri-imx8x = "Colibri-iMX8X_ToradexEasyInstaller_${DISTRO_VERSION}"
TDX_VER_ID:verdin-imx8mm = "Verdin-iMX8MM_ToradexEasyInstaller_${DISTRO_VERSION}"
TDX_VER_ID:verdin-imx8mp = "Verdin-iMX8MP_ToradexEasyInstaller_${DISTRO_VERSION}"
TDX_VER_ID:verdin-am62 = "Verdin-AM62_ToradexEasyInstaller_${DISTRO_VERSION}"
