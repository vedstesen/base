DESCRIPTION = "Miscellaneous files for the base system."
LICENSE = "GPL"
RDEPENDS_${PN} = "${TARGET_ARCH}/sysroot-libnss-files ${TARGET_ARCH}/sysroot-libnss-dns"

RECIPE_OPTIONS += "hostname"
DEFAULT_CONFIG_hostname = "oe-lite"

RECIPE_OPTIONS += "basefiles_version"

SRC_URI = " \
           file://motd \
           file://host.conf \
           file://profile \
           file://fstab \
           file://dot.bashrc \
           file://device_table-minimal.txt \
           file://nsswitch.conf \
           file://dot.profile "

RECIPE_OPTIONS += "basefiles_issue"
DEFAULT_CONFIG_basefiles_issue = "OE Lite Linux"

do_compile[dirs] = "${SRCDIR}"
do_compile () {
    echo ${RECIPE_OPTION_hostname}			> hostname
    echo "${RECIPE_OPTION_basefiles_issue} \n \l"	> issue
    echo						>>issue
    echo "${RECIPE_OPTION_basefiles_issue} %h"		> issue.net
    echo						>>issue.net
}

# Basic filesystem directories (adheres to FHS)
dirs1777 = "/tmp \
	   ${localstatedir}/lock \
	   ${localstatedir}/tmp"
dirs2775 = "/home \
	    ${localstatedir}/local"
dirs755 = "${bindir} \
	   ${sbindir} \
	   ${libdir} \
	   ${libexecdir} \
	   ${includedir} \
	   ${sysconfdir} \
	   ${sysconfdir}/default \
	   ${sysconfdir}/skel \
	   ${prefix} \
	   ${docdir} \
	   ${datadir} \
	   ${infodir} \
	   ${mandir} \
	   ${datadir}/misc \
	   ${localstatedir} \
	   ${localstatedir}/backups \
	   ${localstatedir}/lib \
	   ${localstatedir}/lib/misc \
	   ${localstatedir}/spool \
	   ${localstatedir}/cache \
	   ${localstatedir}/log \
	   ${localstatedir}/run \
	   /sys \
	   /boot \
	   /dev \
	   /dev/pts \
	   /dev/shm \
	   /mnt \
	   /proc \
	   /root \
	   /srv \
	   /media \
	   /media/card \
	   /media/cf \
	   /media/net \
	   /media/ram \
	   /media/hdd "

FILES_${PN} = "/"

do_install () {
	# Install directories
	for d in ${dirs755}; do
		install -m 0755 -d ${D}$d
	done
	for d in ${dirs1777}; do
		install -m 1777 -d ${D}$d
	done
	for d in ${dirs2775}; do
		install -m 2755 -d ${D}$d
	done

	# Install files
        install -m 0644 ${SRCDIR}/hostname ${D}${sysconfdir}/hostname
 	install -m 0644 ${SRCDIR}/issue ${D}${sysconfdir}/issue
        install -m 0644 ${SRCDIR}/issue.net ${D}${sysconfdir}/issue.net
        install -m 0644 ${SRCDIR}/fstab ${D}${sysconfdir}/fstab
        install -m 0644 ${SRCDIR}/profile ${D}${sysconfdir}/profile
        install -m 0755 ${SRCDIR}/dot.profile ${D}${sysconfdir}/skel/.profile
        install -m 0755 ${SRCDIR}/dot.bashrc ${D}${sysconfdir}/skel/.bashrc
        install -m 0644 ${SRCDIR}/motd ${D}${sysconfdir}/motd
        install -m 0644 ${SRCDIR}/host.conf ${D}${sysconfdir}/host.conf
        install -m 0644 ${SRCDIR}/nsswitch.conf ${D}${sysconfdir}/

        ln -sf /proc/mounts ${D}${sysconfdir}/mtab
}

do_install_append_RECIPE_OPTION_basefiles_version () {
	echo "${DISTRO_VERSION}" > \
	      ${SRCDIR}/${RECIPE_OPTION_basefiles_version}
	install -m 0644 ${SRCDIR}/${RECIPE_OPTION_basefiles_version} ${D}${sysconfdir}/${RECIPE_OPTION_basefiles_version}
}

inherit makedevs
MAKEDEVS_FILES = "${SRCDIR}/device_table-minimal.txt"