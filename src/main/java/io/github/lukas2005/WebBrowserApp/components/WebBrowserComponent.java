package io.github.lukas2005.WebBrowserApp.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.core.Laptop;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import io.github.lukas2005.WebBrowserApp.swing.SwingWrapper;
import net.minecraft.client.Minecraft;

public class WebBrowserComponent extends Component {

	private BrowserView view;
	private SwingWrapper wrapper;
	
	public WebBrowserComponent(int x, int y, int width, int height, Browser b) {
		super(x, y);
		
		this.view = new BrowserView(b);
		JButton jb = new JButton("Hello");
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hello from JButton!");
			}
		});
		this.wrapper = new SwingWrapper(width, height, jb);
	}
	
	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		if(!this.visible || !this.enabled)
			return;
		wrapper.handleMouseClick(xPosition, yPosition, mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
		wrapper.render(x, y);
	}

}
