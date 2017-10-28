package io.github.lukas2005.DeviceModApps;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Utils {

    public static void pressUnicode(Robot r, int key_code) {
        r.keyPress(KeyEvent.VK_ALT);
        for(int i = 3; i >= 0; --i) {
            int numpad_kc = key_code / (int) (Math.pow(10, i)) % 10 + KeyEvent.VK_NUMPAD0;
            r.keyPress(numpad_kc);
            r.keyRelease(numpad_kc);
        }
        r.keyRelease(KeyEvent.VK_ALT);
    }
	
}
