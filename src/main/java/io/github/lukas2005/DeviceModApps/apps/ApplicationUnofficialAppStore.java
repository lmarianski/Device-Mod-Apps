package io.github.lukas2005.DeviceModApps.apps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;

public class ApplicationUnofficialAppStore extends ApplicationBase {

    protected ArrayList<String> repos = new ArrayList<String>(Arrays.asList(new String[]{""}));

    @Override
    public void init() {

    }

    @Override
    public void load(NBTTagCompound tagCompound) {
        if (tagCompound.hasKey("repos")) {
            repos = new ArrayList<>();
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
