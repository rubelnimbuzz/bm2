/*
 * MessageList.java
 *
 * Created on 11.12.2005, 3:02
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

package Messages;

import Client.Config;
import Client.Msg;
import Client.StaticData;
import Colors.ColorTheme;
import java.util.Vector;
//#ifndef MENU_LISTENER
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Command;
//#else
//# import Menu.MenuListener;
//# import Menu.Command;
//# import Menu.MyMenu;
//#endif
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import locale.SR;
import ui.VirtualElement;
import ui.VirtualList;
//#ifdef CLIPBOARD
//# import util.ClipBoard;
//#endif

public abstract class MessageList extends VirtualList
    implements
//#ifndef MENU_LISTENER
        CommandListener
//#else
//#         MenuListener
//#endif
    {
    
    private Config cf;
    
    protected Vector messages;
//#ifdef CLIPBOARD
//#     private ClipBoard clipboard=ClipBoard.getInstance();
//#     
//#     protected Command cmdCopy = new Command(SR.MS_COPY, Command.SCREEN, 20);
//#     protected Command cmdCopyPlus = new Command("+ "+SR.MS_COPY, Command.SCREEN, 30);
//#endif
    protected Command cmdxmlSkin = new Command(SR.MS_USE_COLOR_SCHEME, Command.SCREEN, 40);

    protected Command cmdUrl = new Command(SR.MS_GOTO_URL, Command.SCREEN, 80);
    protected Command cmdBack = new Command(SR.MS_BACK, Command.BACK, 99);
    
    /** Creates a new instance of MessageList */
  
    public MessageList() {
        super();
        messages=null;
	messages=new Vector();
//#ifdef MENU_LISTENER
//#         menuCommands.removeAllElements();
//#endif
        cf=Config.getInstance();
        
//#ifdef SMILES
        smiles=cf.smiles;
//#else
//#         smiles=false;
//#endif
        enableListWrapping(false);
	
        cursor=0;//activate

        setCommandListener(this);
    }

    public MessageList(Display display) {
        this();
        attachDisplay(display);
    }
    
    
    public abstract int getItemCount(); // из protected �?делали public

    protected VirtualElement getItemRef(int index) {
	if (messages.size()<getItemCount()) messages.setSize(getItemCount());
	MessageItem mi=(MessageItem) messages.elementAt(index);
	if (mi==null) {
	    mi=new MessageItem(getMessage(index), this, smiles);
            mi.setEven( (index & 1) == 0);
            //mi.getColor();
	    messages.setElementAt(mi, index);
	}
        return mi;
    }
    
    public abstract Msg getMessage(int index);
    
    public void markRead(int msgIndex) {}
    
    protected boolean smiles;

    public void addCommands() {
//#ifdef CLIPBOARD
//#         if (cf.useClipBoard) {
//#             addCommand(cmdCopy);
//#             addCommand(cmdCopyPlus);
//#         }
//#endif
        addCommand(cmdxmlSkin);
        addCommand(cmdUrl);
        addCommand(cmdBack);
    }
    public void removeCommands () {
//#ifdef CLIPBOARD
//#         if (cf.useClipBoard) {
//#             removeCommand(cmdCopy);
//#             removeCommand(cmdCopyPlus);
//#         }
//#endif
        removeCommand(cmdxmlSkin);
        removeCommand(cmdUrl);
        removeCommand(cmdBack);
    }

    public void commandAction(Command c, Displayable d) {
        if (c==cmdBack) {
            StaticData.getInstance().roster.activeContact=null;
            destroyView();
        }
        if (c==cmdUrl) {
            try {
                Vector urls=((MessageItem) getFocusedObject()).getUrlList();
                new MessageUrl(display, urls); //throws NullPointerException if no urls
            } catch (Exception e) {/* no urls found */}
        }
        if (c==cmdxmlSkin) {
           try {
               if (((MessageItem)getFocusedObject()).msg.getBody().indexOf("xmlSkin")>-1) {
                  ColorTheme.getInstance().loadSkin(((MessageItem)getFocusedObject()).msg.getBody(),2);
               }
            } catch (Exception e){}
        }
        
//#ifdef CLIPBOARD
//#         if (c == cmdCopy)
//#         {
//#             try {
//#                 clipboard.add(((MessageItem)getFocusedObject()).msg);
//#             } catch (Exception e) {/*no messages*/}
//#         }
//#         
//#         if (c==cmdCopyPlus) {
//#             try {
//#                 clipboard.append(((MessageItem)getFocusedObject()).msg);
//#             } catch (Exception e) {/*no messages*/}
//#         }
//#endif
    }

    protected void keyPressed(int keyCode) { // overriding this method to avoid autorepeat
//#ifdef MENU_LISTENER
//#         if (keyCode==Config.SOFT_LEFT) {
//#             showMenu();
//#             return;
//#         }
//#         if (keyCode==Config.SOFT_RIGHT || keyCode==Config.KEY_BACK) {
//#             StaticData.getInstance().roster.activeContact=null;
//#             destroyView();
//#             return;
//#         }
//#endif
//#ifdef SMILES
        if (keyCode=='*') {
            try {
                ((MessageItem)getFocusedObject()).toggleSmiles();
            } catch (Exception e){}
            System.gc();
            try { Thread.sleep(50); } catch (InterruptedException ex) { }
            return;
        }
//#endif
        super.keyPressed(keyCode);
    }
   
//#ifdef MENU_LISTENER
//#     public Vector menuCommands=new Vector();
//#     
//#     public Command getCommand(int index) {
//#         if (index>menuCommands.size()-1) return null;
//#         return (Command) menuCommands.elementAt(index);
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
//#     public void showMenu() {
//#         commandState();
//#         new MyMenu(display, parentView, this, "", null, menuCommands);
//#    }
//#     public void touchLeftPressed(){
//#         showMenu();
//#     }
//#endif

    
//#ifdef MENU
//#     public void leftCommand() { showMenu(); }
//#     public String getLeftCommand() { return SR.MS_MENU; }
//#endif

    public void commandState() { }
}
