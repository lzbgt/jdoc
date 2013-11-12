package org.jdoc;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * use Unoconv to convert office doc to pdf, 
 * and use http://viewerjs.org/ to display.
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
 *    > apt-get install libreoffice-impress
 *    > apt-get install libreoffice-writer
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

	public UnoconvTool() {

	}

	/**
	 * add to work queue.
	 * 
	 * @param file
	 * @param outputDir
	 */
	public void add(String file, String outputDir) {

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

		logger.info("convert  {} {} {}  ", file, outputformat, outputDir);
		boolean success = true;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			int retCode = p.exitValue();
			if (retCode != 0) {
				success = false;
				logger.info("convert return {} {} {} - {} ", file,
						outputformat, outputDir, retCode);
			}
		} catch (Exception e) {
			success = false;
			logger.info("convert return {} {} {} - {} ", file, outputformat,
					outputDir, e);
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
