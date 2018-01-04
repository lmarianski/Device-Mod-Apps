package io.github.lukas2005.DeviceModApps.swing;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.minecraft.client.renderer.BufferBuilder;
import org.lwjgl.input.Keyboard;

import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author lukas2005
 *
 */
public class SwingWrapper {
	
	public int width = 0;
	public int height = 0;
	
	public int renderWidth;
	public int renderHeight;

	public JFrame frame;
	public JPanel panel;

	public Component c;

	protected BufferedImage img;
	//private BufferedImage imgOld;
	protected Graphics g;
	
	protected Thread paintThread;
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected TextureManager txt = mc.getTextureManager();
	
	public SwingWrapper(int width, int height, int renderWidth, int renderHeight, Component c) {
		//if (!SwingUtils.initialized) throw new IllegalStateException("SwingUtils not initalized!");
		//if (!c.isLightweight()) throw new IllegalArgumentException("Heavyweight components are not currently supported!"); 

		frame = new JFrame();
		panel = new JPanel();

		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(panel);

		this.width = width;
		this.height = height;
		
		this.renderWidth = renderWidth;
		this.renderHeight = renderHeight;
		
		this.c = c;
		
		img = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_RGB);
		g = img.createGraphics();
		
		panel.add(c, BorderLayout.CENTER);
		
		frame.setState(JFrame.NORMAL);
		
		frame.setVisible(true);
		
		new Thread(() -> {
            try {
                Thread.sleep(500);
                frame.setState(JFrame.ICONIFIED);
            } catch (InterruptedException ignored) {}
        }).start();
		
		if (c.getName() == null) c.setName("Component");
		
		paintThread = new ComponentPaintingThread(renderWidth, renderHeight, this);
		
		paintThread.start();	
	}
	
	public SwingWrapper(int width, int height, Component c) {
		this(width, height, width, height, c);
	}
	
	@Override
	protected void finalize() throws Throwable {
		dispose();
	}
	
	/**
	 * call when this object is no longer needed
	 */
	public void dispose() {
		paintThread.interrupt();
		panel.remove(this.c);
		frame.remove(panel);
		frame.setVisible(false);
		frame = null;
		panel = null;
		c = null;
	}
	
	public void handleMouseClick(int xPosition, int yPosition, int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height) {
			Point p = new Point(Math.round(SwingUtils.map(mouseX-xPosition, 0, width, 0, c.getWidth())), Math.round(SwingUtils.map(mouseY-yPosition, 0, height, 0, c.getHeight())));
			Point globalP = p.getLocation();
			SwingUtilities.convertPointToScreen(globalP, c);
			if (!(c instanceof BrowserView)) {
				SwingUtils.click(c, p.x, p.y, mouseButton);
			} else {
				BrowserView view = (BrowserView) c;
				MouseButtonType buttonType = MouseButtonType.PRIMARY;
				switch (mouseButton) {
					case 1:
						buttonType = MouseButtonType.SECONDARY;
						break;
					case 2:
						buttonType = MouseButtonType.MIDDLE;
						break;
				}
				SwingUtils.forwardMouseClickEvent(view.getBrowser(), buttonType, p.x, p.y, globalP.x, globalP.y);
			}
		}
	}
	
	public void handleMouseScroll(int xPosition, int yPosition, int mouseX, int mouseY, boolean direction) {
		if (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height) {
			Point p = new Point(Math.round(SwingUtils.map(mouseX-xPosition, 0, width, 0, c.getWidth())), Math.round(SwingUtils.map(mouseY-yPosition, 0, height, 0, c.getHeight())));
			if (c instanceof BrowserView) {
				BrowserView view = (BrowserView) c;
				SwingUtils.forwardMouseScrollEvent(view.getBrowser(), direction ? 5 : -5, p.x, p.y);
			}
		}
	}

	public void handleMouseDrag(int xPosition, int yPosition, int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height) {
			Point p = new Point(Math.round(SwingUtils.map(mouseX-xPosition, 0, width, 0, c.getWidth())), Math.round(SwingUtils.map(mouseY-yPosition, 0, height, 0, c.getHeight())));
			Point globalP = p.getLocation();
			SwingUtilities.convertPointToScreen(globalP, c);
			if (!(c instanceof BrowserView)) {
				SwingUtils.click(c, p.x, p.y, mouseButton);
			} else {
				BrowserView view = (BrowserView) c;
				MouseButtonType buttonType = MouseButtonType.PRIMARY;
				switch (mouseButton) {
					case 1:
						buttonType = MouseButtonType.SECONDARY;
						break;
					case 2:
						buttonType = MouseButtonType.MIDDLE;
						break;
				}
				SwingUtils.forwardMouseDragEvent(view.getBrowser(), buttonType, p.x, p.y, globalP.x, globalP.y);
			}
		}
	}

	public void handleMouseRelease(int xPosition, int yPosition, int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height) {
			Point p = new Point(Math.round(SwingUtils.map(mouseX-xPosition, 0, width, 0, c.getWidth())), Math.round(SwingUtils.map(mouseY-yPosition, 0, height, 0, c.getHeight())));
			Point globalP = p.getLocation();
			SwingUtilities.convertPointToScreen(globalP, c);
			if (!(c instanceof BrowserView)) {
				SwingUtils.click(c, p.x, p.y, mouseButton);
			} else {
				BrowserView view = (BrowserView) c;
				MouseButtonType buttonType = MouseButtonType.PRIMARY;
				switch (mouseButton) {
					case 1:
						buttonType = MouseButtonType.SECONDARY;
						break;
					case 2:
						buttonType = MouseButtonType.MIDDLE;
						break;
				}
				SwingUtils.forwardMouseReleaseEvent(view.getBrowser(), buttonType, p.x, p.y, globalP.x, globalP.y);
			}
		}
	}
	
	public void handleKeyTyped(char key, int code) {
		if (c instanceof BrowserView) {
			BrowserView view = (BrowserView) c;
			switch (code) {
				case(Keyboard.KEY_BACK):
					SwingUtils.forwardKeyTypedEvent(view.getBrowser(), KeyCode.VK_BACK);
					break;
				default:
					SwingUtils.forwardKeyTypedEvent(view.getBrowser(), key);
					break;
			}
		}
	}
	
	ResourceLocation rc;
	boolean isTheSameOld = true;
	int lastMouseX = 0;
	int lastMouseY = 0;
	public void render(int x, int y, int mouseX, int mouseY) {
		rc = txt.getDynamicTextureLocation(c.toString(), new DynamicTexture(img));
		if (!frame.isVisible()) frame.setVisible(true);
		
		if (rc != null) {
			mc.getRenderManager().renderEngine.bindTexture(rc);
			drawRectWithFullTexture(x, y, width, height);
			txt.deleteTexture(rc);
		}
		if (mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height) {
			if (mouseX != lastMouseX && mouseY != lastMouseY) {
				if (c instanceof BrowserView) {
					Point p = new Point(Math.round(SwingUtils.map(mouseX - x, 0, width, 0, c.getWidth())), Math.round(SwingUtils.map(mouseY - y, 0, height, 0, c.getHeight())));

					Point globalP = p.getLocation();
					SwingUtilities.convertPointToScreen(globalP, c);

					BrowserView view = (BrowserView) c;

					SwingUtils.forwardMouseMoveEvent(view.getBrowser(), p.x, p.y, globalP.x, globalP.y);
				}
			}
		}
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}

	private static void drawRectWithFullTexture(double x, double y, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(x, y + height, 0).tex(0, 1).endVertex();
		buffer.pos(x + width, y + height, 0).tex(1, 1).endVertex();
		buffer.pos(x + width, y, 0).tex(1, 0).endVertex();
		buffer.pos(x, y, 0).tex(0, 0).endVertex();
		tessellator.draw();
	}

}

//2005920