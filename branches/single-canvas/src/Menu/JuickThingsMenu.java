/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Menu;

import locale.SR;
//import images.MenuIcons;
import Client.MessageEdit;
import Client.Contact;
import java.util.*;
import ui.VirtualList;

/**
 *
 * @author Totktonada
 */
public class JuickThingsMenu extends Menu {
//#ifdef PLUGINS
//#     public static String plugin = new String("PLUGIN_JUICK");
//#endif
    //MenuIcons menuIcons=MenuIcons.getInstance();
    private Contact contact;
    private Vector things;

    public JuickThingsMenu(Vector things, VirtualList pView, Contact contact) {
//#ifdef JUICK
        super(SR.MS_JUICK_THINGS, null); //MenuIcons.getInstance()
//#else
//#         super("", null);
//#endif
        this.things = things;
        this.contact = contact;

        show(parentView);
        this.parentView = pView;

        int quantity = things.size();
        for(int i=0; i<quantity; i++)
           addItem((String) things.elementAt(i), i); //, menuIcons.ICON_JUICK

    }

    public void eventOk() {
        destroyView();
        MenuItem me=(MenuItem) getFocusedObject();
        if (me==null) return;
        int index=me.index;
        try {
//#ifdef RUNNING_MESSAGE
                Client.Roster.me=new MessageEdit((VirtualList)this.parentView, contact, things.elementAt(index)+" ");
//#else
//#         new MessageEdit(parentView, contact, things.elementAt(index)+" "); // To chat
//# //        new MessageEdit( this, contact, things.elementAt(index)+" "); // Previons menu
//#endif
        } catch (Exception e) {/*no messages*/}
    }
}
