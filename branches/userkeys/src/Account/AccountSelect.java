/*
 * AccountSelect.java
 *
 * Created on 19.03.2005, 23:26
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

package Account;
import Client.*;
import locale.SR;
import midlet.BombusMod;
import ui.*;
import java.io.*;
import Menu.MenuCommand;
import io.NvStorage;
import ui.controls.AlertBox;
import ui.controls.form.DefForm;


/**
 *
 * @author Eugene Stahov
 */
public class AccountSelect extends DefForm {
    int activeAccount;
    boolean enableQuit;
    
    MenuCommand cmdLogin=new MenuCommand(SR.MS_SELLOGIN, MenuCommand.OK,1);
    MenuCommand cmdSelect=new MenuCommand(SR.MS_NOLOGIN, MenuCommand.SCREEN,2);
    MenuCommand cmdAdd=new MenuCommand(SR.MS_NEW_ACCOUNT, MenuCommand.SCREEN,3);
    MenuCommand cmdEdit=new MenuCommand(SR.MS_EDIT,MenuCommand.ITEM,3);
    MenuCommand cmdDel=new MenuCommand(SR.MS_DELETE,MenuCommand.ITEM,4);
    MenuCommand cmdConfig=new MenuCommand(SR.MS_OPTIONS,MenuCommand.ITEM,5);
    MenuCommand cmdQuit=new MenuCommand(SR.MS_APP_QUIT,MenuCommand.SCREEN,10);
    
    private Config cf;
    
    /** Creates a new instance of AccountPicker */
    public AccountSelect(boolean enableQuit) {
        super(SR.MS_ACCOUNTS);
        this.enableQuit = enableQuit;

        enableListWrapping(true);
        cf = Config.getInstance();

        if (enableQuit) {
            canBack = false;
        }

        activeAccount = cf.accountIndex;
        loadAccounts();

        if (!itemsList.isEmpty()) {
            moveCursorTo(activeAccount);
        } else {
//#ifdef IMPORT_EXPORT
//#ifdef PLUGINS
//#             if (StaticData.getInstance().IE) {
//#endif
//#             new IE.Accounts("/def_accounts.txt", 0,  true);
//#ifdef PLUGINS
//#             }
//#endif
//#             loadAccounts();
//#         if (itemsList.isEmpty()) {
//#endif
            new AccountForm(this, null);
            return;
//#ifdef IMPORT_EXPORT
//#         }
//#endif
        }
    }
    
    public final void loadAccounts() {
        Account a;
        int index=0;
        do {
            a=Account.createFromStorage(index);
            if (a!=null) {
                a.setActive(activeAccount==index);
                itemsList.addElement(a);
                index++;
             }
       } while (a!=null);
    }

    public final void commandState(){
        menuCommands.removeAllElements();
        if ((itemsList != null) && !itemsList.isEmpty()) {
            addMenuCommand(cmdLogin);
            addMenuCommand(cmdSelect);
            
            addMenuCommand(cmdEdit);
            addMenuCommand(cmdDel);
        }
        addMenuCommand(cmdAdd);
        addMenuCommand(cmdConfig);
        if (enableQuit) 
            addMenuCommand(cmdQuit);
    }


    public void touchRightPressed(){
        if (!canBack)
            return;
        destroyView();
    }
    public String touchLeftCommand() { return SR.MS_MENU; }
    public void touchLeftPressed() {
        showMenu();
    }

    public VirtualElement getItemRef(int Index) {
        if (Index > itemsList.size())
            Index = itemsList.size() - 1;
        return (VirtualElement) itemsList.elementAt(Index);
    }
    protected int getItemCount() { return itemsList.size();  }

    public void menuAction(MenuCommand c, VirtualList d){
        if (c==cmdQuit) {
            destroyView();
            BombusMod.getInstance().notifyDestroyed();
            return;
        }
        if (c==cmdCancel) {
            destroyView();
        }
        if (c==cmdConfig) new ConfigForm();
        if (c==cmdLogin) switchAccount(true);
        if (c==cmdSelect) switchAccount(false);
        if (c==cmdEdit) new AccountForm(this, (Account)getFocusedObject()).show(this);
        if (c==cmdAdd) {
            new AccountForm(this, null).show(this);
        }
        if (c==cmdDel) {
            if (cursor==cf.accountIndex && StaticData.getInstance().roster.isLoggedIn()) return;
            //if (((Account)getFocusedObject()).equals(StaticData.getInstance().account)) return;
            
            new AlertBox(SR.MS_DELETE, getFocusedObject().toString()) {
                public void yes() {
                    delAccount();
                }
                public void no() { }
            };
        }
    }
    

    public void destroyView(){
        if(itemsList.size()>0) {
            if (StaticData.getInstance().account==null)
                Account.loadAccount(false, cf.accountIndex);
            midlet.BombusMod.getInstance().setDisplayable(StaticData.getInstance().roster);
        }
    }

    private void delAccount(){
        if (itemsList.size()==1)
            cf.accountIndex=-1;
        else if (cf.accountIndex>cursor) cf.accountIndex--;

        cf.saveToStorage();

        itemsList.removeElement(getFocusedObject());
        rmsUpdate();
        moveCursorHome();
        commandState();
        redraw();
    }
    
    private void switchAccount(boolean login){
        cf.accountIndex=cursor;
        cf.saveToStorage();
        Account.loadAccount(login, cursor);
        destroyView();
    }
    
    public void eventOk() {
        if (getItemCount()>0) {
            canBack = true;
            switchAccount(true);
        }
    }
    
    public void rmsUpdate(){
        DataOutputStream outputStream=NvStorage.CreateDataOutputStream();
        int j=itemsList.size();
        for (int i=0;i<j;i++) 
            ((Account) itemsList.elementAt(i)).saveToDataOutputStream(outputStream);
        NvStorage.writeFileRecord(outputStream, "accnt_db", 0, true); //Account.storage
    }
    
    protected boolean key(int keyCode, boolean key_long) {
        if (key_long) {
            switch (keyCode) {
                case KEY_NUM6:
                    Config.fullscreen = !Config.fullscreen;
                    cf.saveToStorage();
                    VirtualList.fullscreen = Config.fullscreen;
                    StaticData.getInstance().roster.setFullScreenMode(Config.fullscreen);
                    return true;
            }
        }

        return super.key(keyCode, key_long);
    }
}