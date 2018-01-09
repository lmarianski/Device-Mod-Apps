package io.github.lukas2005.DeviceModApps.apps;

import com.google.gson.reflect.TypeToken;
import io.github.lukas2005.DeviceModApps.Main;
import io.github.lukas2005.DeviceModApps.Utils;
import io.github.lukas2005.DeviceModApps.objects.AppStoreAppInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import scala.actors.threadpool.Arrays;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ApplicationUnofficialAppStore extends ApplicationBase {

    private LinkedHashSet<String> repos = new LinkedHashSet<String>(Arrays.asList(new String[]{"lukas2005/Device-Mod-Apps"}));

    private ArrayList<AppStoreAppInfo> knownApps = new ArrayList<>();
    private ArrayList<String> jars = new ArrayList<>();

    @Override
    public void init() {
        GitHub github = Main.github;
        try {
            for (String repo : repos) {
                GHRepository repository = github.getRepository(repo);
                if (repository.getName().equals("Device-Mod-Apps")) {
                    GHContent content = repository.getFileContent("app-repos.json", "unofficial-app-store");
                    String jsonString = IOUtils.toString(content.read(), Charset.defaultCharset());

                    ArrayList<String> list = Main.gson.fromJson(jsonString, new TypeToken<List<String>>(){}.getType());
                    repos.addAll(list);
                }

                try {
                    GHContent content = repository.getFileContent("apps.json");
                    String jsonString = IOUtils.toString(content.read(), Charset.defaultCharset());

                    ArrayList<AppStoreAppInfo> appInfo = Main.gson.fromJson(jsonString, new TypeToken<List<AppStoreAppInfo>>(){}.getType());

                    List<GHContent> libsContent = null;

                    try {
                        libsContent = repository.getDirectoryContent("libs");
                    } catch (Exception e) {
                        try {
                            libsContent = repository.getDirectoryContent("lib");
                        } catch (Exception ignored) {}
                    }

                    if (libsContent != null) {
                        for (GHContent libContent : libsContent) {
                            jars.add(libContent.getDownloadUrl());
                        }
                    }

                    for (AppStoreAppInfo appInfo1 : appInfo) {
                        for (URL jar : appInfo1.libs) {
                            jars.add(jar.getFile());
                        }
                    }

                    knownApps.addAll(appInfo);
                    loadAllLibJars();
                } catch (IOException e) {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAllLibJars() {
        for (String jarUrl : jars) {
            Utils.loadAllClassesFromJar(jarUrl);
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
