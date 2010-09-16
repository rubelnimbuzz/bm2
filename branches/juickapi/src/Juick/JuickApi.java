/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Juick;

import Client.Msg;
import Client.StaticData;
import com.alsutton.jabber.JabberBlockListener;
import com.alsutton.jabber.JabberDataBlock;
import com.alsutton.jabber.datablocks.Iq;
import com.alsutton.jabber.datablocks.Message;
import com.alsutton.jabber.datablocks.Presence;
import java.util.Vector;

/**
 *
 * @author Vitaly
 */
public class JuickApi implements JabberBlockListener {
    public static String NS_JUICK_MESSAGE = "http://juick.com/message";
    public static String NS_JUICK_USER = "http://juick.com/user";
    public static String NS_JUICK_SUBSCRIPTIONS = "http://juick.com/subscriptions";
    public static String NS_JUICK_QUERY = "http://juick.com/query";

    public int blockArrived(JabberDataBlock data) {
        JabberDataBlock result = null;
        if (data instanceof Presence)
            return BLOCK_REJECTED;
        if (data instanceof Message) {
            // new Juick message
            if (data.findNamespace("juick", NS_JUICK_MESSAGE) != null) {
                StaticData.getInstance().roster.errorLog("Juick message: " + data.toString());
                return BLOCK_PROCESSED;
            }
            return BLOCK_REJECTED;
        }
        if (data instanceof Iq) {
            if (data.getTypeAttribute().equals("result"))
                result = data.findNamespace("query", NS_JUICK_QUERY + "#messages");
                if (result != null) {
                     Vector messages = result.getChildBlocks();
                    for (int i=0; i < messages.size(); i++) {
                        JabberDataBlock message = (JabberDataBlock)messages.elementAt(i);
                        String from = message.findNamespace("user", NS_JUICK_USER).getAttribute("uname");
                        Msg msg = new Msg(Msg.MESSAGE_TYPE_IN, from, from, message.getChildBlockText("body"));
                        msg.setDayTime(message.getAttribute("ts"));
                        msg.juickMid = Integer.parseInt(message.getAttribute("mid"));
                        try {
                        msg.replies = Integer.parseInt(message.getAttribute("replies"));
                        if (msg.replies > 0) {
                            msg.body = msg.body.concat("\n " + String.valueOf(msg.replies) + " replies");
                        }
                        } catch (Exception e) {}
                        StaticData.getInstance().roster.blog.messageArrived(msg);
                    }
                     return BLOCK_PROCESSED;
                }
            return BLOCK_REJECTED;
        }

        return BLOCK_REJECTED;
    }
    public static Iq queryMsgs(boolean myfeed, int msgid, boolean replies) {
            Iq result = new Iq(StaticData.getInstance().roster.getMainJuickContact().bareJid, Iq.TYPE_GET, Long.toString(System.currentTimeMillis()));
            JabberDataBlock query = result.addChildNs("query", JuickApi.NS_JUICK_QUERY + "#messages");
            if (myfeed)
                query.setAttribute("filter", "myfeed");
            if (msgid > 0)
                query.setAttribute("mid", String.valueOf(msgid));
            if (replies)
                query.setAttribute("rid", "*");
            return result;
        }   
    public static Iq queryMsg(int msgid, boolean replies) {
        return queryMsgs(false, msgid, replies);
    }

    public static Iq queryMsgs(boolean myfeed) {
        return queryMsgs(myfeed, 0, false);
    }


}
