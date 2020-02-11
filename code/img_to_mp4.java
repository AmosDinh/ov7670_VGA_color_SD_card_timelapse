package code;

//javac -cp lib\*  img_to_mp4.java
//java -cp code/lib/*; code.img_to_mp4
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class img_to_mp4 {

    private static Dimension screenBounds;
    public static int indexVideo = 0;
    private static final double FRAME_RATE = 30;

    private static final int SECONDS_TO_RUN_FOR = 20;
    private static final String OUTPUT_FILE = "C:/Users/Amos/Documents/Programmieren/arduino/ov7670_vga_color/ov7670_vga_color_sd_card/code/mp4IMG/firsttimelapse.mp4";

    public static void main(String[] arguments) {
        final IMediaWriter writer = ToolFactory.makeWriter(OUTPUT_FILE);
        screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, screenBounds.width / 2, screenBounds.height / 2);
        long startTime = System.nanoTime();
        int[] excludeframes = new int[] { 21, 39, 50, 70, 81, 86, 91, 102, 129, 139, 145, 150, 155, 161, 166, 171, 177,
                180, 182, 187, 193, 198, 203, 209, 214, 219, 225, 230, 235, 241, 246, 251, 257, 262, 267, 273, 278, 283,
                289 };

        for (int index = 0; index < 300; index++) {
            for (int i = 0; i < excludeframes.length; i++)
                if (excludeframes[i] == index)
                    continue;
            BufferedImage bgrScreen = getVideoImage();
            System.out.println("time stamp = " + (System.nanoTime() - startTime));
            bgrScreen = convertToType(bgrScreen, BufferedImage.TYPE_3BYTE_BGR);
            // encode the image to stream #0
            // writer.encodeVideo(0, bgrScreen, (System.nanoTime() -
            // startTime)/2,TimeUnit.NANOSECONDS);
            // encode the image to stream #0
            writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            // sleep for frame rate milliseconds
            try {
                Thread.sleep((long) (100));
            } catch (InterruptedException e) {
                // ignore
            }
        }
        writer.close();
    }

    private static BufferedImage getVideoImage() {

        File imgLoc = new File(
                "C:/Users/Amos/Documents/Programmieren/arduino/ov7670_vga_color/ov7670_vga_color_sd_card/code/pngIMG/img"
                        + indexVideo + ".png");
        BufferedImage img = null;
        try {
            img = ImageIO.read(imgLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(imgLoc.getName());
        indexVideo++;
        return img;
    }

    public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {

        BufferedImage image;

        // if the source image is already the target type, return the source
        // image
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        }
        // otherwise create a new image of the target type and draw the new
        // image
        else {
            image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;

    }

}