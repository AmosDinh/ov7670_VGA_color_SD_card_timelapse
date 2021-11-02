# ov7670_VGA_color_SD_card_timelapse
### Aim of the project:
- Creating a low power timelapse-camera

### Components Used:
- Arduino UNO
- SD-Card Module and a SD-Card
- OV7670 Camera Module

### Steps used:
- Understanding:
    - How Arduino UNO works
    - How the camera module and sd-card module work
    - the Bayer image format (Format is raw Bayer(G B G B.... next line: R G R G...), saved to SD as binary-stream)
- Code:
    - Arduino:
        - Adjusting the camera module to the needed speed(0.133 Hertz) by accessing its register
        - capturing the raw bit data and writing it to the SD-Card
    - Computer, using Java:
        - Converting the raw data to Bayer-pictures
        - Converting the pictures to a mp4-file

### Issues:
 - Camera Module does not allow slower speed than 0.133 Hertz but you can skip images by yourself
 - (Picture cut of): SD card write speed is too slow, therefore I only collect 512 pxdata per row, and save to SD in the remaining time of the 128 px: <br> Pictures will be distorted sometimes, if it takes 12 seconds to save(because two pics overlap)) Therefore image is 512*420 px; .




