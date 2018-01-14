package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.TextField;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import io.github.lukas2005.DeviceModApps.components.WebBrowserComponent;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationWebBrowser extends ApplicationBase {

	Browser b;
	WebBrowserComponent deviceModView;
	BrowserContextParams bcp;
	BrowserContext bc;

	@Override
	public void init() {
		try {
			bcp = new BrowserContextParams(getAppDataDir().getAbsolutePath(), "en-us");
			bc = new BrowserContext(bcp);
			b = new Browser(BrowserType.LIGHTWEIGHT, bc);

			Layout main = new Layout(300,150);
			setCurrentLayout(main);

			deviceModView = new WebBrowserComponent(0, 0, main.width, main.height, b);
			b.loadURL("https://google.com");

			main.addComponent(deviceModView);

			final TextField addressBar = new TextField(10, 5, main.width-30);
			main.addComponent(addressBar);

			Button goButton = new Button(main.width-17, 5, 15, 15, "Go!");
			goButton.setClickListener((mouseX, mouseY, mouseButton) -> b.loadURL(addressBar.getText()));
			main.addComponent(goButton);

			//final Slider scrollBar = new Slider(main.width-5, 10, 100);
			//main.addComponent(scrollBar);

	        b.addLoadListener(new LoadAdapter() {
	            @Override
	            public void onFinishLoadingFrame(FinishLoadingEvent event) {
	                if (event.isMainFrame()) {
	                	addressBar.setText(event.getValidatedURL());
	                    //event.getBrowser().executeJavaScript("document.body.style.overflow = 'hidden';");
	                }
	            }
	        });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();

		b.dispose();
		deviceModView.dispose();
	}

	@Override
	public void load(NBTTagCompound tagCompound) {
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
	}

}
