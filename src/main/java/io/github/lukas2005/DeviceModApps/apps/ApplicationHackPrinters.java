package io.github.lukas2005.DeviceModApps.apps;

import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.io.File;
import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.api.task.TaskManager;
import com.mrcrayfish.device.core.network.NetworkDevice;
import com.mrcrayfish.device.core.network.Router;
import com.mrcrayfish.device.core.print.task.TaskPrint;
import com.mrcrayfish.device.network.PacketHandler;
import com.mrcrayfish.device.network.task.MessageSyncBlock;
import com.mrcrayfish.device.object.Picture;
import com.mrcrayfish.device.programs.ApplicationPixelPainter;
import com.mrcrayfish.device.tileentity.TileEntityPrinter;
import com.mrcrayfish.device.tileentity.TileEntityRouter;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * This app is just an experiment thus why i only have access to it
 */
public class ApplicationHackPrinters extends ApplicationBase {

	public static final ArrayList<WeakReference<Router>> routers = new ArrayList<>();
	public static final ArrayList<WeakReference<TileEntityRouter>> tileEntityRouters = new ArrayList<>();

	private Picture selectedPic = null;

	@Override
	public void init() {
		Layout main = new Layout();
		setCurrentLayout(main);

		Button selectImage = new Button(getWidth() - 85, getHeight() - 25, 40, 10, "Select Image");
		selectImage.setClickListener((mouseX, mouseY, mouseButton) -> {
			Dialog.OpenFile dialog = new Dialog.OpenFile(this);
			dialog.setResponseHandler((success, file) -> {
				if (success) {
					selectedPic = Picture.fromFile(file);
					return true;
				}
				return false;
			});
			openDialog(dialog);
		});
		main.addComponent(selectImage);

		Button hack = new Button(getWidth() - 45, getHeight() - 15, 40, 10, "Hack!");
		hack.setClickListener((mouseX, mouseY, mouseButton) -> {
			for (WeakReference<TileEntityRouter> ref : tileEntityRouters) {
				TileEntityRouter tileEntity = ref.get();
				if (tileEntity != null) {
					PacketHandler.INSTANCE.sendToServer(new MessageSyncBlock(tileEntity.getPos()));
				}
			}

			System.out.println(routers.size());
			for (WeakReference<Router> ref : routers) {
				Router router = ref.get();
				if (router != null) {
					ArrayList<NetworkDevice> printers = new ArrayList<>(router.getConnectedDevices(Minecraft.getMinecraft().world, TileEntityPrinter.class));
					for (NetworkDevice printer : printers) {
						Task taskPrint = new TaskPrint(printer.getPos(), printer, new ApplicationPixelPainter.PicturePrint(selectedPic.getName(), selectedPic.pixels, selectedPic.getWidth()));
						taskPrint.setCallback(((nbtTagCompound, success) -> {
							if (!success) {
								System.out.println("Failed to hack printer id: " + printer.getId() + " Pos: " + printer.getPos());
							} else {
								System.out.println("Hacked printer id: " + printer.getId() + " Pos: " + printer.getPos());
							}
						}));

						TaskManager.sendTask(taskPrint);
					}
				}
			}
		});
		main.addComponent(hack);

	}

	@Override
	public void load(NBTTagCompound tagCompound) {

	}

	@Override
	public void save(NBTTagCompound tagCompound) {

	}
}
