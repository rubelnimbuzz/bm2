/*
 * ColorConfigForm.java
 *
 * Created on 25.05.2008, 14:05
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

package Colors;
//#if FILE_IO
import io.file.FileIO;
import io.file.browse.Browser;
import io.file.browse.BrowserListener;
//#endif
import Client.StaticData;
import java.util.Vector;
import locale.SR;
import ui.VirtualList;
import ui.controls.form.DropChoiceBox;
import ui.controls.form.DefForm;
import ui.controls.form.LinkString;
import ui.controls.form.SpacerItem;
import util.StringLoader;

/**
 *
 * @author ad
 */
public class ColorConfigForm 
        extends DefForm
//#if FILE_IO
        implements BrowserListener
//#endif
    {
    

//#ifdef COLOR_TUNE
    private LinkString configureColors;
//#endif
    private LinkString invertColors;
//#if FILE_IO
    private LinkString loadFromFile;
    private LinkString saveToFile;

    String filePath;
    private int loadType=0;
//#endif
    
    private Vector[] files;
    private DropChoiceBox skinFiles;
    private LinkString useFromJar;
    
    private LinkString reset;

    /** Creates a new instance of ColorConfigForm
     * @param pView
     */
    public ColorConfigForm(VirtualList pView) {
        super(SR.MS_COLOR_TUNE);        
//#ifdef COLOR_TUNE
//#ifdef PLUGINS
            if (StaticData.getInstance().Colors) {
//#endif
        final VirtualList returnTo = this;
        configureColors=new LinkString(SR.MS_COLOR_TUNE) { public void doAction() { new ColorsList( returnTo); } };
        itemsList.addElement(configureColors);
//#ifdef PLUGINS
            }
//#endif
//#endif
        invertColors=new LinkString(SR.MS_INVERT) { public void doAction() { ColorTheme.invertSkin(); } };
        itemsList.addElement(invertColors);
        
        itemsList.addElement(new SpacerItem(10));
        try {
            files=new StringLoader().stringLoader("/skins/res.txt",2);
            if (files[0].size()>0) {
                skinFiles=new DropChoiceBox(SR.MS_SELECT);
                for (int i=0; i<files[0].size(); i++) {
                    skinFiles.append((String)files[1].elementAt(i));
                }
                skinFiles.setSelectedIndex(0);
                itemsList.addElement(skinFiles);
                useFromJar=new LinkString(SR.MS_LOAD_SKIN) { public void doAction() { userThemeFromJar(); } };
                itemsList.addElement(useFromJar);
                itemsList.addElement(new SpacerItem(10));
            }
        } catch (Exception e) {}
        
//#if FILE_IO
        loadFromFile=new LinkString(SR.MS_LOAD_FROM_FILE) { public void doAction() { initBrowser(1); } };
        itemsList.addElement(loadFromFile);
        saveToFile=new LinkString(SR.MS_SAVE_TO_FILE) { public void doAction() { initBrowser(0); } };
        itemsList.addElement(saveToFile);
        itemsList.addElement(new SpacerItem(10));
//#endif
        
        reset=new LinkString(SR.MS_CLEAR) { public void doAction() { ColorTheme.init(); ColorTheme.saveToStorage(); } };
        itemsList.addElement(reset);
        
        moveCursorTo(getNextSelectableRef(-1));
        show(pView);
        this.parentView=pView;
    }
    
    public void cmdOk() {
        destroyView();
    }
   
    
//#if FILE_IO
    public void initBrowser(int type) {
        loadType=type; 
        if (type==0) {
            new Browser(null, this, this, true);
        } else if(type==1) {
            new Browser(null,  this, this, false);
        }
    }
//#endif
    
    public void userThemeFromJar(){
        try {
            if (skinFiles.getSelectedIndex()>-1) {
                ColorTheme.loadSkin((String)files[0].elementAt(skinFiles.getSelectedIndex()), 1);
            }
        } catch (Exception ex) {}
    }

//#if FILE_IO
    public void BrowserFilePathNotify(String pathSelected) {
        if (loadType==0) {
            FileIO file=FileIO.createConnection(pathSelected+"skin.txt");
            file.fileWrite(ColorTheme.getSkin().getBytes());
        } else {
            ColorTheme.loadSkin(pathSelected, 0);
        }
    }
//#endif
}
