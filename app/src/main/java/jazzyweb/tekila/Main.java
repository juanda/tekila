package jazzyweb.tekila;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar tabBar = getActionBar();
        tabBar.setNavigationMode(tabBar.NAVIGATION_MODE_TABS);

        ArrayList<String> compras = new ArrayList<String>();
        compras.add("Hola");
        compras.add("Adios");
        ComprasFragment comprasFragment = ComprasFragment.newInstance(compras);
        DeudasFragment deudasFragment = DeudasFragment.newInstance("kuku", "kaka");
        ResumenFragment resumenFragment = ResumenFragment.newInstance("kuku", "kaka");

        tabBar.addTab(tabBar.newTab().setText("Compras").setTabListener(new TabListener(comprasFragment)));
        tabBar.addTab(tabBar.newTab().setText("Deudas").setTabListener(new TabListener(deudasFragment)));
        tabBar.addTab(tabBar.newTab().setText("Resumen").setTabListener(new TabListener(resumenFragment)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class TabListener implements ActionBar.TabListener {
        private static final String TAG = "TabListener";
        private final Fragment mFragment;
        public TabListener(Fragment fragment) {
            mFragment = fragment;
        }
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
        // When a tab is selected, change the currently visible Fragment
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (null != mFragment) {
                ft.replace(R.id.fragment_container, mFragment);
            }
        }
        // When a tab is unselected, remove the currently visible Fragment
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (null != mFragment)
                ft.remove(mFragment);
        }
    }
}
