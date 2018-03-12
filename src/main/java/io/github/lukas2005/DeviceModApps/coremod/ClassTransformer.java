package io.github.lukas2005.DeviceModApps.coremod;

import com.mrcrayfish.device.api.app.IIcon;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClassTransformer implements IClassTransformer {

	public static ClassPool pool = ClassPool.getDefault();

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
			switch (name) {
				case "com.mrcrayfish.device.api.app.component.TextArea": {
					// Get the CtClass of the TextArea
					CtClass cc = pool.get(name);

					// Get the first (and only) constructor
					CtConstructor[] consts = cc.getDeclaredConstructors();
					CtConstructor constructor = consts[0];

					// Inject this line of code
					constructor.insertAfter("Main.textAreas.add(new WeakReference(this));");

					// Get the new bytecode
					returnBytecode = cc.toBytecode();
					break;
				}
				case "com.teamdev.jxbrowser.chromium.BrowserContextParams": {
					// Get the CtClass of the TextArea
					CtClass cc = pool.get(name);

					CtMethod method = cc.getDeclaredMethod("setAcceptLanguage", new CtClass[]{pool.get(String.class.getName())});

					// Inject this line of code
					method.setBody("d = \"en-us\";");

					// Get the new bytecode
					returnBytecode = cc.toBytecode();
					break;
				}
				case "com.mrcrayfish.device.api.app.component.Button": {
					// Get the CtClass of the TextArea
					CtClass cc = pool.get(name);
					// Get the method
					CtMethod method = cc.getDeclaredMethod("setIcon", new CtClass[]{pool.get(IIcon.class.getName())});

					// TODO: REMOVE THIS AFTER MRCRAYFISH WILL MERGE NEW IIcon
					method.insertAfter("if ($1 instanceof Emoji) {this.iconWidth = 10; this.iconHeight = 10; updateSize(); }");

					// Get the new bytecode
					returnBytecode = cc.toBytecode();
					break;
				}
				case "com.mrcrayfish.device.network.Router": {
					// Get the CtClass of the Router
					CtClass cc = pool.get(name);

					// Get the first (and only) constructor
					CtConstructor[] consts = cc.getDeclaredConstructors();
					CtConstructor constructor = consts[0];

					// Inject this line of code
					constructor.insertAfter("ApplicationHackPrinters.routers.add(new WeakReference(this));");

					// Get the new bytecode
					returnBytecode = cc.toBytecode();
					break;
				}
				case "com.mrcrayfish.device.tileentity.TileEntityRouter": {
					// Get the CtClass
					CtClass cc = pool.get(name);

					// Get the first (and only) constructor
					CtConstructor[] consts = cc.getDeclaredConstructors();
					CtConstructor constructor = consts[0];

					// Inject this line of code
					constructor.insertAfter("ApplicationHackPrinters.tileEntityRouters.add(new WeakReference(this));");

//                CtMethod readFromNbt = cc.getDeclaredMethod("readFromNBT", new CtClass[] {ReflectionManager.pool.get(ResourceLocation.class.toString()) });
//
//                readFromNbt.insertAt(85, "PacketHandler.INSTANCE.sendToServer(new MessageSyncBlock(pos));");

					// Get the new bytecode
					returnBytecode = cc.toBytecode();
					break;
				}
				case "com.mrcrayfish.device.network.task.MessageSyncApplications": {
					// Get the CtClass of the TextArea
					CtClass cc = pool.get(name);

					// Get the method to inject
					CtMethod onMessage = cc.getDeclaredMethod("onMessage", new CtClass[]{cc, pool.get(MessageContext.class.getName())});

					// Replace the method body with my own one
					StringBuilder builder = new StringBuilder();

					builder.append("{ \n");
					builder.append("ArrayList apps = new ArrayList($1.allowedApps); \n");
					//builder.append("apps.add(ApplicationManager.getApplication(new ResourceLocation(\""+ Reference.MOD_ID+"\", \"hpa\").toString())); \n");
					builder.append("apps.addAll(Main.alwaysAvailableApps); \n");
					builder.append("ReflectionHelper.setPrivateValue(CommonProxy.class, MrCrayfishDeviceMod.proxy, apps, new String[]{\"allowedApps\"}); \n");
					builder.append("return null; \n");
					builder.append("}");

					onMessage.setBody(builder.toString());

					// Get the new bytecode
					returnBytecode = cc.toBytecode();
					break;
				}
				case "com.mrcrayfish.device.api.ApplicationManager": {
					// Get the CtClass
					CtClass cc = pool.get(name);

					CtField f = cc.getDeclaredField("APP_INFO");
					f.setModifiers(f.getModifiers() & ~Modifier.FINAL);

					returnBytecode = cc.toBytecode();
					break;
				}
//				case "com.mrcrayfish.device.core.Laptop": {
//					// Get the CtClass
//					CtClass cc = pool.get(name);
//
//					CtMethod onGuiClosed;
//					try {
//						onGuiClosed = cc.getDeclaredMethod("onGuiClosed");
//					} catch (NotFoundException e) {
//						try {
//							onGuiClosed = cc.getDeclaredMethod("func_146281_b");
//						} catch (NotFoundException e1) {
//							throw e;
//						}
//					}
//
//					onGuiClosed.instrument(new ExprEditor() {
//						@Override
//						public void edit(MethodCall m) throws CannotCompileException {
//							String s = m.getMethodName();
//							String w = m.getClassName();
//							if (m.getMethodName().equals("close") && m.getClassName().equals("com.mrcrayfish.device.core.Window")) {
//								m.replace("{}");
//							}
//						}
//					});
//
//					// Get the new bytecode
//					returnBytecode = cc.toBytecode();
//					break;
//				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return returnBytecode;
	}
}
