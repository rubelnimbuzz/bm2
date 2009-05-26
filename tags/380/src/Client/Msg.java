/*
 * msg.java
 *
 * Created on 6.01.2005, 19:20
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import Colors.ColorTheme;
import ui.Time;

/**
 *
 * @author Eugene Stahov
 */
public class Msg {
    // without signaling
    public final static int MESSAGE_TYPE_OUT=1;
    public final static int MESSAGE_TYPE_PRESENCE=2;
    public final static int MESSAGE_TYPE_HISTORY=3;
    // with signaling
    public final static int MESSAGE_TYPE_IN=10;
    public final static int MESSAGE_TYPE_HEADLINE=11;
    public final static int MESSAGE_TYPE_ERROR=12;
    public final static int MESSAGE_TYPE_SUBJ=13;
    public final static int MESSAGE_TYPE_AUTH=14;
    public final static int MESSAGE_TYPE_SYSTEM=15;

    private boolean highlite;
    private boolean history;
    
    public int messageType;
    public String from;
    public String subject;
    private String body;
    public long dateGmt;
    public boolean delivered;
    public String id;
    
    public boolean unread = false;

    public boolean itemCollapsed;
    public int itemHeight=-1;
    
    ColorTheme ct;
    
    /** Creates a new instance of msg */
    public Msg(int messageType, String from, String subj, String body) {
        this.messageType=messageType;
        this.from=from;
        this.body=body;
        this.subject=subj;
        this.dateGmt=Time.utcTimeMillis();
        if (messageType>=MESSAGE_TYPE_IN) unread=true;
        if (messageType==MESSAGE_TYPE_PRESENCE || messageType==MESSAGE_TYPE_HEADLINE)
            itemCollapsed=true;
        else if (body!=null && messageType!=MESSAGE_TYPE_SUBJ)
            if (body.length()>Config.getInstance().messageLimit)
                itemCollapsed=true;
        
        ct=ColorTheme.getInstance();
    }
    
    public void onSelect(){}

    public String getTime(){
        return Time.timeLocalString(dateGmt); 
    }
    public String getDayTime(){
        return Time.dayLocalString(dateGmt)+Time.timeLocalString(dateGmt); 
    }

    public void setDayTime(String date){
        this.dateGmt=Time.dateStringToLong(date);
    }
    
    public int getColor() {
        if (highlite) 
            return ct.getColor(ColorTheme.MSG_HIGHLIGHT);

        switch (messageType) {
            case MESSAGE_TYPE_IN: return ct.getColor(ColorTheme.MESSAGE_IN);
            case MESSAGE_TYPE_HEADLINE: return ct.getColor(ColorTheme.MESSAGE_IN);
            case MESSAGE_TYPE_OUT: return ct.getColor(ColorTheme.MESSAGE_OUT);
            case MESSAGE_TYPE_PRESENCE: return ct.getColor(ColorTheme.MESSAGE_PRESENCE);
            case MESSAGE_TYPE_AUTH: return ct.getColor(ColorTheme.MESSAGE_AUTH);
            case MESSAGE_TYPE_HISTORY: return ct.getColor(ColorTheme.MESSAGE_HISTORY);
            case MESSAGE_TYPE_SUBJ:return ct.getColor(ColorTheme.MSG_SUBJ);
        }
        return ct.getColor(ColorTheme.LIST_INK);
    }
    
    public String toString() {
        StringBuffer time=new StringBuffer();
        if (messageType==MESSAGE_TYPE_PRESENCE) {
            time.append("[").append(getTime()).append("] ");
        }
        time.append(body);
        return time.toString(); 
    }
    
    public boolean isPresence() { return messageType==MESSAGE_TYPE_PRESENCE; }
    
    public void serialize(DataOutputStream os) throws IOException {
	os.writeUTF(from);
	os.writeUTF(body);
	os.writeLong(dateGmt);
	if (subject!=null) os.writeUTF(subject);
    }
    
    public Msg (DataInputStream is) throws IOException {
	from=is.readUTF();
	body=is.readUTF();
	dateGmt=is.readLong();
        messageType=MESSAGE_TYPE_IN;
	try { subject=is.readUTF(); } catch (Exception e) { subject=null; }
    }

    public String getBody() { return body; }
    public String getSubject() { return subject; }

    void setHighlite(boolean state) { highlite=state; }
    public boolean isHighlited() { return highlite; }
    
    public boolean isHistory() { return history; }
    void setHistory(boolean state) { history=state; }
    
    public String quoteString(){
        StringBuffer out=new StringBuffer();
        if (subject!=null)
            out.append(subject).append("\n");
        out.append(body);
        int i=0;
        while (i<out.length()) {
            if (out.charAt(i)<0x03) out.deleteCharAt(i);
            else i++;
        }
        return out.toString();
    }
}