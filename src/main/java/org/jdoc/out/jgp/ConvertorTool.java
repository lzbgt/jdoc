package org.jdoc.out.jgp;

import java.io.File;

/**
 * 
 * docx4j/docxreport/jodconverter/unoconv
 *   docx -> jpg
 * 
 * POI
 *  slides -> jpg
 * 
 * PDFBox 
 *  pdf -> jpg
 * 
 * 
 *
 */
public class ConvertorTool {

	static void mkdir(String output) {
		System.out.println("mkdirs " + output);

		String[] files = { "ppt", "pptx", "pdf" };
		for (String file : files) {
			new File(output + "/" + file).mkdirs();
		}
	}

	public static void main(String[] args) throws Exception {
		String file = args[0];
		Config.outDir = args[1];
		mkdir(Config.outDir);

		if (args[0].endsWith(".ppt")) {
			int zoom = 1;
			if (args.length > 2)
				Integer.parseInt(args[2]);

			PPTConvertor.ppt2png(file, zoom);
		} else if (file.endsWith(".pptx")) {
			int zoom = 1;
			if (args.length > 2)
				Integer.parseInt(args[2]);

			PPTConvertor.pptx2png(file, zoom);
		} else if (file.endsWith(".pdf")) {
			int resolution = 2;
			if (args.length > 2)
				Integer.parseInt(args[2]);

			PDFConvertor.pdf2png(file, resolution);
		}

	}
}
