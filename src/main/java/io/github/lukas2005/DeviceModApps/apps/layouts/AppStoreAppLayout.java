package io.github.lukas2005.DeviceModApps.apps.layouts;

import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.programs.system.layout.StandardLayout;
import io.github.lukas2005.DeviceModApps.apps.ApplicationUnofficialAppStore;
import io.github.lukas2005.DeviceModApps.apps.ModApps;
import io.github.lukas2005.DeviceModApps.objects.AppStoreAppInfo;
import net.minecraft.util.ResourceLocation;

public class AppStoreAppLayout extends StandardLayout {

	Layout prev;
	AppStoreAppInfo appInfo;

	public AppStoreAppLayout(int width, int height, ApplicationUnofficialAppStore app, Layout previous, AppStoreAppInfo appInfo) {
		super(appInfo.name, width, height, app, previous);
		prev = previous;
		this.appInfo = appInfo;
	}

	@Override
	public void init() {
		setIcon(Icons.HOME);

		Button btnPrev = new Button(214, 2, Icons.ARROW_LEFT);
		btnPrev.setToolTip("Back", "Go back to the app list");
		btnPrev.setClickListener((mouseX, mouseY, mouseButton) ->
		{
			if (mouseButton == 0) {
				app.setCurrentLayout(prev);
			}
		});
		addComponent(btnPrev);

		Label shortDesc = new Label(appInfo.shortDescription, 20, 30);
		addComponent(shortDesc);

		Label longDesc = new Label(appInfo.description, 20, 44);
		addComponent(longDesc);

		Button btnInstall = new Button(width-45, height-25, 40, 20, ((ApplicationUnofficialAppStore)app).installedApps.contains(appInfo) ? "Uninstall" : "Install");
		btnInstall.setClickListener((mouseX, mouseY, mouseButton) -> {
			if (!((ApplicationUnofficialAppStore)app).installedApps.contains(appInfo)) {
				((ApplicationUnofficialAppStore) app).installedApps.add(appInfo);
				((ApplicationUnofficialAppStore) app).installedJars.addAll(appInfo.libs);
				((ApplicationUnofficialAppStore) app).loadAllLibJars();
				try {
					appInfo.loadClasses();
					ModApps.registerApp(new ResourceLocation(appInfo.id), appInfo.getClasses().get(appInfo.getClasses().size() - 1), appInfo.needsDataDir);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				btnInstall.setText("Uninstall");
			} else {
				((ApplicationUnofficialAppStore) app).installedApps.remove(appInfo);
				appInfo.unloadClasses();
				btnInstall.setText("Install");
			}
		});
		addComponent(btnInstall);
	}

}
