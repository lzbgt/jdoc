package org.jdoc.out.jgp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 * PDFBox pdf -> jpg
 * 
 */
public class PDFConvertor {

	@SuppressWarnings("unchecked")
	public static void pdf2png(String file, int resolution) throws IOException {
		PDDocument document = PDDocument.load(new File(file));
		List<PDPage> pages = document.getDocumentCatalog().getAllPages();
		for (int i = 0; i < pages.size(); i++) {
			PDPage page = pages.get(i);
			BufferedImage buffImage = page.convertToImage(
					BufferedImage.TYPE_INT_RGB, resolution * 72);
			ImageIO.write(buffImage, "png", new File(Config.outDir + "/"
					+ "/pdf/" + i + ".png"));
		}

		document.close();
	}
}
