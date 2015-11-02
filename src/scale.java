import java.awt.image.BufferedImage;
import java.io.IOException;


public class scale {
	//scale by nearest Neighbor interpolation
	public static BufferedImage nearestNeighbor(BufferedImage image, int w, int h) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		float scaleTimeH = (float)(height)/h;
		float scaleTimeW = (float)(width)/w;
		BufferedImage outImage = new BufferedImage(w, h, image.getType());
		int[] inPixels = new int[width*height];
		image.getRGB(0, 0, width, height, inPixels, 0, width);
		int[] outPixels = new int[w*h];
		
		int[][] inArr = changeDimension2(inPixels, width);
		int[][] outArr = new int[h][w];
		
		for (int i = 0; i < h;i++) {
			for (int y = 0; y < w; y++) {
				outArr[i][y] = inArr[(int)((float)(i)*scaleTimeH)][(int)((float)(y)*scaleTimeW)];
			}
		}
		outPixels = changeDimension1(outArr);

		
		outImage.setRGB(0, 0, w, h, outPixels, 0, w);
		//release the memory
		return outImage;

	}
	
	//scale by bilinear interpolation
	public static BufferedImage bilinear(BufferedImage image, int w, int h) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		float scaleTimeH = (float)(height)/h;
		float scaleTimeW = (float)(width)/w;
		BufferedImage outImage = new BufferedImage(w, h, image.getType());
		int[] inPixels = new int[width*height];
		image.getRGB(0, 0, width, height, inPixels, 0, width);
		int[] outPixels = new int[w*h];
		
		int[][] inArr = changeDimension2(inPixels, width);
		int[][] outArr = new int[h][w];
		
		for (int x = 0; x < w;x++) {
			for (int y = 0; y < h; y++) {
				
				float srcX = x*scaleTimeW;
				float srcY = y*scaleTimeH;
				
				int x1 = (int)(srcX);
				int x2 = x1+1 >= width?x1:x1+1;
				int y1 = (int)(srcY);
				int y2 = y1+1 >= height?y1:y1+1;
				
				float fx1 = srcX - x1;
				float fx2 = 1.0f - fx1;
				float fy1 = srcY - y1; 
				float fy2 = 1.0f - fy1;

				float s1 = fx1*fy1;
				float s2 = fx2*fy1;
				float s3 = fx2*fy2;
				float s4 = fx1*fy2;
				
				//outArr[y][x] = inArr[y2][x2]*s1+inArr[y2][x1]*s2+inArr[y1][x1]*s3+inArr[y1][x2]*s4;
				int red = Math.round(getRGB(inArr[y2][x2], 'r')*s1+getRGB(inArr[y2][x1], 'r')*s2+getRGB(inArr[y1][x1], 'r')*s3
						  +getRGB(inArr[y1][x2], 'r')*s4);
				int green = Math.round(getRGB(inArr[y2][x2], 'g')*s1+getRGB(inArr[y2][x1], 'g')*s2+getRGB(inArr[y1][x1], 'g')*s3
						  +getRGB(inArr[y1][x2], 'g')*s4);
				int blue = Math.round(getRGB(inArr[y2][x2], 'b')*s1+getRGB(inArr[y2][x1], 'b')*s2+getRGB(inArr[y1][x1], 'b')*s3
						  +getRGB(inArr[y1][x2], 'b')*s4);
				outArr[y][x] = (red << 16) | (green << 8) | blue |0xff000000;
			}
				
		}
		outPixels = changeDimension1(outArr);

		
		outImage.setRGB(0, 0, w, h, outPixels, 0, w);
		
		return outImage;
	}
	
	//get RGB value in single channel
	public static int getRGB(int value, char color) {
		if (color == 'r')
			return (value&0x00ff0000) >> 16;
		if (color == 'g')
			return value&0x0000ff00 >> 8;
		if (color == 'b')
			return value&0x000000ff;
		if (color == 'a') 
			return value&0xff000000 >> 24;
		return -1;
	}
	
	
	//change the array dimension to 2
	public static int[][] changeDimension2(int[] oldArr, int width) {
		int[][] newArr = new int[oldArr.length/width][width];
		for (int i = 0; i < oldArr.length/width; i++) {
			for (int y = 0; y < width; y++) {
				newArr[i][y] = oldArr[i*width+y];
			}
		}
		return newArr;
	}
	
	//change the array dimension to 1
	public static int[] changeDimension1(int[][] oldArr) {
		int[] newArr = new int[oldArr.length*oldArr[0].length];
		for (int i = 0; i < oldArr.length; i++) {
			for (int y = 0; y < oldArr[0].length; y++) {
				newArr[i*oldArr[0].length+y] = oldArr[i][y];
			}
		}
		return newArr;
	}
	
}
