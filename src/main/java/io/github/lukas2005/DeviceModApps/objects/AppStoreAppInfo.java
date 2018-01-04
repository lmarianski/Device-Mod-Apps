package io.github.lukas2005.DeviceModApps.objects;

import java.net.URL;
import java.util.ArrayList;

public class AppStoreAppInfo {

    public String name;
    public String short_desc;
    public String desc;

    public ArrayList<URL> urls;
    public ArrayList<Class> classes;

    public void loadClasses() {
        for (URL url : urls) {

        }
    }

}
