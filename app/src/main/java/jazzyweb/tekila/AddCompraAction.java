package jazzyweb.tekila;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Usuario;
import jazzyweb.tekila.widget.ParticipantesAdapter;
import jazzyweb.tekila.widget.SelectUsuariosYCantidadAdapter;
import jazzyweb.tekila.widget.UsuariosResumenAdapter;


public class AddCompraAction extends Activity {

    private List<Usuario> usuariosParaPagos;
    private List<Usuario> usuariosParaParticipaciones;
    private List<Usuario> usuariosPagosSeleccionados;
    private List<Usuario> usuariosPagosSeleccionadosPrev;
    private List<Usuario> usuariosParticipaSeleccionados;
    private List<Usuario> usuariosParticipaSeleccionadosPrev;

    private Long idGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compra);

        Bundle b = getIntent().getExtras();
        idGrupo = b.getLong("idGrupo");
        usuariosParaPagos = getUsuariosFromGrupo(idGrupo);
        usuariosParaParticipaciones = Usuario.clone(usuariosParaPagos);
        usuariosParticipaSeleccionados = Usuario.clone(usuariosParaPagos);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final TextView btnQuienPaga = (TextView) findViewById(R.id.btnQuienPaga);
        final TextView btnQuienParticipa = (TextView) findViewById(R.id.btnQuienParticipa);

        btnQuienPaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getResources().getString(R.string.label_compra_quien_paga);

                SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener listener = new SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener() {
                    @Override
                    public void onUsuariosSelectedChange(List<Usuario> result) {
                        usuariosPagosSeleccionados = result;
                        usuariosPagosSeleccionadosPrev = Usuario.clone(usuariosPagosSeleccionados);
                    }

                    public void resetUsuariosSeleccionados(){
                        usuariosPagosSeleccionados = Usuario.clone(usuariosPagosSeleccionadosPrev);
                    }

                };

                SelectUsuariosYCantidadAdapter adapter = new  SelectUsuariosYCantidadAdapter(AddCompraAction.this, R.layout.dialog_usuarios_y_cantidad, usuariosParaPagos, usuariosPagosSeleccionados);

                SelectUsuariosYCantidadDialogFragment dialog =
                        SelectUsuariosYCantidadDialogFragment.newInstance(title, listener, adapter);

                dialog.show(getFragmentManager(),"tag");
            }
        });

        btnQuienParticipa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getResources().getString(R.string.label_compra_quien_participa);
                SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener listener =
                        new SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener() {
                            @Override
                            public void onUsuariosSelectedChange(List<Usuario> result) {
                                usuariosParticipaSeleccionados = result;
                                usuariosParticipaSeleccionadosPrev = Usuario.clone(usuariosParticipaSeleccionados);
                            }

                            public void resetUsuariosSeleccionados(){
                                usuariosParticipaSeleccionados = Usuario.clone(usuariosParticipaSeleccionadosPrev);
                            }
                        };
                ParticipantesAdapter adapter = new ParticipantesAdapter(AddCompraAction.this, R.layout.dialog_usuarios_y_cantidad, usuariosParaParticipaciones, usuariosParticipaSeleccionados);

                SelectUsuariosYCantidadDialogFragment dialog =
                        SelectUsuariosYCantidadDialogFragment.newInstance(title, listener, adapter);

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

    private List<Usuario> getUsuariosFromGrupo(Long idGrupo){
        DataBaseManager dataBaseManager = new DataBaseManager(this);
        dataBaseManager.open();

        List<Usuario> usuarios = dataBaseManager.getUsuariosFromGrupo(idGrupo);

        return usuarios;
    }
}