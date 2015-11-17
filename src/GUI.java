import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import javax.swing.event.*;
import java.lang.*;
import java.util.*;

public class GUI extends JFrame implements ActionListener {
	public static final int FRAME_WIDTH = 900;
	public static final int FRAME_HEIGHT = 600;
	public static final int LOCATION_X = 250;
	public static final int LOCATION_Y = 200;

	protected JMenuBar menubar;

	protected JMenu menuFile;
	protected JMenuItem open;
	protected JMenuItem save;

	protected JMenu edit;
	protected JMenuItem originalImage;
	protected JMenuItem undo;

	protected JMenu menuGeoTrans;
	protected JMenuItem scale;
	protected JMenuItem rotate;

	protected JMenu menuColorTrans;
	protected JMenuItem grayTrans;
	protected JMenuItem binaryTrans;
	protected JMenuItem colorHistogram;
	protected JMenuItem colorHistEqual;

	protected JMenu grayImageOperation;

	protected JMenu menuIntensityTrans;
	protected JMenu subMenubasicTrans;
	protected JMenu submenuHistogram;
	protected JMenu submenuSmooth;
	protected JMenu subsubmenuLinear;
	protected JMenu subsubmenuNonlinear;
	protected JMenu submenuSharpen;

	protected JMenuItem negativeImage;
	protected JMenuItem logTrans;

	protected JMenuItem grayHistogram;
	protected JMenuItem grayHistEqual;

	protected JMenuItem boxFilter;
	protected JMenuItem weightedAveFilter;

	protected JMenuItem medianFilter;

	protected JMenuItem laplacianFilter;
	protected JMenuItem sobelFilter;

	protected JMenu menuAddNoise;
	protected JMenuItem gaussianNoise;
	protected JMenuItem saltPepperNoise;

	protected JMenu menuDenoise;
	protected JMenu deGaussianUniformNoise;
	protected JMenu deSaltNoise;
	protected JMenu dePepperNoise;
	protected JMenu deSaltpepperNoise;

	protected JMenuItem arithMeanFilter;
	protected JMenuItem geoMeanFilter;
	protected JMenuItem harmMeanFilter;
	protected JMenuItem contraharmMeanFilter;
	protected JMenuItem medianFilter2;
	protected JMenuItem maxFilter;
	protected JMenuItem minFilter;

	protected JMenu menuFlashPlayer;
	protected JMenuItem flashPlayer;

	protected MyPanel panel;
	protected JLabel label;

	protected static ImageProcess ipro = new ImageProcess();
	protected BufferedImage scaleImage;
	protected BufferedImage rotateImage;
	protected BufferedImage grayImage;
	protected BufferedImage binaryImage;
	protected MyPanel grayPanel;
	protected MyPanel binaryPanel;

	public GUI() {
		getContentPane().setLayout(null);

		menubar = new JMenuBar();
		setJMenuBar(menubar);

		menuFile = new JMenu("文件");
		open = new JMenuItem("打开");
		save = new JMenuItem("保存");

		menubar.add(menuFile);
		menuFile.add(open);
		menuFile.addSeparator();
		menuFile.add(save);

		edit = new JMenu("编辑");
		originalImage = new JMenuItem("原图");
		undo = new JMenuItem("撤销");
		menubar.add(edit);
		edit.add(originalImage);
		edit.addSeparator();
		edit.add(undo);

		menuGeoTrans = new JMenu("几何变换");
		scale = new JMenuItem("缩放");
		rotate = new JMenuItem("旋转");
		menubar.add(menuGeoTrans);
		menuGeoTrans.add(scale);
		menuGeoTrans.addSeparator();
		menuGeoTrans.add(rotate);

		menuColorTrans = new JMenu("色彩变换");
		grayTrans = new JMenuItem("灰度变换");
		binaryTrans = new JMenuItem("黑白变换");
		colorHistogram = new JMenuItem("显示直方图");
		colorHistEqual = new JMenuItem("直方图均衡化");

		menubar.add(menuColorTrans);
		menuColorTrans.add(grayTrans);
		menuColorTrans.addSeparator();
		menuColorTrans.add(binaryTrans);
		menuColorTrans.addSeparator();
		menuColorTrans.add(colorHistogram);
		menuColorTrans.addSeparator();
		menuColorTrans.add(colorHistEqual);

		grayImageOperation = new JMenu("灰度图像操作");

		menuIntensityTrans = new JMenu("图像增强");
		subMenubasicTrans = new JMenu("基本变换");
		submenuHistogram = new JMenu("直方图");
		submenuSmooth = new JMenu("平滑");
		submenuSharpen = new JMenu("锐化");
		subsubmenuLinear = new JMenu("线性变换");
		subsubmenuNonlinear = new JMenu("非线性变换");

		negativeImage = new JMenuItem("负图像");
		logTrans = new JMenuItem("对数变换");

		grayHistogram = new JMenuItem("显示直方图");
		grayHistEqual = new JMenuItem("直方图均衡化");

		boxFilter = new JMenuItem("盒状滤波");
		weightedAveFilter = new JMenuItem("加权平均滤波");
		medianFilter = new JMenuItem("中值滤波");

		laplacianFilter = new JMenuItem("Laplacian滤波");
		sobelFilter = new JMenuItem("Sobel滤波");

		menubar.add(grayImageOperation);
		grayImageOperation.add(menuIntensityTrans);
		grayImageOperation.addSeparator();

		menuIntensityTrans.add(subMenubasicTrans);
		menuIntensityTrans.addSeparator();
		menuIntensityTrans.add(submenuHistogram);
		menuIntensityTrans.addSeparator();
		menuIntensityTrans.add(submenuSmooth);
		menuIntensityTrans.addSeparator();
		menuIntensityTrans.add(submenuSharpen);

		subMenubasicTrans.add(negativeImage);
		subMenubasicTrans.addSeparator();
		subMenubasicTrans.add(logTrans);

		submenuSmooth.add(subsubmenuLinear);
		submenuSmooth.addSeparator();
		submenuSmooth.add(subsubmenuNonlinear);

		submenuHistogram.add(grayHistogram);
		submenuHistogram.addSeparator();
		submenuHistogram.add(grayHistEqual);

		subsubmenuLinear.add(boxFilter);
		subsubmenuLinear.addSeparator();
		subsubmenuLinear.add(weightedAveFilter);
		subsubmenuNonlinear.add(medianFilter);

		submenuSharpen.add(laplacianFilter);
		submenuSharpen.addSeparator();
		submenuSharpen.add(sobelFilter);

		menuAddNoise = new JMenu("添加噪声");
		gaussianNoise = new JMenuItem("高斯噪声");
		saltPepperNoise = new JMenuItem("椒盐噪声");

		menubar.add(grayImageOperation);
		grayImageOperation.add(menuAddNoise);
		grayImageOperation.addSeparator();

		menuAddNoise.add(gaussianNoise);
		menuAddNoise.addSeparator();
		menuAddNoise.add(saltPepperNoise);

		menuDenoise = new JMenu("去噪");
		deGaussianUniformNoise = new JMenu("高斯或均匀噪声");
		deSaltNoise = new JMenu("盐粒噪声");
		dePepperNoise = new JMenu("胡椒噪声");
		deSaltpepperNoise = new JMenu("椒盐噪声");
		arithMeanFilter = new JMenuItem("算术均值滤波");
		geoMeanFilter = new JMenuItem("几何均值滤波");
		harmMeanFilter = new JMenuItem("谐波均值滤波");
		contraharmMeanFilter = new JMenuItem("逆谐波均值滤波");
		medianFilter2 = new JMenuItem("中值滤波");
		maxFilter = new JMenuItem("最大值滤波");
		minFilter = new JMenuItem("最小值滤波");

		menubar.add(grayImageOperation);
		grayImageOperation.add(menuDenoise);

		menuDenoise.add(deGaussianUniformNoise);
		menuDenoise.addSeparator();
		menuDenoise.add(deSaltNoise);
		menuDenoise.addSeparator();
		menuDenoise.add(dePepperNoise);
		menuDenoise.addSeparator();
		menuDenoise.add(deSaltpepperNoise);

		deGaussianUniformNoise.add(arithMeanFilter);
		deGaussianUniformNoise.addSeparator();
		deGaussianUniformNoise.add(geoMeanFilter);

		deSaltNoise.add(harmMeanFilter);
		deSaltNoise.addSeparator();
		deSaltNoise.add(minFilter);

		dePepperNoise.add(contraharmMeanFilter);
		dePepperNoise.addSeparator();
		dePepperNoise.add(maxFilter);

		deSaltpepperNoise.add(medianFilter2);

		
		menuFlashPlayer = new JMenu("Flash播放器");
		flashPlayer = new JMenuItem("Flash Player");
		menubar.add(menuFlashPlayer);
		menuFlashPlayer.add(flashPlayer);

		panel = new MyPanel();
		add(panel);
		if (panel.getCurrentImage() != null) {
			panel.setSize(panel.getCurrentImage().getWidth(), panel.getCurrentImage().getHeight());
		} else {
			panel.setSize(500, 500);
		}

		open.addActionListener(this);
		save.addActionListener(this);
		originalImage.addActionListener(this);
		undo.addActionListener(this);
		scale.addActionListener(this);
		rotate.addActionListener(this);
		grayTrans.addActionListener(this);
		binaryTrans.addActionListener(this);
		colorHistogram.addActionListener(this);
		colorHistEqual.addActionListener(this);
		negativeImage.addActionListener(this);
		logTrans.addActionListener(this);
		grayHistogram.addActionListener(this);
		grayHistEqual.addActionListener(this);
		boxFilter.addActionListener(this);
		weightedAveFilter.addActionListener(this);
		medianFilter.addActionListener(this);
		laplacianFilter.addActionListener(this);
		sobelFilter.addActionListener(this);
		gaussianNoise.addActionListener(this);
		saltPepperNoise.addActionListener(this);
		arithMeanFilter.addActionListener(this);
		geoMeanFilter.addActionListener(this);
		harmMeanFilter.addActionListener(this);
		contraharmMeanFilter.addActionListener(this);
		maxFilter.addActionListener(this);
		minFilter.addActionListener(this);
		medianFilter2.addActionListener(this);
		flashPlayer.addActionListener(this);

		setTitle("图像处理器");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocation(LOCATION_X, LOCATION_Y);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == open) {
			JFileChooser fd = new JFileChooser(".");
			fd.showOpenDialog(this);
			File file = fd.getSelectedFile();
			if (file == null) return;
			try {
				//read the image from file and store it in BufferedImage
				BufferedImage img = ImageIO.read(file);
				panel.setImage(img);
				panel.setSize(panel.getCurrentImage().getWidth(), panel.getCurrentImage().getHeight());
			} catch(Exception es) {
				es.printStackTrace();
			}
		} else if (e.getSource() == save) {
			saveFile(panel.getCurrentImage());
		} else if (e.getSource() == originalImage) {
			panel.setCurrentImage(panel.getImage());
			panel.setSize(panel.getImage().getWidth(), panel.getImage().getHeight());
		} else if (e.getSource() == undo) {
			panel.getLastImage();
			panel.setSize(panel.getCurrentImage().getWidth(), panel.getCurrentImage().getHeight());
		} else if (e.getSource() == scale) {
			JFrame setScaleFrame = new JFrame("设置缩放大小");
			Container c1 = setScaleFrame.getContentPane();
			c1.setLayout(new FlowLayout());
			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			c1.add(jl);
			c1.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			c1.add(jl2);
			c1.add(tf2);
			JButton jb = new JButton("缩放");
			c1.add(jb);

			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int width = Integer.parseInt(tf.getText());
					int height = Integer.parseInt(tf2.getText());
					//a new panel on the frame is created with the given size.
					scaleImage = ipro.scaleFunction(panel.getImage(), width, height);
					panel.setCurrentImage(scaleImage);
					panel.setSize(scaleImage.getWidth(), scaleImage.getHeight());
				}
			});
			setScaleFrame.setSize(400, 60);
			setScaleFrame.setLocation(400, 400);
			setScaleFrame.setVisible(true);

		} else if (e.getSource() == rotate) {
			JFrame setRotateFrame = new JFrame("设置旋转角度");
			Container cr = setRotateFrame.getContentPane();
			cr.setLayout(new FlowLayout());
			final JLabel jl = new JLabel("角度: ");
			final JTextField tf = new JTextField(3);
			cr.add(jl);
			cr.add(tf);
			JButton jb = new JButton("旋转");
			cr.add(jb);

			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int angle = Integer.parseInt(tf.getText());
					rotateImage = ipro.rotateFunction(panel.getCurrentImage(), angle);
					panel.setCurrentImage(rotateImage);
					panel.setSize(rotateImage.getWidth(), rotateImage.getHeight());
				}
			});
			setRotateFrame.setSize(300, 60);
			setRotateFrame.setLocation(400, 400);
			setRotateFrame.setVisible(true);
			
		} else if (e.getSource() == grayTrans) {
			panel.setCurrentImage(ipro.grayFunction(panel.getImage(), 255));
			final JFrame grayFrame = new JFrame("灰度变换");
			Container c2 = grayFrame.getContentPane();
			final JPanel qpanel = new JPanel();
			c2.add(qpanel);
			qpanel.setLayout(new FlowLayout());

			//label the current gray level. initialize it to 256.
			final JLabel jl = new JLabel("256");
			qpanel.add(jl);

			//a slider to change gray level from 2 to 256.
			final JSlider js = new JSlider(2, 256);
			qpanel.add(js);

			js.setMajorTickSpacing(50);
			js.setMinorTickSpacing(1);
			js.setPaintTicks(true);
			js.setPaintLabels(true);
			js.setPreferredSize(new Dimension(500, 40));
			js.setValue(256);

			js.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent es) {
					if (es.getSource() instanceof JSlider) {
						//receive the level value from slider
						int value = ((JSlider)es.getSource()).getValue();
						jl.setText(value + "");
						//a new panel on the frame is created with the given size.
						grayImage = ipro.grayFunction(panel.getImage(), value);
						panel.setCurrentImage(grayImage);
					}
				}
			});

			final JButton jb = new JButton("保存当前状态");
			qpanel.add(jb);
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel.setImage(panel.getCurrentImage());
				}
			});

			grayFrame.setSize(500, 150);
			grayFrame.setLocation(400, 400);
			grayFrame.setVisible(true);
		} else if (e.getSource() == binaryTrans) {
			binaryImage = ipro.binFunction(panel.getImage());
			panel.setCurrentImage(binaryImage);
		} else if (e.getSource() == grayHistogram) {
			JFrame histFrame = new JFrame("直方图");
			Container chist = histFrame.getContentPane();
			chist.setLayout(null);

			MyPanel hp = new MyPanel(ipro.plotGrayHistogram(panel.getCurrentImage()));
			chist.add(hp);
			hp.setSize(hp.getCurrentImage().getWidth(), hp.getCurrentImage().getHeight());

			histFrame.setSize(hp.getWidth(), hp.getHeight() + 20);
			histFrame.setLocation(500, 500);
			histFrame.setVisible(true);
		} else if (e.getSource() == grayHistEqual) {
			panel.setCurrentImage(ipro.equalizeGrayHistogram(panel.getCurrentImage()));
		} else if (e.getSource() == negativeImage) {
			panel.setCurrentImage(ipro.negativeImgFunction(panel.getCurrentImage()));
		} else if (e.getSource() == logTrans) {
			JFrame logTransFrame = new JFrame("对数变换：设置参数c");
			Container cltf = logTransFrame.getContentPane();
			cltf.setLayout(new FlowLayout());
			final JLabel jl = new JLabel("c: ");
			final JTextField tf = new JTextField(3);
			cltf.add(jl);
			cltf.add(tf);
			JButton jb = new JButton("确定");
			cltf.add(jb);

			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int c = Integer.parseInt(tf.getText());
					panel.setCurrentImage(ipro.logTransformation(panel.getCurrentImage(), c));
				}
			});
			logTransFrame.setSize(300, 60);
			logTransFrame.setLocation(400, 400);
			logTransFrame.setVisible(true);
		} else if (e.getSource() == colorHistogram) {
			JFrame histFrame = new JFrame("直方图");
			Container chist = histFrame.getContentPane();
			chist.setLayout(null);

			MyPanel hp = new MyPanel(ipro.plotHistogram(panel.getCurrentImage()));
			chist.add(hp);
			hp.setSize(hp.getCurrentImage().getWidth(), hp.getCurrentImage().getHeight());

			histFrame.setSize(hp.getWidth(), hp.getHeight() + 20);
			histFrame.setLocation(500, 500);
			histFrame.setVisible(true);
		} else if (e.getSource() == colorHistEqual) {
			panel.setCurrentImage(ipro.equalizeHistogram(panel.getCurrentImage()));
		} else if (e.getSource() == boxFilter){
			JFrame bf = new JFrame("盒状滤波");
			Container bc = bf.getContentPane();
			bc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			bc.add(jl);
			bc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			bc.add(jl2);
			bc.add(tf2);
			JButton jb = new JButton("确定");
			bc.add(jb);
			bc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.BOXFILTER, pwidth, pheight));
				}
			});
			bf.setSize(300, 60);
			bf.setLocation(400, 400);
			bf.setVisible(true);
		} else if (e.getSource() == weightedAveFilter){
			panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.WEIGHTEDAVERAGE, 3, 3));
		} else if (e.getSource() == laplacianFilter) {
			panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.LAPLACIAN, 3, 3));
		} else if (e.getSource() == sobelFilter) {
			panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.SOBEL, 3, 3));
		} else if (e.getSource() == medianFilter) {
			JFrame medianFrame = new JFrame("中值滤波");
			Container mc = medianFrame.getContentPane();
			mc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			mc.add(jl);
			mc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			mc.add(jl2);
			mc.add(tf2);
			JButton jb = new JButton("确定");
			mc.add(jb);
			mc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.MEDIAN, pwidth, pheight));
				}
			});
			medianFrame.setSize(300, 60);
			medianFrame.setLocation(400, 400);
			medianFrame.setVisible(true);
		} else if (e.getSource() == arithMeanFilter) {
			JFrame af = new JFrame("算术均值滤波");
			Container bc = af.getContentPane();
			bc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			bc.add(jl);
			bc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			bc.add(jl2);
			bc.add(tf2);
			JButton jb = new JButton("确定");
			bc.add(jb);
			bc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.ARITHMETIC, pwidth, pheight));
				}
			});
			af.setSize(300, 60);
			af.setLocation(400, 400);
			af.setVisible(true);
		} else if (e.getSource() == geoMeanFilter) {
			JFrame bf = new JFrame("几何均值滤波");
			Container bc = bf.getContentPane();
			bc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			bc.add(jl);
			bc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			bc.add(jl2);
			bc.add(tf2);
			JButton jb = new JButton("确定");
			bc.add(jb);
			bc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.GEOMETRIC, pwidth, pheight));
				}
			});
			bf.setSize(300, 60);
			bf.setLocation(400, 400);
			bf.setVisible(true);
		} else if (e.getSource() == harmMeanFilter) {
			JFrame bf = new JFrame("谐波均值滤波");
			Container bc = bf.getContentPane();
			bc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			bc.add(jl);
			bc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			bc.add(jl2);
			bc.add(tf2);
			JButton jb = new JButton("确定");
			bc.add(jb);
			bc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.HARMONIC, pwidth, pheight));
				}
			});
			bf.setSize(300, 60);
			bf.setLocation(400, 400);
			bf.setVisible(true);
		} else if (e.getSource() == contraharmMeanFilter) {
			JFrame bf = new JFrame("逆谐波均值滤波");
			Container bc = bf.getContentPane();
			bc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			bc.add(jl);
			bc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			bc.add(jl2);
			bc.add(tf2);
			JButton jb = new JButton("确定");
			bc.add(jb);
			bc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.CONTRAHARMONIC, pwidth, pheight));
				}
			});
			bf.setSize(300, 60);
			bf.setLocation(400, 400);
			bf.setVisible(true);
		} else if (e.getSource() == medianFilter2) {
			JFrame medianFrame = new JFrame("中值滤波");
			Container mc = medianFrame.getContentPane();
			mc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			mc.add(jl);
			mc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			mc.add(jl2);
			mc.add(tf2);
			JButton jb = new JButton("确定");
			mc.add(jb);
			mc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.MEDIAN, pwidth, pheight));
				}
			});
			medianFrame.setSize(300, 60);
			medianFrame.setLocation(400, 400);
			medianFrame.setVisible(true);
		} else if (e.getSource() == maxFilter) {
			JFrame maxFrame = new JFrame("最大值滤波");
			Container mc = maxFrame.getContentPane();
			mc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			mc.add(jl);
			mc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			mc.add(jl2);
			mc.add(tf2);
			JButton jb = new JButton("确定");
			mc.add(jb);
			mc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.MAX, pwidth, pheight));
				}
			});
			maxFrame.setSize(300, 60);
			maxFrame.setLocation(400, 400);
			maxFrame.setVisible(true);
		} else if (e.getSource() == minFilter) {
			JFrame minFrame = new JFrame("最小值滤波");
			Container mc = minFrame.getContentPane();
			mc.setLayout(new FlowLayout());

			final JLabel jl = new JLabel("宽: ");
			final JTextField tf = new JTextField(3);
			mc.add(jl);
			mc.add(tf);
			final JLabel jl2 = new JLabel("高: ");
			final JTextField tf2 = new JTextField(3);
			mc.add(jl2);
			mc.add(tf2);
			JButton jb = new JButton("确定");
			mc.add(jb);
			mc.setLayout(new FlowLayout());
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int pwidth = Integer.parseInt(tf.getText());
					int pheight = Integer.parseInt(tf2.getText());
					panel.setCurrentImage(ipro.filter2d(panel.getCurrentImage(), ImageProcess.MIN, pwidth, pheight));
				}
			});
			minFrame.setSize(300, 60);
			minFrame.setLocation(400, 400);
			minFrame.setVisible(true);
		} else if (e.getSource() == gaussianNoise) {
			JFrame jfh = new JFrame("高斯噪声");
			Container c = jfh.getContentPane();
			final JLabel label = new JLabel("均值: ");
			final JTextField jtf = new JTextField(3);
			final JLabel label2 = new JLabel("标准差: ");
			final JTextField jtf2 = new JTextField(3);
			c.add(label);
			c.add(jtf);
			c.add(label2);
			c.add(jtf2);

			JButton btn = new JButton("确定");
			c.add(btn);
			c.setLayout(new FlowLayout());
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					double mean = Double.parseDouble(jtf.getText());
					double std_variance = Double.parseDouble(jtf2.getText());
					panel.setCurrentImage(ipro.gaussian_noise(panel.getCurrentImage(), mean, std_variance));
				}
			});
			jfh.setSize(300, 100);
			jfh.setLocation(400, 400);
			jfh.setVisible(true);

		} else if (e.getSource() == saltPepperNoise) {
			JFrame jfh = new JFrame("添加椒盐噪声");
			Container c = jfh.getContentPane();
			final JLabel label = new JLabel("盐值: ");
			final JTextField jtf = new JTextField(3);
			final JLabel label2 = new JLabel("胡椒值: ");
			final JTextField jtf2 = new JTextField(3);
			c.add(label);
			c.add(jtf);
			c.add(label2);
			c.add(jtf2);

			JButton btn = new JButton("确定");
			c.add(btn);
			c.setLayout(new FlowLayout());
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					double salt = Double.parseDouble(jtf.getText());
					double pepper = Double.parseDouble(jtf2.getText());
					panel.setCurrentImage(ipro.salt_pepper_noise(panel.getCurrentImage(), salt, pepper));
				}
			});
			jfh.setSize(300, 60);
			jfh.setLocation(400, 400);
			jfh.setVisible(true);

		} else if (e.getSource() == flashPlayer) {
			MainFrame frame = new MainFrame();
		}
	}

	public void saveFile(BufferedImage img) {
		JFileChooser sfd = new JFileChooser(".");
		sfd.setDialogTitle("保存为...");
		sfd.showSaveDialog(null);
		sfd.setVisible(true);

		//get the path the user chooses to save in
		String filename = sfd.getSelectedFile().getAbsolutePath();
		try {
			ImageIO.write(img, "png", new File(filename));
		} catch (Exception es) {
			es.printStackTrace();
		}
	}
	
}