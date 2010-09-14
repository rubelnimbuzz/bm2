/*
 * DefForm.java
 *
 * Created on 21.05.2008, 9:40
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

//import Client.Config;
import java.util.Vector;
import Menu.MenuListener;
import Menu.MenuCommand;
import Menu.MyMenu;
import locale.SR;
import ui.MainBar;
import ui.VirtualElement;
import ui.VirtualList;

/**
 *
 * @author ad
 */
public class DefForm 
        extends VirtualList
        implements MenuListener
    {

    
    public Vector itemsList=new Vector();

    public MenuCommand cmdOk = new MenuCommand(SR.MS_OK, MenuCommand.OK, 1);
    public MenuCommand cmdCancel = new MenuCommand(SR.MS_BACK, MenuCommand.BACK, 99);

    public int superWidth;
    /**
     * Creates a new instance of DefForm
     * @param caption
     */
    public DefForm(String caption) {	
        
	setMainBarItem(new MainBar(caption));
        
        superWidth=super.getWidth();
        
        commandState();
        
	setMenuListener(this);
        
        enableListWrapping(false);  
        show(parentView);
        
    }

    protected int getItemCount() { return itemsList.size(); }

    protected VirtualElement getItemRef(int index) {
        return (VirtualElement)itemsList.elementAt(index);
    }
    
    public void touchLeftPressed(){ cmdOk(); }

    public void touchRightPressed(){ cmdCancel(); }
    
    public void menuAction(MenuCommand command, VirtualList displayable) {
	if (command==cmdCancel) {
	    cmdCancel();
	}
	if (command==cmdOk) {
            cmdOk();
        }
    }

    public void destroyView()	{
	midlet.BombusMod.getInstance().setDisplayable(parentView);
    }

    public void cmdCancel() {
        destroyView();
    }

    public void cmdOk() { }
    
    public void commandState() {
        menuCommands.removeAllElements();
	addMenuCommand(cmdOk);
	addMenuCommand(cmdCancel);
    }
    
    public void showMenu() {
        commandState();
        if (menuCommands.size()==2) {
            if (menuCommands.elementAt(0).equals(cmdOk) && menuCommands.elementAt(1).equals(cmdCancel)) {
                cmdOk();
                return;
            }
        }
        MyMenu menu = new MyMenu(this, this, "", null, menuCommands);
        menu.show(this);
    }
    
    public String touchLeftCommand(){ return SR.MS_OK; }
}