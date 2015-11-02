import java.awt.image.BufferedImage;
import java.io.IOException;



public class quantize {
	public static BufferedImage setGray(BufferedImage image, int level) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] inMatrix = changeDimension2(pixels, width);
		int[][] outMatrix = new int[height][width];
		
		//divide the level of gray
		float temp = (float)(256)/(level-1);
		int[] arr = new int [level];
		arr[0] = 0; arr[level-1] = 255;
		for (int i = 1 ; i < level-1; i++) {
			arr[i] = (int)((float)(i)*temp);
		}

		
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float intervel = (float)(256)/level;
				int blue = inMatrix[y][x]&0x000000ff;
				int green = inMatrix[y][x]&0x0000ff00>>8;
				int red = inMatrix[y][x]&0x00ff0000>>16;
				int gray = (red+green+blue)/3;
				
				for (int i = 0; i < level; i++) {
					if ((float)gray >= (float)(i)*intervel && (float)gray < (float)(i+1)*intervel) {
						outMatrix[y][x] = arr[i]|arr[i]<<8|arr[i]<<16|0xff000000; 
						break;
					}
						
				}
			}
		}
		
		pixels = changeDimension1(outMatrix);
		
		
		BufferedImage outImage = new BufferedImage(width, height, image.getType());
		outImage.setRGB(0, 0, width, height, pixels, 0, width);
		return outImage;
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
