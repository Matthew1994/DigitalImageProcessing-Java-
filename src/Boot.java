import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
  
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Boot {
	public static void main(String[] args) throws IOException {
		UIFrame frame = new UIFrame();
	}
	
}

class UIFrame implements ActionListener {

	private JFrame jframe;
	private JMenuBar menuBar;
	private JMenu docMenu;
	private JMenu opMenu;
	private JLabel label;
	private JPanel jPanel;
	private JMenu hgMenu;
	BufferedImage originImage = null;
	BufferedImage currentImage = null;
	
	public UIFrame() throws IOException {
		super();
		jframe = new JFrame();
	    jframe.setBounds(300, 100, 800, 600);
	    
	    //jframe.getContentPane().setLayout(null); // 默认FlowLayout
	    
	    jframe.setTitle("ImageProcessor");
	    jframe.setJMenuBar(getJMenuBar());
	    jframe.getContentPane().add(getPanel(), BorderLayout.CENTER);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
	}
	
	
	
	public JPanel getPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
		}
		jPanel.setLayout(null);
		jPanel.add(setJLabel(originImage));
		//jPanel.setBounds(10, 10, 500,450);
		jPanel.setBackground(new Color(0xcccccc));
		return jPanel;
	}
	
	public JLabel setJLabel(BufferedImage newImage) {
		if (newImage == null) {
			label = null;
			label = new JLabel(); 
			return label;
		}
		if (label == null) {
			label = new JLabel();
		}
		label.setIcon(new ImageIcon(newImage));
		
		//************* 修改此处函数,使得窗口接受大图的自动放大,小图时保持原来设定 ***********************
		label.setBounds((jPanel.getWidth()-newImage.getWidth())/2, (jPanel.getHeight()-newImage.getHeight())/2, newImage.getWidth(), newImage.getHeight());
		return label;
	}
	
	
	public JMenuBar getJMenuBar() {
		if (menuBar != null) {
			return menuBar;
		}
		menuBar = new JMenuBar();
		menuBar.setLocation(30, 30);
		menuBar.add(getDocMenu(),null);
		menuBar.add(getOpMenu(),null);
		menuBar.add(getHistogramMenu(),null);
		return menuBar;
	}
	
	public JMenu getDocMenu() {
		if (docMenu != null) {
			return docMenu;
		}
		docMenu = new JMenu("文件");
		docMenu.setSize(100, 100);
		JMenuItem item = new JMenuItem("打开");
		item.addActionListener(this);
		JMenuItem itemSave = new JMenuItem("保存");
		itemSave.addActionListener(getSaveAction());
		docMenu.add(item);
		docMenu.add(itemSave);
		return docMenu;
	}

	public JMenu getOpMenu() {
		if (opMenu != null) {
			return opMenu;
		}
		opMenu = new JMenu("操作");
		opMenu.add(getScaleMenu());
		opMenu.add(getGrayMenu());
		opMenu.add(getSmoothMenu());
		opMenu.add(getSharpenMenu());
		opMenu.add(getHBFilterMenu());
		opMenu.add(getOriginalImage());
		return opMenu;
	}
	
	public JMenuItem getHBFilterMenu() {
		JMenuItem item = new JMenuItem("高提升滤波");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(currentImage != null) {
					currentImage = HighBoostFilter.HBFilter(currentImage, 40);
					setJLabel(currentImage);
				}
				else {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
			}
		});
		return item;
	}
	
	public JMenuItem getSharpenMenu() {
		JMenuItem item = new JMenuItem("锐化");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(currentImage != null) {
					currentImage = Sharpen.sharpenByMatrix3(currentImage);
					setJLabel(currentImage);
				}
				else {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
			}
		});
		return item;
	}
	
	public JMenuItem getSmoothMenu() {
		JMenuItem item = new JMenuItem("平滑");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(currentImage != null) {
					currentImage = Smooth.averagingFilter(currentImage, 5);
					setJLabel(currentImage);
				}
				else {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
			}
		});
		return item;
	}
	
	public JMenuItem getOriginalImage() {
		JMenuItem item = new JMenuItem("原图");
		item.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if (originImage != null) {
					currentImage = originImage;
					setJLabel(currentImage);
				}
				else {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
			}
		});
		return item;
	}
	
	public JMenu getHistogramMenu() {
		if (hgMenu != null) {
			return hgMenu;
		}
		hgMenu = new JMenu("直方图");
		hgMenu.add(getGrayHistogramMenu());
		hgMenu.add(getHisEquMenu());
		return hgMenu;
	}
	
	public JMenuItem getGrayHistogramMenu() {
		JMenuItem item = new JMenuItem("灰度直方图");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (currentImage == null) {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
				else {
				Object[] options = {};
				JOptionPane.showOptionDialog(null, "", "灰度直方图", 0, 0,
											new ImageIcon(Histogram.getGrayHistogram(currentImage)), 
											options, null);
				}
			}
		});
		return item;
	}
	
	public JMenuItem getHisEquMenu() {
		JMenuItem item = new JMenuItem("直方图均衡化");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentImage != null) {
					currentImage = HistogramEqualization.equalizeHist(currentImage);
					setJLabel(currentImage);
				}
				else {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
			}
		});
		return item;
	}
	
	public JMenuItem getScaleMenu() {
		JMenuItem subMenuForScale = new JMenu("缩放");
		final String size[] = {"12*8","24*16","48*32","96*64","192*128","300*200","450*300","500*200"};
		JMenuItem subMenuForNB = new JMenu("最临近");
		JMenuItem subMenuForBL = new JMenu("双线性");
		//最临近方法的选项
		JMenuItem[] itemNB = new JMenuItem[size.length+1];
		for (int i = 0; i < size.length; i++) {
			itemNB[i] = new JMenuItem(size[i]);
			itemNB[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s =((JMenuItem) e.getSource()).getText();
					int w = Integer.parseInt(s.substring(0, s.indexOf('*')));
					int h = Integer.parseInt(s.substring(s.indexOf('*')+1,s.length()));
					try {
						if (currentImage != null) {
							currentImage = scale.nearestNeighbor(currentImage, w, h);
							setJLabel(currentImage);
						} else {
							JOptionPane.showMessageDialog(null, "请打开一张图片!");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			
			subMenuForNB.add(itemNB[i]);
		}
		itemNB[size.length] = new JMenuItem("自定义");
		itemNB[size.length].addActionListener(getActionListenerForScale("NB"));
		subMenuForNB.add(itemNB[size.length]);
		
		//双线性的选项
		JMenuItem[] itemBL = new JMenuItem[size.length+1];
		for (int i = 0; i < size.length; i++) {
			itemBL[i] = new JMenuItem(size[i]);
			itemBL[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s =((JMenuItem) e.getSource()).getText();
					int w = Integer.parseInt(s.substring(0, s.indexOf('*')));
					int h = Integer.parseInt(s.substring(s.indexOf('*')+1,s.length()));
					try {
						if (currentImage != null) {
							currentImage = scale.bilinear(currentImage, w, h);
							setJLabel(currentImage);
						} else {
							JOptionPane.showMessageDialog(null, "请打开一张图片!");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			subMenuForBL.add(itemBL[i]);
		}
		itemBL[size.length] = new JMenuItem("自定义");
		itemBL[size.length].addActionListener(getActionListenerForScale("BL"));
	    subMenuForBL.add(itemBL[size.length]);
	    
		
		subMenuForScale.add(subMenuForNB);
		subMenuForScale.add(subMenuForBL);
		return subMenuForScale;
	}
	
	public ActionListener getActionListenerForScale(final String scaleType) {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentImage != null) {
					while(true) {
						String s = JOptionPane.showInputDialog(null, "输入格式为:宽度*长度", "自定义尺寸", JOptionPane.INFORMATION_MESSAGE);
						if (s == null)
							break;
						int w;
						int h;
						if ( s.matches("^[1-9][0-9]*\\*[1-9][0-9]*$")) {
							w = Integer.parseInt(s.substring(0,s.indexOf('*')));
							h = Integer.parseInt(s.substring(s.indexOf('*')+1, s.length()));
							if (w > 1200 || h > 1200) {
								JOptionPane.showMessageDialog(null, "亲,你定制的尺寸太大了!");
							}
							else {
								try {
									if (scaleType == "BL")
										currentImage = scale.bilinear(currentImage, w, h);
									else
										currentImage = scale.nearestNeighbor(currentImage, w, h);
									setJLabel(currentImage);
									break;
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						} 
						else {
							JOptionPane.showMessageDialog(null, "!!!请注意输入格式为:宽度*长度");
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
			}
		};
		return al;
	}
	
	public JMenuItem getGrayMenu() {
		JMenuItem subMenuForGray = new JMenu("灰度");
		final String size[] = {"2","4","8","32","64","128","256"};
		JMenuItem[] itemNB = new JMenuItem[size.length+1];
		for (int i = 0; i < size.length; i++) {
			itemNB[i] = new JMenuItem(size[i]);
			itemNB[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s =((JMenuItem) e.getSource()).getText();
					int level = Integer.parseInt(s);
					try {
						if (currentImage != null) {
							currentImage = quantize.setGray(currentImage, level);
							setJLabel(currentImage);
						} else {
							JOptionPane.showMessageDialog(null, "请打开一张图片!");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			
			subMenuForGray.add(itemNB[i]);
		}
		itemNB[size.length] = new JMenuItem("自定义");
		itemNB[size.length].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originImage != null) {
					while(true) {
						String s = JOptionPane.showInputDialog(null, "请输入一个1~256的整数", "自定义灰度", JOptionPane.INFORMATION_MESSAGE);
						if (s == null)
							break;
						int level;
						if ( s.matches("^[1-9][0-9]{0,2}$")) {
							level = Integer.parseInt(s);
							if (level > 256) {
								JOptionPane.showMessageDialog(null, "输入必须为一个1~256的整数");
							}
							else {
								try {
									currentImage = quantize.setGray(originImage, level);
									setJLabel(currentImage);
									break;
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						} 
						else {
							JOptionPane.showMessageDialog(null, "输入必须为一个1~256的整数");
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
			}
				
		});
		subMenuForGray.add(itemNB[size.length]);
		
		return subMenuForGray;
	}
	




	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand() == "打开") {
			JFileChooser fileChooser = new JFileChooser();
			FileFilter fileFilter = new FileNameExtensionFilter("*.jpg/png/bmp", "jpg", "png","bmp");
			fileChooser.setFileFilter(fileFilter);
			fileChooser.setAcceptAllFileFilterUsed(false);
			int returnVal = fileChooser.showOpenDialog(null);   
		    if(returnVal == fileChooser.APPROVE_OPTION) {   
		        File f = fileChooser.getSelectedFile();   
		        JOptionPane.showConfirmDialog(null, "你选择的文件名是："+fileChooser.getName(f),   
		                                      "确认",JOptionPane.ERROR_MESSAGE);
		        try {
					originImage = ImageIO.read(f);
					currentImage = originImage;
					setJLabel(originImage);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }   
		}
	}
	
	public ActionListener getSaveAction() {
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentImage != null) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.addChoosableFileFilter(new MyFileFilter("jpg", "*.jpg"));
					fileChooser.addChoosableFileFilter(new MyFileFilter("bmp", "*.bmp"));
					fileChooser.setFileFilter(new MyFileFilter("png", "*.png"));

					fileChooser.setAcceptAllFileFilterUsed(false);
					File temp = new File("newImage");
					fileChooser.setSelectedFile(temp);//设置默认文件名
			        int retval = fileChooser.showSaveDialog(null);//显示“保存文件”对话框
			        if(retval == JFileChooser.APPROVE_OPTION) {
			            File name = fileChooser.getSelectedFile();
			            MyFileFilter finalFilter = (MyFileFilter)(fileChooser.getFileFilter());
			            String end =finalFilter.getEnds();
			            File newFile = null;
			            if (name.getAbsolutePath().toUpperCase().endsWith(end.toUpperCase())) {
			                // 如果文件是以选定扩展名结束的，则使用原名
			                newFile = name;
			            }
			            else {
			                // 否则加上选定的扩展名
			                newFile = new File(name.getAbsolutePath() +"." +end);
			            }
			            name = null;
		                temp = null;
			            try {
							ImageIO.write(currentImage, end, newFile);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "保存失败!");
						}
			            JOptionPane.showMessageDialog(null, "保存成功!");
			        }
				} else {
					JOptionPane.showMessageDialog(null, "请打开一张图片!");
				}
			}
		};
		return action;
	}	
}

class MyFileFilter extends FileFilter {

	  String ends; // 文件后缀
	  String description; // 文件描述文字

	  public MyFileFilter(String ends, String description) { // 构造函数
	    this.ends = ends; // 设置文件后缀
	    this.description = description; // 设置文件描述文字
	  }

	  @Override
	  // 只显示符合扩展名的文件，目录全部显示
	  public boolean accept(File file) {
	    if (file.isDirectory()) return true;
	    String fileName = file.getName();
	    if (fileName.toUpperCase().endsWith(this.ends.toUpperCase())) return true;
	    return false;
	  }

	  @Override
	  // 返回这个扩展名过滤器的描述
	  public String getDescription() {
	    return this.description;
	  }
	  
	  // 返回这个扩展名过滤器的扩展名
	  public String getEnds() {
	    return this.ends;
	  }
	}