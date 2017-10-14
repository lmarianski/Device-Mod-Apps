package io.github.lukas2005.WebBrowserApp.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mrcrayfish.device.api.utils.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class SwingWrapper {
	
	public int width = 0;
	public int height = 0;
	
	public Component c;

	private BufferedImage img;
	private Graphics g;
	
	private volatile ResourceLocation rc;
	private Thread paintThread;
	
	public SwingWrapper(final int width, final int height, final Component c) {
		if (!SwingUtils.initialized) throw new IllegalStateException("SwingUtils not initalized!");
		//if (!c.isLightweight()) throw new IllegalArgumentException("Heavyweight components are not currently supported!"); 
		
		this.width = width;
		this.height = height;
		this.c = c;
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = img.createGraphics();
		System.out.println("Creating new img");
		
		System.out.println("Added component:"+c.toString());
		SwingUtils.panel.add(c, BorderLayout.CENTER);
		SwingUtils.panel.setVisible(true);
		
		if (c.getName() == null) c.setName("Component");
		
		paintThread = new Thread("Swing Wraper Painitng Thread for "+c.getName()) {
			@Override
			public void run() {
				while(!Thread.interrupted()) {
					if (!SwingUtils.frame.isVisible()) SwingUtils.frame.setVisible(true);
					
					SwingUtils.frame.setSize(width, height);
					
					c.paint(g);
					img.flush();
					
					SwingUtils.frame.setSize(width, 0);
					
					rc = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(c.toString(), new DynamicTexture(img));
				}
			}
		};
		
		paintThread.start();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		paintThread.interrupt();
	}
	
	public void handleMouseClick(int xPosition, int yPosition, int mouseX, int mouseY, int mouseButton) {
		if ((mouseX > xPosition && mouseX < width) && (mouseY < yPosition && mouseY > height)) {
			System.out.println("Clicked on a warpper!");
			@SuppressWarnings("unchecked")
			Class<Component> class1 = (Class<Component>) c.getClass();
			try {
				Method m = class1.getDeclaredMethod("processEvent", AWTEvent.class);
				m.invoke(c, new MouseEvent(c, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, mouseX, mouseY, mouseX, mouseY, 1, false, mouseButton));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void render(int x, int y) {
		
		//frame.setVisible(false);
		
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(rc);
		RenderUtil.drawRectWithTexture(x, y, 0, 0, width, height, width, height);
		Minecraft.getMinecraft().getTextureManager().deleteTexture(rc);
	}
	
}
