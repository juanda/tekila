package jazzyweb.tekila.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.compras.AddOrEditCompraActivity;
import jazzyweb.tekila.compras.ComprasFragment;
import jazzyweb.tekila.db.LoadTestData;
import jazzyweb.tekila.db.ModelManager;
import jazzyweb.tekila.grupos.GrupoActivity;
import jazzyweb.tekila.grupos.GrupoAdapter;
import jazzyweb.tekila.model.Grupo;
import jazzyweb.tekila.usuarios.UsuarioActivity;


public class MainActivity extends Activity {

    private Long idGrupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle b = getIntent().getExtras();
//        idGrupo = b.getLong("idGrupo", 0);
        idGrupo = getIdGrupo();

        setContentView(R.layout.activity_main);

        final ActionBar tabBar = getActionBar();
        tabBar.setNavigationMode(tabBar.NAVIGATION_MODE_TABS);

        Grupo grupo = getGrupo(idGrupo);
        if(grupo == null){
            tabBar.setTitle(getResources().getString(R.string.app_name));
        }else{
            tabBar.setTitle(getResources().getString(R.string.app_name) + "-" + grupo.getNombre());
        }

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

        switch (id){
            case R.id.action_settings:
                try {
                    LoadTestData ld = new LoadTestData("datatest.json", this);
                    ld.load();
                }catch (Exception e){
                    throw new Error("no puedo cargar datos de prueba");
                }
                return true;
            case R.id.action_add_compra:
                startAddOrEditCompraActivity(idGrupo);
                return true;
            case R.id.action_grupos:
                startGruposActivity();
                return true;
            case R.id.action_usuarios:
                startUsuariosActivity();
                return true;
            case R.id.action_cambiar_grupo:
                showDialogCambiarDeGrupo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogCambiarDeGrupo(){

        ChoiceGrupoDialogFragment choiceGrupoFragment =
                ChoiceGrupoDialogFragment.newInstance(getResources().getString(R.string.label_selecciona_grupo));

        choiceGrupoFragment.show(getFragmentManager(), "choiceGrupo");
    }


    private void startAddOrEditCompraActivity(Long idGrupo){
        Intent intent = new Intent(this, AddOrEditCompraActivity.class);
        Bundle b = new Bundle();
        b.putLong("idGrupo", idGrupo);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void startUsuariosActivity(){
        Intent intent = new Intent(this, UsuarioActivity.class);
        Bundle b = new Bundle();
        b.putLong("idGrupo", idGrupo);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void startGruposActivity(){
        Intent intent = new Intent(this, GrupoActivity.class);
        Bundle b = new Bundle();
        b.putLong("idGrupo", idGrupo);
        intent.putExtras(b);
        startActivity(intent);
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

    private Long getIdGrupo(){

        ModelManager modelManager = new ModelManager(this);
        List<Grupo> grupos = modelManager.getGrupos();

        Long idGrupo = Long.valueOf(0);

        if(grupos.size() != 0){ // Si existe algún grupo
            SharedPreferences settings = getPreferences(0); // Pillalo de las preferencias
            idGrupo = settings.getLong("idGrupo", 0); // Si no hay aún preferencias id = 0
            Grupo grupoSeleccionado = modelManager.getGrupo(idGrupo);
            if(idGrupo == 0 || grupoSeleccionado == null){
                idGrupo = grupos.get(0).getId();
                SharedPreferences.Editor editor = settings.edit();
                editor.putLong("idGrupo", idGrupo);
                editor.commit();
            }
        }

        return idGrupo;
    }

    private Grupo getGrupo(Long idGrupo){
        ModelManager modelManager = new ModelManager(this);

        Grupo grupo = modelManager.getGrupo(idGrupo);

        return  grupo;
    }
}
