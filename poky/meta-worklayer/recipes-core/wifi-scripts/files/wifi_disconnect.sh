#!/bin/sh

echo "WiFi disconnecting... $(date)"

if command -v pkill >/dev/null 2>&1; then
    pkill -f "udhcpc" 2>/dev/null || true
    pkill -f "dhclient" 2>/dev/null || true
else
    killall udhcpc 2>/dev/null || true
    killall dhclient 2>/dev/null || true
fi

if command -v wpa_cli >/dev/null 2>&1; then
    wpa_cli -i wlan0 disconnect 2>/dev/null || true
    sleep 1
fi

if command -v pkill >/dev/null 2>&1; then
    pkill -f "wpa_supplicant" 2>/dev/null || true
else
    killall wpa_supplicant 2>/dev/null || true
fi

rm -rf /var/run/wpa_supplicant 2>/dev/null || true

ip addr flush dev wlan0 2>/dev/null || true
ip link set wlan0 down 2>/dev/null || true

echo "WiFi disconnected successfully $(date)"
exit 0

