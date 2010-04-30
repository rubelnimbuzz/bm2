/*
 * NativeCanvas.java
 *
 * Created on 31 Август 2009 г., 19:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ui;

import Client.Config;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Vladimir Krukov
 */
public class VirtualCanvas extends Canvas {
    private VirtualList list;
    
    /** Creates a new instance of NativeCanvas */
    public VirtualCanvas() {
        setFullScreenMode(Config.fullscreen);
    }
    public void show(VirtualList virtualList) {
        if (this == midlet.BombusMod.getInstance().getDisplay().getCurrent()
                && isShown()) {
            if (null != list) {
                list.hideNotify();
            }
            list = virtualList;
            list.showNotify();
            repaint();
            //midlet.BombusMod.getInstance().getDisplay().setCurrent(this);
            
        } else {
            list = virtualList;
            midlet.BombusMod.getInstance().getDisplay().setCurrent(this);
        }
        
    }
    public VirtualList getList() {
        return list;
    }

    protected void paint(Graphics graphics) {
        list.paint(graphics);
    }
    protected void keyPressed(int keyCode) {
        list.keyPressed(keyCode);           
    }
    protected final void keyRepeated(int keyCode){
        list.keyRepeated(keyCode);           
    }
    protected void keyReleased(int keyCode) {
        list.keyReleased(keyCode);           
    }
    
    protected void pointerPressed(int x, int y) {
        list.pointerPressed(x, y);        
    }
    protected void pointerDragged(int x, int y) {
        list.pointerDragged(x, y);         
    }
    protected void pointerReleased(int x, int y) {
        list.pointerReleased(x, y);           
    }

    protected void showNotify() {
        list.showNotify();        
    }
    protected void hideNotify() {
        if (list != null) {
            list.hideNotify();
        }
    }
    
    protected void sizeChanged(int w, int h) {
        if (list != null)
            list.sizeChanged(w, h);
    }
    public static final VirtualCanvas nativeCanvas = new VirtualCanvas();

}
