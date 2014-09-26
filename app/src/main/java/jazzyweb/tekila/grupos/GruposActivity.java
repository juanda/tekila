package jazzyweb.tekila.grupos;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import jazzyweb.tekila.main.MainActivity;
import jazzyweb.tekila.R;
import jazzyweb.tekila.db.ModelManager;
import jazzyweb.tekila.model.Grupo;
import jazzyweb.tekila.widget.GruposAdapter;


public class GruposActivity extends ListActivity {

    private Long idGrupo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ModelManager modelManager = new ModelManager(this);

        List<Grupo> grupos = modelManager.getGrupos();

        GruposAdapter adapter = new GruposAdapter(this, R.layout.item_grupos, grupos);

        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Grupo grupo = (Grupo) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(GruposActivity.this, AddOrEditGrupoActivity.class);
                Bundle b = new Bundle();
                b.putLong("idGrupo", grupo.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.grupos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_add_grupo:
                showAddOrEditGrupoActivity(null);
                return true;
            case R.id.action_cancel_grupos:
                returnToMain();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddOrEditGrupoActivity(Long idGrupo){
        Intent intent = new Intent(this, AddOrEditGrupoActivity.class);
        if(idGrupo != null){
            Bundle b = new Bundle();
            b.putLong("idGrupo", idGrupo);
            intent.putExtras(b);
        }
        startActivity(intent);
    }
    private void returnToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        if(idGrupo != null){
            Bundle b = new Bundle();
            b.putLong("idGrupo", idGrupo );
            intent.putExtras(b);
        }

        startActivity(intent);
    }
}
