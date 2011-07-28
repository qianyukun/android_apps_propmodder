package com.n00bware.propmodder.activities;

import com.n00bware.propmodder.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.Object;
import java.lang.Process;

public class propmodder extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

   /*
    * Strings for the PropModder
    */
    private static final String GENERAL_CATEGORY = "general_category";
    private static String TAG = "PropModder";

   /*
    *Strings for wifi_scan
    */
    private static final String WIFI_SCAN_PREF = "pref_wifi_scan_interval";
    private static final String WIFI_SCAN_PROP = "wifi.supplicant_scan_interval";
    private static final String WIFI_SCAN_PERSIST_PROP = "persist.wifi_scan_interval";
    private static final String WIFI_SCAN_DEFAULT = System.getProperty("wifi.supplicant_scan_interval");

   /*
    * Strings for lcd_density
    */
    private static final String LCD_DENSITY_PREF = "pref_lcd_density";
    private static final String LCD_DENSITY_PROP = "ro.sf.lcd_density";
    private static final String LCD_DENSITY_PERSIST_PROP = "persist.lcd_density";
    private static final String LCD_DENSITY_DEFAULT = System.getProperty("ro.sf.lcd_density");

   /*
    * Strings for max_events
    */
    private static final String MAX_EVENTS_PREF = "pref_max_events";
    private static final String MAX_EVENTS_PROP = "windowsmgr.max_events_per_sec";
    private static final String MAX_EVENTS_PERSIST_PROP = "persist.max_events";
    private static final String MAX_EVENTS_DEFAULT = System.getProperty("windowsmgr.max_events_per_sec");

   /*
    * Strings for usb_mode
    */
    private static final String USB_MODE_PREF = "pref_usb_mode";
    private static final String USB_MODE_PROP = "ro.default_usb_mode";
    private static final String USB_MODE_PERSIST_PROP = "persist.usb_mode";
    private static final String USB_MODE_DEFAULT = System.getProperty("ro.default_usb_mode");

   /*
    * Strings for ring_delay
    */
    private static final String RING_DELAY_PREF = "pref_ring_delay";
    private static final String RING_DELAY_PROP = "ro.telephony.call_ring.delay";
    private static final String RING_DELAY_PERSIST_PROP = "persist.call_ring.delay";
    private static final String RING_DELAY_DEFAULT = System.getProperty("ro.telephony.call_ring");

   /*
    * Strings for vm_heapsize
    */
    private static final String VM_HEAPSIZE_PREF = "pref_vm_heapsize";
    private static final String VM_HEAPSIZE_PROP = "dalvik.vm.heapsize";
    private static final String VM_HEAPSIZE_PERSIST_PROP = "persist.vm_heapsize";
    private static final String VM_HEAPSIZE_DEFAULT = System.getProperty("dalvik.vm.heapsize");

   /*
    * Strings for modversion
    *
    *private static final String MODVERSION_PREF = "pref_modversion";
    *private static final String MODVERSION_PROP = "ro.modversion";
    *private static final String MODVERSION_PERSIST_PROP = "persist.modversion";
    *private static final String MODVERSION_DEFAULT = System.getProperty("ro.modversion");
    */

    private ListPreference mWifiScanPref;
    private ListPreference mLcdDensityPref;
    private ListPreference mMaxEventsPref;
    private ListPreference mUsbModePref;
    private ListPreference mRingDelayPref;
    private ListPreference mVmHeapsizePref;
    //private ListPreference mModVersionPref;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
     /* Log program as loading 
      * TODO Set logging to proper channels ie info debug error
      * TODO all logging is currently set to Log.i
      */ 
        Log.i(TAG, "loading PropModder");
       super.onCreate(savedInstanceState);

        setTitle(R.string.main_title_subhead);
        addPreferencesFromResource(R.xml.propmodder_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        PreferenceCategory generalCategory = (PreferenceCategory)prefSet.findPreference(GENERAL_CATEGORY);

        mWifiScanPref = (ListPreference) prefSet.findPreference(WIFI_SCAN_PREF);
        mWifiScanPref.setValue(SystemProperties.get(WIFI_SCAN_PERSIST_PROP,
                SystemProperties.get(WIFI_SCAN_PROP, WIFI_SCAN_DEFAULT)));
        mWifiScanPref.setOnPreferenceChangeListener(this);
        Log.i(TAG, "loaded mWifiScanPref");

        mLcdDensityPref = (ListPreference) prefSet.findPreference(LCD_DENSITY_PREF);
        mLcdDensityPref.setValue(SystemProperties.get(LCD_DENSITY_PERSIST_PROP,
                SystemProperties.get(LCD_DENSITY_PROP, LCD_DENSITY_DEFAULT)));
        mLcdDensityPref.setOnPreferenceChangeListener(this);
        Log.i(TAG, "loaded mLcdDensityPref");

        mMaxEventsPref = (ListPreference) prefSet.findPreference(MAX_EVENTS_PREF);
        mMaxEventsPref.setValue(SystemProperties.get(MAX_EVENTS_PERSIST_PROP,
                SystemProperties.get(MAX_EVENTS_PROP, MAX_EVENTS_DEFAULT)));
        mMaxEventsPref.setOnPreferenceChangeListener(this);
        Log.i(TAG, "loaded mMaxEventsPref");

        mUsbModePref = (ListPreference) prefSet.findPreference(USB_MODE_PREF);
        mUsbModePref.setValue(SystemProperties.get(USB_MODE_PERSIST_PROP,
                SystemProperties.get(USB_MODE_PROP, USB_MODE_DEFAULT)));
        mUsbModePref.setOnPreferenceChangeListener(this);
        Log.i(TAG, "loaded mUsbModePref");

        mRingDelayPref = (ListPreference) prefSet.findPreference(RING_DELAY_PREF);
        mRingDelayPref.setValue(SystemProperties.get(RING_DELAY_PERSIST_PROP,
                SystemProperties.get(RING_DELAY_PROP, RING_DELAY_DEFAULT)));
        mRingDelayPref.setOnPreferenceChangeListener(this);
        Log.i(TAG, "loaded mRingDelayPref");

        mVmHeapsizePref = (ListPreference) prefSet.findPreference(VM_HEAPSIZE_PREF);
        mVmHeapsizePref.setValue(SystemProperties.get(VM_HEAPSIZE_PERSIST_PROP,
                SystemProperties.get(VM_HEAPSIZE_PROP, VM_HEAPSIZE_DEFAULT)));
        mVmHeapsizePref.setOnPreferenceChangeListener(this);
        Log.i(TAG, "loaded mVmHeapsizePref");
        }

     /*
      * TODO: We don't want to use ListPreferece this should be a text box entry
      *
      * mModVersionPref = (ListPreference) prefSet.findPreference(MODVERSION_PREF);
      * mModVersionPref.setValue(SystemProperties.get(MODVERSION_PERSIST_PROP;
      *         SystemProperties.get(MODVERSION_PROP, MODVERSION_DEFAILT)));
      * mModVersionPref.setOnPreferenceChangeListener(this);
      */

      /*
       * TODO our warning isn't working; we can just fix later not our biggest concern right now
       *alertDialog = new AlertDialog.Builder(this).create();
       *alertDialog.setTitle(R.string.propmodder_warning_title);
       *alertDialog.setMessage(getResources().getString(R.string.propmodder_warning));
       *alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
       *        getResources().getString(com.android.internal.R.string.ok),
       *        new DialogInterface.OnClickListener() {
       *    public void onClick(DialogInterface dialog, int which) {
       *        return;
       *    }
       *});
       *alertDialog.show();       
       */

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue != null) {
          Log.i(TAG, "new preference selected: newValue");
            if (preference == mWifiScanPref) {
                Log.i(TAG, "init mWifiScanPref get wifi_scan_persist_prop");
                SystemProperties.set(WIFI_SCAN_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/wifi.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("wifi.supplicant_scan_interval ") ||
                        params[0].equalsIgnoreCase("wifi.supplicant_scan_interval")) {
                        out.println("wifi.supplicant_scan_interval=" + newValue);
                        Log.i(TAG, "Wifi Policy wrote to /tmp/wifi.prop");
                    } else {
                        out.println(line);
                        Log.i(TAG, "Wifi Policy not set");
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Log.i(TAG, "requesting root shell");
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/wifi.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                } catch (Exception e) {
                Log.i(TAG, "root operations failed", e);
            }
                return true;
            }

            else if (preference == mLcdDensityPref) {
                SystemProperties.set(LCD_DENSITY_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/lcd.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("ro.sf.lcd_density ") ||
                        params[0].equalsIgnoreCase("ro.sf.lcd_density")) {
                        out.println("ro.sf.lcd_density=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/lcd.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
            }

            else if (preference == mMaxEventsPref) {
                SystemProperties.set(MAX_EVENTS_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/events.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("windowsmgr.max_events_per_sec ") ||
                        params[0].equalsIgnoreCase("windowsmgr.max_events_per_sec")) {
                        out.println("windowsmgr.max_events_per_sec=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/events.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
            }

            else if (preference == mUsbModePref) {
                SystemProperties.set(USB_MODE_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/usb.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("ro.default_usb_mode ") ||
                        params[0].equalsIgnoreCase("ro.default_usb_mode")) {
                        out.println("ro.default_usb_mode=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/usb.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
            }

            else if (preference == mRingDelayPref) {
                SystemProperties.set(RING_DELAY_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/ring.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("ro.telephony.call_ring.delay ") ||
                        params[0].equalsIgnoreCase("ro.telephony.call_ring.delay")) {
                        out.println("ro.telephony.call_ring.delay=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/ring.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
        }

            else if (preference == mVmHeapsizePref) {
                SystemProperties.set(VM_HEAPSIZE_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/heapsize.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("dalvik.vm.heapsize ") ||
                        params[0].equalsIgnoreCase("dalvik.vm.heapsize")) {
                        out.println("dalvik.vm.heapsize=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/heapsize.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
        }

        return false;
        }
    return false;
    }
}
