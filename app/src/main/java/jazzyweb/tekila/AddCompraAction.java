package jazzyweb.tekila;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import jazzyweb.tekila.model.Usuario;


public class AddCompraAction extends Activity implements QuienPagaDialogFragment.OnUsuariosSelectedChangeListener, QuienParticipaDialogFragment.OnUsuariosSelectedChangeListener {

    private List<Usuario> usuariosSeleccionados;

    private Long idGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compra);

        Bundle b = getIntent().getExtras();
        idGrupo = b.getLong("idGrupo");

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        final TextView lblQuienPaga = (TextView) findViewById(R.id.lblQuienLoHaPagado);
        final TextView lblQuienParticipa = (TextView) findViewById(R.id.lblQuienParticipa);

        lblQuienPaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuienPagaDialogFragment dialog = new QuienPagaDialogFragment();

                Bundle b = new Bundle();
                b.putLong("idGrupo", idGrupo);

                dialog.setArguments(b);
                dialog.setUsuariosSeleccionados(usuariosSeleccionados);

                dialog.show(getFragmentManager(),"tag");
            }
        });

        lblQuienParticipa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuienParticipaDialogFragment dialog = QuienParticipaDialogFragment.newInstance(idGrupo, usuariosSeleccionados);
                dialog.show(getFragmentManager(),"tag");
            }
        });
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


    @Override
    public void onUsuariosSelectedChange(List<Usuario> uSeleccionados) {
        usuariosSeleccionados = uSeleccionados;
    }
}