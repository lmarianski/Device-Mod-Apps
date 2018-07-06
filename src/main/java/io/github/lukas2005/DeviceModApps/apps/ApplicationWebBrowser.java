package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Spinner;
import com.mrcrayfish.device.api.app.component.TextField;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import io.github.lukas2005.DeviceModApps.components.WebBrowserComponent;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class ApplicationWebBrowser extends ApplicationBase {

	Browser b;
	WebBrowserComponent browserView;
	public TextField addressBar;

	@Override
	public void init(NBTTagCompound nbt) {
		Layout main = new Layout(300, 150);
		setCurrentLayout(main);

		Spinner spinner = new Spinner(main.width / 2 - 12 / 2, main.height / 2 - 12 / 2);
		main.addComponent(spinner);

		addressBar = new TextField(10, 5, main.width - 30);
		main.addComponent(addressBar);

		Button goButton = new Button(main.width - 17, 5, 15, 15, "Go!");
		goButton.setClickListener((mouseX, mouseY, mouseButton) -> b.loadURL(addressBar.getText()));
		main.addComponent(goButton);

		//final Slider scrollBar = new Slider(main.width-5, 10, 100);
		//main.addComponent(scrollBar);


		new Thread(() -> {
			try {
				b = initJXBrowser(getAppDataDir());

				browserView = new WebBrowserComponent(0, 0, main.width, main.height, b);
				b.loadURL("https://google.com");

				b.addLoadListener(new LoadAdapter() {

					@Override
					public void onStartLoadingFrame(StartLoadingEvent event) {
						spinner.setVisible(true);
					}

					@Override
					public void onFinishLoadingFrame(FinishLoadingEvent event) {
						if (event.isMainFrame()) {
							spinner.setVisible(false);
							addressBar.setText(event.getValidatedURL());
						}
					}
				});

				main.addComponent(browserView);
				main.components.remove(browserView);
				main.components.add(0, browserView);

				System.out.println(b.getRemoteDebuggingURL());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public void handleKeyReleased(char character, int code) {
		super.handleKeyReleased(character, code);

		if (code == 28) { // ENTER
			b.loadURL(addressBar.getText());
		}
	}

	@Override
	public void onClose() {
		super.onClose();

		try {
			b.dispose();
			browserView.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Browser init code
	 * @param dataDir
	 * @return
	 */
	public static Browser initJXBrowser(File dataDir) {
		BrowserPreferences.setChromiumSwitches("--remote-debugging-port=9222");

		BrowserContextParams bcp = new BrowserContextParams(dataDir.getAbsolutePath(), "en-us");
		BrowserContext bc = new BrowserContext(bcp);

//		b.addLoadListener(new LoadAdapter() {
//			@Override
//			public void onFinishLoadingFrame(FinishLoadingEvent e) {
//				if (e.isMainFrame()) {
//					DOMDocument document = e.getBrowser().getDocument();
//				}
//			}
//		});

		return new Browser(BrowserType.LIGHTWEIGHT, bc);
	}


	@Override
	public void load(NBTTagCompound tagCompound) {
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
	}

}
