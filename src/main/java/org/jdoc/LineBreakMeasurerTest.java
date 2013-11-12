package org.jdoc;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

/**
 * https://bitbucket.org/fakraemer/line-break-measurer-test
 * 
 * Calibri, Calibri Bold, Calibri Bold Italic, Calibri Italic and Cambria Bold
 * 
 * not working
 * java version "1.6.0_45"
 * java version "1.7.0_21"
 * 
 * working
 * java version "1.6.0_43"
 * java version "1.7.0_25"
 * 
 */
public class LineBreakMeasurerTest {

	public static void main(String[] args) {
		Graphics2D canvas = (Graphics2D) new BufferedImage(960, 720,
				BufferedImage.TYPE_INT_RGB).getGraphics();
		AttributedString testString = new AttributedString("testing");
		GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] fonts = localGraphicsEnvironment.getAllFonts();
		for (Font font : fonts) {
			testString.addAttribute(TextAttribute.FONT, font);
			LineBreakMeasurer measurer = new LineBreakMeasurer(
					testString.getIterator(), canvas.getFontRenderContext());
			try {
				TextLayout layout = measurer.nextLayout(798.0f);
				System.out.println(String.format(
						"SUCCESS: Calculated %s with font %s", layout, font));
			} catch (RuntimeException e) {
				System.err
						.println(String
								.format("ERROR: Layout calculation escaped with [%s] for font %s",
										e.getClass().getName(), font));
			}
		}
	}
}