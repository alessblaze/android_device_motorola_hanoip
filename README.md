# Motorola G60/G40 Fusion

## Device specifications

Basic   | Spec Sheet
-------:|:-------------------------
CPU     | Octa-core (2x2.3 GHz Kryo 470 Gold & 6x1.8 GHz Kryo 470 Silver)
CHIPSET | Qualcomm Snapdragon 732G
GPU     | Adreno 618
Memory  | 4 / 6GB
Shipped Android Version | 11
Storage | 64 / 128GB
Battery | 6000 mAh
Dimensions | 169.6 x 75.9 x 9.7 mm
Display | 1080 x 2460 pixels, 6.8" IPS LCD
Rear Camera  | 64 MP, f/1.7 (wide), 0.7µm, PDAF + 8 MP, f/2.2, 118˚ (ultrawide), 1/4.0", 1.12µm + 2 MP, f/2.4, (depth)
Front Camera | 16 MP, f/2.2, (wide), 1.0µm


Changes present in current device tree and not in official build from PixelBuilds

1) Changed to newer kernel with upstream patches and security fixes backported from ASBs.
2) fixed pick up to ambient display doze. Added option to double tap on ambient display to wake the device.
3) fixed location accuracy to a point which is acceptable.
4) added 120hz display to our kernel source.
5) wide gamut should work along with Widevine.
6) Fixed some Volte issues.
7) added QTEE/QSEE which is trusted components for secure keymaster hal.
8) added dpm and soter for display and modem power management and stability.
9) newer EGL libs and open source vulkan turnip added.
10) Echo in calls are fixed also.
11) face unlock is working on this.
12) downclocked so 1.61ghz for small cores and 2.17ghz for perf cores.
13) added nonstandard capsense sensor which should be working fine currently to limit modem radio power 
    as it is needed for human health and safety.
14) this is mainly for moto g40, as i did not include nfc.  
15) Tap2Wake works now  
16) userspace HAL tap to wake implemented. stay in older commit ffd9f8ad70e5a027f31ddfa40fb26f035ae2c332 for fully working version 
    and in kernel 20e1b3e106c668d7869b055f4ec3d245094c990c commit in cpu-freq. this needs time and patience. the older mode wakes screen
    completely. this does doze aod on while also managing stowed. means in pocket it wont turn on screen. this is experimental.
17) The feature works well now as i can see its responsive. Settings>Display>Tap To Wake, Needs to be enabled for tap to doze screeen showing.
    one small nit is that, when Settings>Display>Tap To Wake, is disabled, other moto specific Pick to wake and all works to show doze screen,
    but on double tap post doze does not wake up the screen, which is intended as thats how android system works.
    now why this and why not .kl and keymap simple fix. its because if we can doze via sensor reading in priv-app, we can also decide, when
    phone is in pocket with stowed sensor, which is just proximity,ambient and vl series tof laser sensor which i used in quite someplaces.
    greater issues remain how driver is written. AOSP calls DOUBLE_TAP_WAKE to power manager which massively enable and disable the feature,
    this is good yet bad, so its gated in power manager properly. what i have seen with ilitek driver is that, android puts display to screen,
    ilitek driver puts touchpanel to deep sleep and then AOSP sets up the gesture enabled sysfs context, which in case would delay system kernel,
    to let the irq driver subsystem to not load neccessary firmware settings because panel have to transition into deep seep to gesture aware,
    low power mode, in case driver is missing that. why we did not fix it? in a 0flash touchpanel, it makes sense. but on this, gesture mode is only 
    enabled in kernel driver when touch panel is in sleep mode controlled by DRM_MSM, so its completely fine to fix the power manager and not rewrite,
    the driver. its just tap to wake but yea this is all what it takes. we implemented an atomic counter in ilitek driver which exposes a sysfs node,
    at /sys/devices/platform/soc/a80000.spi/spi_master/spi2/spi2.0/tap_gesture_pressed or /sys/touchpanel/tap_gesture_pressed which shows the atomic counter,
    of taps and also has kernel irq mechanism to sysfs_notify() and we ported an android hal to communicate with the driver via sysfs to get the one tap interrupts,
    there are seperate ways. like most code here and there in lineage mainly uses oneshot method of getting the value while listening to updates from,
    the sysfs changed but also disable the sensor after and re enabling it. it goes to down same issue. so we chosen the software method, to not disable the,
    sensorm rather only get the interrupt and listen again, disabling and enabling is managed in software flags, which is fine as the earlier part of this,
    explains why. its mostly functional currently.
![Device Picture](https://motorolain.vtexassets.com/arquivos/ids/157377-1200-auto?width=1200&height=auto&aspect=true)
