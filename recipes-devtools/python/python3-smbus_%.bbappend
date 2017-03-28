do_configure_prepend() {
    # Allow to access device from Python even when in use...
    sed -i s:I2C_SLAVE:I2C_SLAVE_FORCE: smbusmodule.c
}
