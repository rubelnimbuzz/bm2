/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PEP;

import Client.Config;
import Client.StaticData;
import com.alsutton.jabber.JabberDataBlock;
import com.alsutton.jabber.datablocks.Iq;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import locale.SR;
import ui.controls.form.CheckBox;
import ui.controls.form.DefForm;
import ui.controls.form.DropChoiceBox;
import ui.controls.form.LinkString;
import ui.controls.form.SimpleString;
import ui.controls.form.SpacerItem;
import ui.controls.form.TextInput;

/**
 *
 * @author Vitaly
 */
public class PepForm extends DefForm {
//#ifdef PLUGINS
//#     public static String plugin = "PLUGIN_PEP";
//#endif

//#ifdef PEP
//#     private CheckBox sndrcvmood;
//#ifdef PEP_TUNE
//#    private CheckBox rcvtune;
//#endif
//#ifdef PEP_ACTIVITY
//#    private CheckBox rcvactivity;
//#endif
//#endif
    DropChoiceBox activity;
    TextInput ti;
    LinkString updmood, updact;
    
    public PepForm(Display display, Displayable pView) {
        super(display, pView, SR.MS_PEP);
        this.display = display;
        
//#ifdef PEP
//#ifdef PLUGINS
//#         if (StaticData.getInstance().PEP) {
//#endif            
//#             itemsList.addElement(new SimpleString("Receive events", true));
//#             sndrcvmood = new CheckBox(SR.MS_USERMOOD, Config.getInstance().sndrcvmood);
//#             itemsList.addElement(sndrcvmood);
//#             updmood = new LinkString(SR.MS_USERMOOD) {
//#                 public void doAction() {
//#                      Config.getInstance().sndrcvmood = true;
//#                      new MoodList(midlet.BombusMod.getInstance().getDisplay());
//#                 }
//#             }; 
//#             
//#ifdef PEP_TUNE
//#             rcvtune = new CheckBox(SR.MS_USERTUNE, Config.getInstance().rcvtune); 
//#             itemsList.addElement(rcvtune);
//#endif
//#ifdef PEP_ACTIVITY
//#             rcvactivity = new CheckBox(SR.MS_USERACTIVITY, Config.getInstance().rcvactivity);
//#             itemsList.addElement(rcvactivity);
//#             updact = new LinkString(SR.MS_USERACTIVITY) {
//#                 public void doAction() {
//#                      Config.getInstance().rcvactivity = true;
//#                      new ActivityList(midlet.BombusMod.getInstance().getDisplay());
//#                 }
//#             };
//#endif
//#ifdef PEP
//#             itemsList.addElement(new SpacerItem(10));
//#             itemsList.addElement(new SimpleString("Publish events", true));
//#             itemsList.addElement(updmood);
//#ifdef PEP_ACTIVITY
//#             itemsList.addElement(updact);
//#endif
//#ifdef PEP_LOCATION
//#             if (System.getProperty("microedition.location.version")!=null) {
//#             LinkString updloc = new LinkString("Location") {
//#                 public void doAction() {
//#                      new LocationForm(midlet.BombusMod.getInstance().getDisplay(), StaticData.getInstance().roster);
//#                 }
//#             };
//#             itemsList.addElement(updloc);
//#             }
//#endif
//#ifdef PLUGINS
//#         }
//#endif
//#endif
//#endif
        
        attachDisplay(display);
    }
    public void cmdOk() {        
        //publish(activity.getSelectedIndex(), ti.getText());
//#ifdef PEP
//#ifdef PLUGINS
//#         if (StaticData.getInstance().PEP) {
//#endif
//#             Config.getInstance().sndrcvmood=sndrcvmood.getValue();
//#ifdef PEP_TUNE
//#             Config.getInstance().rcvtune=rcvtune.getValue();
//#endif
//#ifdef PEP_ACTIVITY
//#             Config.getInstance().rcvactivity=rcvactivity.getValue();
//#endif
//#ifdef PLUGINS
//#         }
//#endif
//#         Config.getInstance().saveToStorage();
//#endif       
        
        display.setCurrent(StaticData.getInstance().roster);
    }   
}


