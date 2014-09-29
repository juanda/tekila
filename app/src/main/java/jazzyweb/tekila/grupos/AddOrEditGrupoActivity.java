package jazzyweb.tekila.grupos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import jazzyweb.tekila.R;
import jazzyweb.tekila.db.ModelManager;
import jazzyweb.tekila.main.MainActivity;
import jazzyweb.tekila.model.Grupo;


public class AddOrEditGrupoActivity extends Activity {

    private Long idGrupo;
    private Long idGrupoToEdit;
    private EditText etxtNombreGrupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_grupo);

        Bundle b = getIntent().getExtras();
        idGrupo = b.getLong("idGrupo", 0);
        idGrupoToEdit = b.getLong("idGrupoToEdit");

        etxtNombreGrupo = (EditText) findViewById(R.id.etxtNombreGrupo);

        initializeDataWidget();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_or_edit_grupo, menu);
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
        Intent intent = new Intent(this, GruposActivity.class);
        Bundle b = new Bundle();
        b.putLong("idGrupo", idGrupo );
        intent.putExtras(b);
        startActivity(intent);
    }

    private void initializeDataWidget(){
        ModelManager modelManager = new ModelManager(this);

        Grupo grupo = modelManager.getGrupo(idGrupoToEdit);

        etxtNombreGrupo.setText(grupo.getNombre());
    }

    private void persistData(Long idGrupoToEdit, String nombre){

        ModelManager modelManager = new ModelManager(this);

        if(idGrupoToEdit == 0){
            modelManager.createGrupo(nombre);
        }else {
            modelManager.updateGrupo(idGrupoToEdit, nombre);
        }
    }
}
