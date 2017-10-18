package io.github.lukas2005.DeviceModApps.apps;

import java.io.File;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.TextField;
import com.mrcrayfish.device.api.app.listener.ClickListener;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import io.github.lukas2005.DeviceModApps.Reference;
import io.github.lukas2005.DeviceModApps.components.WebBrowserComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ApplicationWebBrowser extends Application {

	File dataDir = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()+"\\mods\\WebBrowserAppData");	
	
	Browser b;
	BrowserView view;
	WebBrowserComponent deviceModView;
	BrowserContextParams bcp;
	BrowserContext bc;
	
	public ApplicationWebBrowser() {
		super(new ResourceLocation(Reference.MOD_ID, "mwb").toString(), "Mineium Web Browser");
	}

	@Override
	public void init() {
		
		dataDir.mkdirs();
		BrowserContextParams bcp = new BrowserContextParams(dataDir.getAbsolutePath());
		BrowserContext bc = new BrowserContext(bcp);
		b = new Browser(BrowserType.LIGHTWEIGHT, bc);
		
		Layout main = new Layout(300,150);
		setCurrentLayout(main);
		
		deviceModView = new WebBrowserComponent(0, 0, main.width, main.height, b);
		b.loadURL("https://google.com");
		
		main.addComponent(deviceModView);
		
		final TextField addressBar = new TextField(10, 5, main.width-30);
		addressBar.setText("https://google.com");
		main.addComponent(addressBar);
		
		Button goButton = new Button("Go!", main.width-17, 5, 15, 15);
		goButton.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				b.loadURL(addressBar.getText());
			}
		});
		main.addComponent(goButton);
		
		//final Slider scrollBar = new Slider(main.width-5, 10, 100);
		//main.addComponent(scrollBar);
		
        b.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                	addressBar.setText(event.getValidatedURL());
                    event.getBrowser().executeJavaScript("document.body.style.overflow = 'hidden';");
                }
            }
        });
	}
	
	@Override
	public void onClose() {
		super.onClose();
		
		b.dispose();
		deviceModView.dispose();
	}
	
	@Override
	public void load(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub

	}

}
