import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import javax.swing.event.*;
import java.lang.*;
import java.util.*;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class ImageProcess {
	public static final int HPANELWIDTH = 280;
	public static final int HPANELHEIGHT = 250;

	public static final int BOXFILTER = 0;
	public static final int WEIGHTEDAVERAGE = 1;

	public static final int LAPLACIAN = 2;
	public static final int SOBEL = 3;

	public static final int ARITHMETIC = 4;//box filter = arithmetic filter
	public static final int GEOMETRIC = 5;
	public static final int HARMONIC = 6;
	public static final int CONTRAHARMONIC = 7;
	public static final	int MEDIAN = 8;
	public static final	int MAX = 9;
	public static final	int MIN = 10;

	public ImageProcess() {

	}
	public BufferedImage scaleFunction(BufferedImage image, int width, int height) {
		//iwidth and iheight represent the size of original image.
		int iwidth = image.getWidth(null);
		int iheight = image.getHeight(null);
		//rateW and rateH represent the ratio of original size and the zooming size.
		double rateW = (double)iwidth / width;
		double rateH = (double)iheight / height;

		BufferedImage tmp = new BufferedImage(width, height, image.getType());
		//using bilinear algorithm to calculate each point of object image from the 4 points of the original image.
		for (int i = 0; i < width; i++) {
			int w = (int)(rateW * i);
			int w2 = (w + 1) < (iwidth - 1) ? (w + 1) : (iwidth - 1);//avoid exceeding the range
			float u = (float)(rateW * i - w);//difference between the exact value and integer value close to it
			for (int j = 0; j < height; j++) {
				int h = (int) (rateH * j);
				int h2 = (h + 1) < (iheight - 1) ? (h + 1) : (iheight - 1);
				float v = (float)(rateH * j - h);

				//calculate the gray value of the target size image using bilinear algorithm.
				int b = (int)((1 - u) * (1 - v) * (image.getRGB(w, h) & 0xff) + (1 - u) * v * (image.getRGB(w, h2) & 0xff)
							+ u * (1 - v) * (image.getRGB(w2, h) & 0xff) + u * v * (image.getRGB(w2, h2) & 0xff));

				int g = (int)((1 - u) * (1 - v) * ((image.getRGB(w, h) & 0xff00) >> 8) + (1 - u) * v * ((image.getRGB(w, h2) & 0xff00) >> 8)
							+ u * (1 - v) * ((image.getRGB(w2, h) & 0xff00) >> 8) + u * v * ((image.getRGB(w2, h2) & 0xff00) >> 8));

				int r = (int)((1 - u) * (1 - v) * ((image.getRGB(w, h) & 0xff0000) >> 16) + (1 - u) * v * ((image.getRGB(w, h2) & 0xff0000) >> 16)
							+ u * (1 - v) * ((image.getRGB(w2, h) & 0xff0000) >> 16) + u * v * ((image.getRGB(w2, h2) & 0xff0000) >> 16));

				int rgb = ((r * 256) + g) * 256 + b;
				tmp.setRGB(i, j, rgb);
			}
		}
		return tmp;
	}
	public BufferedImage rotateFunction(BufferedImage image, int degree) {
		int width = image.getWidth();
		int height = image.getHeight();
		int rotateW = 0;
		int rotateH = 0;
		degree = (degree % 360) < 0 ? (degree % 360 + 360) : degree % 360;
		double angle = Math.toRadians(degree);

		switch(degree) {
			case 0:
			case 180:
			case 360: 
				rotateW = width;
				rotateH = height;
				break;
			case 90:
			case 270:
				rotateW = height;
				rotateH = width;
				break;
			default:
				double cosV = Math.abs(Math.cos(angle));
				double sinV = Math.abs(Math.sin(angle));
				rotateW = (int)(cosV * width) + (int)(sinV * height);
				rotateH = (int)(sinV * width) + (int)(cosV * height);
				break;
		}
 
		int originx = rotateW / 2 - width / 2;
		int originy = rotateH / 2 - height / 2;
		BufferedImage rotatedImg =  new BufferedImage(rotateW, rotateH, image.getType());
		Graphics2D g = (Graphics2D)rotatedImg.getGraphics();
		Color alpha = new Color(1.0F, 1.0F, 1.0F, 0.93F);
		g.setColor(alpha);
		g.fillRect(0, 0, rotateW, rotateH); //background of the image

		AffineTransform atf = new AffineTransform();
		atf.rotate(angle, rotateW / 2, rotateH / 2);
		atf.translate(originx, originy);
		AffineTransformOp op = new AffineTransformOp(atf, AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImg);

		return rotatedImg;
	}
	public BufferedImage grayFunction(BufferedImage image, int grayLevel) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				int gray = (rgb & 0xff);
				//convert the gray level to the given value.
				gray = grayConversion(gray, grayLevel);
				int newRGB = ((gray * 256) + gray) * 256 + gray;
				tmp.setRGB(i, j, newRGB);
			}
		}
		return tmp;
	}
	public int grayConversion(int gray, int level) {
		int pixels[] = new int[300];
		pixels[0] = 0;
		int count = 1;
		int border = 255 / (level - 1);
		int tmpBorder = border;
		while (tmpBorder < 256) {
			pixels[count] = tmpBorder;
			tmpBorder = border * (count + 1);
			count++;
		}
		int dif = pixels[1] / 2;
		for (int i = 0; i < count; i++) {
			if (gray - pixels[i] <= dif) {
				return pixels[i];
			} else if (gray - pixels[i] <= pixels[1]) {
				return pixels[i + 1];
			}
		}
		return gray;
	}
	public BufferedImage binFunction(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				int gray = (rgb & 0xff);
				gray = gray > 127 ? 255 : 0;
				int newRGB = ((gray * 256) + gray) * 256 + gray;
				tmp.setRGB(i, j, newRGB);
			}
		}
		return tmp;
	}
	public BufferedImage negativeImgFunction(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage tmp = new BufferedImage(width, height, image.getType());
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int gray = 255 - (image.getRGB(i, j) & 0xff);
					tmp.setRGB(i, j, ((gray * 256) + gray) * 256 + gray);
				}
			}
		} else if (image.getType() == BufferedImage.TYPE_3BYTE_BGR) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int r = 255 - ((image.getRGB(i, j) & 0xff0000) >> 16);
					int g = 255 - ((image.getRGB(i, j) & 0xff00) >> 8);
					int b = 255 - (image.getRGB(i, j) & 0xff);
					tmp.setRGB(i, j, ((r * 256) + g) * 256 + b);
				}
			}
		}
		return tmp;
	}
	public BufferedImage logTransformation(BufferedImage image, int c) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage tmp = new BufferedImage(width, height, image.getType());
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int gray = image.getRGB(i, j) & 0xff;
					gray = (int)(c * Math.log(gray + 1));
					tmp.setRGB(i, j, ((gray * 256) + gray) * 256 + gray);
				}
			}
		} else if (image.getType() == BufferedImage.TYPE_3BYTE_BGR) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int r = 255 - ((image.getRGB(i, j) & 0xff0000) >> 16);
					int g = 255 - ((image.getRGB(i, j) & 0xff00) >> 8);
					int b = 255 - (image.getRGB(i, j) & 0xff);
					tmp.setRGB(i, j, ((r * 256) + g) * 256 + b);
				}
			}
		}
		return tmp;
	}
	public BufferedImage plotGrayHistogram(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();

		int hist[] = calculateGrayFrequency(img);

		BufferedImage tmp = new BufferedImage(HPANELWIDTH, HPANELHEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = tmp.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, HPANELWIDTH, HPANELHEIGHT);

		g.setColor(Color.BLACK);

		g.drawLine(10, 0, 10, HPANELHEIGHT - 10);//y
		g.drawLine(10, HPANELHEIGHT - 10, HPANELWIDTH, HPANELHEIGHT - 10);//x

		float rate = calculateScale(hist);
		for (int i = 0; i < hist.length; i++) {
			g.setColor(Color.BLACK);
			int x = i;
			int y = (int) (hist[i] * rate);

			g.drawLine(10 + x, HPANELHEIGHT - 10, 10 + x, HPANELHEIGHT - 10 -y);
		}

		return tmp;
	}
	public BufferedImage plotHistogram(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();

		int hist[][] = calculateFrequency(img);

		BufferedImage tmp = new BufferedImage(HPANELWIDTH * 3, HPANELHEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = tmp.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, HPANELWIDTH * 3, HPANELHEIGHT);


		//draw the R channel histogram
		g.setColor(Color.RED);
		g.drawLine(10, 0, 10, HPANELHEIGHT - 10);//y
		g.drawLine(10, HPANELHEIGHT - 10, HPANELWIDTH, HPANELHEIGHT - 10);//x
		float rate = calculateScale(hist[0]);
		for (int i = 0; i < hist[0].length; i++) {
			// g.setColor(Color.BLACK);
			int x = i;
			int y = (int) (hist[0][i] * rate);

			g.drawLine(10 + x, HPANELHEIGHT - 10, 10 + x, HPANELHEIGHT - 10 -y);
		}

		//draw the G channel histogram
		g.setColor(Color.GREEN);
		g.drawLine(10 + HPANELWIDTH, 0, 10 + HPANELWIDTH, HPANELHEIGHT - 10);//y
		g.drawLine(10 + HPANELWIDTH, HPANELHEIGHT - 10, 10 + HPANELWIDTH * 2, HPANELHEIGHT - 10);//x
		rate = calculateScale(hist[1]);
		for (int i = 0; i < hist[1].length; i++) {
			// g.setColor(Color.BLACK);
			int x = i;
			int y = (int) (hist[1][i] * rate);

			g.drawLine(10 + HPANELWIDTH + x, HPANELHEIGHT - 10, 10 + HPANELWIDTH + x, HPANELHEIGHT - 10 -y);
		}

		//draw the B channel histogram
		g.setColor(Color.BLUE);
		g.drawLine(10 + HPANELWIDTH * 2, 0, 10 + HPANELWIDTH * 2, HPANELHEIGHT - 10);//y
		g.drawLine(10 + HPANELWIDTH * 2, HPANELHEIGHT - 10, 10 + HPANELWIDTH * 3, HPANELHEIGHT - 10);//x
		rate = calculateScale(hist[2]);
		for (int i = 0; i < hist[2].length; i++) {
			// g.setColor(Color.BLACK);
			int x = i;
			int y = (int) (hist[2][i] * rate);

			g.drawLine(10 + HPANELWIDTH * 2 + x, HPANELHEIGHT - 10, 10 + HPANELWIDTH * 2 + x, HPANELHEIGHT - 10 -y);
		}
		return tmp;
	}
	public float calculateScale(int hist[]) {
		int max = -1;
		for (int i = 0; i < hist.length; i++) {
			if (max < hist[i]) {
				max = hist[i];
			}
		}
		if (max > HPANELHEIGHT - 10) {
			return ((int)((HPANELHEIGHT - 10) * 1.0f / max * 1000)) * 1.0f / 1500;
		}
		return 1.0f;
	}
	public BufferedImage equalizeGrayHistogram(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();

		int hist[] = calculateGrayFrequency(img);

		double cul[] = new double[256];
		cul[0] = (double)(hist[0]) / (width * height);
		for (int i = 1; i < 256; i++) {
			cul[i] = cul[i - 1] + (double)(hist[i]) / (width * height);
		}

		BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = img.getRGB(i, j);
				int gray = (rgb & 0xff);
				int newGray = (int)(cul[gray] * 255);

				int newRGB = ((newGray * 256) + newGray) * 256 + newGray;
				tmp.setRGB(i, j, newRGB);
			}
		}
		return tmp;
	}
	public BufferedImage equalizeHistogram(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();

		int hist[][] = calculateFrequency(img);
		int aveHist[] = average_hist(hist);

		double cul[] = new double[256];
		cul[0] = (double)(aveHist[0]) / (width * height);
		for (int i = 1; i < 256; i++) {
			cul[i] = cul[i - 1] + (double)(aveHist[i]) / (width * height);
		}

		BufferedImage tmp = new BufferedImage(width, height, img.getType());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = img.getRGB(i, j);
				int r = (rgb & 0xff0000) >> 16;
				int g = (rgb & 0xff00) >> 8;
				int b = rgb & 0xff;
				
				r = (int)(cul[r] * 255);
				g = (int)(cul[g] * 255);
				b = (int)(cul[b] * 255);

				int newRGB = ((r * 256) + g) * 256 + b;
				tmp.setRGB(i, j, newRGB);
			}
		}
		return tmp;
	}
	public int[] average_hist(int hists[][]) {
		int avHist[] = new int[256];
		for (int i = 0; i < 256; i++) {
			avHist[i] = (hists[0][i] + hists[1][i] + hists[2][i]) / 3;
		}
		return avHist;
	}
	public int[] calculateGrayFrequency(BufferedImage img) {
		int hist[] = new int[256];
		for (int i = 0; i < 256; i++) {
			hist[i] = 0;
		}
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int rgb = img.getRGB(i, j);
				int gray = (rgb & 0xff);
				hist[gray]++;
			}
		}
		return hist;
	}
	public int[][] calculateFrequency(BufferedImage img) {
		int hist[][] = new int[3][256];//hist[0] for R channel, hist[1] G, hist[2] B.
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int rgb = img.getRGB(i, j);
				int r = (rgb & 0xff0000) >> 16;
				int g = (rgb & 0xff00) >> 8;
				int b = rgb & 0xff;
				hist[0][r]++;
				hist[1][g]++;
				hist[2][b]++;
			}
		}
		return hist;
	}

	//get patches of the image with given width and height of the mask
	public int[][][] view_as_window(BufferedImage img, int pwidth, int pheight) {
		int width = img.getWidth();
		int height = img.getHeight();

		int pcount = (width - pwidth + 1) * (height - pheight + 1);
		int patches[][][] = new int[pcount][pheight][pwidth];
		int count = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (j + pwidth > width || i + pheight > height) break;
				for (int m = 0; m < pheight; m++) {
					for (int n = 0; n < pwidth; n++) {
						patches[count][m][n] = img.getRGB(j + n, i + m) & 0xff;
					}
				}
				count++;				
			}
		}
		return patches;
	}

	//filter the image with a specific filter
	public BufferedImage filter2d(BufferedImage img, int filter, int mask_width, int mask_height) {
		int pwidth = mask_width;
		int pheight = mask_height;
		
		int width = img.getWidth();
		int height = img.getHeight();

		BufferedImage padImg = new BufferedImage(width + pwidth - 1, height + pheight - 1, BufferedImage.TYPE_INT_RGB);
		//padding. preparation for the next step.
		int padding[][] = new int[height + pheight - 1][width + pwidth - 1];
		for (int i = 0; i < height + pheight - 1; i++) {
			for (int j = 0; j < width + pwidth - 1; j++) {
				if (j < (pwidth - 1) / 2 || j > width - 1 + (pwidth - 1) / 2
						|| i < (pheight - 1) / 2 || i > height - 1 + (pheight - 1) / 2) {
					padding[i][j] = 0;
				} else {
					int padGray = img.getRGB(j - (pwidth - 1) / 2, i - (pheight - 1) / 2) & 0xff;
					padding[i][j] = ((padGray * 256) + padGray) * 256 + padGray;
				}
				padImg.setRGB(j, i, padding[i][j]);
			}
		}

		//get the patches with the padding image.
		int patches[][][] = view_as_window(padImg, pwidth, pheight);

		//set the result image with a specific filter.
		int patchIndex = 0;
		int gray = 0;
		BufferedImage filterImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int imageArr[][] = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (filter == BOXFILTER) {
					gray = arithmetic_mean_filter(patches[patchIndex++]);//boxfilter is the same as arithmetic mean filter
				} else if (filter == WEIGHTEDAVERAGE) {
					gray = weighted_average_filter(patches[patchIndex++]);
				} else if (filter == LAPLACIAN) {
					gray = laplacian_filter(patches[patchIndex++]);
				} else if (filter == SOBEL) {
					gray = sobel_filter(patches[patchIndex++]);
				} else if (filter == ARITHMETIC) {
					gray = arithmetic_mean_filter(patches[patchIndex++]);
				} else if (filter == GEOMETRIC) {
					gray = geometric_mean_filter(patches[patchIndex++]);
				} else if (filter == HARMONIC) {
					gray = harmonic_mean_filter(patches[patchIndex++]);
				} else if (filter == CONTRAHARMONIC) {
					gray = contraharmonic_mean_filter(patches[patchIndex++], 1.5);
				} else if (filter == MEDIAN) {
					gray = median_filter(patches[patchIndex++]);
				} else if (filter == MAX) {
					gray = max_filter(patches[patchIndex++]);
				} else if (filter == MIN) {
					gray = min_filter(patches[patchIndex++]);
				}
				// if (gray > 255) gray = 255;
				// if (gray < 0) gray = 0;
				// int rgb = ((gray * 256) + gray) * 256 + gray;
				// filterImg.setRGB(j, i, rgb);
				imageArr[i][j] = gray;
			}
		}
		imageArr = grayScale(imageArr);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				gray = imageArr[i][j];
				filterImg.setRGB(j, i, ((gray * 256) + gray) * 256 + gray);
			}
		}	

		// Graphics g = filterImg.getGraphics();
		// g.drawImage(filterImg, 0, 0, width, height, null);

		return filterImg;
	}
	public int[][] grayScale(int array[][]) {
		int tmp[][] = array;
		int minValue = min_filter(tmp);
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				tmp[i][j] -= minValue;
			}
		}

		int maxValue = max_filter(tmp);
		double temp;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				temp = (double)tmp[i][j] / maxValue;
				tmp[i][j] = (int)(temp * 255);
			}
		}
		return tmp;
	}

	public int weighted_average_filter(int array[][]) {//only suitable for 3x3 mask
		int sum = 0;

		// array[0][0] *= 1;
		// array[0][2] *= 1;
		// array[2][0] *= 1;
		// array[2][2] *= 1;

		array[0][1] *= 2;
		array[1][0] *= 2;
		array[1][2] *= 2;
		array[2][1] *= 2;

		array[1][1] *= 4;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				sum += array[i][j];
			}
		}

		return sum / 16;
	}

	public int arithmetic_mean_filter(int array[][]) {
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				sum += array[i][j];
			}
		}
		return sum / (array.length * array[0].length);

	}

	public int geometric_mean_filter(int array[][]) {
		double product = 1.0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				product *= Math.pow(array[i][j], 1.0 / (array.length * array[0].length)) ;
			}
		}
		return (int)product;
	}

	public int harmonic_mean_filter(int array[][]) {
		double result = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] != 0) {
					result += 1.0 / array[i][j];
				} else {
					result += 255; //when the denominator is 0, result is added by 255.
				}
			}
		}
		return result == 0 ? 255 : (int)((array.length * array[0].length) / result);
	}

	public int contraharmonic_mean_filter(int array[][], double q) {
		double g1 = 0;
		double g2 = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				g1 += Math.pow(array[i][j], q + 1);
				g2 += Math.pow(array[i][j], q);
			}
		}
		return g2 == 0 ? 255 : (int)(g1 / g2);
	}

	public int median_filter(int array[][]) {
		int size = array.length * array[0].length;
		int arr[] = new int[size];
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				arr[index++] = array[i][j];
			}
		}
		Arrays.sort(arr);
		int median = 0;
		if (size % 2 != 0) {
			median = arr[(size - 1) / 2];
		} else {
			median = (arr[size / 2] + arr[size / 2 - 1]) / 2;
		}
		return median;
	}

	public int max_filter(int array[][]) {
		int max = array[0][0];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (max < array[i][j]) {
					max = array[i][j];
				}
			}
		}
		return max;
	}

	public int min_filter(int array[][]) {
		int min = array[0][0];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (min > array[i][j]) {
					min = array[i][j];
				}
			}
		}
		return min;
	}

	public int laplacian_filter(int array[][]) {
		int sum = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i - j == 2 || j - i == 2) {//four corners
					array[i][j] *= -1;
				} else if (i - j == 1 || j - i == 1) {//middle
					array[i][j] *= 0;
				} else { //center
					array[i][j] *= 4;
				}
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				sum += array[i][j];
			}
		}
		return sum;
	}

	public int sobel_filter(int array[][]) {
		int sum = 0;

		int sobeltype = 1;// type 1
		// int sobeltype = 2;// type 2

		if (sobeltype == 1) {
			array[0][0] *= -1;
			array[0][1] *= -2;
			array[0][2] *= -1;
			array[1][0] *= 0;
			array[1][1] *= 0;
			array[1][2] *= 0;
			array[2][0] *= 1;
			array[2][1] *= 2;
			array[2][2] *= 1;
		} else if (sobeltype == 2) {
			array[0][0] *= -1;
			array[0][1] *= 0;
			array[0][2] *= 1;
			array[1][0] *= -2;
			array[1][1] *= 0;
			array[1][2] *= 2;
			array[2][0] *= -1;
			array[2][1] *= 0;
			array[2][2] *= 1;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				sum += array[i][j];
			}
		}
		return sum;
	}
	
	public BufferedImage gaussian_noise(BufferedImage img, double mean, double std_variance) {
		BufferedImage noise_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

		Random random = new Random();
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int gray = (int)(random.nextGaussian() * std_variance + mean);
				gray += img.getRGB(i, j) & 0xff;
				if (gray < 0) gray = 0;
				if (gray > 255) gray = 255;
				int rgb = ((gray * 256) + gray) * 256 + gray;

				noise_img.setRGB(i, j, rgb);
			}
		}
		return noise_img;
	}

	public BufferedImage salt_pepper_noise(BufferedImage img, double salt, double pepper) {
		BufferedImage noise_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				if (Math.random() < pepper) {
					noise_img.setRGB(i, j, 0);
					continue;
				}
				if (Math.random() < salt) {
					int gray = 255;
					int rgb = ((gray * 256) + gray) * 256 + gray;
					noise_img.setRGB(i, j, rgb);
					continue;
				}
				noise_img.setRGB(i, j, img.getRGB(i, j));
			}
		}
		return noise_img;
	}
	
}