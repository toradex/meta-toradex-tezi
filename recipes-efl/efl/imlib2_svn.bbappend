#backport changes from meta-openembedded/meta-efl/recipes-efl/efl master branch b14728a
#as svn.enlightenment.org is no longer reachable

PV = "1.4.6+gitr${SRCPV}"
SRCREV = "560a58e61778d84953944f744a025af6ce986334"

SRC_URI = "git://git.enlightenment.org/legacy/${BPN}.git"
S = "${WORKDIR}/git"

# autotools-brokensep
B = "${S}"
