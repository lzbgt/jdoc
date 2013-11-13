package org.jdoc;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * use Unoconv to convert office doc to pdf, and use http://viewerjs.org/ to
 * display.
 * 
 * <pre>
 * 1) install unoconf
 *    > apt-get remove --purge unoconv 
 *    > git clone https://github.com/dagwieers/unoconv
 *    > sudo make install
 *    
 * 2) install libreoffice  
 *    > apt-get install libreoffice
 *    
 *    
 *    > killall soffice.bin
 *    
 * 3) font 
 *    http://www.cnblogs.com/zhj5chengfeng/p/3251009.html
 *    
 *    > C/Windows/Fonts
 *    > sudo cp ~/WinFonts/* /usr/share/fonts/winfonts
 *    > cd /usr/share/fonts/winfonts
 *    > sudo chmod 744 *
 *    > sudo mkfontscale
 *    > sudo mkfontdir
 *    > sudo fc-cache -f -v
 * 
 * </pre>
 * 
 */
public class UnoconvTool {
	static Logger logger = LoggerFactory.getLogger(UnoconvTool.class);

	static long time_out = 5 * 60 * 1000;

	public static UnoconvTool instance = new UnoconvTool(true);
	
	// at most blocking 1024 task
	ArrayBlockingQueue<Task> queue = new ArrayBlockingQueue<Task>(1024);
	
	boolean winOS = false;
	
	static class Task {
		File file;
		String format;
		String outputDir;

		public Task(File file, String format, String outputDir) {
			this.file = file;
			this.format = format;
			this.outputDir = outputDir;
		}
	}
	
	public UnoconvTool(boolean startDaemon) {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			winOS = true;
		}
		
		if (!winOS && startDaemon) {
			startDaemon();
		}
	}

	void startDaemon() {
		final String name = "Unoconv-Thread";
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Task task = queue.take();
						
						convert(task.file.getAbsolutePath(), task.format, task.outputDir);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}

				logger.info("{} is stopped ", name);
			}

		}, name);

		t.setDaemon(true);
		t.start();
		logger.info("{} is started ", name);
	}

	/**
	 * add to work queue.
	 * 
	 * @param file
	 * @param outputDir
	 */
	public void add(File file, String format, String outputDir) {
		// simple return
		if (winOS)
			return;
		
		boolean add = queue.offer(new Task(file, format, outputDir));

		if (!add) {
			logger.error("unoconv is full, something wrong");
		}
	}

	public static boolean convert(String file, String outputDir) {
		File f = new File(file);
		String name = f.getName();

		String format = "pdf";
		if (name.endsWith(".ppt") || name.endsWith(".pptx")) {
			format = "odp";
		} else if (name.endsWith(".doc") || name.endsWith(".docx")) {
			format = "odt";
		} else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
			format = "ods";
		}

		return convert(file, format, outputDir);
	}

	/**
	 * unoconv -f format -o output file
	 * 
	 */
	public static boolean convert(String file, String outputformat,
			String outputDir) {
		String cmd = String.format("unoconv -f %s -o %s %s", outputformat,
				outputDir, file);

		logger.info(cmd);
		boolean success = true;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			int retCode = p.exitValue();
			if (retCode != 0) {
				success = false;
				logger.error(cmd + " exit " + retCode);
			}
		} catch (Exception e) {
			success = false;
			logger.error(cmd, e);
		}

		return success;
	}

	public static void main(String[] args) {
		if (args.length == 2)
			UnoconvTool.convert(args[0], args[1]);
		else
			UnoconvTool.convert(args[0], args[1], args[2]);
	}

}
