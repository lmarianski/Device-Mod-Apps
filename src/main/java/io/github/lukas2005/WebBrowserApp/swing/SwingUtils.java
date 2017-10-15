package io.github.lukas2005.WebBrowserApp.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.mrcrayfish.device.api.utils.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

public class SwingUtils {

	public static JFrame frame = new JFrame();
	public static JPanel panel = new JPanel();
	public static boolean initialized = false;
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Call this before any other method in this class or new Swingwrapper();
	 */
	public static void init() {
		frame = new JFrame();
		panel = new JPanel();
		
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(panel);
		
		initialized = true;
	}
	
	static int prev_width  = 0;
	static int prev_height = 0;
	static BufferedImage img;
	
	@Deprecated
	/**
	 * use new SwingWrapper() now
	 */
	public static void drawSwingComponent(int x, int y, int width, int height, Component c) {
		if (!initialized) throw new IllegalStateException("SwingUtils not initalized!");
		
		if (prev_width != width || prev_height != height) {
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			img.createGraphics();
			System.out.println("Creating new img");
			
		}
		Graphics g = img.getGraphics();
		
		prev_width = width;
		prev_height = height;

		if (!Arrays.asList(panel.getComponents()).contains(c)) {
			System.out.println("Added component:"+c.toString());
			panel.add(c, BorderLayout.CENTER);
			frame.setVisible(true);
		}	
		
		if (!frame.isVisible()) frame.setVisible(true);
		
		frame.setSize(width, height);
		
		c.paint(g);
		img.flush();
		
		frame.setSize(width, 0);
		
		//frame.setVisible(false);
		
		ResourceLocation rc = mc.getTextureManager().getDynamicTextureLocation(c.toString(), new DynamicTexture(img));
		
		mc.getRenderManager().renderEngine.bindTexture(rc);
		RenderUtil.drawRectWithTexture(x, y, 0, 0, width, height, width, height);
		mc.getTextureManager().deleteTexture(rc);
	}
	
	public static void click(Component target, int x, int y)
	{
	   MouseEvent press, release, click;
	   Point point;
	   long time;

	   point = new Point(x, y);

	   SwingUtilities.convertPointToScreen(point, target);

	   time    = System.currentTimeMillis();
	   press   = new MouseEvent(target, MouseEvent.MOUSE_PRESSED,  time, 0, x, y, point.x, point.y, 1, false, MouseEvent.BUTTON1);
	   release = new MouseEvent(target, MouseEvent.MOUSE_RELEASED, time, 0, x, y, point.x, point.y, 1, false, MouseEvent.BUTTON1);
	   click   = new MouseEvent(target, MouseEvent.MOUSE_CLICKED,  time, 0, x, y, point.x, point.y, 1, false, MouseEvent.BUTTON1);

	   target.dispatchEvent(press);
	   target.dispatchEvent(release);
	   target.dispatchEvent(click);
	}
	
	static public final float map(float value, float istart, float istop, float ostart, float ostop) {
	      return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}
	
}
