# ov7670_VGA_color_SD_card_timelapse
#### Components Used:
- Arduino UNO
- SD-Card Module and a SD-Card
- OV7670 Camera Module
#### Aim of the project:
- 

** In progress **
pics every 6 seconds, unfotunately, the camera module doesn't allow much slower speed, but you could skip images by yourself.
SD card write speed is still too slow, therefore I only collect 512 pxdata per row, and save to SD in the remaining time of the 128 px;
(Picture still will be distorted sometimes, if it takes 12 seconds to save(because two pics overlap))
Therefore image is 512*420 px; Format is raw Bayer(G B G B.... next line: R G R G...), saved to SD as binary-stream file.
