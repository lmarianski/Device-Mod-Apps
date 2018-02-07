package io.github.lukas2005.DeviceModApps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrcrayfish.device.api.app.component.TextArea;
import com.mrcrayfish.device.object.AppInfo;
import io.github.lukas2005.DeviceModApps.apps.ApplicationMusicPlayer;
import io.github.lukas2005.DeviceModApps.apps.ApplicationUnofficialAppStore;
import io.github.lukas2005.DeviceModApps.apps.ModApps;
import io.github.lukas2005.DeviceModApps.objects.ListedSong;
import io.github.lukas2005.DeviceModApps.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:cdm@[0.2.0,]")
public class Main {

	public static File modDataDir;

	public static final ArrayList<WeakReference<TextArea>> textAreas = new ArrayList<>();

	@SidedProxy(serverSide = "io.github.lukas2005.DeviceModApps.proxy.ServerProxy", clientSide = "io.github.lukas2005.DeviceModApps.proxy.ClientProxy")
	public static IProxy proxy = null;

	public static final RemoteClassLoader classLoader = new RemoteClassLoader(Main.class.getClassLoader());
	public static GitHub github;
	public static Gson gson;

	public static final Random rand = new Random();

	public static ArrayList<AppInfo> alwaysAvailableApps = new ArrayList<>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);

		ModConfig.initConfig(e.getSuggestedConfigurationFile());

		try {
			github = GitHubBuilder.fromCredentials()/*.withOAuthToken("f6710197d10c01c77b8f5a1574c10ee0b57f5e6b", "lukas2005.38@gmail.com")
					*/.build();

			gson = new GsonBuilder()
					.serializeNulls()
					.setPrettyPrinting()
					.create();

//            ArrayList<AppStoreAppInfo> list = Main.gson.fromJson("[\n" +
//                    "  {\n" +
//                    "    \"name\": \"Mineuim Web Browser\",\n" +
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
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (!modDataDir.exists()) modDataDir.mkdirs();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);

		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Cat", SoundEvents.RECORD_CAT, 183000000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Blocks", SoundEvents.RECORD_BLOCKS, 327000000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Chirp", SoundEvents.RECORD_CHIRP, 183000000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Far", SoundEvents.RECORD_FAR, 152400000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Mall", SoundEvents.RECORD_MALL, 190200000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Mellohi", SoundEvents.RECORD_MELLOHI, 81600000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Stal", SoundEvents.RECORD_STAL, 138000000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Strad", SoundEvents.RECORD_STRAD, 184800000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Wait", SoundEvents.RECORD_WAIT, 214800000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Ward", SoundEvents.RECORD_WARD, 246600000));
		//ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - 11",    SoundEvents.RECORD_11,        66600000));
		ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - 13", SoundEvents.RECORD_13, 154800000));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);

		ModApps.init();
	}

}
