package io.github.lukas2005.DeviceModApps.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class SwingWrapper {
	
	public int width = 0;
	public int height = 0;
	
	public int renderWidth;
	public int renderHeight;

	public Component c;

	protected BufferedImage img;
	//private BufferedImage imgOld;
	protected Graphics g;
	
	protected Thread paintThread;
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected TextureManager txt = mc.getTextureManager();
	
	public SwingWrapper(int width, int height, int renderWidth, int renderHeight, Component c) {
		if (!SwingUtils.initialized) throw new IllegalStateException("SwingUtils not initalized!");
		//if (!c.isLightweight()) throw new IllegalArgumentException("Heavyweight components are not currently supported!"); 
		
		this.width = width;
		this.height = height;
		
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;
		
		this.c = c;
		
		img = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_RGB);
		//imgOld = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		g = img.createGraphics();
		System.out.println("Creating new img");
		
		System.out.println("Added component:"+c.toString());
		SwingUtils.panel.add(c, BorderLayout.CENTER);
		SwingUtils.panel.setVisible(true);
		
		if (c.getName() == null) c.setName("Component");
		
		paintThread = new ComponentPaintingThread(renderWidth, renderHeight, c, g);
		
		paintThread.start();	
	}
	
	public SwingWrapper(int width, int height, Component c) {
		this(width, height, width, height, c);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		dispose();
	}
	
	/**
	 * call when this object is no longer needed
	 */
	public void dispose() {
		paintThread.interrupt();
		SwingUtils.panel.remove(this.c);
		c.setVisible(false);
	}
	
	public void handleMouseClick(int xPosition, int yPosition, int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height) {
			
			SwingUtils.click(c, Math.round(SwingUtils.map(mouseX-xPosition, 0, width, 0, c.getWidth())), Math.round(SwingUtils.map(mouseY-yPosition, 0, height, 0, c.getHeight())));
		}
	}
	
	public void handleMouse(int xPosition, int yPosition, int mouseX, int mouseY) {
		c.dispatchEvent(new MouseEvent(c, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, Math.round(SwingUtils.map(mouseX-xPosition, 0, width, 0, c.getWidth())), Math.round(SwingUtils.map(mouseY-yPosition, 0, height, 0, c.getHeight())), 1, false));
	}
	
	//int i = 0;
	ResourceLocation rc;
	boolean isTheSameOld = true;
	public void render(int x, int y) {
		//boolean isTheSame = SwingUtils.compareImages(img, imgOld);
		/*if (rc == null || !isTheSame)*/ rc = txt.getDynamicTextureLocation(c.toString(), new DynamicTexture(img));
		
		if (rc != null) {
			mc.getRenderManager().renderEngine.bindTexture(rc);
			drawRectWithFullTexture(x, y, 0, 0, width, height);
			/*if (rc != null || !isTheSame && isTheSameOld)*/ txt.deleteTexture(rc);
		}
		//isTheSameOld = isTheSame;
	}

	protected static void drawRectWithFullTexture(double x, double y, float u, float v, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + height, 0).tex(0, 1).endVertex();
		buffer.pos(x + width, y + height, 0).tex(1, 1).endVertex();
		buffer.pos(x + width, y, 0).tex(1, 0).endVertex();
		buffer.pos(x, y, 0).tex(0, 0).endVertex();
		tessellator.draw();
	}
	
}

//2005920