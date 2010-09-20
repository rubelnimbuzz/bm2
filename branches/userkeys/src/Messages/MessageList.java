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
import Menu.MenuListener;
import Menu.MenuCommand;
import Menu.MyMenu;
import locale.SR;
import ui.VirtualElement;
import ui.VirtualList;
import ui.reconnectWindow;
//#ifdef CLIPBOARD
//# import util.ClipBoard;
//#endif

public abstract class MessageList extends VirtualList
    implements
        MenuListener
    {
    
    private Config cf;
    
    protected Vector messages;
//#ifdef CLIPBOARD
//#     private ClipBoard clipboard=ClipBoard.getInstance();
//#     
//#     protected MenuCommand cmdCopy = new MenuCommand(SR.MS_COPY, MenuCommand.SCREEN, 20);
//#     protected MenuCommand cmdCopyPlus = new MenuCommand("+ "+SR.MS_COPY, MenuCommand.SCREEN, 30);
//#endif
    protected MenuCommand cmdxmlSkin = new MenuCommand(SR.MS_USE_COLOR_SCHEME, MenuCommand.SCREEN, 40);

    protected MenuCommand cmdUrl = new MenuCommand(SR.MS_GOTO_URL, MenuCommand.SCREEN, 80);
    protected MenuCommand cmdBack = new MenuCommand(SR.MS_BACK, MenuCommand.BACK, 99);
    
    /** Creates a new instance of MessageList */
  
    public MessageList() {
        super();
        messages=null;
	messages=new Vector();
        menuCommands.removeAllElements();
        cf=Config.getInstance();
        
//#ifdef SMILES
        smiles=cf.smiles;
//#else
//#         smiles=false;
//#endif
        enableListWrapping(false);
	
        cursor=0;//activate

        setMenuListener(this);
    }    
    
    
    public abstract int getItemCount(); // из protected сделали public

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
    
    protected abstract Msg getMessage(int index);
    
    public void markRead(int msgIndex) {}
    
    protected boolean smiles;

    public void addMenuCommands() {
//#ifdef CLIPBOARD
//#         if (cf.useClipBoard) {
//#             addMenuCommand(cmdCopy);
//#             addMenuCommand(cmdCopyPlus);
//#         }
//#endif
        addMenuCommand(cmdxmlSkin);
        addMenuCommand(cmdUrl);
        addMenuCommand(cmdBack);
    }
    public void removeCommands () {
//#ifdef CLIPBOARD
//#         if (cf.useClipBoard) {
//#             removeMenuCommand(cmdCopy);
//#             removeMenuCommand(cmdCopyPlus);
//#         }
//#endif
        removeMenuCommand(cmdxmlSkin);
        removeMenuCommand(cmdUrl);
        removeMenuCommand(cmdBack);
    }

    public void menuAction(MenuCommand c, VirtualList d) {
        if (c==cmdBack) {
            StaticData.getInstance().roster.activeContact=null;
            destroyView();
        }
        if (c==cmdUrl) {
            try {
                Vector urls=((MessageItem) getFocusedObject()).getUrlList();
                new MessageUrl(urls); //throws NullPointerException if no urls
            } catch (Exception e) {/* no urls found */}
        }
        if (c==cmdxmlSkin) {
           try {
               if (((MessageItem)getFocusedObject()).msg.body.indexOf("xmlSkin")>-1) {
                    ColorTheme.loadSkin(((MessageItem)getFocusedObject()).msg.body,2);
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

    protected void key(int keyCode, boolean key_long) { // overriding this method to avoid autorepeat
        if (!key_long) {
            if (keyCode == Config.SOFT_RIGHT || keyCode == Config.KEY_BACK) {
                if (!reconnectWindow.getInstance().isActive() && !cf.oldSE) {
                    StaticData.getInstance().roster.activeContact = null;
                    destroyView();
                    return;
                }
            }
        }

        super.key(keyCode, key_long);
    }
   
    public void showMenu() {
        commandState();
        String capt="";
        try {
            capt=getMainBarItem().elementAt(0).toString();
        } catch (Exception ex){ }
        new MyMenu( this, this, capt, null, menuCommands);
   }

    public void commandState() { }
}
