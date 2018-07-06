package io.github.lukas2005.DeviceModApps.coremod;

import javassist.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClassTransformer implements IClassTransformer {

	private static ClassPool pool = ClassPool.getDefault();

	private static CtClass CtString;
	private static CtClass CtMessageContext;

	static {
		pool.insertClassPath(new LoaderClassPath(CoreModMain.class.getClassLoader()));

		pool.importPackage("java.util");
		pool.importPackage("java.lang.ref");

		pool.importPackage("net.minecraft.util");

		pool.importPackage("net.minecraftforge.fml.relauncher");

		pool.importPackage("com.mrcrayfish.device");
		pool.importPackage("com.mrcrayfish.device.api");
		pool.importPackage("com.mrcrayfish.device.api.utils");
		pool.importPackage("com.mrcrayfish.device.network");
		pool.importPackage("com.mrcrayfish.device.network.task");
		//pool.importPackage("com.mrcrayfish.device.object");
		pool.importPackage("com.mrcrayfish.device.proxy");

		pool.importPackage("io.github.lukas2005.DeviceModApps");
		pool.importPackage("io.github.lukas2005.DeviceModApps.apps");

		try {
			CtString  = pool.get(String.class.getName());
			CtMessageContext = pool.get(MessageContext.class.getName());
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		byte[] returnBytecode = basicClass;

		try {
			if (!name.equals("javassist.ByteArrayClassPath")) {
				pool.insertClassPath(new ByteArrayClassPath(name, basicClass));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// Get the CtClass
			CtClass cc = pool.get(name);

			switch (name) {
				case "com.mrcrayfish.device.api.app.component.TextArea": {
					// Get the handleMouseClick method
					CtMethod method = cc.getDeclaredMethod("handleMouseClick");

					// Inject code
					method.insertAfter("if (this.isFocused) io.github.lukas2005.DeviceModApps.Main.lastFocusedTextArea = new WeakReference(this);");

					break;
				}
				case "com.teamdev.jxbrowser.chromium.BrowserContextParams": {
					CtMethod method = cc.getDeclaredMethod("setAcceptLanguage", new CtClass[]{CtString});

					// Inject code
					method.setBody("d = \"en-us\";");

					break;
				}
//				case "com.mrcrayfish.device.network.Router": {
//					// Get the first (and only) constructor
//					CtConstructor[] consts = cc.getDeclaredConstructors();
//					CtConstructor constructor = consts[0];
//
//					// Inject this line of code
//					constructor.insertAfter("ApplicationHackPrinters.routers.add(new WeakReference(this));");
//
//					break;
//				}
//				case "com.mrcrayfish.device.tileentity.TileEntityRouter": {
//					// Get the first (and only) constructor
//					CtConstructor[] consts = cc.getDeclaredConstructors();
//					CtConstructor constructor = consts[0];
//
//					// Inject this line of code
//					constructor.insertAfter("ApplicationHackPrinters.tileEntityRouters.add(new WeakReference(this));");
//
//					break;
//				}
//				case "com.mrcrayfish.device.network.task.MessageSyncApplications": {
//					// Get the method to inject
//					CtMethod onMessage = cc.getDeclaredMethod("onMessage", new CtClass[]{cc, CtMessageContext});
//
//					// Replace the method body with my own one
//
//					String code = "{ \n" +
//							"ArrayList apps = new ArrayList($1.allowedApps); \n" +
//							"apps.addAll(Main.alwaysAvailableApps); \n" +
//							"ReflectionHelper.setPrivateValue(CommonProxy.class, MrCrayfishDeviceMod.proxy, apps, new String[]{\"allowedApps\"}); \n" +
//							"return null; \n" +
//							"}";
//					onMessage.setBody(code);
//
//					break;
//				}
//				case "com.mrcrayfish.device.api.ApplicationManager": {
//
//					CtField f = cc.getDeclaredField("APP_INFO");
//					f.setModifiers(f.getModifiers() & ~Modifier.FINAL);
//
//					break;
//				}
			}

			if (cc.isModified()) {
				// Get the new bytecode
				returnBytecode = cc.toBytecode();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return returnBytecode;
	}
}
