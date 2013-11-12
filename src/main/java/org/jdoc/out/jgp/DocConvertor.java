package org.jdoc.out.jgp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

/**
 * 
 *
 */
public class DocConvertor {

	public static void docx2jpg_xdocreport(String file) throws IOException {
		InputStream in = new FileInputStream(new File(file));
		XWPFDocument document = new XWPFDocument(in);

		// 2) Prepare Pdf options
		PdfOptions options = PdfOptions.create();

		// 3) Convert XWPFDocument to Pdf
		OutputStream out = new FileOutputStream(new File(file + ".pdf"));
		PdfConverter.getInstance().convert(document, out, options);
	}

	/**
	 * docx4j
	 * 
	 * @param file
	 * @throws IOException
	 * @throws Docx4JException
	 */
	public static void docx2jpg(String file) throws IOException,
			Docx4JException {
		WordprocessingMLPackage word = WordprocessingMLPackage.load(new File(
				file));

		org.docx4j.convert.out.pdf.PdfConversion c = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(
				word);
		OutputStream os = new FileOutputStream(file + ".pdf");
		c.output(os, new PdfSettings());

		// FOSettings foSettings = Docx4J.createFOSettings();
		// foSettings.setWmlPackage(word);
		// OutputStream os = new java.io.FileOutputStream(file + ".pdf");
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_NONE);
	}

}
