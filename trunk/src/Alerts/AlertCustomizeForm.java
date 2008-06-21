/*
 * AlertCustomizeForm.java
 *
 * Created on 26.05.2008, 13:12
 *
 * Copyright (c) 2006-2008, Daniel Apatin (ad), http://apatin.net.ru
 *
 * Copyright (c) 2006-2008, Daniel Apatin (ad), http://apatin.net.ru
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

package Alerts;

import Client.*;
import locale.SR;
import java.util.Vector;
import ui.EventNotify;
import ui.controls.form.SimpleString;
import ui.controls.form.CheckBox;
import ui.controls.form.DefForm;
import ui.controls.form.DropChoiceBox;
import ui.controls.form.SpacerItem;
import ui.controls.form.TrackItem;
import util.StringLoader;
import java.util.Enumeration;
import javax.microedition.lcdui.*;


public class AlertCustomizeForm 
        extends DefForm {
    private Display display;
    private Displayable parentView;

    private CheckBox statusBox;
    private CheckBox blinkBox;
    private CheckBox soundBox;
    
    private DropChoiceBox MessageFile;
    private DropChoiceBox OnlineFile;
    private DropChoiceBox OfflineFile;
    private DropChoiceBox ForYouFile;
    private DropChoiceBox ComposingFile;
    private DropChoiceBox ConferenceFile;
    private DropChoiceBox StartUpFile;
    private DropChoiceBox OutgoingFile;
    private DropChoiceBox VIPFile;
    
    private CheckBox vibrateOnlyHighlited;
    
    private CheckBox IQNotify;
    
    private TrackItem sndVol;
    
    AlertCustomize ac;
    Config cf;
    Vector files[];
    Vector fileNames;
//#ifndef MENU
    Command cmdTest=new Command(SR.MS_TEST_SOUND, Command.SCREEN, 2);
//#endif
    /** Creates a new instance of ConfigForm */
    public AlertCustomizeForm(Display display) {
        super(display, SR.MS_NOTICES_OPTIONS);
        this.display=display;
        parentView=display.getCurrent();
        
        ac=AlertCustomize.getInstance();
        cf=Config.getInstance();
        
        files=new StringLoader().stringLoader("/sounds/res.txt",3);
        fileNames=new Vector();
	for (Enumeration file=files[2].elements(); file.hasMoreElements(); ) {
            fileNames.addElement((String)file.nextElement());
	}
        
        itemsList.addElement(new SimpleString(SR.MS_MESSAGE_SOUND, true));
        MessageFile=new DropChoiceBox(display); MessageFile.items=fileNames; 
        MessageFile.setSelectedIndex(ac.soundsMsgIndex); itemsList.addElement(MessageFile);
        
        itemsList.addElement(new SimpleString(SR.MS_ONLINE_SOUND, true));
        OnlineFile=new DropChoiceBox(display); OnlineFile.items=fileNames; 
        OnlineFile.setSelectedIndex(ac.soundOnlineIndex); itemsList.addElement(OnlineFile);
        
        itemsList.addElement(new SimpleString(SR.MS_OFFLINE_SOUND, true));
        OfflineFile=new DropChoiceBox(display); OfflineFile.items=fileNames; 
        OfflineFile.setSelectedIndex(ac.soundOfflineIndex); itemsList.addElement(OfflineFile);
        
        itemsList.addElement(new SimpleString(SR.MS_MESSAGE_FOR_ME_SOUND, true));
        ForYouFile=new DropChoiceBox(display); ForYouFile.items=fileNames; 
        ForYouFile.setSelectedIndex(ac.soundForYouIndex); itemsList.addElement(ForYouFile);
        
        itemsList.addElement(new SimpleString(SR.MS_COMPOSING_SOUND, true));
        ComposingFile=new DropChoiceBox(display); ComposingFile.items=fileNames; 
        ComposingFile.setSelectedIndex(ac.soundComposingIndex); itemsList.addElement(ComposingFile);
        
        itemsList.addElement(new SimpleString(SR.MS_CONFERENCE_SOUND, true));
        ConferenceFile=new DropChoiceBox(display); ConferenceFile.items=fileNames; 
        ConferenceFile.setSelectedIndex(ac.soundConferenceIndex); itemsList.addElement(ConferenceFile);
        
        itemsList.addElement(new SimpleString(SR.MS_STARTUP_SOUND, true));
        StartUpFile=new DropChoiceBox(display); StartUpFile.items=fileNames; 
        StartUpFile.setSelectedIndex(ac.soundStartUpIndex); itemsList.addElement(StartUpFile);
        
        itemsList.addElement(new SimpleString(SR.MS_OUTGOING_SOUND, true));
        OutgoingFile=new DropChoiceBox(display); OutgoingFile.items=fileNames; 
        OutgoingFile.setSelectedIndex(ac.soundOutgoingIndex); itemsList.addElement(OutgoingFile);
        
        itemsList.addElement(new SimpleString(SR.MS_VIP_SOUND, true));
        VIPFile=new DropChoiceBox(display); VIPFile.items=fileNames; 
        VIPFile.setSelectedIndex(ac.soundVIPIndex); itemsList.addElement(VIPFile);

        itemsList.addElement(new SimpleString(SR.MS_SHOW_LAST_APPEARED_CONTACTS, true));
        statusBox=new CheckBox(SR.MS_STATUS, cf.notifyPicture); itemsList.addElement(statusBox);
        blinkBox=new CheckBox(SR.MS_BLINKING, cf.notifyBlink); itemsList.addElement(blinkBox);
        soundBox=new CheckBox(SR.MS_SOUND, cf.notifySound); itemsList.addElement(soundBox);
        
        itemsList.addElement(new SpacerItem(10));
        vibrateOnlyHighlited=new CheckBox(SR.MS_VIBRATE_ONLY_HIGHLITED, ac.vibrateOnlyHighlited); itemsList.addElement(vibrateOnlyHighlited);
        
        itemsList.addElement(new SimpleString(SR.MS_SOUND_VOLUME, true));
        sndVol=new TrackItem(ac.soundVol/10, 10);
        itemsList.addElement(sndVol);
        
        itemsList.addElement(new SpacerItem(10));
        IQNotify=new CheckBox(SR.MS_SHOW_IQ_REQUESTS, cf.IQNotify); itemsList.addElement(IQNotify);
        
//#ifndef MENU
        addCommand(cmdTest);
//#endif
        moveCursorTo(getNextSelectableRef(-1));
        attachDisplay(display);
    }
    
    public void cmdOk() {
        ac.soundsMsgIndex=MessageFile.getSelectedIndex();
        ac.soundVol=sndVol.getValue()*10;
        ac.soundOnlineIndex=OnlineFile.getSelectedIndex();
        ac.soundOfflineIndex=OfflineFile.getSelectedIndex();
        ac.soundForYouIndex=ForYouFile.getSelectedIndex();
        ac.soundComposingIndex=ComposingFile.getSelectedIndex();
        ac.soundConferenceIndex=ConferenceFile.getSelectedIndex(); 
        ac.soundStartUpIndex=StartUpFile.getSelectedIndex();
        ac.soundOutgoingIndex=OutgoingFile.getSelectedIndex(); 
        ac.soundVIPIndex=VIPFile.getSelectedIndex();
        
        ac.vibrateOnlyHighlited=vibrateOnlyHighlited.getValue();

        ac.loadSoundName();
        ac.loadOnlineSoundName();
        ac.loadOfflineSoundName();
        ac.loadForYouSoundName();
        ac.loadComposingSoundName();
        ac.loadConferenceSoundName();
        ac.loadStartUpSoundName();
        ac.loadOutgoingSoundName();
        ac.loadVIPSoundName();

        ac.saveToStorage();

        cf.notifyPicture=statusBox.getValue();
        cf.notifyBlink=blinkBox.getValue();
        cf.notifySound=soundBox.getValue();
        
        cf.IQNotify=IQNotify.getValue();

        cf.saveToStorage();

        destroyView();
    }
    
//#ifndef MENU
    public void commandAction(Command c, Displayable d) {
        if (c==cmdTest) {
            PlaySound();
            return;
        }
        super.commandAction(c, d);
    }
//#else
//#     public String getCenterCommand() { return (playable()>-1)?SR.MS_TEST_SOUND:""; }
//#     public void centerCommand() { System.out.println("test"); if (playable()>-1) PlaySound(); }
//#endif
    
    private int playable () {
        int ret=-1;
        switch (cursor) {
            case 1: //MessageFile
            case 3: //OnlineFile
            case 5: //OfflineFile
            case 7: //ForYouFile
            case 9: //ComposingFile
            case 11: //ConferenceFile
            case 13: //StartUpFile
            case 15: //OutgoingFile
            case 17: //VIPFile
                return cursor;
        }
        return -1;
    }
    
    private void PlaySound(){
        int sound=-1;
        switch (cursor) {
            case 1: //MessageFile
                sound=MessageFile.getSelectedIndex();
                break;
            case 3: //OnlineFile
                sound=OnlineFile.getSelectedIndex();
                break;
            case 5: //OfflineFile
                sound=OfflineFile.getSelectedIndex();
                break;
            case 7: //ForYouFile
                sound=ForYouFile.getSelectedIndex();
                break;
            case 9: //ComposingFile
                sound=ComposingFile.getSelectedIndex();
                break;
            case 11: //ConferenceFile
                sound=ConferenceFile.getSelectedIndex();
                break;
            case 13: //StartUpFile
                sound=StartUpFile.getSelectedIndex();
                break;
            case 15: //OutgoingFile
                sound=OutgoingFile.getSelectedIndex();
                break;
            case 17: //VIPFile
                sound=VIPFile.getSelectedIndex();
                break;
        }
        if (sound<0) return;
        
        String soundFile=(String)files[1].elementAt(sound);
        String soundType=(String)files[0].elementAt(sound);
        int soundVol=sndVol.getValue()*10;
        //System.out.println(sound+" "+soundFile+" "+soundType+" "+soundVol);
        new EventNotify(display, soundType, soundFile, soundVol, 0).startNotify();
    } 
}
