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


public class AddCompraAction extends Activity implements SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener {

    private List<Usuario> usuariosPagosSeleccionados;
    private List<Usuario> usuariosParticipaSeleccionados;

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
                String title = getResources().getString(R.string.label_compra_quien_paga);
                SelectUsuariosYCantidadDialogFragment dialog =
                        SelectUsuariosYCantidadDialogFragment.newInstance(idGrupo, usuariosPagosSeleccionados, title, SelectUsuariosYCantidadDialogFragment.MODO_PAGO);
//                QuienPagaDialogFragment dialog = new QuienPagaDialogFragment();
//
//                Bundle b = new Bundle();
//                b.putLong("idGrupo", idGrupo);
//
//                dialog.setArguments(b);
//                dialog.setUsuariosSeleccionados(usuariosPagosSeleccionados);

                dialog.show(getFragmentManager(),"tag");
            }
        });

        lblQuienParticipa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getResources().getString(R.string.label_compra_quien_participa);
                SelectUsuariosYCantidadDialogFragment dialog =
                        SelectUsuariosYCantidadDialogFragment.newInstance(idGrupo, usuariosParticipaSeleccionados, title, SelectUsuariosYCantidadDialogFragment.MODO_PARTICIPACION);
//
//                QuienParticipaDialogFragment dialog = QuienParticipaDialogFragment.newInstance(idGrupo, usuariosParticipaSeleccionados);
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
    public void onUsuariosPagoSelectedChange(List<Usuario> uSeleccionados) {
        usuariosPagosSeleccionados = uSeleccionados;
    }

    @Override
    public void onUsuariosParticipaSelectedChange(List<Usuario> uSeleccionados) {
        usuariosParticipaSeleccionados = uSeleccionados;
    }
}