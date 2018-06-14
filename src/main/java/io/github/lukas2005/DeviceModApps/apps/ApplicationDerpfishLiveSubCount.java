package io.github.lukas2005.DeviceModApps.apps;

import java.io.IOException;

import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Image;
import com.mrcrayfish.device.api.app.component.Text;

import io.github.lukas2005.DeviceModApps.utils.Utils;
import io.github.lukas2005.DeviceModApps.YoutubeUtils;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Don't tell anyone that i called MrCrayfish a Derpfish here!
 */
public class ApplicationDerpfishLiveSubCount extends ApplicationBase {

	Thread subUpdateThread;

	public static final String MRCRAYFISH_CHANNEL_ID = "UCSwwxl2lWJcbGOGQ_d04v2Q";
	public static final String MRCRAYFISHDEV_CHANNEL_ID = "UC4GuSW24-hQTAz2fSWt0_Vw";

	@Override
	public void init(NBTTagCompound nbt) {
		Layout main = new Layout(200, 130);

		Image derpfishFace = null;
		try {
			derpfishFace = new Image(10, 10, 50, 50, YoutubeUtils.getChannelIcon(MRCRAYFISH_CHANNEL_ID));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Text derpfishName = new Text("MrCrayfish", 65, 20, main.width);
		final Text derpfishSubCount = new Text("0", 65, 30, main.width);

		main.addComponent(derpfishName);
		main.addComponent(derpfishSubCount);
		main.addComponent(derpfishFace);

		Image derpfishDevFace = null;
		try {
			derpfishDevFace = new Image(10, 70, 50, 50, YoutubeUtils.getChannelIcon(MRCRAYFISHDEV_CHANNEL_ID));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Text derpfishDevName = new Text("MrCrayfishDev", 65, 80, main.width);
		final Text derpfishDevSubCount = new Text("0", 65, 90, main.width);

		main.addComponent(derpfishDevName);
		main.addComponent(derpfishDevSubCount);
		main.addComponent(derpfishDevFace);

		Text aboutText = new Text("Requested by vos6434", main.width - 115, main.height - 10, main.width);
		//Text aboutText2 = new Text("Made by lukas2005", main.width-95, main.height-10, main.width);

		main.addComponent(aboutText);
		//main.addComponent(aboutText2);

		subUpdateThread = new Thread("Sub Update Thread") {
			public void run() {
				while (!Thread.interrupted()) {
					try {
						derpfishSubCount.setText(Utils.formatNumber(YoutubeUtils.getSubscriberCount(MRCRAYFISH_CHANNEL_ID)));
						derpfishDevSubCount.setText(Utils.formatNumber(YoutubeUtils.getSubscriberCount(MRCRAYFISHDEV_CHANNEL_ID)));
						Thread.sleep(5000);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};

		subUpdateThread.start();
		setCurrentLayout(main);
	}

	@Override
	public void onClose() {
		super.onClose();
		subUpdateThread.interrupt();
	}

	@Override
	public void load(NBTTagCompound arg0) {
	}

	@Override
	public void save(NBTTagCompound arg0) {
	}

}
