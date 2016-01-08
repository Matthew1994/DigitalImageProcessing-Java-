import java.awt.image.BufferedImage;
import java.util.Random;


public class Noise {
	public static BufferedImage addSaltPepper(BufferedImage img, float saltRate, float pepperRate) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixelArr = quantize.changeDimension2(pixels, width);
		
		for (int i = 0 ;i < (saltRate*width*height); i++) {
			int w = (int)(Math.random()*width);
			int h = (int)(Math.random()*height);
			pixelArr[h][w] = 0xffffffff;
		}
		
		for (int i = 0 ;i < (pepperRate*width*height); i++) {
			int w = (int)(Math.random()*width);
			int h = (int)(Math.random()*height);
			pixelArr[h][w] = 0xff000000;
		}
		
		BufferedImage image = new BufferedImage(width, height, img.getType());
		image.setRGB(0, 0, width, height, quantize.changeDimension1(pixelArr), 0, width);
		pixelArr = null;
		pixelArr = null;
		return image;
	}
	
	public static BufferedImage addGaussianNoise(BufferedImage img, float mean, float standard) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixelArr = quantize.changeDimension2(pixels, width);
		float[][] newArr = new float[height][width];
		
		float max = -10000f;
		float min = 10000f;
		
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				newArr[h][w] = (float)(pixelArr[h][w]&0x000000ff) + mean + standard*(float)(new Random().nextGaussian());
				if (max < newArr[h][w])
					max = newArr[h][w];
				if (min > newArr[h][w])
					min = newArr[h][w];
			}
		}
		
		//标定
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				int temp = (int)((newArr[h][w]-min)/(max-min)*255);
				pixelArr[h][w] = temp|temp<<8|temp<<16|0xff000000; 
			}
		}	
		 
		
		BufferedImage image = new BufferedImage(width, height, img.getType());
		image.setRGB(0, 0, width, height, quantize.changeDimension1(pixelArr), 0, width);
		pixelArr = null;
		pixelArr = null;
		return image;
	}
	
}
