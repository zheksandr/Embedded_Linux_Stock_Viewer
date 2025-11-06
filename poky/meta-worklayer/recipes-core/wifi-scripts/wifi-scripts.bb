SUMMARY = "Skrypty Wi-Fi i konfiguracja wpa_supplicant"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://wifi_connect.sh \
    file://wifi_disconnect.sh \
    file://wpa_supplicant.conf \
"

S = "${WORKDIR}"

do_install() {
# instalacja skryptów wifi w lokalizacji /home/usr/bin/, usr to domyślnie 'weston'
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/wifi_connect.sh ${D}${bindir}/wifi_connect.sh
    install -m 0755 ${WORKDIR}/wifi_disconnect.sh ${D}${bindir}/wifi_disconnect.sh
    
# instalacja pliku konfiguracyjnego sieci w lokalizacji /etc/wpa_supplicant
    install -d ${D}${sysconfdir}/wpa_supplicant
    install -m 0600 ${WORKDIR}/wpa_supplicant.conf \
        ${D}${sysconfdir}/wpa_supplicant/wpa_supplicant.conf
}

CONFFILES:${PN} += "${sysconfdir}/wpa_supplicant/wpa_supplicant.conf"

RDEPENDS:${PN} = "wpa-supplicant busybox"
