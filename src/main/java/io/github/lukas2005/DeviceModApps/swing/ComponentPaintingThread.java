package io.github.lukas2005.DeviceModApps.swing;

/**
 * @author lukas2005
 */
public class ComponentPaintingThread extends Thread {

	public int width = 0;
	public int height = 0;

	private SwingWrapper w;

	public ComponentPaintingThread(int width, int height, SwingWrapper w) {
		super("Swing Wraper Painitng Thread for " + w.c.getName());

		this.width = width;
		this.height = height;
		this.w = w;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			if (!w.frame.isVisible()) w.frame.setVisible(true);

			w.frame.setSize(width + 26, height + 49);

			//img.copyData(imgOld.getRaster());
			//w.g.setColor(Color.WHITE);
			//w.g.fillRect(0,0, w.c.getWidth(), w.c.getHeight());
			w.c.paint(w.g);
			//img.flush();

			//SwingUtils.frame.setSize(width, 0);
		}
	}
}
