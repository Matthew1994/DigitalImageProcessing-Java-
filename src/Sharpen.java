import java.awt.image.BufferedImage;


public class Sharpen {
	private static int[][] coefficient9 = {{ -1, -1, -1},
									 		 { -1,  8, -1},
									 		 { -1, -1, -1}};

	//sharpen by a 3*3 matrix
	public static BufferedImage sharpenByMatrix3(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixelArr = quantize.changeDimension2(pixels, width);
		int[][] newArr = new int[height][width];
		for (int i = 0 ;i < width; i++) {
			for (int j = 0; j < height; j++) {
				newArr[j][i] = getNewData(pixelArr, coefficient9, i, j);
			}
		}
		BufferedImage image = new BufferedImage(width, height, img.getType());
		image.setRGB(0, 0, width, height, quantize.changeDimension1(newArr), 0, width);
		return image;
	}
	
	private static int getNewData(int[][] arr, int[][] coefficient, int w, int h) {
		int size = coefficient.length;
		int height = arr.length;
		int width = arr[0].length;
		int sumBlue = 0, sumGreen = 0, sumRed = 0;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				sumBlue += (w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? 0 : arr[h+j][w+i]&0x000000ff)*coefficient[j+size/2][i+size/2];
				sumGreen += (w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? 0 : (arr[h+j][w+i]&0x0000ff00)>>8)*coefficient[j+size/2][i+size/2];
				sumRed += (w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? 0 : (arr[h+j][w+i]&0x00ff0000)>>16)*coefficient[j+size/2][i+size/2];
			}
		}
		
//		sumBlue = (arr[h][w]&0x000000ff)+sumBlue*(coefficient[size/2][size/2]>0?1:-1);
//		sumGreen = (arr[h][w]&0x0000ff00)>>8+sumGreen*(coefficient[size/2][size/2]>0?1:-1);
//		sumRed = (arr[h][w]&0x00ff0000)>>16+sumRed*(coefficient[size/2][size/2]>0?1:-1);
		
		sumBlue = (int)(sumBlue) < 0 ? 0 : ((int)(sumBlue) > 255 ? 255:sumBlue); 
		sumGreen = (int)(sumGreen) < 0 ? 0 : ((int)(sumGreen) > 255 ? 255:sumGreen);
		sumRed = (int)(sumRed) < 0 ? 0 : ((int)(sumRed) > 255 ? 255:sumRed);
		return sumBlue|sumGreen<<8|sumRed<<16|0xff000000;
	}
}
