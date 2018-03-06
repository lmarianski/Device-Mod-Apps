package io.github.lukas2005.DeviceModApps.apps;

import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.renderer.ListItemRenderer;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.programs.system.ApplicationFileBrowser;
import com.mrcrayfish.device.programs.system.layout.StandardLayout;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.Utils;
import io.github.lukas2005.DeviceModApps.apps.layouts.AppStoreAppLayout;
import io.github.lukas2005.DeviceModApps.objects.AppCategory;
import io.github.lukas2005.DeviceModApps.objects.AppStoreAppInfo;
import io.github.lukas2005.DeviceModApps.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

public class ApplicationUnofficialAppStore extends ApplicationBase {

	public static final int LAYOUT_WIDTH = 250;
	public static final int LAYOUT_HEIGHT = 150;

	private volatile LinkedHashSet<String> repos = new LinkedHashSet<>(Arrays.asList("lukas2005/Device-Mod-Apps"));
	private volatile ArrayList<AppStoreAppInfo> knownApps = new ArrayList<>();

	private volatile HashMap<String, String> jars = new HashMap<>();
	public volatile LinkedHashSet<URL> installedJars = new LinkedHashSet<>();

	public ArrayList<AppStoreAppInfo> installedApps = new ArrayList<>();
	private ItemList<AppStoreAppInfo> knownAppsList;
	private ItemList<AppCategory> categoriesList;
	private StandardLayout layoutMain;

	@Override
	public void init() {
		layoutMain = new StandardLayout("Home", LAYOUT_WIDTH, LAYOUT_HEIGHT, this, null);
		layoutMain.setIcon(Icons.HOME);
		setCurrentLayout(layoutMain);

		Button btnSearch = new Button(214, 2, Icons.SEARCH);
		btnSearch.setToolTip("Search", "Find a specific application");
		btnSearch.setClickListener((mouseX, mouseY, mouseButton) ->
		{
			if (mouseButton == 0) {
				//this.setCurrentLayout(new LayoutSearchApps(this, getCurrentLayout()));
			}
		});
		layoutMain.addComponent(btnSearch);

		Button btnManageApps = new Button(232, 2, Icons.HAMMER);
		btnManageApps.setToolTip("Manage Apps", "Manage your installed applications");
		layoutMain.addComponent(btnManageApps);

		knownAppsList = new ItemList<>(60, 25, LAYOUT_WIDTH - 65, 4); /* 8 */
		knownAppsList.setLoading(true);
		knownAppsList.setListItemRenderer(new ListItemRenderer<AppStoreAppInfo>(27)/* 13 */ {

			private final Color ITEM_BACKGROUND = Color.decode("0x9E9E9E");
			private final Color ITEM_SELECTED = Color.decode("0x757575");

			@Override
			public void render(AppStoreAppInfo appStoreAppInfo, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
				Gui.drawRect(x, y, x + width, y + height, selected ? ITEM_SELECTED.getRGB() : ITEM_BACKGROUND.getRGB());
				ClientProxy.myFontRenderer.drawString(appStoreAppInfo.name, x + 5, (y+6)-(9/2), 0xFFFFFF);
				ClientProxy.myFontRenderer.drawString(appStoreAppInfo.shortDescription, x + 5, (y+18)-(9/2), 0xFFFFFF);
			}
		});
		knownAppsList.setItemClickListener((appInfo, index, mouseButton) -> {
			if (mouseButton == 0) {
				setCurrentLayout(new AppStoreAppLayout(LAYOUT_WIDTH, LAYOUT_HEIGHT, this, layoutMain, appInfo));
			}
		});
		layoutMain.addComponent(knownAppsList);

		categoriesList = new ItemList<>(5, 25, 50, 8);
		categoriesList.setItems(Arrays.asList(AppCategory.values()));
		categoriesList.setItemClickListener((appCategory, index, mouseButton) -> {
			knownAppsList.removeAll();
			for (AppStoreAppInfo info : knownApps) {
				if (appCategory == info.category) knownAppsList.addItem(info);
			}
		});
		layoutMain.addComponent(categoriesList);

		doUpdateFromGithub();
	}

	public void doUpdateFromGithub() {
		knownAppsList.setLoading(true);
		knownApps.clear();
		installedApps.clear();
		installedJars.clear();
		jars.clear();
		new Thread(() -> {
			GitHub github = Main.github;
			try {
				for (String repo : repos) {
					GHRepository repository = github.getRepository(repo);
					if (repository.getName().equals("Device-Mod-Apps")) {
						GHContent content = repository.getFileContent("app-repos.json", "unofficial-app-store");
						String jsonString = IOUtils.toString(content.read(), Charset.defaultCharset());

						ArrayList<String> list = Main.gson.fromJson(jsonString, new TypeToken<List<String>>() {
						}.getType());

						repos.addAll(list);
					}

					try {
						GHContent content = repository.getFileContent("apps.json");
						String jsonString = IOUtils.toString(content.read(), Charset.defaultCharset());

						ArrayList<AppStoreAppInfo> appInfo = Main.gson.fromJson(jsonString, new TypeToken<List<AppStoreAppInfo>>() {
						}.getType());

						LinkedHashSet<GHContent> libsContent = new LinkedHashSet<>();

						try {
							libsContent.addAll(repository.getDirectoryContent("libs"));
						} catch (Exception e) {
							try {
								libsContent.addAll(repository.getDirectoryContent("lib"));
							} catch (Exception ignored) {
							}
						}

						LinkedHashSet<GHContent> addToLibContent = null;
						LinkedHashSet<GHContent> removeFromLibContent = null;
						while (addToLibContent == null || !addToLibContent.isEmpty()) {
							addToLibContent = new LinkedHashSet<>();
							removeFromLibContent = new LinkedHashSet<>();
							if (!libsContent.isEmpty()) {
								for (GHContent libContent : libsContent) {
									if (libContent.isFile() && !libContent.getName().endsWith("(2).jar") && libContent.getName().endsWith(".jar")) {
										jars.put(repo, libContent.getDownloadUrl());
									} else if (libContent.isDirectory()) {
										if (!(libContent.getName().equals("doc") || libContent.getName().equals("javadoc")))
											addToLibContent.addAll(repository.getDirectoryContent(libContent.getPath()));
										removeFromLibContent.add(libContent);
									}
								}
								libsContent.addAll(addToLibContent);
								libsContent.removeAll(removeFromLibContent);
							}
						}

//                    for (AppStoreAppInfo appInfo1 : appInfo) {
//                        for (URL jar : appInfo1.libs) {
//                            jars.add(jar.toString());
//                        }
//                    }

						knownApps.addAll(appInfo);
						knownAppsList.setLoading(false);
						categoriesList.setSelectedIndex(0);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void loadAllLibJars() {
		for (URL jarUrl : installedJars) {
			//System.out.println(jarUrl);
			for (Class c : Utils.loadAllClassesFromRemoteJar(jarUrl.toString())) {
				//System.out.println(c.getName());
			}
		}
	}


	@Override
	public void load(NBTTagCompound tagCompound) {
		if (tagCompound.hasKey("repos")) {
			NBTTagList list = (NBTTagList) tagCompound.getTag("repos");
			for (int i = 0; i < list.tagCount(); i++) {
				repos.add(list.getStringTagAt(i));
			}
		}
	}

	@Override
	public void save(NBTTagCompound tagCompound) {
		NBTTagList list = new NBTTagList();
		for (String r : repos) {
			list.appendTag(new NBTTagString(r));
		}
		tagCompound.setTag("repos", list);
	}
}
