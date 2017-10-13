package io.github.lukas2005.WebBrowserApp.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mrcrayfish.device.api.utils.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

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
		frame.setLayout(new GridBagLayout());
		panel = new JPanel(new GridBagLayout());
		frame.add(panel);
		initialized = true;
	}
	
	static int prev_width;
	static int prev_height;
	static BufferedImage img;
	
	public static void drawSwingComponent(int x, int y, int width, int height, Component c) {
		if (!initialized) throw new IllegalStateException("SwingUtils not initalized!");
		
		if (prev_width != width || prev_height != height) {
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			img.createGraphics();
		}
		Graphics g = img.getGraphics();
		
		prev_width = width;
		prev_height = height;
		
		try {
			@SuppressWarnings("unused")
			Component tmp = panel.getComponents()[0];
		} catch (IndexOutOfBoundsException e) {
			panel.add(c);
		}
		
		frame.setVisible(true);
		
		c.paint(g);
		
		//frame.setVisible(false);
		
		ResourceLocation rc = mc.getTextureManager().getDynamicTextureLocation(" ", new DynamicTexture(img));
		
		mc.getRenderManager().renderEngine.bindTexture(rc);
		RenderUtil.drawRectWithTexture(x, y, 0, 0, width, height, width, height);
		mc.getTextureManager().deleteTexture(rc);
	}
	
}
