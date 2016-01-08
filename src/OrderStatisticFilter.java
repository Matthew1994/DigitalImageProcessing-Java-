import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;


public class OrderStatisticFilter {
	public static BufferedImage MedianFilter(BufferedImage input, int size) {
		BufferedImage output = null;
		output = sortedFilter(input, size, 0);
		return output;
	}
	
	public static BufferedImage MaxFilter(BufferedImage input, int size) {
		BufferedImage output = null;
		output = sortedFilter(input, size, 1);
		return output;
	}
	
	public static BufferedImage MinFilter(BufferedImage input, int size) {
		BufferedImage output = null;
		output = sortedFilter(input, size, 2);
		return output;
	}
	
	private static BufferedImage sortedFilter(BufferedImage img, int matrixSize, int flag) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixelArr = quantize.changeDimension2(pixels, width);
		int[][] newArr = new int[height][width];
		for (int i = 0 ;i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (flag == 0) {
					newArr[j][i] = getMedianValue(pixelArr, matrixSize, i, j);
				}
				else if (flag == 1) {
					newArr[j][i] = getMaxValue(pixelArr, matrixSize, i, j);
				}
				else if(flag == 2) {
					newArr[j][i] = getMinValue(pixelArr, matrixSize, i, j);
				}
				else {
					newArr[j][i] = pixelArr[j][i];
				}
			}
		}
		BufferedImage image = new BufferedImage(width, height, img.getType());
		image.setRGB(0, 0, width, height, quantize.changeDimension1(newArr), 0, width);
		newArr = null;
		pixelArr = null;
		pixelArr = null;
		return image;
	}
	
	private static int getMedianValue(int[][] arr, int size, int w, int h) {
		int height = arr.length;
		int width = arr[0].length;
		int[] vector = new int[size*size];
		int counter = 0;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				vector[counter]= w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? arr[h][w]&0x000000ff : arr[h+j][w+i]&0x000000ff;
				counter++;
				//sumGreen += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x0000ff00)>>8 : (arr[h+j][w+i]&0x0000ff00)>>8;
				//sumRed += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x00ff0000)>>16 : (arr[h+j][w+i]&0x00ff0000)>>16;
			}
		}
		Arrays.sort(vector);
		return (int)(vector[size*size/2])|(int)(vector[size*size/2])<<8|(int)(vector[size*size/2])<<16|0xff000000;
	}
	
	private static int getMinValue(int[][] arr, int size, int w, int h) {
		int height = arr.length;
		int width = arr[0].length;
		int[] vector = new int[size*size];
		int counter = 0;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				vector[counter]= w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? arr[h][w]&0x000000ff : arr[h+j][w+i]&0x000000ff;
				counter++;
				//sumGreen += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x0000ff00)>>8 : (arr[h+j][w+i]&0x0000ff00)>>8;
				//sumRed += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x00ff0000)>>16 : (arr[h+j][w+i]&0x00ff0000)>>16;
			}
		}
		Arrays.sort(vector);
		return (int)(vector[0])|(int)(vector[0])<<8|(int)(vector[0])<<16|0xff000000;
	}
	
	private static int getMaxValue(int[][] arr, int size, int w, int h) {
		int height = arr.length;
		int width = arr[0].length;
		int[] vector = new int[size*size];
		int counter = 0;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				vector[counter]= w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? arr[h][w]&0x000000ff : arr[h+j][w+i]&0x000000ff;
				counter++;
				//sumGreen += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x0000ff00)>>8 : (arr[h+j][w+i]&0x0000ff00)>>8;
				//sumRed += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x00ff0000)>>16 : (arr[h+j][w+i]&0x00ff0000)>>16;
			}
		}
		Arrays.sort(vector);
		System.out.println(vector[0]+" # "+vector[1]+" # "+vector[7]+" # "+vector[8]);
		return (int)(vector[size*size-1])|(int)(vector[size*size-1])<<8|(int)(vector[size*size-1])<<16|0xff000000;
	}
}
