package io.github.lukas2005.DeviceModApps.components;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.core.Laptop;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import io.github.lukas2005.DeviceModApps.swing.SwingWrapper;
import net.minecraft.client.Minecraft;

import javax.swing.*;

public class WebBrowserComponent extends Component {

    private SwingWrapper wrapper;
	
	public WebBrowserComponent(int x, int y, int width, int height, Browser b) {
		super(x, y);

        BrowserView view = new BrowserView(b);

		this.wrapper = new SwingWrapper(width, height, 1000, 450, view);
	}
	
	/*
	 * Call at the closing of the app
	 */
	public void dispose() {
		this.wrapper.dispose();
	}
	
	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		if(!this.visible || !this.enabled)
			return;
		wrapper.handleMouseClick(xPosition, yPosition, mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void handleMouseScroll(int mouseX, int mouseY, boolean direction) {
		wrapper.handleMouseScroll(xPosition, yPosition, mouseX, mouseY, direction);
	}

	@Override
	protected void handleMouseDrag(int mouseX, int mouseY, int mouseButton) {
		wrapper.handleMouseDrag(xPosition, yPosition, mouseX, mouseY, mouseButton);
	}

	@Override
	protected void handleMouseRelease(int mouseX, int mouseY, int mouseButton) {
		wrapper.handleMouseRelease(xPosition, yPosition, mouseX, mouseY, mouseButton);
	}

	@Override
	public void handleKeyTyped(char key, int code) {
		wrapper.handleKeyTyped(key, code);
	}

	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
		wrapper.render(x, y, mouseX, mouseY);
	}

}
