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

![Device Picture](https://motorolain.vtexassets.com/arquivos/ids/157377-1200-auto?width=1200&height=auto&aspect=true)
