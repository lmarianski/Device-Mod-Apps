package io.github.lukas2005.DeviceModApps.swing;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyEventType;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiers;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiersBuilder;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.BrowserMouseEventBuilder;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseScrollType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * @author lukas2005
 */
public class SwingUtils {

	public static void click(Component target, int x, int y, int mouseButton) {
		MouseEvent press, release, click;
		Point point;
		long time;

		point = new Point(x, y);

		Graphics g = target.getGraphics();
		g.setColor(mouseButton == 1 ? Color.RED : Color.BLUE);
		g.drawRoundRect(x, y, 5, 5, 5, 5);

		SwingUtilities.convertPointToScreen(point, target);

		time = System.currentTimeMillis();
		press = new MouseEvent(target, MouseEvent.MOUSE_PRESSED, time, 0, x, y, point.x, point.y, 1, false, mouseButton);
		release = new MouseEvent(target, MouseEvent.MOUSE_RELEASED, time, 0, x, y, point.x, point.y, 1, false, mouseButton);
		click = new MouseEvent(target, MouseEvent.MOUSE_CLICKED, time, 0, x, y, point.x, point.y, 1, false, mouseButton);

		target.dispatchEvent(press);
		target.dispatchEvent(release);
		target.dispatchEvent(click);
	}

	static public float map(float value, float istart, float istop, float ostart, float ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}

	/**
	 * Compares two images pixel by pixel.
	 *
	 * @param imgA the first image.
	 * @param imgB the second image.
	 * @return whether the images are both the same or not.
	 */
	public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
		// The images must be the same size.
		if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
			int width = imgA.getWidth();
			int height = imgA.getHeight();

			// Loop over every pixel.
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					// Compare the pixels for equality.
					if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
						return false;
					}
				}
			}
		} else {
			return false;
		}

		return true;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}


	public static void forwardMousePressEvent(Browser browser, MouseButtonType buttonType, int x, int y, int globalX, int globalY) {
		BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
		builder.setEventType(MouseEventType.MOUSE_PRESSED)
				.setButtonType(buttonType)
				.setX(x)
				.setY(y)
				.setGlobalX(globalX)
				.setGlobalY(globalY)
				.setClickCount(1)
				.setModifiers(new KeyModifiersBuilder().mouseButton().build());
		browser.forwardMouseEvent(builder.build());
	}

	public static void forwardMouseReleaseEvent(Browser browser, MouseButtonType buttonType, int x, int y, int globalX, int globalY) {
		BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
		builder.setEventType(MouseEventType.MOUSE_RELEASED)
				.setButtonType(buttonType)
				.setX(x)
				.setY(y)
				.setGlobalX(globalX)
				.setGlobalY(globalY)
				.setClickCount(1)
				.setModifiers(KeyModifiers.NO_MODIFIERS);
		browser.forwardMouseEvent(builder.build());
	}

	public static void forwardMouseMoveEvent(Browser browser, int x, int y, int globalX, int globalY) {
		BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
		builder.setEventType(MouseEventType.MOUSE_MOVED)
				.setX(x)
				.setY(y)
				.setGlobalX(globalX)
				.setGlobalY(globalY);
		browser.forwardMouseEvent(builder.build());
	}

	public static void forwardMouseClickEvent(Browser browser, MouseButtonType buttonType, int x, int y, int globalX, int globalY) {
		forwardMousePressEvent(browser, buttonType, x, y, globalX, globalY);
		forwardMouseReleaseEvent(browser, buttonType, x, y, globalX, globalY);
	}

	public static void forwardMouseDragEvent(Browser browser, MouseButtonType buttonType, int x, int y, int globalX, int globalY) {
		BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
		builder.setEventType(MouseEventType.MOUSE_DRAGGED)
				.setButtonType(buttonType)
				.setX(x)
				.setY(y)
				.setGlobalX(globalX)
				.setGlobalY(globalY)
				.setClickCount(1)
				.setModifiers(KeyModifiers.NO_MODIFIERS);
		browser.forwardMouseEvent(builder.build());
	}

	public static void forwardMouseScrollEvent(Browser browser, int unitsToScroll, int x, int y) {
		BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
		builder.setEventType(MouseEventType.MOUSE_WHEEL)
				.setX(x)
				.setY(y)
				.setGlobalX(0)
				.setGlobalY(0)
				.setScrollBarPixelsPerLine(25)
				.setScrollType(MouseScrollType.WHEEL_UNIT_SCROLL)
				.setUnitsToScroll(unitsToScroll);
		browser.forwardMouseEvent(builder.build());
	}

	public static void forwardKeyTypedEvent(Browser browser, char key) {
		BrowserKeyEvent event = new BrowserKeyEvent(KeyEventType.TYPED, KeyCode.VK_A, key);
		browser.forwardKeyEvent(event);
	}

	public static void forwardKeyTypedEvent(Browser browser, KeyCode key) {
		BrowserKeyEvent event = new BrowserKeyEvent(KeyEventType.TYPED, key);
		browser.forwardKeyEvent(event);
	}

}
