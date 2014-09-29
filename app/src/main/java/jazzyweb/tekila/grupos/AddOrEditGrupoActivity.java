package jazzyweb.tekila.grupos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.compras.ParticipantesAdapter;
import jazzyweb.tekila.compras.SelectUsuariosYCantidadAdapter;
import jazzyweb.tekila.compras.SelectUsuariosYCantidadDialogFragment;
import jazzyweb.tekila.db.ModelManager;
import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.model.Grupo;
import jazzyweb.tekila.model.Usuario;


public class AddOrEditGrupoActivity extends Activity {

    private Long idGrupo;
    private Long idGrupoToEdit;
    private EditText etxtNombreGrupo;
    private TextView lblUsuariosAsociados;

    private List<Usuario> usuariosSeleccionados;
    private List<Usuario> usuariosSeleccionadosPrev;
    private List<Usuario> usuariosParaSeleccionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_grupo);

        Bundle b = getIntent().getExtras();
        idGrupo = b.getLong("idGrupo", 0);
        idGrupoToEdit = b.getLong("idGrupoToEdit");

        etxtNombreGrupo = (EditText) findViewById(R.id.etxtNombreGrupo);
        lblUsuariosAsociados = (TextView) findViewById(R.id.lblUsuariosAsociados);

        usuariosParaSeleccionar = getUsuarios();
        usuariosSeleccionados = getUsuariosSeleccionados();

        initializeDataWidget();

        lblUsuariosAsociados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getResources().getString(R.string.label_usuarios_asociados);

                SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener listener =
                        new SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener() {
                            @Override
                            public void onUsuariosSelectedChange(List<Usuario> result) {
                                usuariosSeleccionados = result;
                                usuariosSeleccionadosPrev = Usuario.clone(usuariosSeleccionados);
                                lblUsuariosAsociados.setText(createTextUsuariosSeleccionados());
                            }

                            public void resetUsuariosSeleccionados(){
                                usuariosSeleccionados = Usuario.clone(usuariosSeleccionadosPrev);
                                lblUsuariosAsociados.setText(createTextUsuariosSeleccionados());
                            }

                        };

                SelectUsuariosYCantidadAdapter adapter = new ParticipantesAdapter(AddOrEditGrupoActivity.this, R.layout.dialog_usuarios_y_cantidad, usuariosParaSeleccionar, usuariosSeleccionados);

                SelectUsuariosYCantidadDialogFragment dialog =
                        SelectUsuariosYCantidadDialogFragment.newInstance(title, listener, adapter);

                dialog.show(getFragmentManager(),"tag");
            }
        });

    }

    private String createTextUsuariosSeleccionados(){
        String texto = "";

        int usuariosSize = usuariosSeleccionados.size();
        for(int i = 0; i < usuariosSize ; i++){
            texto += usuariosSeleccionados.get(i).getNombre();
            texto += (i == usuariosSize - 1) ? "" : ", ";

        }

        return texto;
    }

    private List<Usuario> getUsuarios(){
        List<Usuario> _usuarios = new ArrayList<Usuario>();
        ModelManager modelManager = new ModelManager(this);

        _usuarios = modelManager.getUsuarios();

        return _usuarios;

    }

    private List<Usuario> getUsuariosSeleccionados(){
        List<Usuario> _usuarios; new ArrayList<Usuario>();
        ModelManager modelManager = new ModelManager(this);

        _usuarios = modelManager.getUsuariosFromGrupo(idGrupoToEdit);

        return _usuarios;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_or_edit_grupo, menu);
        if(idGrupoToEdit == 0){
            MenuItem menuItem = (MenuItem) menu.findItem(R.id.action_addoreditgrupo_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
            case R.id.action_addoreditgrupo_cancel:
                returnToMain();
                return true;
            case R.id.action_addoreditgrupo_delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteGrupo(idGrupoToEdit);
                                returnToMain();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String mensaje = getResources().getString(R.string.label_estas_seguro);
                String yes     = getResources().getString(R.string.label_yes);
                String no      = getResources().getString(R.string.label_no);
                builder.setMessage(mensaje).setPositiveButton(yes, dialogClickListener)
                        .setNegativeButton(no, dialogClickListener).show();
                return true;
            case R.id.action_addoreditgrupo_save:
                String nombre = etxtNombreGrupo.getText().toString();
                persistData(idGrupoToEdit, nombre);
                returnToMain();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void returnToMain(){
        Intent intent = new Intent(this, GrupoActivity.class);
        Bundle b = new Bundle();
        b.putLong("idGrupo", idGrupo );
        intent.putExtras(b);
        startActivity(intent);
    }

    private void initializeDataWidget(){
        ModelManager modelManager = new ModelManager(this);

        Grupo grupo = modelManager.getGrupo(idGrupoToEdit);

        etxtNombreGrupo.setText(grupo.getNombre());

        lblUsuariosAsociados.setText(createTextUsuariosSeleccionados());
    }

    private void persistData(Long idGrupoToEdit, String nombre){

        ModelManager modelManager = new ModelManager(this);

        Long id;
        if(idGrupoToEdit == 0){
            id = modelManager.createGrupo(nombre);
        }else {
            id = idGrupoToEdit;
            modelManager.updateGrupo(idGrupoToEdit, nombre);
            modelManager.desasociaUsuariosDeGrupo(id);
        }

        for(Usuario u: usuariosSeleccionados){
            modelManager.asociaUsuarioAGrupo(u.getId(), id);
        }
    }

    private void deleteGrupo(Long idGrupoToEdit){
        ModelManager modelManager = new ModelManager(getBaseContext());

        List<Compra> compras = modelManager.getComprasFromGrupo(idGrupoToEdit);

        // Eliminación de compras, pagos y participaciones
        if(compras != null) {
            for (Compra c : compras) {
                modelManager.deleteParticipaciones(c.getId());
                modelManager.deletePagos(c.getId());
                modelManager.deleteCompra(c.getId());
            }
        }

        // Desasociación de los usuarios del grupo y eliminación del grupo
        Grupo grupo = modelManager.getGrupo(idGrupoToEdit);

        if(grupo.getUsuarios() != null) {
            for (Usuario u : grupo.getUsuarios()) {
                modelManager.desasociaUsuarioDeGrupo(u.getId(), idGrupoToEdit);
            }
        }
        modelManager.desasociaUsuariosDeGrupo(idGrupoToEdit);
        modelManager.deleteGrupo(idGrupoToEdit);
    }
}
