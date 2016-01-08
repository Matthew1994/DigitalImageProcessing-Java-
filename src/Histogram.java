import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.*;  
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.data.category.CategoryDataset;  
import org.jfree.data.category.DefaultCategoryDataset;  

/** 
* 该类用于演示最简单的柱状图生成 
*/  
public class Histogram {  
	public static BufferedImage getGrayHistogram(BufferedImage img) {  
		CategoryDataset dataset = getDataSet(img, 2);  
        JFreeChart chart = ChartFactory.createBarChart3D(  
                                                        "灰度直方图", // 图表标题  
                                                        "灰度级别", // 目录轴的显示标签  
                                                        "数目", // 数值轴的显示标签  
                                                        dataset, // 数据集  
                                                        PlotOrientation.VERTICAL, // 图表方向：水平、垂直  
                                                        false,         // 是否显示图例(对于简单的柱状图必须是false)  
                                                        false,         // 是否生成工具  
                                                        false         // 是否生成URL链接  
                                                        );    
        BufferedImage image = chart.createBufferedImage(600, 400);
        
        
        return image;
    }
	
	public static BufferedImage getColorHistogram(BufferedImage img, int flag) {  
		CategoryDataset dataset = getDataSet(img, flag);  
		JFreeChart chart;
		if (flag == 0) {
        	chart = ChartFactory.createBarChart3D(  
                                                "R值直方图", // 图表标题  
                                                "级别", // 目录轴的显示标签  
                                                "数目", // 数值轴的显示标签  
                                                dataset, // 数据集  
                                                PlotOrientation.VERTICAL, // 图表方向：水平、垂直  
                                                false,         // 是否显示图例(对于简单的柱状图必须是false)  
                                                false,         // 是否生成工具  
                                                false         // 是否生成URL链接  
                                                );
        }
        else if (flag == 1) {
        	chart = ChartFactory.createBarChart3D(  
							                    "G值直方图", // 图表标题  
							                    "级别", // 目录轴的显示标签  
							                    "数目", // 数值轴的显示标签  
							                    dataset, // 数据集  
							                    PlotOrientation.VERTICAL, // 图表方向：水平、垂直  
							                    false,         // 是否显示图例(对于简单的柱状图必须是false)  
							                    false,         // 是否生成工具  
							                    false         // 是否生成URL链接  
							                    );
		}
        else {
        	chart = ChartFactory.createBarChart3D(  
							                    "B值直方图", // 图表标题  
							                    "级别", // 目录轴的显示标签  
							                    "数目", // 数值轴的显示标签  
							                    dataset, // 数据集  
							                    PlotOrientation.VERTICAL, // 图表方向：水平、垂直  
							                    false,         // 是否显示图例(对于简单的柱状图必须是false)  
							                    false,         // 是否生成工具  
							                    false         // 是否生成URL链接  
							                    );
        }
        
        BufferedImage image = chart.createBufferedImage(600, 400);
        return image;
    }
	
    /** 
     * 获取一个演示用的简单数据集对象 
     * @param flag : (0,1,3) <==> (r,g,b)
     * @return 
     */  
    private static CategoryDataset getDataSet(BufferedImage img, int flag) {  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = new int[width*height];
		int[] count = new int[256];
		img.getRGB(0, 0, width, height, pixels, 0, width);
        for (int i = 0; i < width*height; i++) {
        	if (flag == 0)
        		count[(pixels[i]&0x00ff0000) >> 16]++;
        	else if (flag == 1)
        		count[(pixels[i]&0x0000ff00) >> 8]++;
        	else
        		count[pixels[i]&0x000000ff]++;
        }
		for(int i = 0; i < 256; i++) {
        	dataset.addValue(count[i], "", i+"");  
        }  
        return dataset;  
    }  
   
}  