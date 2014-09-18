package jazzyweb.tekila;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Usuario;


public class AddCompraAction extends Activity {

    private List<Usuario> usuariosSeleccionados;

    public List<Usuario> getUsuariosSeleccionados() {
        return usuariosSeleccionados;
    }

    public void setUsuariosSeleccionados(List<Usuario> usuariosSeleccionados) {
        this.usuariosSeleccionados = usuariosSeleccionados;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compra);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        DataBaseManager dataBaseManager = new DataBaseManager(this);
        dataBaseManager.open();
        List<Usuario> usuarios = dataBaseManager.getUsuariosFromGrupo(Long.valueOf(1));


        final TextView lblQuienPaga = (TextView) findViewById(R.id.lblQuienLoHaPagado);
        final TextView lblQuienParticipa = (TextView) findViewById(R.id.lblQuienParticipa);
        lblQuienPaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCompraDialogFragment dialog = new AddCompraDialogFragment();
                dialog.setDialogResult(new AddCompraDialogFragment.OnAddCompraDialogResult() {
                    @Override
                    public void finish(List<Usuario> result) {
                        AddCompraAction.this.setUsuariosSeleccionados(result);
                        lblQuienPaga.setText(result.get(0).getNombre() + " " + result.get(0).getCantidadAux());
                    }
                });
                dialog.show(getFragmentManager(),"tag");
            }
        });


//        Spinner spQuienParticipa = (Spinner) findViewById(R.id.spQuienParticipa);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_compra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save_compra) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
