package in.omerjerk.greyscalenexus;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by Umair Khan <omerjerk@gmail.com>
 */
public class MainActivity extends Activity {

    private ContentResolver contentResolver;

    private static final String INSTALL_SCRIPT =
            "mount -o rw,remount /system\n" +
                    "cat %s > /system/priv-app/GreyScaleNexus.apk.tmp\n" +
                    "chmod 644 /system/priv-app/GreyScaleNexus.apk.tmp\n" +
                    "pm uninstall %s\n" +
                    "mv /system/priv-app/GreyScaleNexus.apk.tmp /system/priv-app/GreyScaleNexus.apk\n" +
                    "pm install -r /system/priv-app/GreyScaleNexus.apk\n" +
                    "sleep 5\n" +
                    "am start -n in.omerjerk.greyscalenexus/.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentResolver = getContentResolver();
    }

    public void installToSystem (View v) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Shell.SU.run(String.format(INSTALL_SCRIPT,
                        new String[] {
                                MainActivity.this.getPackageCodePath(),
                                MainActivity.this.getPackageName()
                        }));
                return null;
            }
        }.execute();
    }

    public void doTheMagic (View v) {
        pourPixieDust();
    }

    public void removeTheMagic (View v) {
        removePixieDust();
    }

    private void pourPixieDust() {
        Settings.Secure.putInt(contentResolver, "accessibility_display_daltonizer_enabled", 1);
        Settings.Secure.putInt(contentResolver, "accessibility_display_daltonizer", 0);
    }

    private void removePixieDust() {
        Settings.Secure.putInt(contentResolver, "accessibility_display_daltonizer_enabled", 0);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
