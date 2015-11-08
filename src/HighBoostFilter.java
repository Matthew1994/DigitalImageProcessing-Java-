import java.awt.image.BufferedImage;


public class HighBoostFilter {
	private static int[][] getMaskArray(BufferedImage img) {
		BufferedImage smoothImage = Smooth.averagingFilter(img, 3);
		int width = smoothImage.getWidth();
		int height = smoothImage.getHeight();
		int[] smPixels = new int[width*height];
		int[] oldPixels = new int[width*height];
		
		//用一个二维数组存储RGB的差值(因为每一位都有可能是负数,所以不能用一个32位int存储RBG)
		int[][] rgbArray = new int[width*height][3];
		smoothImage.getRGB(0, 0, width, height, smPixels, 0, width);
		img.getRGB(0, 0, width, height, oldPixels, 0, width);
		for (int i = 0; i < width*height; i++) {
			rgbArray[i][0] = (oldPixels[i]&0x00ff0000)>>16 - (smPixels[i]&0x00ff0000)>>16; //R
			rgbArray[i][1] = (oldPixels[i]&0x0000ff00)>>8 - (smPixels[i]&0x0000ff00)>>8;   //G
			rgbArray[i][2] = (oldPixels[i]&0x000000ff) - (smPixels[i]&0x000000ff); 		   //B
		}
		smPixels = null;
		oldPixels = null;
		return rgbArray;
	}
	
	public static BufferedImage HBFilter(BufferedImage img, int weight) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] mask = getMaskArray(img);
		for (int i = 0; i < width*height; i++) {
			int red = (pixels[i]&0x00ff0000)>>16+weight*mask[i][0];
			int green = (pixels[i]&0x0000ff00)>>8+weight*mask[i][1];
			int blue = (pixels[i]&0x000000ff)+weight*mask[i][2];
			red = red < 0 ? 0 : (red > 255 ? 255 : red);
			green = green < 0 ? 0 : (green > 255 ? 255 : green);
			blue = blue < 0 ? 0 : (blue > 255 ? 255 : blue);
			pixels[i]= red<<16|green<<8|blue|0xff000000;
		}
		mask = null;
		BufferedImage image = new BufferedImage(width, height, img.getType());
		image.setRGB(0, 0, width, height, pixels, 0, width);
		pixels = null;
		return image;
	}
}
