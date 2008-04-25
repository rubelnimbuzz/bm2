/*
 * vGauge.java
 *
 * Created on 21 ������ 2008 �., 13:16
 * Copyright (c) 2006-2007, Daniel Apatin (ad), http://apatin.net.ru
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

package ui.controls;

import Client.StaticData;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import locale.SR;
import ui.FontCache;
//#ifdef GRADIENT
//# import ui.Gradient;
//#endif
/**
 *
 * @author ad
 */
public abstract class vGauge
        extends Canvas
        implements Runnable, CommandListener {
    
    protected Display display;
    protected Displayable next;
    protected Command cmdOk=new Command(SR.MS_OK, Command.OK, 1);
    protected Command cmdCancel=new Command(SR.MS_CANCEL, Command.BACK, 2);
    
    boolean vibrate;
    
    private boolean isShowing;
    private int timeout;

    private int value;
    
    Font f;

    private String text;
    private String mainbar;
    
//#ifdef GRADIENT
//#     private Gradient gr=null;
//#endif
        
    public vGauge(String mainbar, String text, int timeout, Display display, Displayable nextDisplayable) {
        this.display=display;
        f=FontCache.getBalloonFont();
        next=(nextDisplayable==null)? display.getCurrent() : nextDisplayable;
        
        this.mainbar=mainbar;
        this.text=text;
        this.timeout=timeout;

        isShowing=true;
        
        addCommand(cmdOk);
        addCommand(cmdCancel);

        setCommandListener(this);
        display.setCurrent(this);
        
        if (timeout>0)
            new Thread(this).start();
    }
    
    public void commandAction(Command command, Displayable displayable) {
        if (command==cmdOk) {
            doAction();
        }
        destroyView();
    }
    
    public void destroyView()	{
        isShowing=false;
        removeCommand(cmdOk);
        removeCommand(cmdCancel);
        display.setCurrent(next);
    }
    
    public void run() {
        while (isShowing) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) { break; }
            value+=1;
            if (value>=timeout) {
                if (vibrate) display.vibrate(1000);
                //System.out.println("execute");
                doAction();
                destroyView();
                break;
            }
            repaint();
            if (vibrate) display.vibrate(200);
        }
    }

    protected void paint(Graphics g) {
        if (isShowing) {
            String caption=text;

            caption+=" - "+(timeout-value);
            
            int width=getWidth();
            int height=getHeight();

            int border=10;
            int y=height/2;
            int xt=(width/2);

            int oldColor=g.getColor();
            g.setColor(0xffffff);
            g.fillRect(0,0, width, height); //fill back

            g.setFont(f);
            int fh=f.getHeight();
            g.setClip(0,0, width, fh);
//#ifdef GRADIENT
//#             if (gr==null) {
//#                 gr=new Gradient(0, 0, width, fh, 0x4c7300, 0x78ad10, false);
//#             }
//#             gr.paint(g);
//#else
         g.setColor(0x4c7300);
         g.fillRect(0, 0, width, fh);
//#endif
            g.setColor(0xffffff);
            g.drawString(mainbar, xt, 0, Graphics.TOP|Graphics.HCENTER);
            g.setClip(0,0, width, height);
            
            if (timeout>0) {
                int itemWidth=width-(border*2);
                int itemHeight=5;
                int filled=(itemWidth*value)/timeout;
                g.fillRect(border, y, itemWidth, itemHeight);
                g.setColor(0x668866);
                g.drawRect(border, y, itemWidth, itemHeight);
                g.fillRect(border, y, filled, itemHeight);
            }
            g.setColor(0x668866);
            int yt=y-f.getHeight();
            g.drawString(caption, xt, yt, Graphics.TOP|Graphics.HCENTER);
            
            g.setColor(oldColor);
        }
    }
    
    public abstract void doAction();
    
}
