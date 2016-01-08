import java.awt.image.BufferedImage;

import org.w3c.dom.css.Counter;



public class HistogramEqualization {
	public static BufferedImage equalizeHist(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		int[] newPixels = new int[width*height];
		
		int[] oldCountR = new int[256];
		int[] newCountR = new int[256];
		
		int[] oldCountG = new int[256];
		int[] newCountG = new int[256];
		
		int[] oldCountB = new int[256];
		int[] newCountB = new int[256];
		
		
		img.getRGB(0, 0, width, height, pixels, 0, width);
		for (int i = 0; i < pixels.length; i++) {
			oldCountR[(pixels[i]&0x00ff0000) >> 16]++;
			oldCountG[(pixels[i]&0x0000ff00) >> 8]++;
			oldCountB[pixels[i]&0x000000ff]++;
		}
		
		//here are test
		int[] countAll = new int[256];
		int[] newCountAll = new int[256];
		for (int i =0 ; i < countAll.length; i++) {
			countAll[i] = oldCountR[i] + oldCountG[i]+ oldCountB[i];
		}
		for (int i = 0; i < countAll.length; i++) {
			for(int j = 0 ; j <= i; j++) {
				newCountAll[i] += countAll[j];
			}
		}
		for (int i = 0; i < newPixels.length; i++) { 
			int redChannel = (int)((float)(newCountAll[(pixels[i]&0x00ff0000) >> 16])/(newPixels.length*3)*255);
			int greenChannel = (int)((float)(newCountAll[(pixels[i]&0x0000ff00) >> 8])/(newPixels.length*3)*255);
			int blueChannel = (int)((float)(newCountAll[pixels[i]&0x000000ff])/(newPixels.length*3)*255);
			newPixels[i] = blueChannel | greenChannel<<8 | redChannel<<16|0xff000000;
		}
		
		/* here are the right method
		for (int i = 0; i < oldCountB.length; i++) {
			for(int j = 0 ; j <= i; j++) {
				newCountR[i] += oldCountR[j];
				newCountG[i] += oldCountG[j];
				newCountB[i] += oldCountB[j];
			}
		}
		
		for (int i = 0; i < newPixels.length; i++) { 
			int redChannel = (int)((float)(newCountR[(pixels[i]&0x00ff0000) >> 16])/newPixels.length*255);
			int greenChannel = (int)((float)(newCountG[(pixels[i]&0x0000ff00) >> 8])/newPixels.length*255);
			int blueChannel = (int)((float)(newCountB[pixels[i]&0x000000ff])/newPixels.length*255);
			newPixels[i] = blueChannel | greenChannel<<8 | redChannel<<16|0xff000000;
		}
		*/
		BufferedImage image = new BufferedImage(width, height, img.getType());
		image.setRGB(0, 0, width, height, newPixels, 0, width);
		return image;
	}
}
