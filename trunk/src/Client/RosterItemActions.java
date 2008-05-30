/*
 * RosterItemActions.java
 *
 * Created on 11.12.2005, 19:05
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
import Colors.ColorTheme;
//#ifndef WMUC
import Conference.ConferenceGroup;
import Conference.InviteForm;
import Conference.MucContact;
import Conference.QueryConfigForm;
import Conference.affiliation.ConferenceQuickPrivelegeModify;
//#ifdef REQUEST_VOICE
//# import Conference.QueryRequestVoice;
//#endif
import Conference.affiliation.Affiliations;
//#endif
//#ifdef SERVICE_DISCOVERY
import ServiceDiscovery.ServiceDiscovery;
//#endif

import images.ActionsIcons;

import ui.controls.AlertBox;

import xmpp.extensions.IqLast;
import xmpp.extensions.IqPing;
import xmpp.extensions.IqTimeReply;
import xmpp.extensions.IqVersionReply;

import com.alsutton.jabber.datablocks.Presence;
//#if FILE_TRANSFER
import io.file.transfer.TransferSendFile;
//#endif
import java.util.Enumeration;
import javax.microedition.lcdui.Display;
import locale.SR;
import ui.Menu;
import ui.MenuItem;
import ui.Time;
//#ifdef CLIPBOARD
//# import util.ClipBoard;
//#endif

import vcard.VCard;
import vcard.VCardEdit;

/**
 *
 * @author EvgS
 */
public class RosterItemActions extends Menu {

    Object item;
//#ifdef CLIPBOARD
//#     private ClipBoard clipboard;
//#endif
    private int action;
    
    StaticData sd=StaticData.getInstance();
    
    /** Creates a new instance of RosterItemActions */
    public RosterItemActions(Display display, Object item, int action) {
	super(item.toString(), ActionsIcons.getInstance());
	this.item=item;
        this.action=action;
        
        if (!sd.roster.isLoggedIn()) return;
	
        if (item==null) return;
        boolean isContact=( item instanceof Contact );
//#ifdef CLIPBOARD
//#         if (Config.getInstance().useClipBoard)
//#             clipboard=ClipBoard.getInstance();
//#endif
	if (isContact) {
	    Contact contact=(Contact)item;
	    if (contact.getGroupType()==Groups.TYPE_TRANSP) {
		addItem(SR.MS_LOGON,5, ActionsIcons.ICON_ON);
		addItem(SR.MS_LOGOFF,6, ActionsIcons.ICON_OFF);
                addItem(SR.MS_RESOLVE_NICKNAMES, 7, ActionsIcons.ICON_NICK_RESOLVE);
//#if CHANGE_TRANSPORT
//#                 addItem("Change transport", 915);
//#endif
	    }
	    addItem(SR.MS_VCARD,1, ActionsIcons.ICON_VCARD);
//#ifdef POPUPS
            addItem(SR.MS_INFO,86, ActionsIcons.ICON_INFO);
//#endif
            addItem(SR.MS_CLIENT_INFO,0, ActionsIcons.ICON_VERSION);
//#ifdef SERVICE_DISCOVERY
	    addItem(SR.MS_COMMANDS,30, ActionsIcons.ICON_COMMAND);
//#endif
//#ifdef CLIPBOARD
//#             if (Config.getInstance().useClipBoard) {
//#                 addItem(SR.MS_SEND_BUFFER,914, ActionsIcons.ICON_SEND_BUFFER);
//#                 if (contact.getGroupType()!=Groups.TYPE_SELF) {
//#                     addItem(SR.MS_COPY_JID,892, ActionsIcons.ICON_COPY_JID);
//#                 }
//#             }
//#endif
            addItem(SR.MS_SEND_COLOR_SCHEME, 912, ActionsIcons.ICON_SEND_COLORS);
            if (contact.status<Presence.PRESENCE_OFFLINE) {
                addItem(SR.MS_TIME,891, ActionsIcons.ICON_TIME);
                addItem(SR.MS_IDLE,889, ActionsIcons.ICON_IDLE);
                addItem(SR.MS_PING,893, ActionsIcons.ICON_PING);
            }
	    
	    if (contact.getGroupType()!=Groups.TYPE_SELF && contact.getGroupType()!=Groups.TYPE_SEARCH_RESULT && contact.origin<Contact.ORIGIN_GROUPCHAT) {
		if (contact.status<Presence.PRESENCE_OFFLINE) {
                    addItem(SR.MS_ONLINE_TIME,890, ActionsIcons.ICON_ONLINE);    
                } else {
                    addItem(SR.MS_SEEN,890, ActionsIcons.ICON_ONLINE); 
                }
                if (contact.getGroupType()!=Groups.TYPE_TRANSP) {
                    addItem(SR.MS_EDIT,2, ActionsIcons.ICON_RENAME);
                }
		addItem(SR.MS_SUBSCRIPTION,3, ActionsIcons.ICON_SUBSCR);
		addItem(SR.MS_MOVE,1003, ActionsIcons.ICON_MOVE);
		addItem(SR.MS_DELETE, 4, ActionsIcons.ICON_DELETE);
		addItem(SR.MS_DIRECT_PRESENCE,45, ActionsIcons.ICON_SET_STATUS);
	    }
	    if (contact.origin==Contact.ORIGIN_GROUPCHAT) 
                return;
//#ifndef WMUC
            boolean onlineConferences=false;
            for (Enumeration cI=sd.roster.getHContacts().elements(); cI.hasMoreElements(); ) {
                try {
                    MucContact mcI=(MucContact)cI.nextElement();
                    if (mcI.origin==Contact.ORIGIN_GROUPCHAT && mcI.status==Presence.PRESENCE_ONLINE)
                        onlineConferences=true;
                } catch (Exception e) {}
            }
            
            if (contact instanceof MucContact) {
                MucContact selfContact= ((ConferenceGroup) contact.getGroup()).getSelfContact();
                MucContact mc=(MucContact) contact;
                
                //invite
                if (mc.realJid!=null) {
                    if (onlineConferences)
                        addItem(SR.MS_INVITE,40, ActionsIcons.ICON_INVITE);
                }
                //invite
                
                int myAffiliation=selfContact.affiliationCode;
                if (myAffiliation==MucContact.AFFILIATION_OWNER) 
                    myAffiliation++; // allow owner to change owner's affiliation

            
                //addItem(SR.MS_TIME,891); 
                
                if (selfContact.roleCode==MucContact.ROLE_MODERATOR) {
                    if(mc.roleCode<MucContact.ROLE_MODERATOR)
                        addItem(SR.MS_KICK,8, ActionsIcons.ICON_KICK);
                    
                    if (myAffiliation>=MucContact.AFFILIATION_ADMIN && mc.affiliationCode<myAffiliation)
                        addItem(SR.MS_BAN,9, ActionsIcons.ICON_BAN);
                    
                    if (mc.affiliationCode<MucContact.AFFILIATION_ADMIN) 
                        /* 5.1.1 *** A moderator MUST NOT be able to revoke voice privileges from an admin or owner. */ 
                    if (mc.roleCode==MucContact.ROLE_VISITOR) addItem(SR.MS_GRANT_VOICE,31, ActionsIcons.ICON_VOICE);
                    else addItem(SR.MS_REVOKE_VOICE, 32, ActionsIcons.ICON_DEVOICE);
                }
//#ifdef REQUEST_VOICE
//# 		if (selfContact.roleCode==MucContact.ROLE_VISITOR) {
//#                     //System.out.println("im visitor");
//#                     if (mc.roleCode==MucContact.ROLE_MODERATOR) {
//#                         //System.out.println(mc.getJid()+" is a moderator");
//#                         addItem(SR.MS_REQUEST_PARTICIPANT_ROLE,39);
//#                     }
//#  		}
//#endif
                if (myAffiliation>=MucContact.AFFILIATION_ADMIN) {
                    // admin use cases
                    
                    //roles
                    if (mc.affiliationCode<MucContact.AFFILIATION_ADMIN) 
                        /* 5.2.1 ** An admin or owner MUST NOT be able to revoke moderation privileges from another admin or owner. */ 
                    if (mc.roleCode==MucContact.ROLE_MODERATOR) 
                        addItem(SR.MS_REVOKE_MODERATOR,31, ActionsIcons.ICON_MEMBER);
                    else 
                        addItem(SR.MS_GRANT_MODERATOR,33, ActionsIcons.ICON_ADMIN);
                    
                    //affiliations
                    if (mc.affiliationCode<myAffiliation) {
                        if (mc.affiliationCode!=MucContact.AFFILIATION_NONE) 
                            addItem(SR.MS_UNAFFILIATE,36, ActionsIcons.ICON_DEMEMBER);
                        /* 5.2.2 */
                        if (mc.affiliationCode!=MucContact.AFFILIATION_MEMBER) 
                            addItem(SR.MS_GRANT_MEMBERSHIP,35, ActionsIcons.ICON_MEMBER);
                    }
                    
                    
               //m.addItem(new MenuItem("Set Affiliation",15));
                }
                if (myAffiliation>=MucContact.AFFILIATION_OWNER) {
                    // owner use cases
                    if (mc.affiliationCode!=MucContact.AFFILIATION_ADMIN) 
                        addItem(SR.MS_GRANT_ADMIN,37, ActionsIcons.ICON_ADMIN);
                    
                    if (mc.affiliationCode!=MucContact.AFFILIATION_OWNER) 
                        addItem(SR.MS_GRANT_OWNERSHIP,38, ActionsIcons.ICON_OWNER);
                }
                if (mc.realJid!=null && mc.getStatus()<Presence.PRESENCE_OFFLINE) {
                    
                }
            } else if (contact.getGroupType()!=Groups.TYPE_TRANSP && contact.getGroupType()!=Groups.TYPE_SEARCH_RESULT) {
                // usual contact - invite item check
                 if (onlineConferences) 
                     addItem(SR.MS_INVITE,40, ActionsIcons.ICON_INVITE);
            }
//#endif
//#if (FILE_IO && FILE_TRANSFER)
            if (contact.getGroupType()!=Groups.TYPE_TRANSP) 
                if (contact!=sd.roster.selfContact())
                    addItem(SR.MS_SEND_FILE, 50, ActionsIcons.ICON_SEND_FILE);
            
//#endif
        } else {
	    Group group=(Group)item;
	    if (group.type==Groups.TYPE_SEARCH_RESULT)
		addItem(SR.MS_DISCARD,21, ActionsIcons.ICON_BAN);
//#ifndef WMUC
	    if (group instanceof ConferenceGroup) {
		MucContact self=((ConferenceGroup)group).getSelfContact();
                
		addItem(SR.MS_LEAVE_ROOM,22, ActionsIcons.ICON_LEAVE);
                
                if (self.status>=Presence.PRESENCE_OFFLINE) {// offline or error
		    addItem(SR.MS_REENTER,23, ActionsIcons.ICON_CHANGE_NICK);
                } else {
                    addItem(SR.MS_DIRECT_PRESENCE,46, ActionsIcons.ICON_SET_STATUS);
                    addItem(SR.MS_CHANGE_NICKNAME,23, ActionsIcons.ICON_CHANGE_NICK);
		    if (self.affiliationCode>=MucContact.AFFILIATION_OWNER) {
			addItem(SR.MS_CONFIG_ROOM,10, ActionsIcons.ICON_CONFIGURE);
                    }
		    if (self.affiliationCode>=MucContact.AFFILIATION_ADMIN) {
			addItem(SR.MS_OWNERS,11, ActionsIcons.ICON_OWNERS);
			addItem(SR.MS_ADMINS,12, ActionsIcons.ICON_ADMINS);
			addItem(SR.MS_MEMBERS,13, ActionsIcons.ICON_MEMBERS);
			addItem(SR.MS_BANNED,14, ActionsIcons.ICON_OUTCASTS);
 		    }
 		}
	    } else {
//#endif
                if (    group.type!=Groups.TYPE_IGNORE
                        && group.type!=Groups.TYPE_NOT_IN_LIST
                        && group.type!=Groups.TYPE_SEARCH_RESULT
                        && group.type!=Groups.TYPE_SELF
                        && group.type!=Groups.TYPE_TRANSP)
                {
                    addItem(SR.MS_RENAME,1001, ActionsIcons.ICON_RENAME);
                    addItem(SR.MS_DELETE, 1004, ActionsIcons.ICON_DELETE);
                }
//#ifndef WMUC
            }
//#endif
 	}
	if (getItemCount()>0) {
            if (action<0) 
                attachDisplay(display);
            else try {
                this.display=display; // to invoke dialog Y/N
                doAction(action);
            } catch (Exception e) { 
                //e.printStackTrace();
            }
        }
     }
     
     public void eventOk(){
         try {
             MenuItem me=(MenuItem) getFocusedObject();
            destroyView();
            if (me==null) return;
            int index=action=me.index;
            doAction(index);
        } catch (Exception e) { }
    }

    private void doAction(final int index) {
        boolean isContact=( item instanceof Contact );
        Contact c = null;
        Group g = null;
        if (isContact) c=(Contact)item; else g=(Group) item;
        
        String to=null;
        if (isContact) to=(index<3)? c.getJid() : c.getBareJid();
            switch (index) {
                case 0: // version
                    sd.roster.setQuerySign(true);
                    sd.roster.theStream.send(IqVersionReply.query(to));
                    break;
                case 86: // info
//#ifdef POPUPS
                    sd.roster.showInfo();
//#endif
                    break;
                case 1: // vCard
                    if (c.vcard!=null) {
                        new VCardEdit(display, c.vcard);
                        return;
                    }
                    VCard.request(c.getBareJid(), c.getJid());
                    break;
                case 2:
                    (new ContactEdit(display, c )).parentView=sd.roster;
                    return; //break;
                case 3: //subscription
                    new SubscriptionEdit(display, c);
                    return; //break;
                case 4:
                    new AlertBox(SR.MS_DELETE_ASK, c.getNickJid(), display,  sd.roster) {
                        public void yes() {
                            sd.roster.deleteContact((Contact)item);
                        }
                        public void no() { }
                    };
                    return;
                case 6: // logoff
                    sd.roster.blockNotify(-111,10000); //block sounds to 10 sec
                    Presence presence = new Presence(
                    Presence.PRESENCE_OFFLINE, -1, "", null);
                    presence.setTo(c.getJid());
                    sd.roster.theStream.send( presence );
                    break;
                case 5: // logon
                    sd.roster.blockNotify(-111,10000); //block sounds to 10 sec
                    //querysign=true; displayStatus();
                    Presence presence2 = new Presence(sd.roster.myStatus, 0, "", null);
                    presence2.setTo(c.getJid());
                    sd.roster.theStream.send( presence2 );
                    break;
                case 7: // Nick resolver
                    sd.roster.resolveNicknames(c.getBareJid());
                    break;
//#if CHANGE_TRANSPORT
//#                 case 915: // change transport
//#                     new ChangeTransport(display, c.getBareJid());
//#                     return;
//#endif
                case 21:
                    sd.roster.cleanupSearch();
                    break;
//#ifdef SERVICE_DISCOVERY
                case 30:
                    new ServiceDiscovery(display, c.getJid(), "http://jabber.org/protocol/commands");
                    return;
//#endif
                case 1003: 
                    new RenameGroup(display, null, c);
                    return;
                case 889: //idle
                    sd.roster.setQuerySign(true);
                    sd.roster.theStream.send(IqLast.query(c.getJid()));
                    break;
                case 890: //seen & online
                    sd.roster.setQuerySign(true);
                    sd.roster.theStream.send(IqLast.query(c.getBareJid()));
                    break;
                case 891: //time
                    sd.roster.setQuerySign(true);
                    sd.roster.theStream.send(IqTimeReply.query(c.getJid()));
                    break;
//#ifdef CLIPBOARD
//#                 case 892: //Copy JID
//#ifndef WMUC
//#                     if (!(c instanceof MucContact)) {
//#endif
//#                         try {
//#                             if (c.bareJid!=null)
//#                                 clipboard.setClipBoard(c.bareJid);
//#                         } catch (Exception e) {/*no messages*/}
//#ifndef WMUC
//#                     }
//#endif
//#                     break;
//#endif
                case 893: //ping
                    try {
                        sd.roster.setQuerySign(true);
                        //c.setPing();
                        sd.roster.theStream.send(IqPing.query(c.getJid(), "_ping_"+Time.utcTimeMillis()));
                    } catch (Exception e) {/*no messages*/}
                    break;
                case 912: //send color scheme
                    String from=sd.account.toString();
                    String body=ColorTheme.getInstance().getSkin();
                    String subj="";

                    String id=String.valueOf((int) System.currentTimeMillis());

                    Msg msg=new Msg(Msg.MESSAGE_TYPE_OUT,from,subj,body);
                    msg.id=id;

                    try {
                    sd.roster.sendMessage(c, id, body, subj, null);
                        c.addMessage(new Msg(Msg.MESSAGE_TYPE_OUT,from,subj,"scheme sended"));
                    } catch (Exception e) {
                        c.addMessage(new Msg(Msg.MESSAGE_TYPE_OUT,from,subj,"scheme NOT sended"));
                        //e.printStackTrace();
                    }
                break;
//#ifdef CLIPBOARD
//#                 case 914: //send message from buffer
//#                     String body2=clipboard.getClipBoard();
//#                     if (body2.length()==0)
//#                         return;
//#                     
//#                     String from2=sd.account.toString();
//#                     
//#                     String id2=String.valueOf((int) System.currentTimeMillis());
//#                     
//#                     Msg msg2=new Msg(Msg.MESSAGE_TYPE_OUT,from2,null,body2);
//#                     msg2.id=id2;
//#                     try {
//#                         sd.roster.sendMessage(c, id2, body2, null, null);
//#                         c.addMessage(new Msg(Msg.MESSAGE_TYPE_OUT,from2,null,"message sended from clipboard("+body2.length()+"chars)"));
//#                     } catch (Exception e) {
//#                         c.addMessage(new Msg(Msg.MESSAGE_TYPE_OUT,from2,null,"message NOT sended"));
//#                         //e.printStackTrace();
//#                     }
//#                     break;
//#endif
//#ifndef WMUC
                case 40: //invite
                    //new InviteForm(c, display);
                    if (c.jid!=null) {
                        new InviteForm(c, display);                        
                    } else {
                        MucContact mcJ=(MucContact) c;

                        if (mcJ.realJid!=null) {
                            boolean onlineConferences=false;
                            for (Enumeration cJ=sd.roster.getHContacts().elements(); cJ.hasMoreElements(); ) {
                                try {
                                    MucContact mcN=(MucContact)cJ.nextElement();
                                    if (mcN.origin==Contact.ORIGIN_GROUPCHAT && mcN.status==Presence.PRESENCE_ONLINE)
                                        onlineConferences=true;
                                } catch (Exception e) {}
                            }
                            if (onlineConferences) new InviteForm(mcJ, display);
                        }
                    }
                    return;
//#endif
                case 45: //direct presence
                    (new StatusSelect(display, c)).setParentView(sd.roster);
                    return;
//#if (FILE_IO && FILE_TRANSFER)
                case 50: //send file
                    new TransferSendFile(display, c.getJid());
                    return;
//#endif
            }
//#ifndef WMUC
            if (c instanceof MucContact || g instanceof ConferenceGroup) {
                MucContact mc=(MucContact) c;
                
                String roomJid="";
                if (g instanceof ConferenceGroup) {
                    roomJid=((ConferenceGroup)g).getConference().getJid();
                }
                
                String myNick="";
                if (c instanceof MucContact) {
                    myNick=((ConferenceGroup)c.getGroup()).getSelfContact().getName();
                }
                
                switch (index) { // muc contact actions
                    case 10: // room config
                        new QueryConfigForm(display, roomJid);
                        break;
                    case 11: // owners
                    case 12: // admins
                    case 13: // members
                    case 14: // outcasts
                        new Affiliations(display, roomJid, (short)(index-10));
                        return;
                    case 22:
                        sd.roster.leaveRoom( g );
                        break;
                    case 23:
                        sd.roster.reEnterRoom( g );
                        return; //break;
                    case 46: //conference presence
                        new StatusSelect(display, ((ConferenceGroup)g).getConference()).setParentView(sd.roster);
                        return;
                     case 8: // kick
                        new ConferenceQuickPrivelegeModify(display, mc, ConferenceQuickPrivelegeModify.KICK,myNick);
                        return;
                     case 9: // ban
                        new ConferenceQuickPrivelegeModify(display, mc, ConferenceQuickPrivelegeModify.OUTCAST,myNick);
                        return;
                     case 31: //grant voice and revoke moderator
                        new ConferenceQuickPrivelegeModify(null, mc, ConferenceQuickPrivelegeModify.PARTICIPANT,null);
                        return;
                     case 32: //revoke voice
                        new ConferenceQuickPrivelegeModify(display, mc, ConferenceQuickPrivelegeModify.VISITOR,null);
                        return;
                     case 33: //grant moderator
                        new ConferenceQuickPrivelegeModify(null, mc, ConferenceQuickPrivelegeModify.MODERATOR,null);
                        return;
                    case 35: //grant membership and revoke admin
                        new ConferenceQuickPrivelegeModify(null, mc, ConferenceQuickPrivelegeModify.MEMBER,null);
                        return;
                    case 36: //revoke membership
                        new ConferenceQuickPrivelegeModify(null, mc, ConferenceQuickPrivelegeModify.NONE,null);
                         return;
                    case 37: //grant admin and revoke owner
                        new ConferenceQuickPrivelegeModify(null, mc, ConferenceQuickPrivelegeModify.ADMIN,null);
                        return;
                    case 38: //grant owner
                        new ConferenceQuickPrivelegeModify(null, mc, ConferenceQuickPrivelegeModify.OWNER,null);
                        return;
//#ifdef REQUEST_VOICE		 
//#                 case 39: //request voice
//#                     new QueryRequestVoice(display, mc, ConferenceQuickPrivelegeModify.PARTICIPANT);
//#                     return;
//#endif
//#ifdef CLIPBOARD
//#                     case 892: //Copy JID
//#                         try {
//#                             if (mc.realJid!=null)
//#                                 clipboard.setClipBoard(mc.realJid);
//#                         } catch (Exception e) {}
//#                         break;
//#endif
             }
        } else {
//#endif
            Group sg=(Group)item;

            if (       sg.type!=Groups.TYPE_IGNORE 
                    && sg.type!=Groups.TYPE_NOT_IN_LIST
                    && sg.type!=Groups.TYPE_SEARCH_RESULT
                    && sg.type!=Groups.TYPE_SELF
                    && sg.type!=Groups.TYPE_TRANSP)
            {
                switch (index) {
                    case 1001: //rename
                        new RenameGroup(display, sg, null);
                        return;
                    case 1004: //delete
                        new AlertBox(SR.MS_DELETE_ASK, sg.getName(), display, sd.roster) {
                            public void yes() {
                                sd.roster.deleteGroup((Group)item);
                            }
                            public void no() {
                            }
                        };
                        return;
                }
            }
//#ifndef WMUC
         }
//#endif
     }
}
