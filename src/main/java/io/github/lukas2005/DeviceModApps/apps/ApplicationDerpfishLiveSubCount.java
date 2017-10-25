package io.github.lukas2005.DeviceModApps.apps;

import java.io.IOException;

import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Image;
import com.mrcrayfish.device.api.app.component.Text;

import io.github.lukas2005.DeviceModApps.YoutubeUtils;
import net.minecraft.nbt.NBTTagCompound;

/*
 * Don't tell anyone that i called MrCrayfish a Derpfish here!
 */
public class ApplicationDerpfishLiveSubCount extends ApplicationBase {

	Thread subUpdateThread;
	
	public static final String MRCRAYFISH_CHANNEL_ID = "UCSwwxl2lWJcbGOGQ_d04v2Q";
	public static final String MRCRAYFISHDEV_CHANNEL_ID = "UC4GuSW24-hQTAz2fSWt0_Vw";
	
	@Override
	public void init() {
		try {
			Layout main = new Layout(200, 130);
			setCurrentLayout(main);
			
			Image derpfishFace = new Image(10, 10, 50, 50, "http://yt3.ggpht.com/-3GSWfHL9moQ/AAAAAAAAAAI/AAAAAAAAAAA/0tcO6ddi8eY/s88-c-k-no-mo-rj-c0xffffff/photo.jpg");
			Text derpfishName = new Text("MrCrayfish", 65, 20, main.width);
			final Text derpfishSubCount = new Text("0", 65, 30, main.width);
			
			main.addComponent(derpfishName);
			main.addComponent(derpfishSubCount);
			main.addComponent(derpfishFace);
			
			System.out.println(YoutubeUtils.getChannelIcon(MRCRAYFISH_CHANNEL_ID));
			
			Image derpfishDevFace = new Image(10, 70, 50, 50, "http://yt3.ggpht.com/-AzEP6eQw3Nc/AAAAAAAAAAI/AAAAAAAAAAA/BKsMPLothZE/s88-c-k-no-mo-rj-c0xffffff/photo.jpg");
			Text derpfishDevName = new Text("MrCrayfishDev", 65, 80, main.width);
			final Text derpfishDevSubCount = new Text("0", 65, 90, main.width);
			
			main.addComponent(derpfishDevName);
			main.addComponent(derpfishDevSubCount);
			main.addComponent(derpfishDevFace);
			
			subUpdateThread = new Thread("Sub Update Thread") {
				public void run() {
					while (!Thread.interrupted()) {
						try {
							derpfishSubCount.setText(YoutubeUtils.getSubscriberCount(MRCRAYFISH_CHANNEL_ID)+"");
							derpfishDevSubCount.setText(YoutubeUtils.getSubscriberCount(MRCRAYFISHDEV_CHANNEL_ID)+"");
							Thread.sleep(5000);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				};
			};
			
			subUpdateThread.start();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void onClose() {
		super.onClose();
		subUpdateThread.interrupt();
	}
	
	@Override
	public void load(NBTTagCompound arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(NBTTagCompound arg0) {
		// TODO Auto-generated method stub

	}

}
