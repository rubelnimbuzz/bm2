/*
 * ContactMessageList.java
 *
 * Created on 19.02.2005, 23:54
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

package Client;
//#ifndef WMUC
import Conference.MucContact;
//#endif
//#ifdef HISTORY
//# import History.HistoryAppend;
//#ifdef LAST_MESSAGES
//# import History.HistoryStorage;
//#endif
//#ifdef HISTORY_READER
//# import History.HistoryReader;
//#endif
//#endif
import Menu.RosterItemActions;
import Messages.MessageList;
import images.RosterIcons;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import locale.SR;
import ui.MainBar;
import java.util.*;
import ui.reconnectWindow;
//#ifndef MENU_LISTENER
//# import javax.microedition.lcdui.Command;
//#else
import Menu.Command;
//#endif
//#ifdef CLIPBOARD
//# import util.ClipBoard;
//#endif
//#ifdef ARCHIVE
import Archive.MessageArchive;
//#endif
//#ifdef JUICK
//# import Menu.JuickThingsMenu;
//#endif

public class ContactMessageList extends MessageList {
    Contact contact;

    Command cmdSubscribe=new Command(SR.MS_SUBSCRIBE, Command.SCREEN, 1);
    Command cmdUnsubscribed=new Command(SR.MS_DECLINE, Command.SCREEN, 2);
    Command cmdMessage=new Command(SR.MS_NEW_MESSAGE,Command.SCREEN,3);
    Command cmdResume=new Command(SR.MS_RESUME,Command.SCREEN,1);
    Command cmdReply=new Command(SR.MS_REPLY,Command.SCREEN,4);
    Command cmdQuote=new Command(SR.MS_QUOTE,Command.SCREEN,5);
//#ifdef ARCHIVE
    Command cmdArch=new Command(SR.MS_ADD_ARCHIVE,Command.SCREEN,6);
//#endif
    Command cmdPurge=new Command(SR.MS_CLEAR_LIST, Command.SCREEN, 7);
    Command cmdSelect=new Command(SR.MS_SELECT, Command.SCREEN, 8);
    Command cmdActions=new Command(SR.MS_CONTACT,Command.SCREEN,9);
    Command cmdActive=new Command(SR.MS_ACTIVE_CONTACTS,Command.SCREEN,10);
//#if TEMPLATES
//#     Command cmdTemplate=new Command(SR.MS_SAVE_TEMPLATE,Command.SCREEN,11);
//#endif
//#ifdef FILE_IO
    Command cmdSaveChat=new Command(SR.MS_SAVE_CHAT, Command.SCREEN, 12);
//#endif
//#ifdef HISTORY
//#ifdef HISTORY_READER
//#          Command cmdReadHistory=new Command("Read history", Command.SCREEN, 13);
//#endif
//# //        if (cf.lastMessages && !contact.isHistoryLoaded()) loadRecentList();
//#endif
//#ifdef CLIPBOARD    
//#     Command cmdSendBuffer=new Command(SR.MS_SEND_BUFFER, Command.SCREEN, 14);
//#endif
    
//#ifdef JUICK
//#     Command cmdSendJuickPostReply=new Command(SR.MS_SEND_JUICK_POST_REPLY, Command.SCREEN, 15);
//#     Command cmdSendJuickCommentReply=new Command(SR.MS_SEND_JUICK_COMMENT_REPLY, Command.SCREEN, 16);
//#     Command cmdJuickThings=new Command(SR.MS_JUICK_THINGS, Command.SCREEN, 17);
//#     Command cmdJuickPostSubscribe=new Command(SR.MS_JUICK_POST_SUBSCRIBE, Command.SCREEN, 18);
//#     Command cmdJuickPostUnsubscribe=new Command(SR.MS_JUICK_POST_UNSUBSCRIBE, Command.SCREEN, 19);
//#     Command cmdJuickPostRecommend=new Command(SR.MS_JUICK_POST_RECOMMEND, Command.SCREEN, 20);
//#endif

//#ifdef CLIPBOARD    
//#     private ClipBoard clipboard=ClipBoard.getInstance();
//#endif

    StaticData sd=StaticData.getInstance();
    
    private Config cf;
    
    private boolean composing=true;

    private boolean startSelection;

    /** Creates a new instance of MessageList */
    public ContactMessageList(Contact contact, Display display) {
        super(display);
        this.contact=contact;
        sd.roster.activeContact=contact;

        cf=Config.getInstance();
        
        MainBar mainbar=new MainBar(contact);
        setMainBarItem(mainbar);
        
        cursor=0;//activate
        commandState();
        setCommandListener(this);
        
        contact.setIncoming(0);
//#ifdef FILE_TRANSFER
        contact.fileQuery=false;
//#endif
//#ifdef HISTORY
//#ifdef LAST_MESSAGES
//#         if (cf.lastMessages && !contact.isHistoryLoaded()) loadRecentList();
//#endif
//#endif
        if (contact.msgs.size()>0)
            moveCursorTo(firstUnread());
    }
    
    public int firstUnread(){
        int unreadIndex=0;
        for (Enumeration e=contact.msgs.elements(); e.hasMoreElements();) {
            if (((Msg)e.nextElement()).unread)
                break;
            unreadIndex++;
        }
        return unreadIndex;
    }
    
    public void commandState(){
//#ifdef MENU_LISTENER
        menuCommands.removeAllElements();
//#endif
        if (startSelection) addCommand(cmdSelect);
        
        if (contact.msgSuspended!=null) addCommand(cmdResume);
        
        if (cmdSubscribe==null) return;
        
        try {
            Msg msg=(Msg) contact.msgs.elementAt(cursor);
            if (msg.messageType==Msg.MESSAGE_TYPE_AUTH) {
                addCommand(cmdSubscribe);
                addCommand(cmdUnsubscribed);
            }
        } catch (Exception e) {}
        
        addCommand(cmdMessage);
        
        if (contact.msgs.size()>0) {
//#ifndef WMUC
            if (contact instanceof MucContact && contact.origin==Contact.ORIGIN_GROUPCHAT) {
                addCommand(cmdReply);
            }
//#endif
            addCommand(cmdQuote);
            addCommand(cmdPurge);
            
            if (!startSelection) addCommand(cmdSelect);
        
//#ifdef CLIPBOARD
//#             if (cf.useClipBoard) {
//#                 addCommand(cmdCopy);
//#                 if (!clipboard.isEmpty()) addCommand(cmdCopyPlus);
//#             }
//#endif
//#ifdef MENU_LISTENER
            if (isHasScheme())
//#endif
                addCommand(cmdxmlSkin);
//#ifdef MENU_LISTENER
            if (isHasUrl())
//#endif
                addCommand(cmdUrl);
        }
        
        if (contact.origin!=Contact.ORIGIN_GROUPCHAT)
            addCommand(cmdActions);
    
	addCommand(cmdActive);
        if (contact.msgs.size()>0) {
//#ifdef ARCHIVE
//#ifdef PLUGINS
//#          if (sd.Archive)
//#endif
            addCommand(cmdArch);
//#endif
//#if TEMPLATES
//#ifdef PLUGINS         
//#          if (sd.Archive)
//#endif
//#             addCommand(cmdTemplate);
//#endif
        }
//#ifdef CLIPBOARD
//#         if (cf.useClipBoard && !clipboard.isEmpty()) {
//#             addCommand(cmdSendBuffer);
//#         }
//#endif
//#ifdef HISTORY
//#         if (cf.saveHistory)
//#             if (cf.msgPath!=null)
//#                 if (!cf.msgPath.equals(""))
//#                     if (contact.msgs.size()>0)
//#                         addCommand(cmdSaveChat);
//#ifdef HISTORY_READER
//#         if (cf.saveHistory && cf.lastMessages)
//#             addCommand(cmdReadHistory);
//#endif
//#endif
        
//#ifdef JUICK
//#         // http://code.google.com/p/bm2/issues/detail?id=94#c1
//#         if (isJuickContact()) {
//#             Msg msg = getMessage(cursor);
//#             String body;
//# 
//#             if (msg != null) {
//#                 body = getMessage(cursor).body;
//#             } else {
//#                 body = "";
//#             } msg = null;
//# 
//#             addCommand(cmdJuickThings);
//#             if (!getNumberJuickPostOrComment(body).equals("toThings")) {
//#                 if (getNumberJuickPostOrComment(body).indexOf('/') > 0) {
//#                     addCommand(cmdSendJuickCommentReply);
//#                 } else {
//#                     addCommand(cmdSendJuickPostReply);
//#                     addCommand(cmdJuickPostRecommend);
//#                 }
//#                 addCommand(cmdJuickPostSubscribe);
//#                 addCommand(cmdJuickPostUnsubscribe);
//#             }
//#         }
//#endif

        addCommand(cmdBack);
    }
    
    public void showNotify(){
        sd.roster.activeContact=contact;
//#ifdef LOGROTATE
//#         getRedraw(true);
//#endif
        super.showNotify();
//#ifndef MENU_LISTENER
//#         if (cmdResume==null) return;
//#         if (contact.msgSuspended==null)
//#             removeCommand(cmdResume);
//#         else
//#             addCommand(cmdResume);
//#
//#         if (cmdSubscribe==null) return;
//#         try {
//#             Msg msg=(Msg) contact.msgs.elementAt(cursor);
//#             if (msg.messageType==Msg.MESSAGE_TYPE_AUTH) {
//#                 addCommand(cmdSubscribe);
//#                 addCommand(cmdUnsubscribed);
//#             }
//#             else {
//#                 removeCommand(cmdSubscribe);
//#                 removeCommand(cmdUnsubscribed);
//#             }
//#         } catch (Exception e) {}
//#endif
    }
    
    public void forceScrolling() { //by voffk
        if (contact.moveToLatest) {
            if (cursor==(messages.size()-1)) {
                contact.moveToLatest=false;
                moveCursorEnd();
            }
        }
    }

    protected void beginPaint(){
        markRead(cursor);
        forceScrolling();
    }   
    
    public void markRead(int msgIndex) {
	if (msgIndex>=getItemCount()) return;
        if (msgIndex<contact.lastUnread) return;

        sd.roster.countNewMsgs();
//#ifdef LOGROTATE
//#         getRedraw(contact.redraw);
//#endif
    }
//#ifdef LOGROTATE
//#     private void getRedraw(boolean redraw) {
//#         if (!redraw) return;
//#         
//#         contact.redraw=false;
//#         messages=null;
//#         messages=new Vector();
//#         redraw();
//#     }
//#endif
    public int getItemCount(){ return contact.msgs.size(); }

    public Msg getMessage(int index) {
        if (index> getItemCount()-1) return null;
        
	Msg msg=(Msg) contact.msgs.elementAt(index);
	if (msg.unread) contact.resetNewMsgCnt();
	msg.unread=false;
	return msg;
    }
    
    public void focusedItem(int index){ 
        markRead(index); 
    }
    
    public void commandAction(Command c, Displayable d){
        super.commandAction(c,d);
		
        /** login-insensitive commands */
//#ifdef ARCHIVE
        if (c==cmdArch) {
            try {
                MessageArchive.store(getMessage(cursor),1);
            } catch (Exception e) {/*no messages*/}
        }
//#endif
//#if TEMPLATES
//#         if (c==cmdTemplate) {
//#             try {
//#                 MessageArchive.store(getMessage(cursor),2);
//#             } catch (Exception e) {/*no messages*/}
//#         }
//#endif
        if (c==cmdPurge) {
            if (messages.isEmpty()) return;
            
            if (startSelection) {
                for (Enumeration select=contact.msgs.elements(); select.hasMoreElements(); ) {
                    Msg mess=(Msg) select.nextElement();
                    if (mess.selected) {
                        contact.msgs.removeElement(mess);
                    }
                }
                startSelection = false;
                
                messages=null;
                messages=new Vector();
            } else {
                clearReadedMessageList();
            }
        }
        if (c==cmdSelect) {
            startSelection=true;
            Msg mess=((Msg) contact.msgs.elementAt(cursor));
            mess.selected = !mess.selected;
            mess.oldHighlite = mess.highlite;
            mess.highlite = mess.selected;
            //redraw();
            return;
        }
//#ifdef HISTORY
//#ifdef HISTORY_READER
//#         if (c==cmdReadHistory) {
//#             new HistoryReader(display, contact);
//#             return;
//#         }
//#endif
//#endif
//#if (FILE_IO && HISTORY)
//#         if (c==cmdSaveChat) saveMessages();
//#endif
        /** login-critical section */
        if (!sd.roster.isLoggedIn()) return;

        if (c==cmdMessage) { 
            contact.msgSuspended=null; 
            keyGreen(); 
        }
        if (c==cmdResume) keyGreen();
        if (c==cmdQuote) Quote();
        if (c==cmdReply) Reply();
        
        if (c==cmdActions) {
//#ifndef WMUC
            if (contact instanceof MucContact) {
                MucContact mc=(MucContact) contact;
                new RosterItemActions(display, this, mc, -1);
            } else
//#endif
                new RosterItemActions(display, this, contact, -1);
        }
	if (c==cmdActive) new ActiveContacts(display, this, contact);
        
        if (c==cmdSubscribe) sd.roster.doSubscribe(contact);
		
        if (c==cmdUnsubscribed) sd.roster.sendPresence(contact.bareJid, "unsubscribed", null, false);

//#ifdef CLIPBOARD
//#         if (c==cmdSendBuffer) {
//#             String from=sd.account.toString();
//#             String body=clipboard.getClipBoard();
//#             //String subj=null;
//#             
//#             String id=String.valueOf((int) System.currentTimeMillis());
//#             Msg msg=new Msg(Msg.MESSAGE_TYPE_OUT,from,null,body);
//#             msg.id=id;
//#             msg.itemCollapsed=true;
//#             
//#             try {
//#                 if (body!=null && body.length()>0) {
//#                     sd.roster.sendMessage(contact, id, body, null, null);
//#                     if (contact.origin!=Contact.ORIGIN_GROUPCHAT) contact.addMessage(msg);
//#                 }
//#             } catch (Exception e) {
//#                 contact.addMessage(new Msg(Msg.MESSAGE_TYPE_OUT,from,null,"clipboard NOT sended"));
//#             }
//#             redraw();
//#         }
//#endif
        
//#ifdef JUICK
//#         Msg msg = getMessage(cursor);
//#         String body;
//# 
//#         if (msg != null) {
//#             body = getMessage(cursor).body;
//#         } else {
//#             body = "";
//#         } msg = null;
//# 
//#         if (c == cmdSendJuickCommentReply || c == cmdSendJuickPostReply) {
//#             juickReply(body);
//#         } else if (c == cmdJuickPostSubscribe) {
//#             juickAction("S", body);
//#         } else if (c == cmdJuickPostUnsubscribe) {
//#             juickAction("U", body);
//#         } else if (c == cmdJuickPostRecommend) {
//#             juickAction("!", body);
//#         } else if (c == cmdJuickThings) {
//#             viewJuickThings(body);
//#         }
//#endif
    }
    
 //#ifdef JUICK
//#     public boolean isJuickContact() {
//#         return (contact.bareJid.equals("juick@juick.com")
//#              || contact.bareJid.startsWith("juick%juick.com@"));
//#     }
//# 
//#     public void viewJuickThings(String str) {
//#             char[] valueChars = str.toCharArray();
//#             int msg_length = valueChars.length;
//#             Vector things = new Vector();
//#             for(int i=0; i<msg_length; i++) {
//#                 if((i==0)||isCharBeforeJuickThing(valueChars[i-1]))
//#                 switch(valueChars[i]) {
//#                     case '#':
//#                     case '@':
//#                     case '*':
//#                         char firstSymbol = valueChars[i];
//#                         String thing = ""+firstSymbol;
//#                         while(i<(msg_length-1) && isCharFromJuickThing(valueChars[++i], firstSymbol))
//#                             thing = thing + valueChars[i];
//#                         while(thing.charAt(thing.length()-1) == '.')
//#                             thing = thing.substring(0, thing.length()-1);
//#                         if ((thing.length()>1) && (things.indexOf(thing)<0))
//#                             things.addElement(thing);
//#                         if(i>0) i--;
//#                         break;
//#                 }
//#             }
//#             if(things.isEmpty()) {
//#                 things.addElement("#");
//#                 things.addElement("#+");
//#                 things.addElement("*");
//#                 things.addElement("@");
//#             }
//#             new JuickThingsMenu(things, display, this, contact);
//#     }
//# 
//#     public boolean isCharBeforeJuickThing(char ch) {
//#         switch(ch) {
//#             case '\u0020': // space
//#             case '\u0009': // tab
//#             case '\u000C': // formfeed
//#             case '\n': // newline
//#             case '\r': // carriage return
//#             case '(':
//#                 return true;
//#         }
//#         return false;
//#     }
//#     
//#     public boolean isCharFromJuickThing(char ch, char type) {
//#         boolean result = false;
//#         switch(type) {
//#             case '#': // #number
//#                 result = (ch>46) && (ch<58); // [0-9/]
//#                 break;
//#             case '@': // @username
//#                 result = ((ch>47)&&(ch<58)) || ((ch>63)&&(ch<91)) || ((ch>96)&&(ch<123)) || ((ch=='_')||(ch=='|')||(ch=='.')) || ((ch>44)&&(ch<47)); // [a-zA-Z0-9-.@_|]
//#                 break;
//#             case '*': // *tag
//#                 result = ((ch>47)&&(ch<58)) || ((ch>64)&&(ch<91)) || ((ch>96)&&(ch<123)) || ((ch>1039)&&(ch<1104)) || ((ch=='_')||(ch=='|')||(ch=='.')) || ((ch>44)&&(ch<47)); // [a-zA-ZА-Яа-я0-9-._|])
//#                 break;
//#         }
//#         return result;
//#     }
//# 
//#     public String getNumberJuickPostOrComment(String str) {
//#         if ((str == null) || (str.equals("")))
//#             return "toThings";
//#         if ((str.charAt(0) != '@') && !str.startsWith("Reply by @"))
//#             return "toThings";
//#         int lastStrStartIndex = str.lastIndexOf('\n')+1;
//#         if (lastStrStartIndex<0)
//#             return "toThings";
//#         int numberEndsIndex = str.indexOf(" http://juick.com/", lastStrStartIndex);
//#         if (numberEndsIndex>0) {
//#             numberEndsIndex = str.indexOf(' ', lastStrStartIndex);
//#             return str.substring(lastStrStartIndex, numberEndsIndex);
//#         }
//#         return "toThings";
//#     }
//# 
//#     public void juickReply(String str) {
//#           try {
//#ifdef RUNNING_MESSAGE
//#                 sd.roster.me=new MessageEdit(display, this, contact, getNumberJuickPostOrComment(str)+" ");
//#else
//#             new MessageEdit(display, this, contact, getNumberJuickPostOrComment(str)+" ");
//#endif
//#             } catch (Exception e) {/*no messages*/}
//#     }
//# 
//#     public void juickAction(String action, String body) {
//#         String post = getNumberJuickPostOrComment(body);
//#         if (post.indexOf("/") > 0) {
//#             post = post.substring(0, post.indexOf("/"));
//#         }
//#         try {
//#ifdef RUNNING_MESSAGE
//#                 sd.roster.me=new MessageEdit(display, this, contact, action + " " + post);
//#else
//#             new MessageEdit(display, this, contact, action + " " + post);
//#endif
//#             } catch (Exception e) {/*no messages*/
//#         }
//#     }
//#endif

    public void clearReadedMessageList() {
        smartPurge();
        messages=null;
        messages=new Vector();
        cursor=0;
        moveCursorHome();
        redraw();
    }
    
    public void eventLongOk(){
        super.eventLongOk();
//#ifndef WMUC
        if (contact instanceof MucContact && contact.origin==Contact.ORIGIN_GROUPCHAT) {
            Reply();
            return;
        }
//#endif
        keyGreen();
    }
    
    public void keyGreen(){
        if (!sd.roster.isLoggedIn()) return;
        
//#ifdef RUNNING_MESSAGE
//#         sd.roster.me=new MessageEdit(display, this, contact, contact.msgSuspended);
//#else
        new MessageEdit(display, this, contact, contact.msgSuspended);
//#endif
        contact.msgSuspended=null;
    }
    
    protected void keyClear(){
        if (!messages.isEmpty())
            clearReadedMessageList();
    }
    
    public void keyRepeated(int keyCode) {
        if (keyCode==KEY_NUM0) 
            clearReadedMessageList();
	else 
            super.keyRepeated(keyCode);
    }

    public void keyPressed(int keyCode) {
        //kHold=0;
        if (keyCode==KEY_POUND) {
//#ifndef WMUC
            if (contact instanceof MucContact && contact.origin==Contact.ORIGIN_GROUPCHAT) {
                Reply();
                return;
            }
//#endif
//#ifdef JUICK
//#             if (isJuickContact()) {
//#                 Msg msg = getMessage(cursor);
//#                 String body;
//# 
//#                 if (msg != null) {
//#                     body = getMessage(cursor).body;
//#                 } else {
//#                     body = "";
//#                 } msg = null;
//# 
//#                 if (getNumberJuickPostOrComment(body).equals("toThings"))
//#                     viewJuickThings(body);
//#                 else
//#                     juickReply(body);
//#                 return;
//#             }
//#endif
            keyGreen();
            return;
        }
        super.keyPressed(keyCode);
    }

    public void userKeyPressed(int keyCode) {
        switch (keyCode) {
            case KEY_NUM4:
                if (cf.useTabs)
                    sd.roster.searchActiveContact(-1); //previous contact with messages
                else
                    super.pageLeft();
                break;
            case KEY_NUM6:
                if (cf.useTabs)
                    sd.roster.searchActiveContact(1); //next contact with messages
                else
                    super.pageRight();
                break;
            case KEY_NUM3:
                new ActiveContacts(display, this, contact);
                break;        
            case KEY_NUM9:
                Quote();
                break;
        }
    }

//#ifdef MENU_LISTENER
    public void touchRightPressed(){ if (cf.oldSE) showMenu(); else destroyView(); }
    public void touchLeftPressed(){ if (cf.oldSE) keyGreen(); else showMenu(); }
//#endif
    
    private void Reply() {
        if (!sd.roster.isLoggedIn()) return;
        
        try {
            Msg msg=getMessage(cursor);
            
            if (msg==null || msg.messageType == Msg.MESSAGE_TYPE_OUT || msg.messageType == Msg.MESSAGE_TYPE_SUBJ) {
                keyGreen();
            } else {
//#ifdef RUNNING_MESSAGE
//#                 sd.roster.me=new MessageEdit(display, this, contact, msg.from+": ");
//#else
                new MessageEdit(display, this, contact, msg.from+": ");
//#endif
            }
        } catch (Exception e) {/*no messages*/}
    }
    
    private void Quote() {
        if (!sd.roster.isLoggedIn()) return;
        
        try {
            String msg=new StringBuffer()
                .append((char)0xbb) //
                .append(" ")
                .append(getMessage(cursor).quoteString())
                .append("\n")
                .toString();
//#ifdef RUNNING_MESSAGE
//#             sd.roster.me=new MessageEdit(display, this, contact, msg);
//#else
            new MessageEdit(display, this, contact, msg);
//#endif
            msg=null;
        } catch (Exception e) {/*no messages*/}
    }
    
//#ifdef HISTORY
//#ifdef LAST_MESSAGES
//#     public void loadRecentList() {
//#         contact.setHistoryLoaded(true);
//#         HistoryStorage hs = new HistoryStorage(contact.bareJid);
//#         Vector history=hs.importData();
//# 
//#         for (Enumeration messages2=history.elements(); messages2.hasMoreElements(); )  {
//#             Msg message=(Msg) messages2.nextElement();
//#             if (!isMsgExists(message)) {
//#                 message.history=true;
//#                 contact.msgs.insertElementAt(message, 0);
//#             }
//#             message=null;
//#         }
//#         history=null;
//#     }
//# 
//#     private boolean isMsgExists(Msg msg) {
//#          for (Enumeration messages=contact.msgs.elements(); messages.hasMoreElements(); )  {
//#             Msg message=(Msg) messages.nextElement();
//#             if (message.body.equals(msg.body)) {
//#                 return true;
//#             }
//#             message=null;
//#          }
//#         return false;
//#     }
//#endif
//# 
//#     private void saveMessages() {
//#         StringBuffer histRecord=new StringBuffer("chatlog_");
//#ifndef WMUC
//#         if (contact instanceof MucContact) {
//#             if (contact.origin>=Contact.ORIGIN_GROUPCHAT) {
//#                 histRecord.append(contact.bareJid);
//#             } else {
//#                 String nick=contact.getJid();
//#                 int rp=nick.indexOf('/');
//#                 histRecord.append(nick.substring(rp+1)).append("_").append(nick.substring(0, rp));
//#                 nick=null;
//#             }
//#         } else {
//#endif
//#             histRecord.append(contact.bareJid);
//#ifndef WMUC
//#         }
//#endif
//#         StringBuffer messageList=new StringBuffer();
//#         if (startSelection) {
//#             for (Enumeration select=contact.msgs.elements(); select.hasMoreElements(); ) {
//#                 Msg mess=(Msg) select.nextElement();
//#                 if (mess.selected) {
//#                     messageList.append(mess.quoteString()).append("\n").append("\n");
//#                     mess.selected=false;
//#                     mess.highlite = mess.oldHighlite;
//#                 }
//#             }
//#             startSelection = false;
//#         } else {
//#             for (Enumeration cmessages=contact.msgs.elements(); cmessages.hasMoreElements(); ) {
//#                 Msg message=(Msg) cmessages.nextElement();
//#                 messageList.append(message.quoteString()).append("\n").append("\n");
//#             }
//#         }
//#         HistoryAppend.getInstance().addMessageList(messageList.toString(), histRecord.toString());
//#         messageList=null;
//#         histRecord=null;
//#     }
//#endif
    
    public final void smartPurge() {
        Vector msgs=contact.msgs;
        int cur=cursor+1;
        try {
            if (msgs.size()>0){
                int virtCursor=msgs.size();
                boolean delete = false;
                int i=msgs.size();
                while (true) {
                    if (i<0) break;

                    if (i<cur) {
                        if (!delete) {
                            //System.out.println("not found else");
                            if (((Msg)msgs.elementAt(virtCursor)).dateGmt+1000<System.currentTimeMillis()) {
                                //System.out.println("can delete: "+ delPos);
                                msgs.removeElementAt(virtCursor);
                                //delPos--;
                                delete=true;
                            }
                        } else {
                            //System.out.println("delete: "+ delPos);
                            msgs.removeElementAt(virtCursor);
                            //delPos--;
                        }
                    }
                    virtCursor--;
                    i--;
                }
                contact.activeMessage=msgs.size()-1; //drop activeMessage count
            }
        } catch (Exception e) { }
        
        contact.clearVCard();
        
        contact.lastSendedMessage=null;
        contact.lastUnread=0;
        contact.resetNewMsgCnt();
    }

    public void destroyView(){
        /*
        if (startSelection) {
            for (Enumeration select=contact.msgs.elements(); select.hasMoreElements(); ) {
                Msg mess=(Msg) select.nextElement();

                mess.selected=false;
                mess.highlite = mess.oldHighlite;
            }
            startSelection = false;
        }
        */
        sd.roster.activeContact=null;
        sd.roster.reEnumRoster(); //to reset unread messages icon for this conference in roster
        if (display!=null) display.setCurrent(sd.roster);
    }

//#ifndef MENU_LISTENER
//#         public void eventOk(){ // For Juick without MENU_LISTENER, but possibly for other commands need this
//#         super.eventOk();
//#         commandState(); // Если переделывать на событие открытия меню, то поменять местами вызовы ф-ий.
//#     }
//#endif

//#ifdef MENU_LISTENER
    public void showMenu() {
         commandState();
         super.showMenu();
    }

    public boolean isHasScheme() {
        if (contact.msgs.size()<1) {
            return false;
        }
        String body=((Msg) contact.msgs.elementAt(cursor)).body;

        if (body.indexOf("xmlSkin")>-1) return true;
        return false;
    }

    public boolean isHasUrl() {
        if (contact.msgs.size()<1) {
            return false;
        }
        String body=((Msg) contact.msgs.elementAt(cursor)).body;
        if (body.indexOf("http://")>-1) return true;
        if (body.indexOf("https://")>-1) return true;
        if (body.indexOf("ftp://")>-1) return true;
        if (body.indexOf("tel:")>-1) return true;
        if (body.indexOf("native:")>-1) return true;
        return false;
    }

    public String touchLeftCommand(){ return (cf.oldSE)?((contact.msgSuspended!=null)?SR.MS_RESUME:SR.MS_NEW):SR.MS_MENU; }
    public String touchRightCommand(){ return (cf.oldSE)?SR.MS_MENU:SR.MS_BACK; }
//#endif
}
