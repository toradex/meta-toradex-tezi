{
  'variables': {
    # Configure for armv7 compilation
    'target_arch': 'arm',
    'armv7': 1,
    'arm_thumb': 1,
    #'arm_neon': 1,
    'arm_fpu': 'vfpv3-d16',
    'arm_float_abi': 'hard',
    'v8_use_arm_eabi_hardfloat': 'true',
    'v8_target_arch' : 'arm',
    'disable_sse2' : 1,
  }, 
}
