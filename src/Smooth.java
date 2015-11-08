import java.awt.Image;
import java.awt.image.BufferedImage;


public class Smooth {
	public static BufferedImage averagingFilter(BufferedImage img, int matrixSize) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixelArr = quantize.changeDimension2(pixels, width);
		int[][] newArr = new int[height][width];
		for (int i = 0 ;i < width; i++) {
			for (int j = 0; j < height; j++) {
				newArr[j][i] = getNewData(pixelArr, matrixSize, i, j);
			}
		}
		BufferedImage image = new BufferedImage(width, height, img.getType());
		image.setRGB(0, 0, width, height, quantize.changeDimension1(newArr), 0, width);
		newArr = null;
		pixelArr = null;
		pixelArr = null;
		return image;
	}
	private static int getNewData(int[][] arr, int size, int w, int h) {
		int height = arr.length;
		int width = arr[0].length;
		int sumBlue = 0, sumGreen = 0, sumRed = 0;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				sumBlue += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? 0 : arr[h+j][w+i]&0x000000ff;
				sumGreen += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? 0 : (arr[h+j][w+i]&0x0000ff00)>>8;
				sumRed += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? 0 : (arr[h+j][w+i]&0x00ff0000)>>16;
			}
		}
		return (int)(sumBlue/size/size)|((int)(sumGreen/size/size))<<8|((int)(sumRed/size/size))<<16|0xff000000;
	}
}
