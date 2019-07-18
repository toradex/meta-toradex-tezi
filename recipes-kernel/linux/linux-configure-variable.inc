# Set a variable in ${B}/.config
# $1 - Configure variable to be set
# $2 - value [n/y/value]
kernel_configure_variable() {
    # Remove the config
    sed -i -e "/CONFIG_$1[ =]/d" ${B}/.config
    if [ "$2" = "n" ]; then
        echo "# CONFIG_$1 is not set" >> ${B}/.config
    else
        echo "CONFIG_$1=$2" >> ${B}/.config
    fi
}
