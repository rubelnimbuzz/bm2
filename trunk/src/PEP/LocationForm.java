/*
 * Location.java
 *
 * Created on 19 Апрель 2010 г., 1:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package PEP;
//#ifdef PEP_LOCATION
//# import Client.StaticData;
//# import com.alsutton.jabber.JabberDataBlock;
//# import com.alsutton.jabber.datablocks.Iq;
//# import javax.microedition.lcdui.Display;
//# import javax.microedition.lcdui.Displayable;
//# import javax.microedition.lcdui.TextField;
//# import javax.microedition.location.Coordinates;
//# import javax.microedition.location.Location;
//# import javax.microedition.location.LocationListener;
//# import javax.microedition.location.LocationProvider;
//# import locale.SR;
//# import ui.controls.AlertBox;
//# import ui.controls.form.DefForm;
//# import ui.controls.form.LinkString;
//# import ui.controls.form.SimpleString;
//# import ui.controls.form.TextInput;
//# 
//# /**
//#  *
//#  * @author Vitaly
//#  */
//# public class LocationForm extends DefForm implements LocationListener {
//#ifdef PLUGINS
//#     public static String plugin = "PLUGIN_PEP";
//#endif
//# 
//#     SimpleString loc;
//#     TextInput location, descr;
//#     TextInput lat, lon;
//#     LinkString detect;
//# 
//#     /** Creates a new instance of Location
//#      * @param display
//#      * @param parent
//#      */
//#     public LocationForm(Display display, Displayable parent) {
//#         super(display, parent, "Publish location");
//#         location = new TextInput(display, "Location name", null, null, TextField.ANY);
//#         descr = new TextInput(display, "Location description", null, null, TextField.ANY);
//#         lat = new TextInput(display, "Latitude", null, null, TextField.DECIMAL);
//#         lon = new TextInput(display, "Longitude", null, null, TextField.DECIMAL);
//#         detect = new LinkString("Retrieve location") {
//#             public void doAction() {
//#                 detectLocation();
//#             }
//#         };
//#         itemsList.addElement(lat);
//#         itemsList.addElement(lon);
//#         itemsList.addElement(location);
//#         itemsList.addElement(descr);
//#         itemsList.addElement(detect);
//#         
//#         attachDisplay(display);
//#         parentView = parent;
//#     }
//# 
//#     public void detectLocation() {
//#         new GeoRetriever(this).start();
//#     }
//# 
//#     public void locationUpdated(LocationProvider lp, Location lctn) {
//#         Coordinates c;
//#         if (lctn != null && (c = lctn.getQualifiedCoordinates()) != null) {
//#             lat.setValue(String.valueOf(c.getLatitude()));
//#             lon.setValue(String.valueOf(c.getLongitude()));
//#         } else {
//#             new AlertBox(SR.MS_ERROR, "Error retrieving coordinates", display, this) {
//#                 public void yes() {}
//#                 public void no() {}
//#             };
//#         }
//#         redraw();
//#     }
//# 
//#     public void providerStateChanged(LocationProvider lp, int i) {
//#     }
//# 
//#     public void cmdOk() {
//#         String sid="publish-location";
//#         JabberDataBlock setActivity=new Iq(null, Iq.TYPE_SET, sid);
//#         JabberDataBlock action=setActivity.addChildNs("pubsub", "http://jabber.org/protocol/pubsub") .addChild("publish", null);
//#         action.setAttribute("node", "http://jabber.org/protocol/geoloc");
//#         JabberDataBlock item=action.addChild("item", null);
//#         JabberDataBlock geoloc=item.addChildNs("geoloc", "http://jabber.org/protocol/geoloc");
//#         try {
//#             geoloc.addChild("lat", lat.getValue());
//#             geoloc.addChild("lon", lon.getValue());
//#             //todo: refactor theStream call; send notification to JabberBlockListener if stream was terminated
//#             StaticData.getInstance().roster.theStream.addBlockListener(new PepPublishResult(display, sid));
//#             StaticData.getInstance().roster.theStream.send(setActivity);
//#         } catch (Exception e) {e.printStackTrace(); }
//#         destroyView();
//#     }
//#ifdef MENU_LISTENER
//#     public String touchLeftCommand() { return SR.MS_PUBLISH; }
//#endif
//# }
//# 
//# /**
//#  *
//#  * @author ugnich
//#  */
//# class GeoRetriever extends Thread {
//# 
//#     private LocationListener returnto;
//# 
//#     public GeoRetriever(LocationListener returnto) {
//#         this.returnto = returnto;
//#     }
//# 
//#     public void run() {
//#         try {
//#             retrieveLocation();
//#         } catch (Exception ex) {
//#             ex.printStackTrace();
//#             returnto.locationUpdated(null, null);
//#         }
//#     }
//# 
//#     public void retrieveLocation() throws Exception {
//#         LocationProvider lp = LocationProvider.getInstance(null);
//#         Location l = lp.getLocation(60);
//#         returnto.locationUpdated(lp, l);
//#     }
//# }
//#endif
