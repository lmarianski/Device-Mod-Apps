package io.github.lukas2005.DeviceModApps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.device.api.app.component.TextArea;
import io.github.lukas2005.DeviceModApps.apps.ApplicationMusicPlayer;
import io.github.lukas2005.DeviceModApps.apps.ApplicationUnofficialAppStore;
import io.github.lukas2005.DeviceModApps.apps.ModApps;
import io.github.lukas2005.DeviceModApps.objects.AppCategory;
import io.github.lukas2005.DeviceModApps.objects.AppStoreAppInfo;
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
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.OkHttpConnector;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:cdm@[0.2.0-pre4,]")
public class Main {

    public static File modDataDir;

    public static final ArrayList<WeakReference<TextArea>> textAreas = new ArrayList<>();

    @SidedProxy(serverSide = "io.github.lukas2005.DeviceModApps.proxy.ServerProxy", clientSide = "io.github.lukas2005.DeviceModApps.proxy.ClientProxy")
    public static IProxy proxy = null;

    public static final RemoteClassLoader classLoader = new RemoteClassLoader(Main.class.getClassLoader());
    public static GitHub github;
    public static Gson gson;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);

        try {
            Properties props = new Properties();
            props.setProperty("oauth", "f6710197d10c01c77b8f5a1574c10ee0b57f5e6b");
            github = GitHubBuilder.fromProperties(props)
                    .build();

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
            new ApplicationUnofficialAppStore().init();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (!modDataDir.exists()) modDataDir.mkdirs();

        ReflectionManager.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        ReflectionManager.init(e);

        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Cat", SoundEvents.RECORD_CAT));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Blocks", SoundEvents.RECORD_BLOCKS));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Chirp", SoundEvents.RECORD_CHIRP));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Far", SoundEvents.RECORD_FAR));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Mall", SoundEvents.RECORD_MALL));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Mellohi", SoundEvents.RECORD_MELLOHI));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Stal", SoundEvents.RECORD_STAL));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Strad", SoundEvents.RECORD_STRAD));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Wait", SoundEvents.RECORD_WAIT));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - Ward", SoundEvents.RECORD_WARD));
        //ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - 11",    SoundEvents.RECORD_11));
        ApplicationMusicPlayer.registerDefaultSong(new ListedSong("C418 - 13", SoundEvents.RECORD_13));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
        ReflectionManager.postInit(e);

        ModApps.init();
    }

}
