package io.github.lukas2005.DeviceModApps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrcrayfish.device.api.app.component.TextArea;
import com.mrcrayfish.device.object.AppInfo;
import io.github.lukas2005.DeviceModApps.apps.ApplicationDerpfishLiveSubCount;
import io.github.lukas2005.DeviceModApps.apps.ApplicationMusicPlayer;
import io.github.lukas2005.DeviceModApps.apps.ModApps;
import io.github.lukas2005.DeviceModApps.classloader.MultiClassLoader;
import io.github.lukas2005.DeviceModApps.classloader.RemoteClassLoader;
import io.github.lukas2005.DeviceModApps.objects.ListedSong;
import io.github.lukas2005.DeviceModApps.proxy.IProxy;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:cdm@[0.3.0,]")
public class Main {

	public static File modDataDir;

	public static final ArrayList<WeakReference<TextArea>> textAreas = new ArrayList<>();

	@SidedProxy(serverSide = "io.github.lukas2005.DeviceModApps.proxy.ServerProxy", clientSide = "io.github.lukas2005.DeviceModApps.proxy.ClientProxy")
	public static IProxy proxy = null;

	public static final MultiClassLoader classLoader = new MultiClassLoader(Main.class.getClassLoader());
	public static GitHub github;
	public static Gson gson;

	public static final Random rand = new Random();

	public static ArrayList<AppInfo> alwaysAvailableApps = new ArrayList<>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);

		ModConfig.initConfig(e.getSuggestedConfigurationFile());

		try {
			//github = GitHub.connectAnonymously();

			gson = new GsonBuilder()
					.serializeNulls()
					.setPrettyPrinting()
					.create();

//            ArrayList<AppStoreAppInfo> list = Main.gson.fromJson("[\n" +
//                    "  {\n" +
//                    "    \"name\": \"Mineium Web Browser\",\n" +
//                    "    \"shortDescription\": \"A web browser in mc!\",\n" +
//                    "    \"description\": \"\",\n" +
//                    "    \"category\": \"INTERNET\",\n" +
//                    "    \"urls\": []\n" +
//                    "  }\n" +
//                    "]", new TypeToken<List<AppStoreAppInfo>>(){}.getType());
//            System.out.println(list.get(0));
//
//            ArrayList<AppStoreAppInfo> info = new ArrayList<>();
//            info.add(new AppStoreAppInfo("Mineuim Web Browser", "A web browser in mc!", "", AppCategory.INTERNET, new ArrayList<>()));
//            System.out.println(gson.toJson(info));
			//new ApplicationUnofficialAppStore().init();
			System.out.println(YoutubeUtils.getChannelIcon(ApplicationDerpfishLiveSubCount.MRCRAYFISH_CHANNEL_ID));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (!modDataDir.exists()) modDataDir.mkdirs();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);

		ModApps.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

}
