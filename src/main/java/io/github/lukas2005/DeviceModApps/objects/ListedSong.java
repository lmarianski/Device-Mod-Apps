package io.github.lukas2005.DeviceModApps.objects;

import java.io.File;

public class ListedSong {

	public String name;
	public File file;
	
	public ListedSong(String name, File file) {
		super();
		this.name = name;
		this.file = file;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
