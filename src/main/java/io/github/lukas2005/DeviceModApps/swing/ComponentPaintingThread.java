package io.github.lukas2005.DeviceModApps.swing;

import java.awt.Component;
import java.awt.Graphics;

/**
 * 
 * @author lukas2005
 *
 */
public class ComponentPaintingThread extends Thread {
	
	public int width = 0;
	public int height = 0;
	
	private Component c;
	private Graphics g;
	
	public ComponentPaintingThread(int width, int height, Component c, Graphics g) {
		super("Swing Wraper Painitng Thread for "+c.getName());
		
		this.width = width;
		this.height = height;
		this.c = c;
		this.g = g;
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			if (!SwingUtils.frame.isVisible()) SwingUtils.frame.setVisible(true);
			
			SwingUtils.frame.setSize(width+26, height+49);
			
			//img.copyData(imgOld.getRaster());
			c.paint(g);
			//img.flush();
			
			//SwingUtils.frame.setSize(width, 0);
		}
	}
}
