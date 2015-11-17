import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import javax.swing.event.*;
import java.lang.*;
import java.util.*;

//extend JPanel to store image, get image and repaint image on the screen
class MyPanel extends JPanel {
	private BufferedImage image;
	private BufferedImage currentImage;
	private java.util.List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	private int listCount = -1;

	public MyPanel() {
		try {
			image = null;
			currentImage = image;
			imageList.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public MyPanel(File file) {
		try {
			image = ImageIO.read(file);
			currentImage = image;
			imageList.clear();
			imageList.add(image);
			listCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
	}
	public MyPanel(BufferedImage img) {
		image = img;
		currentImage = img;
		imageList.clear();
		imageList.add(image);
		listCount++;
		repaint();
	}
	public void paint(Graphics g) {
		super.paint(g);
		if (currentImage == null) return;
		g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), null);
	}
	public void setImage(BufferedImage img) {
		image = img;
		currentImage = img;
		imageList.add(image);
		listCount = imageList.size() - 1;
		repaint();
	}
	public void setCurrentImage(BufferedImage img) {
		currentImage = img;
		imageList.add(currentImage);
		listCount = imageList.size() - 1;//reset the count of the image list
		repaint();
	}

	public BufferedImage getImage() {
		return image;
	}
	public BufferedImage getCurrentImage() {
		return currentImage;
	}
	public void getLastImage() {
		if (listCount > 0) {
			imageList.remove(listCount);
			currentImage = imageList.get(--listCount);
		} else {
			currentImage = imageList.get(0);
		}	
		repaint();
	}

}