import java.awt.image.BufferedImage;


public class MeanFilter {
	public static BufferedImage ArithmeticMean(BufferedImage input, int size) {
		BufferedImage output = null;
		output = averagingFilter(input, size, 0);
		return output;
	}
	
	
	public static BufferedImage  HarmonicMean(BufferedImage input, int size) {
		BufferedImage output = null;
		output = averagingFilter(input, size, 1);
		return output;
	}
	
	public static BufferedImage  ContraharmonicMean(BufferedImage input, int size) {
		BufferedImage output = null;
		output = averagingFilter(input, size, 2);
		return output;		
	}
	
	public static BufferedImage  GeometricMean(BufferedImage input, int size) {
		BufferedImage output = null;
		output = averagingFilter(input, size, 3);
		return output;		
	}
	
	
	
	private static BufferedImage averagingFilter(BufferedImage img, int matrixSize, int flag) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixelArr = quantize.changeDimension2(pixels, width);
		int[][] newArr = new int[height][width];
		for (int i = 0 ;i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (flag == 0) {
					newArr[j][i] = getArithmeticMean(pixelArr, matrixSize, i, j);
				}
				else if (flag == 1) {
					newArr[j][i] = getHarmonicMean(pixelArr, matrixSize, i, j);
				}
				else if(flag == 2) {
					newArr[j][i] = getContraharmonicMean(pixelArr, matrixSize, i, j, 1.5);
				}
				else if(flag == 3) {
					newArr[j][i] = getGeometricMean(pixelArr, matrixSize, i, j);
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
	
	private static int getArithmeticMean(int[][] arr, int size, int w, int h) {
		int height = arr.length;
		int width = arr[0].length;
		int sumBlue = 0, sumGreen = 0, sumRed = 0;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				sumBlue += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? arr[h][w]&0x000000ff : arr[h+j][w+i]&0x000000ff;
				sumGreen += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x0000ff00)>>8 : (arr[h+j][w+i]&0x0000ff00)>>8;
				sumRed += w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x00ff0000)>>16 : (arr[h+j][w+i]&0x00ff0000)>>16;
			}
		}
		return (int)(sumBlue/size/size)|((int)(sumGreen/size/size))<<8|((int)(sumRed/size/size))<<16|0xff000000;
	}
	
	private static int getHarmonicMean(int[][] arr, int size, int w, int h) {
		int height = arr.length;
		int width = arr[0].length;
		double sumBlue = 0.0, sumGreen = 0.0, sumRed = 0.0;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				int blue = (w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? arr[h][w]&0x000000ff : arr[h+j][w+i]&0x000000ff);
				int green = w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x0000ff00)>>8 : (arr[h+j][w+i]&0x0000ff00)>>8;
				int red = w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x00ff0000)>>16 : (arr[h+j][w+i]&0x00ff0000)>>16;
				
				sumBlue += 1.0/(double)(blue == 0 ? 0.001 : blue);
				sumGreen += 1.0/(double)(green == 0 ? 0.001 : green);
				sumRed += 1.0/(double)(red == 0 ? 0.001 : red);
			}
		}
		return (int)((double)(size*size)/sumBlue)|((int)((double)(size*size)/sumGreen))<<8|((int)((double)(size*size)/sumRed))<<16|0xff000000;
	}
	
	private static int getContraharmonicMean(int[][] arr, int size, int w, int h, double q) {
		int height = arr.length;
		int width = arr[0].length;
		double sumBlue = 0.0, sumGreen = 0.0, sumRed = 0.0;
		double sumBlue1 = 0.0, sumGreen1 = 0.0, sumRed1 = 0.0;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				int blue = (w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? arr[h][w]&0x000000ff : arr[h+j][w+i]&0x000000ff);
				int green = w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x0000ff00)>>8 : (arr[h+j][w+i]&0x0000ff00)>>8;
				int red = w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x00ff0000)>>16 : (arr[h+j][w+i]&0x00ff0000)>>16;
				
				sumBlue += Math.pow((double)blue, q);
				sumGreen += Math.pow((double)green, q);
				sumRed += Math.pow((double)red, q);
			
				sumBlue1 += Math.pow((double)blue, q+1);
				sumGreen1 += Math.pow((double)green, q+1);
				sumRed1 += Math.pow((double)red, q+1);
			}
		}
		sumBlue = (sumBlue == 0) ? 0.001 : sumBlue; 
		sumGreen = (sumGreen == 0) ? 0.001 : sumGreen;
		sumRed = (sumRed == 0) ? 0.001 : sumRed;
		
		int r,g,b;
		b = (int)(sumBlue1/sumBlue) > 255 ? 255 : (int)(sumBlue1/sumBlue);
		g = (int)(sumGreen1/sumGreen) > 255 ? 255 : (int)(sumGreen1/sumGreen);
		r = (int)(sumRed1/sumRed) > 255 ? 255 : (int)(sumRed1/sumRed);
		
		return b|g<<8|r<<16|0xff000000;
	}
	
	private static int getGeometricMean(int[][] arr, int size, int w, int h) {
		int height = arr.length;
		int width = arr[0].length;
		double sumBlue = 1, sumGreen = 1, sumRed = 1;
		for (int i = (-1)*size/2; i <= size/2; i++) {
			for (int j = (-1)*size/2; j <= size/2; j++) {
				int b = w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? arr[h][w]&0x000000ff : arr[h+j][w+i]&0x000000ff;
				int g = w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x0000ff00)>>8 : (arr[h+j][w+i]&0x0000ff00)>>8;
				int r = w+i < 0 || w+i >= width || h+j < 0 || h+j >= height? (arr[h][w]&0x00ff0000)>>16 : (arr[h+j][w+i]&0x00ff0000)>>16;
				sumBlue *= Math.pow((double)b, 1.0/(double)(size*size));
				sumGreen *= Math.pow((double)g, 1.0/(double)(size*size));
				sumRed *= Math.pow((double)r, 1.0/(double)(size*size));
			}
		}
		
		return (int)sumBlue | ((int)sumGreen)<<8| ((int)sumRed)<<16|0xff000000;
	}
}
