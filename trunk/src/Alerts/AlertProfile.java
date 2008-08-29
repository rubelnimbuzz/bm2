/*
 * AlertProfile.java
 *
 * Created on 28.03.2005, 0:05
 * Copyright (c) 2005-2008, Eugene Stahov (evgs), http://bombus-im.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * You can also redistribute and/or modify this program under the
 * terms of the Psi License, specified in the accompanied COPYING
 * file, as published by the Psi Project; either dated January 1st,
 * 2005, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package Alerts;

import Client.*;
import images.RosterIcons;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import locale.SR;
import ui.*;
//#ifndef MENU_LISTENER
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Command;
//#else
//# import java.util.Vector;
//# import Menu.MenuListener;
//# import Menu.Command;
//# import Menu.MyMenu;
//#endif
import ui.MainBar;

/**
 *
 * @author Eugene Stahov
 */
public class AlertProfile extends VirtualList implements
//#ifndef MENU_LISTENER
        CommandListener
//#else
//#         MenuListener
//#endif
    {
    
    private final static int ALERT_COUNT=4;
    
    //public final static int AUTO=0;
    public final static int ALL=0;
    public final static int VIBRA=1;
    public final static int SOUND=2;
    public final static int NONE=3;
    
    private Profile profile=new Profile();
    int defp;
    Config cf;
    
    /** Creates a new instance of Profile */
    
    private Command cmdOk=new Command(SR.MS_SELECT,Command.OK,1);
    private Command cmdDef=new Command(SR.MS_SETDEFAULT,Command.OK,2);
    private Command cmdCancel=new Command(SR.MS_BACK,Command.BACK,99);
    /** Creates a new instance of SelectStatus */
    public AlertProfile(Display d, Displayable pView) {
        super();
        
        cf=Config.getInstance();
        
        setMainBarItem(new MainBar(SR.MS_ALERT_PROFILE));
        
        commandState();

        setCommandListener(this);
        
        int p=cf.profile;
        defp=cf.def_profile;
        
        moveCursorTo(p);
        attachDisplay(d);
        this.parentView=pView;
    }

    public void commandState() {
//#ifdef MENU_LISTENER
//#         menuCommands.removeAllElements();
//#endif
        addCommand(cmdOk);
        addCommand(cmdDef);
        addCommand(cmdCancel);
    }
    
//#ifdef MENU_LISTENER
//#     public Vector menuCommands=new Vector();
//#     
//#     public Command getCommand(int index) {
//#         if (index>menuCommands.size()-1) return null;
//#         return (Command) menuCommands.elementAt(index);
//#     }
//# 
//#     public void touchLeftPressed(){
//#         showMenu();
//#     }
//# 
//#     public void addCommand(Command command) {
//#         if (menuCommands.indexOf(command)<0)
//#             menuCommands.addElement(command);
//#     }
//#     public void removeCommand(Command command) {
//#         menuCommands.removeElement(command);        
//#     }
//#     
//#     public void setCommandListener(MenuListener menuListener) { }
//#     
//#     protected void keyPressed(int keyCode) { // overriding this method to avoid autorepeat
//#         if (keyCode==Config.SOFT_LEFT) {
//#             showMenu();
//#             return;
//#         }
//#         if (keyCode==Config.SOFT_RIGHT || keyCode==Config.KEY_BACK) {
//#             destroyView();
//#             return;
//#         }
//#         super.keyPressed(keyCode);
//#     }
//# 
//#     public void showMenu() {
//#         commandState();
//#         new MyMenu(display, parentView, this, SR.MS_STATUS, null, menuCommands);
//#     }
//#endif
    
    int index;
    public VirtualElement getItemRef(int Index){
        if (Index>=ALERT_COUNT) throw new IndexOutOfBoundsException();
        index=Index;
        return profile;
    }
    
    
    private class Profile extends IconTextElement {
        public Profile(){
            super(RosterIcons.getInstance());
        }
        public int getImageIndex(){return index+RosterIcons.ICON_PROFILE_INDEX+1;}
        public String toString(){ 
            StringBuffer s=new StringBuffer();
            switch (index) {
                //case AUTO: s.append(SR.MS_ALERT_PROFILE_AUTO); break;
                case ALL: s.append(SR.MS_ALERT_PROFILE_ALLSIGNALS); break;
                case VIBRA: s.append(SR.MS_ALERT_PROFILE_VIBRA); break;
                case SOUND: s.append(SR.MS_SOUND); break;
                case NONE: s.append(SR.MS_ALERT_PROFILE_NOSIGNALS); break;
            }
            if (index==defp) s.append(SR.MS_IS_DEFAULT);
            return s.toString();
        }
    }
    
    public void commandAction(Command c, Displayable d){
        if (c==cmdOk) eventOk(); 
        if (c==cmdDef) { 
            cf.def_profile=defp=cursor;
	    cf.saveToStorage();
            redraw();
        }
        if (c==cmdCancel) destroyView();
    }
    
    public void eventOk(){
        cf.profile=cursor;
        destroyView();
    }
    
    public int getItemCount(){ return ALERT_COUNT; }
}
