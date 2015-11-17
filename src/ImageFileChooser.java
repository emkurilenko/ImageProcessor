import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


public class ImageFileChooser extends JFileChooser {

	public ImageFileChooser() {
		super();
		setAcceptAllFileFilterUsed(false);
		this.addChoosableFileFilter(new ImageFilter());
	}

	public ImageFileChooser(String path) {
		super(path);
		setAcceptAllFileFilterUsed(false);
		this.addChoosableFileFilter(new ImageFilter());
	}

	class ImageFilter extends FileFilter {

		private String extensions = ".swf";

		@Override
		public boolean accept(File file) {
			if (file.getName().toLowerCase().endsWith(extensions)) {
				return true;
			}
			return file.isDirectory();
		}

		@Override
		public String getDescription() {
			return "所有swf文件";
		}
	}

}