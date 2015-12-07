import java.awt.image.BufferedImage;

import org.jfree.util.Log;
import org.omg.CORBA.PUBLIC_MEMBER;



public class DFT {
	
	final static double PI = 3.1415;
	
	public static BufferedImage dft2d(BufferedImage image, boolean flag) {
		if (flag)
			return showDFT(image);
		else
			return showIDFT(image);
	}
	
	public static BufferedImage filter2d_freq(BufferedImage image,  int[][] filter) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		//imageOriginal data
		int[][] f = quantize.changeDimension2(pixels, width);
		int[][] fp = new int[height*2][width*2];
		Complex[][] F = new Complex[height*2][width*2];
		
		//filter data
		int[][] hp = new int[height*2][width*2];
		Complex[][] H = new Complex[height*2][width*2];
		
		//imageOutput data
		Complex[][] G = new Complex[height*2][width*2];
		Complex[][] g = new Complex[height*2][width*2];

		
		//将M*N的image填充成P*Q (P = 2M, Q = 2N)
		for (int x = 0; x < width*2; x++) {
			for (int y = 0; y < height*2; y++) {
				if (x < width && y < height)
					fp[y][x] = f[y][x];
				else 
					fp[y][x] = 0;
			}
		}
		
		//通过傅里叶变换将填充后的f(x,y)转化为中心化后F(u,v) ===> 可调用Complex[][] dft(int[][] spatialArr, int width, int height)
		F = dft(fp, width*2, height*2);
		
		//将filter的h(x,y)填充成P*Q
		for (int x = 0; x < width*2; x++) {
			for (int y = 0; y < height*2; y++) {
				int w = filter[0].length;
				int h = filter.length;
				if (x < w && y < h) 
					hp[y][x] = filter[y][x];
				else
					hp[y][x] = 0;
			}
		}
		
		//通过傅里叶变换将填充后的h(x,y)转化为中心化后的H(u,v)
		H = dft(hp, width*2, height*2);
		
		//########### !!!这里需要重新标定H(u,v)实部的值在[0,1]之间 ############
		double max = -1000000;
		double min = 1000000;
		for (int v = 0; v < height*2; v++) {
			for (int u = 0; u < width*2; u++){
				if (H[v][u].getReal() > max)
					max =  H[v][u].getReal();
				if (H[v][u].getReal() < min)
					min = H[v][u].getReal();
			}
		}
		for (int v = 0; v < height*2; v++) {
			for (int u = 0; u < width*2; u++){
				H[v][u].setReal((H[v][u].getReal()-min)/(max-min));
			}
		}
		
		//#################  Debug code ##############
//		for (int v = 0; v < height*2; v++) {
//			System.out.println();
//			for (int u = 0; u < width*2; u++){
//				System.out.print((int)(H[v][u].getReal()*10)+" ");
//			}
//		}
		
		//G(u,v) = F(u,v)H(u,v)
		for (int u = 0; u < width*2; u++) {
			for (int v = 0; v < height*2; v++) {
				G[v][u] = Complex.multiply(F[v][u], new Complex(H[v][u].getReal(), 0));
			}
		}
		
		//将G(u,v) 反变换 g(x,y)
		g = idft(G, width*2, height*2);
		
		
		//输出图像
		int[][] showIDFT = new int[height][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pixel= (int)g[y][x].getReal();				
				showIDFT[y][x] = pixel | pixel << 8 | pixel << 16 | 0xff000000;
			}
		}
		
		BufferedImage outImage = new BufferedImage(width, height, image.getType());
		outImage.setRGB(0, 0, width, height, quantize.changeDimension1(showIDFT), 0, width);
		return outImage;
	}
	
	/*
	 * @formula ====>  F(u,v) = EE f(x,y)e^-j2π(ux/M+vy/N) for M*N
	 * @parameter a matrix in spatial, the number of columns and rows
	 * @return a matrix (centered) in frequency after dft
	 * */
	private static Complex[][] dft(int[][] spatialArr, int width, int height) {
		Complex[][] freArr = new Complex[height][width];
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						if (freArr[v][u] == null) {
							freArr[v][u] = new Complex(0, 0);
						}
						double real = (spatialArr[y][x]&0x000000ff)*Math.cos(-1*PI*2*(u*x/(double)width+v*y/(double)height))*Math.pow(-1, x+y);
						double imaginary = (spatialArr[y][x]&0x000000ff)*Math.sin(-1*PI*2*(u*x/(double)width+v*y/(double)height))*Math.pow(-1, x+y);
						freArr[v][u] = Complex.add(freArr[v][u], new Complex(real, imaginary));
					}
				}
			}
		}
		return freArr;
	}
	
	
	/*
	 * @formula ====>  f(x,y) = EE F(u,v)e^j2π(ux/M+vy/N) for M*N
	 * @parameter a matrix in frequency, the number of columns and rows
	 * @return a matrix in spatial after idft
	 * */
	private static Complex[][] idft(Complex[][] freArr, int width, int height) {
		Complex[][] spatialArr = new Complex[height][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int u = 0; u < width; u++) {
					for (int v = 0; v < height; v++) {
						if (spatialArr[v][u] == null) {
							spatialArr[v][u] = new Complex(0, 0);
						}
						Complex exp = new Complex();
						exp.setReal(Math.cos(PI*2*(u*x/(double)width+v*y/(double)height)));
						exp.setImaginary(Math.sin(PI*2*(u*x/(double)width+v*y/(double)height)));
						spatialArr[y][x] = Complex.add(Complex.multiply(freArr[v][u], exp), spatialArr[y][x]); 
					}
				}
			}
		}
		 
		// restore centered
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				spatialArr[y][x].setReal(spatialArr[y][x].getReal()*Math.pow(-1, x+y)/width/height);
				spatialArr[y][x].setImaginary(spatialArr[y][x].getImaginary()*Math.pow(-1, x+y)/width/height);
			}
		}
		return spatialArr;
	}
	
	
	private static BufferedImage showDFT(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixelArr = quantize.changeDimension2(pixels, width);
		int[][] showDFT = new int[height][width];
		Complex[][] newArr = new Complex[height][width];
		
		//F(u,v) = EE f(x,y)e^-j2π(ux/M+vy/N) for M*N
		newArr = dft(pixelArr, width, height);
		
		
		//log transformation of the spectrum
		double max=-10000000, min=10000000;
		double[][] temp = new double[height][width];
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				newArr[v][u].setReal(newArr[v][u].getReal()/width/height);
				newArr[v][u].setImaginary(newArr[v][u].getImaginary()/width/height);
				double s = Math.sqrt(newArr[v][u].getReal()*newArr[v][u].getReal()+newArr[v][u].getImaginary()*newArr[v][u].getImaginary());
				double sum = Math.log(1+s);
				if (sum > max)
					max = sum;
				if (sum < min)
					min = sum;
				temp[v][u] = sum;
			}
		}
		
		//scaling
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {  
				int sum = (int)(((temp[v][u]-min)/(max-min))*255);
				showDFT[v][u] = sum | sum << 8 | sum << 16 | 0xff000000;
			}
		}
		
		
		
		
		BufferedImage outImage = new BufferedImage(width, height, image.getType());
		outImage.setRGB(0, 0, width, height, quantize.changeDimension1(showDFT), 0, width);
		return outImage;
	}
	
	private static BufferedImage showIDFT(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		int[][] pixelArr = quantize.changeDimension2(pixels, width);
		int[][] showIDFT = new int[height][width];
		Complex[][] newArr = new Complex[height][width];
		
		newArr = idft(dft(pixelArr, width, height), width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pixel= (int)newArr[y][x].getReal();
				
				//System.out.println((int)newArr[y][x].getImaginary());
				//虚部取整后全是0,而实部取整后是[0, 255]的数
				
				showIDFT[y][x] = pixel | pixel << 8 | pixel << 16 | 0xff000000;
			}
		}
		
		BufferedImage outImage = new BufferedImage(width, height, image.getType());
		outImage.setRGB(0, 0, width, height, quantize.changeDimension1(showIDFT), 0, width);
		return outImage;
		
	}

}









class Complex {
	
	private double real;
	private double imaginary;
	
	public Complex(double re, double imagin) {
		this.real = re;
		this.imaginary = imagin;
	}
	
	public Complex() {
		this.real = 0;
		this.imaginary = 0;		
	}
	
	public void setReal(double re) {
		this.real = re;
	}
	
	public void setImaginary(double imagin) {
		this.imaginary = imagin;
	}
	
	public double getReal() {
		return this.real;
	}
	
	public double getImaginary() {
		return this.imaginary;
	}
	
	public static Complex add(Complex a, Complex b) {
		return new Complex(a.getReal()+b.getReal(), a.getImaginary()+b.getImaginary());
	}
	
	public static Complex subtract(Complex a, Complex b) {
		return new Complex(a.getReal()-b.getReal(), a.getImaginary()-b.getImaginary());
	}
	
	public static Complex multiply(Complex a, Complex b) {
		double newReal = a.getReal() * b.getReal() - a.getImaginary() * b.getImaginary();  
        double newImaginary = a.getReal() * b.getImaginary() + a.getImaginary() * b.getReal();  
        Complex result = new Complex(newReal,newImaginary);  
        return result;  
	}
	
	public static Complex divide(Complex a, Complex b) {
		
		double newReal = (a.getReal() * b.getReal() + a.getImaginary() * b.getImaginary())/
						  (b.getReal() * b.getReal() + b.getImaginary() * b.getImaginary());
		
		double newImaginary = (a.getImaginary() * b.getReal() - a.getReal() * b.getImaginary())/ 
							   (b.getReal() * b.getImaginary() + b.getImaginary() * b.getImaginary()); 
		Complex result = new Complex(newReal, newImaginary);
		return result;
	}
}


