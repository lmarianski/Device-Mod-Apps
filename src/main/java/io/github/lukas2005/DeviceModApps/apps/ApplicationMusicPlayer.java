package io.github.lukas2005.DeviceModApps.apps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.listener.ClickListener;

import io.github.lukas2005.DeviceModApps.objects.ListedSong;
import net.minecraft.nbt.NBTTagCompound;

public class ApplicationMusicPlayer extends ApplicationBase {
	
	/**
	 * Will be used when sound cloud will allow for registration of apps again - currently unused
	 */
	//ApiWrapper api = new ApiWrapper("", "", null, null);
	
	ItemList<ListedSong> playList;
	
	public static ArrayList<ListedSong> defaultRecords = new ArrayList<>();
	
	boolean isPlaying = false;
	
	public SoundPlayingThread soundThread;
	
	@Override
	public void init() {
		Layout main = new Layout();
		setCurrentLayout(main);
		
		playList = new ItemList<>(5, 5, 75, 6);
		
		//playList.addItem(new ListedSong("C418 - Cat", new File("")));
		//markDirty();
		
		main.addComponent(playList);
		
		final Button play = new Button("Play", 130, 10, 20, 20);
		
		main.addComponent(play);
		
		final Button pause = new Button("Pause", 100, 10, 20, 20);
		pause.setEnabled(false);
		main.addComponent(pause);
		
		final Button stop = new Button("Stop", 140, 10, 20, 20);
		pause.setEnabled(false);
		main.addComponent(stop);
		
		play.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				soundThread = new SoundPlayingThread(playList.getSelectedItem().file);
				soundThread.start();
				isPlaying = true;
				pause.setEnabled(isPlaying);
				stop.setEnabled(true);
				play.setEnabled(!isPlaying);
			}
		});
		
		pause.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				soundThread.pause();
				isPlaying = false;
				pause.setEnabled(isPlaying);
				stop.setEnabled(true);
				play.setEnabled(!isPlaying);
			}
		});
		
		stop.setClickListener(new ClickListener() {
			@Override
			public void onClick(Component c, int mouseButton) {
				soundThread.close();
				isPlaying = false;
				pause.setEnabled(isPlaying);
				stop.setEnabled(false);
				play.setEnabled(!isPlaying);
			}
		});
		
	}
	
	@Override
	public void load(NBTTagCompound nbt) {
		NBTTagCompound songList = nbt.getCompoundTag("songList");
		playList.removeAll();
		for (String key : songList.getKeySet()) {
			playList.addItem(new ListedSong(key, new File(songList.getString(key))));
		}
	}

	@Override
	public void save(NBTTagCompound nbt) {
		NBTTagCompound songList = new NBTTagCompound();
		for (ListedSong s : playList.getItems()) {
			songList.setString(s.name, s.file.getAbsolutePath());
		}
		nbt.setTag("songList", songList);
	}

	public class SoundPlayingThread extends Thread {
		
		File audioFile;
		Clip clip;
		
		public SoundPlayingThread(File audioFile) {
			this.audioFile = audioFile;
		}
		
		public void pause() {
			clip.stop();
		}
		
		public void close() {
			clip.stop();
			clip.close();
			Thread.currentThread().interrupt();
		}
		
		@Override
		public void run() {
	        try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile.getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                Thread.sleep(clip.getMicrosecondLength()/1000);
            } catch(InterruptedException ex) {
            	Thread.currentThread().interrupt();
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            	System.out.println("Error playing sound");
            	e.printStackTrace();
			}
		}
	}
	
}
