package org.jdoc.out.jgp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.xmlbeans.XmlException;

/**
 * POI slides -> jpg
 * 
 */
public class PPTConvertor {

	/**
	 * PPTX2PNG.java
	 * 
	 * @param file
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws XmlException
	 */
	public static void pptx2png(String file, int zoom) throws IOException,
			OpenXML4JException, XmlException {
		FileInputStream is = new FileInputStream(file);
		XMLSlideShow ppt = new XMLSlideShow(is);
		is.close();

		AffineTransform at = new AffineTransform();
		at.setToScale(zoom, zoom);

		Dimension pgsize = ppt.getPageSize();

		XSLFSlide[] slide = ppt.getSlides();
		for (int i = 0; i < slide.length; i++) {
			System.out.println("TO slides " + i + " " + pgsize);

			BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width
					* zoom), (int) Math.ceil(pgsize.height * zoom),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = img.createGraphics();
			graphics.setTransform(at);

			graphics.setPaint(Color.white);
			graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,
					pgsize.height));
			slide[i].draw(graphics);
			FileOutputStream out = new FileOutputStream(Config.outDir
					+ "/pptx/" + (i + 1) + ".png");
			javax.imageio.ImageIO.write(img, "png", out);
			out.close();
		}
	}

	public static void ppt2png(String file, int zoom) throws IOException {
		FileInputStream is = new FileInputStream(file);
		SlideShow ppt = new SlideShow(is);
		is.close();

		AffineTransform at = new AffineTransform();
		at.setToScale(zoom, zoom);

		Dimension pgsize = ppt.getPageSize();

		Slide[] slide = ppt.getSlides();
		for (int i = 0; i < slide.length; i++) {
			System.out.println("TO slides " + i);

			BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width
					* zoom), (int) Math.ceil(pgsize.height * zoom),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = img.createGraphics();
			graphics.setTransform(at);

			graphics.setPaint(Color.white);
			graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,
					pgsize.height));
			slide[i].draw(graphics);
			FileOutputStream out = new FileOutputStream(Config.outDir + "/ppt/"
					+ (i + 1) + ".png");
			javax.imageio.ImageIO.write(img, "png", out);
			out.close();
		}
	}

	public static void main(String[] args) throws IOException,
			OpenXML4JException, XmlException {
		PPTConvertor.pptx2png("content/zhipeng.pptx", 1);
	}
}
