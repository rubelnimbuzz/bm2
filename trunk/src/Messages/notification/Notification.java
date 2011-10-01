/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Messages.notification;

import Client.Config;

/**
 *
 * @author Vitaly
 */
public abstract class Notification {
    private static Notificator notifier;
    public static Notificator getNotificator() {
        if (notifier == null) {
//#if android
//#             notifier = new AndroidNotification();
//#else            
            switch (Config.getInstance().phoneManufacturer) {                
                case Config.SONYE:
                    notifier = new SEMCNotificator();
                    break;
            }
//#endif            
        }
        return notifier;
    }
}
