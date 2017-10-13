package io.github.lukas2005.WebBrowserApp.components;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.core.Laptop;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import net.minecraft.client.Minecraft;

public class WebBrowserComponent extends Component {

	private BrowserView view;
	
	private int width;
	private int height;
	
	public WebBrowserComponent(int x, int y, int width, int height, Browser b) {
		super(x, y);
		
		this.view = new BrowserView(b);
		
		this.width = width;
		this.height = height;
		
	}
	
	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
		
		SwingUtils.drawSwingComponent(x, y, width, height, view);
		
	}

}
