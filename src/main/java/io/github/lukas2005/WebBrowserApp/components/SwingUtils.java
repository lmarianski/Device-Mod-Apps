package io.github.lukas2005.WebBrowserApp.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
	 * Call this before any other method in this class
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
			System.out.println("Added component:"+c.getName());
			panel.add(c, BorderLayout.CENTER);
			frame.setVisible(true);
		}	
		
		if (!frame.isVisible()) frame.setVisible(true);
		
		frame.setSize(width, height);
		
		c.paint(g);
		img.flush();
		
		frame.setSize(0, 0);
		
		//frame.setVisible(false);
		
		ResourceLocation rc = mc.getTextureManager().getDynamicTextureLocation(c.toString(), new DynamicTexture(img));
		
		mc.getRenderManager().renderEngine.bindTexture(rc);
		RenderUtil.drawRectWithTexture(x, y, 0, 0, width, height, width, height);
		mc.getTextureManager().deleteTexture(rc);
	}
	
}
