import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class ImagePlayer extends JLabel implements Runnable{
	
	private List<String> imageList = new ArrayList<String>();
	private ImageIcon currentImage;
	private int interval = 50;
	
	private boolean isWork = true;
	private boolean isStop = false;

	private boolean isChange = false;

	public ImagePlayer() {

	}
	
	public ImagePlayer(List<String> imageList) {
		this.imageList = imageList;
	}
	
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	@Override
	public void run() {
		while (isWork) {
			while (isChange) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (isStop || imageList.isEmpty()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			for (int i = 0; i < imageList.size(); i++) {
				if (isChange) break;//if the flash is changed, break the cycle
				//中途暂停
				 if (isStop) {
					while (isStop){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				currentImage = new ImageIcon(imageList.get(i));
				this.setIcon(currentImage);

				//时间间隔
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void play() {
		isStop = false;
	}
	
	public void pause() {
		isStop = true;
	}
	
	public void speedUp() {
		if (interval > 20) interval -= 20;
	}
	
	public void slowDown() {
		if (interval < 300) interval += 20;
	}

	public void changeFlash() {
		isChange = true;
	}
	public void finishChange() {
		isChange = false;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getInterval() {
		return interval;
	}

}
