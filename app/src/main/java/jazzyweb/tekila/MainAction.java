package jazzyweb.tekila;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import jazzyweb.tekila.db.LoadTestData;


public class MainAction extends Activity {

    private Long idGrupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idGrupo = Long.valueOf(1);

        setContentView(R.layout.activity_main);

        final ActionBar tabBar = getActionBar();
        tabBar.setNavigationMode(tabBar.NAVIGATION_MODE_TABS);

        ComprasFragment comprasFragment = ComprasFragment.newInstance(idGrupo);
        ResumenFragment resumenFragment = ResumenFragment.newInstance(idGrupo);
        DeudasFragment deudasFragment = DeudasFragment.newInstance("kuku", "kaka");

        tabBar.addTab(tabBar.newTab().setText("Compras").setTabListener(new TabListener(comprasFragment)));
        tabBar.addTab(tabBar.newTab().setText("Resumen").setTabListener(new TabListener(resumenFragment)));
        tabBar.addTab(tabBar.newTab().setText("Deudas").setTabListener(new TabListener(deudasFragment)));
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
            try {
                LoadTestData ld = new LoadTestData("datatest.json", this);
                ld.load();
            }catch (Exception e){
                throw new Error("no puedo cargar datos de prueba");
            }
            return true;
        }else if(id == R.id.action_add_compra){
            Intent intent = new Intent(this, AddOrEditCompraAction.class);
            Bundle b = new Bundle();
            b.putLong("idGrupo", idGrupo );
            intent.putExtras(b);
            startActivity(intent);
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
