/*
 * userKeyEdit.java
 *
 * Created on 14.09.2007, 11:01
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

package ui.keys;
//#ifdef USER_KEYS
//# import locale.SR;
//# import ui.controls.form.CheckBox;
//# import ui.controls.form.DefForm;
//# import ui.controls.form.DropChoiceBox;
//# import ui.controls.form.KeyInput;
//# import ui.controls.form.SpacerItem;
//# 
//# /**
//#  *
//#  * @author ad
//#  */
//# 
//# 
//# class UserKeyEdit extends DefForm {
//# 
//#     private final UserKeysList userKeysList;
//# 
//#     private CheckBox two_keys_t;
//#     private KeyInput key_t;
//#     private DropChoiceBox commands_t = new DropChoiceBox(SR.MS_ACTION);
//# 
//#     UserKey origin_key;
//#     UserKey u;
//# 
//#     public UserKeyEdit(UserKeysList originUserKeysList, UserKey origin_key) {
//#         super((origin_key == null) ?
//#              SR.MS_ADD :
//#                 (origin_key.toString()));
//# 
//#         userKeysList = originUserKeysList;
//#         u = new UserKey(origin_key);
//#         this.origin_key = origin_key;
//# 
//# 
//#         two_keys_t = new CheckBox("Two keys", u.two_keys);
//#         itemsList.addElement(two_keys_t);
//# 
//#         key_t = new KeyInput(u, "Press it");
//#         itemsList.addElement(key_t);
//# 
//#         itemsList.addElement(new SpacerItem(10));
//# 
//#         int selected = 0;
//#         for (int i = 0; i < UserKeyExec.cmds.length; i++) {
//#             if (UserKeyExec.cmds[i] != null)
//#                 commands_t.add(UserKeyExec.cmds[i]);
//#             if (u.command_id == i)
//#                 selected = commands_t.size() - 1;
//#         }
//#         commands_t.setSelectedIndex(selected);
//#         itemsList.addElement(commands_t);
//# 
//# 
//#         moveCursorTo(getNextSelectableRef(-1));
//#         parentView = userKeysList;
//#     }
//# 
//#     public void cmdOk() {
//#         u.command_id = UserKeyExec.getCommandID((String) commands_t.items.elementAt(commands_t.getSelectedIndex()));
//# 
//#         if (origin_key == null) {
//#             userKeysList.itemsList.addElement(u);
//#         } else {
//#             origin_key.copyFrom(u);
//#         }
//# 
//#         userKeysList.commandState();
//# 		sd.canvas.show(userKeysList);
//#     }
//# 
//#     public void eventOk() {
//#         if (key_t.selected)
//#             return;
//# 
//#         super.eventOk();
//#         key_t.setTwoKeys(two_keys_t.getValue());
//#     }
//# 
//#     public boolean key(int keyCode, boolean key_long) {
//#         if (!key_t.selected)
//#             return false;
//# 
//#         key_t.key(keyCode, key_long);
//#         redraw();
//#         return true;
//#     }
//# }
//#endif