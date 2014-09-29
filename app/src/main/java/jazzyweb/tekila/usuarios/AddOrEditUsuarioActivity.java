package jazzyweb.tekila.usuarios;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.db.ModelManager;
import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.model.Pago;
import jazzyweb.tekila.model.Participacion;
import jazzyweb.tekila.model.Usuario;


public class AddOrEditUsuarioActivity extends Activity {

    private Long idGrupo;
    private Long idUsuarioToEdit;
    private EditText etxtNombreUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_usuario);

        Bundle b = getIntent().getExtras();
        idGrupo = b.getLong("idGrupo", 0);
        idUsuarioToEdit = b.getLong("idUsuarioToEdit");

        etxtNombreUsuario = (EditText) findViewById(R.id.etxtNombreUsuario);

        initializeDataWidget();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_or_edit_usuario, menu);
        if(idUsuarioToEdit == 0){
            MenuItem menuItem = (MenuItem) menu.findItem(R.id.action_addoreditusuario_delete);
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
            case R.id.action_addoreditusuario_cancel:
                returnToMain();
                return true;
            case R.id.action_addoreditusuario_delete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteUsuario(idUsuarioToEdit);
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
            case R.id.action_addoreditusuario_save:
                String nombre = etxtNombreUsuario.getText().toString();
                persistData(idUsuarioToEdit, nombre);
                returnToMain();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void returnToMain(){
        Intent intent = new Intent(this, UsuarioActivity.class);
        Bundle b = new Bundle();
        b.putLong("idGrupo", idGrupo );
        intent.putExtras(b);
        startActivity(intent);
    }

    private void initializeDataWidget(){
        ModelManager modelManager = new ModelManager(this);

        Usuario usuario = modelManager.getUsuario(idUsuarioToEdit);

        etxtNombreUsuario.setText(usuario.getNombre());
    }

    private void persistData(Long idUsuarioToEdit, String nombre){

        ModelManager modelManager = new ModelManager(this);

        if(idUsuarioToEdit == 0){
            modelManager.createUsuario(nombre);
        }else {
            modelManager.updateUsuario(idUsuarioToEdit, nombre);
        }
    }

    private void deleteUsuario(Long idUsuarioToEdit){

        ModelManager modelManager = new ModelManager(this);

        List<Pago> pagos = modelManager.getPagosFromUsuario(idUsuarioToEdit);
        List<Participacion> participaciones = modelManager.getParticipacionesFromUsuario(idUsuarioToEdit);

        if(pagos.size() != 0 || participaciones.size() != 0){
            Toast.makeText(this, getResources().getString(R.string.label_usuario_con_datos_asociados), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, getResources().getString(R.string.label_no_implementado), Toast.LENGTH_LONG).show();
        }

    }
}
