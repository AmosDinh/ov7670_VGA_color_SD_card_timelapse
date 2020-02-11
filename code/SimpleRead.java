// compiled with Java 8 
// javac --release 8 -cp "comm.jar" BMP.java SimpleRead.java
// javac BMP.java SimpleRead.java 
//to get major version:  javap -verbose myClass | findstr "major"
// java code.SimpleRead

package code;

import java.io.IOException;

//from bmp class
import java.io.File;
import java.io.ByteArrayInputStream;

import java.io.FileOutputStream;

//from bmp class/

//for file to arraay conversion
import java.nio.file.Files;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

//for StandardCharsets
import java.nio.charset.StandardCharsets;

//for time in filena,e
import java.time.LocalDateTime;
//for dynamic array/list

public class SimpleRead {
	private static final int WIDTH = 512;
	private static final int HEIGHT = 480;

	public static void main(String[] args) throws IOException {

		SimpleRead reader = new SimpleRead();

	}

	public SimpleRead() throws IOException {
		LocalDateTime now = LocalDateTime.now();
		String year = String.valueOf(now.getYear());
		String month = String.valueOf(now.getMonthValue());

		String day = String.valueOf(now.getDayOfMonth());
		String hour = String.valueOf(now.getHour());
		String minute = String.valueOf(now.getMinute());
		String second = String.valueOf(now.getSecond());
		if (month.length() < 2)
			month = "0" + month;
		if (day.length() < 2)
			day = "0" + day;
		if (hour.length() < 2)
			hour = "0" + hour;
		if (minute.length() < 2)
			minute = "0" + minute;
		if (second.length() < 2)
			second = "0" + second;

		String timestamp = year + month + day + "T" + hour + minute + second;
		int count = 0;

		final File folder = new File("code/binIMG");
		ArrayList<String> filenames = listFilesForFolder(folder);
		int[] range = new int[] { 0, filenames.size() };
		filenames = sortByIndex("img", ".bin", range, filenames);
		for (int indexs = 0; indexs < filenames.size(); indexs++) {
			String fname = filenames.get(indexs);

			int[][] pxmap = new int[WIDTH][HEIGHT];
			int isZero = 0;
			// number of isZeros in img tells alot about how much it is corrupted

			final byte[] byteArray = Files.readAllBytes(Paths.get("code/binIMG/" + fname));

			ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
			// 512 -> // 480 // | // V

			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {

					int temps = byteStream.read();

					pxmap[x][y] = temps;
					if (temps == 0)
						isZero++;
				}
			}

			int[][] rPlane = new int[WIDTH][HEIGHT];
			int[][] gPlane = new int[WIDTH][HEIGHT];
			int[][] bPlane = new int[WIDTH][HEIGHT];

			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					if (y % 2 == 0) { // sidenote: I spent 2 hours until I realized that my images weren't colorful //
										// because I switched up the content of the if satements.

						if (x % 2 == 1) { // blue

							rPlane[x][y] = compAVRG(new int[][] { { x - 1, y - 1 }, { x + 1, y - 1 }, { x + 1, y + 1 },
									{ x - 1, y + 1 } }, pxmap);
							gPlane[x][y] = compAVRG(
									new int[][] { { x, y - 1 }, { x + 1, y }, { x, y + 1 }, { x - 1, y } }, pxmap);
							bPlane[x][y] = pxmap[x][y];

						} else { // green inline with blue

							rPlane[x][y] = compAVRG(new int[][] { { x, y - 1 }, { x, y + 1 } }, pxmap);
							gPlane[x][y] = pxmap[x][y];
							bPlane[x][y] = compAVRG(new int[][] { { x - 1, y }, { x + 1, y } }, pxmap);

						}
					} else {
						if (x % 2 == 0) { // red
							rPlane[x][y] = pxmap[x][y];
							gPlane[x][y] = compAVRG(
									new int[][] { { x, y - 1 }, { x + 1, y }, { x, y + 1 }, { x - 1, y } }, pxmap);
							bPlane[x][y] = compAVRG(new int[][] { { x - 1, y - 1 }, { x + 1, y - 1 }, { x + 1, y + 1 },
									{ x - 1, y + 1 } }, pxmap);

						} else { // green inline with red
							rPlane[x][y] = compAVRG(new int[][] { { x - 1, y }, { x + 1, y } }, pxmap);
							gPlane[x][y] = pxmap[x][y];
							bPlane[x][y] = compAVRG(new int[][] { { x, y - 1 }, { x, y + 1 } }, pxmap);

						}
					}

				}
			}

			// saved as BMP .ppm
			// EXAMPLE:
			// P3 <- specific type
			// 3 2 #<-width height
			// 255 #<- color range
			// 255 0 0 0 255 0 0 0 255 <- r g b r g b r g b....

			// 255 255 0 255 255 255 0 0 0

			// try {
			// FileOutputStream fos = new FileOutputStream(new File(
			// "C:/Users/Amos/Documents/Programmieren/arduino/ov7670_vga_color/ov7670_vga_color_sd_card/code/out/"
			// + "_t" + hour + "_" + minute + "_" + second + "_isZero_" + isZero + ".ppm"));
			// fos.write(("P3\n").getBytes(StandardCharsets.US_ASCII));
			// // flipwidth and height to get an upside img
			// fos.write((HEIGHT + " " + WIDTH + "\n").getBytes(StandardCharsets.US_ASCII));
			// fos.write(("255\n").getBytes(StandardCharsets.US_ASCII));
			// for (int x = WIDTH - 1; x >= 0; x--) {
			// String row = "";

			// for (int y = 0; y < HEIGHT; y++) {
			// String str = ""; // save as rgb
			// str += rPlane[x][y] + " ";
			// str += gPlane[x][y] + " ";
			// str += bPlane[x][y] + " ";
			// row += str;

			// }
			// fos.write((row + "\n").getBytes(StandardCharsets.US_ASCII));
			// }

			// fos.close();

			// } catch (IOException e) {
			// throw new IllegalStateException(e);
			// }
			BufferedImage image = new BufferedImage(HEIGHT, WIDTH, BufferedImage.TYPE_INT_RGB);

			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					int rgb = rPlane[x][y];
					rgb = (rgb << 8) + gPlane[x][y];
					rgb = (rgb << 8) + bPlane[x][y];
					// rotate
					image.setRGB(y, WIDTH - 1 - x, rgb);
				}
			}

			// File outputFile = new File(
			// "code/out/" + "_t" + hour + "_" + minute + "_" + second + "_isZero_" + isZero
			// + ".png");
			// fname = fname.substring(0, fname.length() - 4);
			File outputFile = new File("code/pngIMG/imgN" + (count++) + "_" + timestamp + ".png");
			ImageIO.write(image, "png", outputFile);

			System.out.println("ZERO: " + isZero);

			// try {
			// BufferedImage img = ImageIO.read(new
			// File("code/out/0_t14_46_42_isZero_0.ppm"));

			// File outputfile = new File("name.png");
			// ImageIO.write(img, "png", outputfile);
			// } catch (IOException e) {
			// }
		}

	}

	private int compAVRG(int[][] arr, int[][] pxmap) {
		int divider = 0;
		int val = 0;

		for (int i = 0; i < arr.length; i++) {
			int temp[] = arr[i];
			if ((temp[0] + 1 <= 0 || temp[0] + 1 >= WIDTH || temp[1] + 1 <= 0 || temp[1] + 1 >= HEIGHT
					|| temp.length == 0))
				continue;
			divider++;
			val += pxmap[temp[0]][temp[1]];

		}

		if (divider == 0) {
			return 0;
		}
		return (int) (val / divider);

	}

	private ArrayList<String> listFilesForFolder(final File folder) {
		ArrayList<String> temp = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			// if (fileEntry.isDirectory()) {
			// listFilesForFolder(fileEntry);
			// } else {
			temp.add(fileEntry.getName());

			// }
		}
		return temp;
	}

	private ArrayList<String> sortByIndex(final String prefix, final String fileending, int[] range,
			ArrayList<String> filenames) {
		// VERY inefficient but does not matter
		for (int i = range[0]; i < range[1]; i++) {
			for (int j = range[0]; j < range[1]; j++) {
				if (i != j) {

					String tempa = filenames.get(i);
					String tempb = filenames.get(j);

					String a = tempa.substring(prefix.length(), tempa.length() - fileending.length());

					String b = tempb.substring(prefix.length(), tempb.length() - fileending.length());
					// if file is bad, ending will be .bin.isBad therefore substring will be wrong
					// Exception in thread "main" java.lang.NumberFormatException: For input string:
					// "10.bin.i"
					boolean aIsBad = false;
					boolean bIsBad = false;
					if (a.charAt(a.length() - 1) == 'i') {
						// a = a.substring(0, a.length() - 6);
						filenames.remove(i);
						range[1] = range[1] - 1;
						aIsBad = true;
					}

					if (b.charAt(b.length() - 1) == 'i') {
						// b = b.substring(0, b.length() - 6);
						filenames.remove(j);
						range[1] = range[1] - 1;
						bIsBad = true;
					}
					if (aIsBad || bIsBad)
						continue;
					int aInt = Integer.parseInt(a);
					int bInt = Integer.parseInt(b);

					if (bInt > aInt)
						Collections.swap(filenames, i, j);
				}
			}

		}
		for (int i = 0; i < filenames.size(); i++)
			System.out.println(filenames.get(i));
		return filenames;

	}

}