package io.github.lukas2005.WebBrowserApp.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.mrcrayfish.device.api.utils.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class SwingWrapper {
	
	public int width = 0;
	public int height = 0;
	
	public Component c;

	private volatile BufferedImage img;
	private Graphics g;
	
	private ResourceLocation rc;
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
					
					SwingUtils.frame.setSize(width-18, height-12);
					
					c.paint(g);
					img.flush();
					
					//SwingUtils.frame.setSize(width, 0);
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
		if (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height) {
			
			SwingUtils.click(c, Math.round(SwingUtils.map(mouseX-xPosition, 0, width, 0, c.getWidth())), Math.round(SwingUtils.map(mouseY-yPosition, 0, height, 0, c.getHeight())));
		}
	}
	//int i = 0;
	public void render(int x, int y) {
		rc = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(c.toString(), new DynamicTexture(img));
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(rc);
		RenderUtil.drawRectWithTexture(x, y, 0, 0, width, height, width, height);
		Minecraft.getMinecraft().getTextureManager().deleteTexture(rc);
	}
	
}
