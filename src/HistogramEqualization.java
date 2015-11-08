import java.awt.image.BufferedImage;



public class HistogramEqualization {
	public static BufferedImage equalizeHist(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		int[] newPixels = new int[width*height];
		int[] oldCount = new int[256];
		int[] newCount = new int[256];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		for (int i = 0; i < pixels.length; i++) {
			oldCount[pixels[i]&0x000000ff]++;
		}
		for (int i = 0; i < oldCount.length; i++) {
			for(int j = 0 ; j <= i; j++) {
				newCount[i] += oldCount[j];
			}
		}
		
		for (int i = 0; i < newPixels.length; i++) { 
			int singleChannel = (int)((float)(newCount[pixels[i]&0x000000ff])/newPixels.length*255);
			newPixels[i] = singleChannel|singleChannel<<8|singleChannel<<16|0xff000000;
		}
		BufferedImage image = new BufferedImage(width, height, 10);
		image.setRGB(0, 0, width, height, newPixels, 0, width);
		return image;
	}
}
