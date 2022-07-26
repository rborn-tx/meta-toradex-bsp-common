#!/usr/bin/env sh
#
# Bind mount boot partition /boot -> /${MOUNT_BASE}/BOOT-PARTITION-MOUNTPOINT
# in which:
# MOUNT_BASE is defined in /etc/udev/scripts/mount.sh
# BOOT-PARTITION-MOUNTPOINT is the mount point of boot partition.

BASEUUID=$(sed -r 's/^.*\broot=PARTUUID=([0-9a-f]+)-02.*$/\1/' /proc/cmdline)
BOOTPART=$(readlink -f /dev/disk/by-partuuid/"${BASEUUID}-01")
if [ x"$DEVNAME" = x"$BOOTPART" ]; then
	MOUNTNAME="$(/sbin/blkid | grep "$DEVNAME:" | grep -o 'LABEL=".*"' | cut -d '"' -f2)-$(basename "$DEVNAME")"
	MOUNTPOINT="$(sed -n 's/MOUNT_BASE=\"\(.*\)\"/\1/p' /etc/udev/scripts/mount.sh)/$MOUNTNAME"
	BASE_INIT="$(readlink -f "@base_sbindir@/init")"
	INIT_SYSTEMD="@systemd_unitdir@/systemd"

	if [ "x$BASE_INIT" = "x$INIT_SYSTEMD" ];then
		# systemd as init uses systemd-mount to mount block devices
		MOUNT="/usr/bin/systemd-mount --no-block -o silent"
		UMOUNT="/usr/bin/systemd-umount"
	else
		MOUNT="/bin/mount"
		UMOUNT="/bin/umount"

		# Silent util-linux's version of mounting auto
		if [ "x$(readlink $MOUNT)" = "x/bin/mount.util-linux" ]; then
			MOUNT="$MOUNT -o silent"
		fi
	fi

	if [ -e /tmp/.automount-"$MOUNTNAME" ]; then
		$MOUNT -o bind "$MOUNTPOINT" /boot
	fi
fi
