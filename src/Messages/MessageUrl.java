/*
 * MessageUrl.java
 *
 * Created on 22.12.2005, 3:01
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

import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.TextField;
import midlet.BombusMod;
import ui.MIDPTextBox;
import ui.MIDPTextBox.TextBoxNotify;
import ui.controls.form.DefForm;
import locale.SR;
import Menu.MenuCommand;
//#ifdef CLIPBOARD
//# import util.ClipBoard;
//# import Client.Msg;
//#endif
import ui.VirtualList;
import ui.controls.form.ListItem;

/**
 *
 * @author EvgS
 */
public class MessageUrl extends DefForm implements TextBoxNotify {

//#ifdef CLIPBOARD
//#     private ClipBoard clipboard=ClipBoard.getInstance();
//# 
//#     protected MenuCommand cmdCopy = new MenuCommand(SR.MS_COPY, MenuCommand.SCREEN, 20);
//#     protected MenuCommand cmdCopyPlus = new MenuCommand("+ "+SR.MS_COPY, MenuCommand.SCREEN, 30);
//#endif
    MenuCommand cmdGoto=new MenuCommand("Goto", MenuCommand.OK, 2);
    MenuCommand cmdEdit=new MenuCommand("Edit", MenuCommand.SCREEN, 3);

    /** Creates a new instance of MessageUrl
     * @param urlList
     */
    public MessageUrl(Vector urlList) {
	super("URLs");
    
	loadItemsFrom(urlList);
        enableListWrapping(true);
    }

    public void menuAction(MenuCommand c, VirtualList d) {
        super.menuAction(c, d);
        if (c == cmdGoto) {
            eventOk();
        } else if (c == cmdEdit) {
            EditURL();
        }
//#ifdef CLIPBOARD
//#         if (c == cmdCopy) {
//#             clipboard.set(new Msg(Msg.MESSAGE_TYPE_IN, "url", null, itemsList.elementAt(getCursor()).toString()));
//#         }
//# 
//#         if (c == cmdCopyPlus) {
//#             clipboard.append(new Msg(Msg.MESSAGE_TYPE_IN, "url", null, itemsList.elementAt(getCursor()).toString()));
//#         }
//#endif
    }
    
    public void eventOk() {
        try {
            BombusMod.getInstance().platformRequest(itemsList.elementAt(getCursor()).toString());
        } catch (ConnectionNotFoundException ex) {
//#ifdef DEBUG            
//#             ex.printStackTrace();
//#endif            
        }

        destroyView();
    }

    public void EditURL() {
        new MIDPTextBox("Edit URL", itemsList.elementAt(getCursor()).toString(), this, TextField.ANY);
    }

    public void OkNotify(String text_return) {
        itemsList.setElementAt(new ListItem(text_return), getCursor());
    }
    
     public void commandState() {
         menuCommands.removeAllElements();
         addMenuCommand(cmdGoto);
         addMenuCommand(cmdEdit);
//#ifdef CLIPBOARD
//#          if (cf.useClipBoard) {
//#              addMenuCommand(cmdCopy);
//#              addMenuCommand(cmdCopyPlus);
//#          }
//#endif
     }    
}
