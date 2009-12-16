/*
 * EventNotify.java
 *
 * Created on 3.03.2005, 23:37
 *
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
 */

package ui;
import javax.microedition.lcdui.*;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 *
 * @author Eugene Stahov
 */
public class EventNotify 
        implements Runnable
//#ifndef NOSOUND
	, PlayerListener
//#endif
{
    
    private int vibraLength;
    private boolean toneSequence;
    private String soundName;
    private String soundType;
    
    private Display display;

    private static Player player;
    
    private final static String tone="A6E6J6";
    private int sndVolume;

    //private boolean flashBackLight;
    
    /** Creates a new instance of EventNotify */
    public EventNotify(
	Display display, 
	String soundMediaType, 
	String soundFileName, 
	int sndVolume,
	int vibraLength)
    {
        this.display=display;
        this.soundName=soundFileName;
        this.soundType=soundMediaType;
        this.vibraLength=vibraLength;
        //this.flashBackLight=flashBackLight;
        if (soundType!=null) 
            toneSequence= soundType.equals("tone");
        this.sndVolume=sndVolume;
    }
    
    public void startNotify (){
        release();
        //try { if (flashBackLight) display.flashBacklight(1000); } catch (Exception e2) { /* e.printStackTrace(); */}
//#ifndef NOSOUND
//#         if (soundName!=null) {
//#             try {
//#                 InputStream is = getClass().getResourceAsStream(soundName);
//#                 player = Manager.createPlayer(is, soundType);
//#
//#                 player.addPlayerListener(this);
//#                 player.realize();
//#                 player.prefetch();
//#
//#                 try {
//#                     VolumeControl vol=(VolumeControl) player.getControl("VolumeControl");
//#                     vol.setLevel(sndVolume);
//#                 } catch (Exception e) { /* e.printStackTrace(); */}
//#
//#                 player.start();
//#             } catch (Exception e) { }
//#         }
//#endif

        if (vibraLength>0) {
            display.vibrate(vibraLength);
        }

	if (toneSequence)
            new Thread(this).start();
    }
    
    public void run(){
        try {
            for (int i=0; i<tone.length(); ) {
                int note=(tone.charAt(i++)-'A')+12*(tone.charAt(i++)-'0');
                int duration=150;
                Manager.playTone(note, duration, sndVolume);
                Thread.sleep(duration);
            }
        } catch (Exception e) { }
    }
    
    public synchronized void release(){
//#ifndef NOSOUND
//#         if (player!=null) {
//# 	    player.removePlayerListener(this);
//# 	    player.close();
//# 	}
//#         player=null;
//#endif
    }
    
    public void playerUpdate(Player player, String string, Object object) {
	if (string.equals(PlayerListener.END_OF_MEDIA)) {    release(); }
    }
}
