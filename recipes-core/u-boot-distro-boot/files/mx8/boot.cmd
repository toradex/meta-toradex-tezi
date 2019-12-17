# Hardware dependant setup

if test ${board} = "apalis-imx8"
then
    env set bootcmd_hdp 'load ${devtype} ${devnum}:${distro_bootpart} ${hdp_addr} ${hdp_file}; hdp load ${hdp_addr}'
    env set ramdisk_addr_r 0x83000000
    env set vidargs 'video=LVDS-1:d video=HDMI-A-1:640x480-16@60D'
else
    # TODO: modify once we move to mainline U-Boot
    if test ${board} = "colibri-imx8qxp"
    then
        env set bootcmd_hdp ';'
	env set ramdisk_addr_r 0x83000000
        env set vidargs 'video=VGA-1:640x480-16@60D'
    else
        if test ${board} = "apalis-imx8x"
        then
            env set bootcmd_hdp ';'
	    env set ramdisk_addr_r 0x83000000
            env set vidargs 'video=HDMI-A-1:800x600D video=DPI-1:d video=LVDS-1:d'
        else
            if test ${board} = "verdin-imx8mm"
            then
                env set bootcmd_hdp ';'
		env set ramdisk_addr_r 0x43000000
                env set vidargs 'video=HDMI-A-1:1024x768-16@60D'
            else
                echo "This script is only meant for Apalis iMX8 / Apalis iMX8X / Colibri iMX8X"
                echo "and Verdin iMX8MM"
                exit
            fi
        fi
    fi
fi

env set bootcmd_args 'env set bootargs quiet ${vidargs} initcall_blacklist=vpu_driver_init rootfstype=@@TEZI_INITRD_IMAGE@@ root=/dev/ram autoinstall clk_ignore_unused pci=nomsi ${teziargs}'
# TODO: modify once our refactoring to use kernel fitImage is done
#env set bootcmd_run 'bootm ${ramdisk_addr_r}#config@${fdt_file}'
env set bootcmd_run 'bootm ${ramdisk_addr_r}'
env set bootcmd 'run bootcmd_args && run bootcmd_hdp && run bootcmd_tezi && run bootcmd_run'
env set tezi_image ${prefix}tezi.itb

# Re-enable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
env set fdt_high

if test -n ${fastboot_buffer}
then
    # Recovery mode
    # Note: the memory locations are bound to the ones as used in uuu.auto

    if test ${board} = "apalis-imx8"
    then
        env set bootcmd_hdp 'hdp load 0x82fe0000'
    fi

    env set bootcmd_tezi ';'
else
    env set bootcmd_tezi 'load ${devtype} ${devnum}:${distro_bootpart} ${ramdisk_addr_r} ${tezi_image}'
fi

run bootcmd
