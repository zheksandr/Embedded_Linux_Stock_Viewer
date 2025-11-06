#!/bin/sh

echo "WiFi connection initialized $(date)"

ip link set wlan0 up
wpa_supplicant -B -i wlan0 -c /etc/wpa_supplicant/wpa_supplicant.conf -Dnl80211
udhcpc -i wlan0


echo "WiFi connection done $(date)"
