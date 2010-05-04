/*
 * TextListBox.java
 *
 * Created on 25 ��� 2008 �., 16:58
 *
 * Copyright (c) 2006-2008, Daniel Apatin (ad), http://apatin.net.ru
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
 */

package ui.controls.form;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import ui.MainBar;
import ui.VirtualElement;
import ui.VirtualList;

import java.util.Vector;
import locale.SR;

import Menu.MenuListener;
import Menu.MenuCommand;
import Menu.MyMenu;

/**
 *
 * @author ad
 */
public class TextListBox 
        extends VirtualList 
        implements
        MenuListener
    {

    private MenuCommand cmdCancel=new MenuCommand(SR.MS_CANCEL, MenuCommand.BACK,99);
    private MenuCommand cmdOk=new MenuCommand(SR.MS_OK, MenuCommand.OK,1);
    private MenuCommand cmdClear=new MenuCommand(SR.MS_CLEAR, MenuCommand.SCREEN, 2);

    private Vector recentList;

    private EditBox ti;

    public TextListBox(EditBox ti) {
        super();
        this.ti=ti;
        this.recentList=ti.recentList;
        setMainBarItem(new MainBar(SR.MS_SELECT));

        commandState();
    }
    
    public void commandState() {
        menuCommands.removeAllElements();
        addMenuCommand(cmdOk);
        addMenuCommand(cmdClear);
        addMenuCommand(cmdCancel);
        setCommandListener(this);
    }
    
    public void eventOk() {
        if (recentList.size()>0)
            ti.setValue((String) recentList.elementAt(cursor));
        
        midlet.BombusMod.getInstance().setDisplayable(ti.t);
    }

    public void menuAction(MenuCommand c, VirtualList d){
        if (c==cmdClear) {
            ti.recentList.removeAllElements();
            ti.saveRecentList();
        }
        if (c==cmdOk) {
            eventOk();
            return;
        }
        
        midlet.BombusMod.getInstance().setDisplayable(parentView);
    }

    public VirtualElement getItemRef(int index){ 
        return new ListItem((String) recentList.elementAt(index)); 
    }
    public int getItemCount() { return recentList.size(); }
    
    public void showMenu() {
        commandState();
        String capt="";
        try {
            capt=getMainBarItem().elementAt(0).toString();
        } catch (Exception ex){ }
        new MyMenu( parentView, this, capt, null, menuCommands);
   }
}
