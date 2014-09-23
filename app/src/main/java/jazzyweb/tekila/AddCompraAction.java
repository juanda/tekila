package jazzyweb.tekila;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
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

        final TextView lblQuienPaga = (TextView) findViewById(R.id.lblQuienPaga);
        final TextView lblQuienParticipa = (TextView) findViewById(R.id.lblQuienParticipa);
        final TextView lblTotalCompra = (TextView) findViewById(R.id.lblTotalCompra);

        final TextView lblDate = (TextView) findViewById(R.id.lblDate);

        lblDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();

                datePickerFragment.setTextView(lblDate);

                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });


        lblQuienPaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getResources().getString(R.string.label_compra_quien_paga);

                SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener listener = new SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener() {
                    @Override
                    public void onUsuariosSelectedChange(List<Usuario> result) {
                        usuariosPagosSeleccionados = getUsuariosConCantidadNoNula(result);
                        usuariosPagosSeleccionadosPrev = Usuario.clone(usuariosPagosSeleccionados);
                        lblQuienPaga.setText(createTextPagadores());
                        lblTotalCompra.setText("Total: " + String.valueOf(getTotalCompra(usuariosPagosSeleccionados)));
                    }

                    public void resetUsuariosSeleccionados(){
                        usuariosPagosSeleccionados = Usuario.clone(usuariosPagosSeleccionadosPrev);
                        lblQuienPaga.setText(createTextPagadores());
                    }

                };

                SelectUsuariosYCantidadAdapter adapter = new  SelectUsuariosYCantidadAdapter(AddCompraAction.this, R.layout.dialog_usuarios_y_cantidad, usuariosParaPagos, usuariosPagosSeleccionados);

                SelectUsuariosYCantidadDialogFragment dialog =
                        SelectUsuariosYCantidadDialogFragment.newInstance(title, listener, adapter);

                dialog.show(getFragmentManager(),"tag");
            }
        });

        lblQuienParticipa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getResources().getString(R.string.label_compra_quien_participa);
                SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener listener =
                        new SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener() {
                            @Override
                            public void onUsuariosSelectedChange(List<Usuario> result) {
                                usuariosParticipaSeleccionados = result;
                                usuariosParticipaSeleccionadosPrev = Usuario.clone(usuariosParticipaSeleccionados);
                                lblQuienParticipa.setText(createTextParticipantes());
                            }

                            public void resetUsuariosSeleccionados(){
                                usuariosParticipaSeleccionados = Usuario.clone(usuariosParticipaSeleccionadosPrev);
                                lblQuienParticipa.setText(createTextParticipantes());
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

    private String createTextPagadores(){
        String texto = "";

        int usuariosSize = usuariosPagosSeleccionados.size();
        for(int i = 0; i < usuariosSize ; i++){
            if(usuariosPagosSeleccionados.get(i).getCantidadAux() != null) {
                texto += usuariosPagosSeleccionados.get(i).getNombre() + "(" + usuariosPagosSeleccionados.get(i).getCantidadAux() + ")";
                texto += (i == usuariosSize - 1) ? "" : ", ";
            }
        }

        return texto;
    }

    private String createTextParticipantes(){
        String texto = "";

        int usuariosSize = usuariosParticipaSeleccionados.size();
        if(usuariosSize == usuariosParaParticipaciones.size()){
            texto += "Todos";
        }else {
            for (int i = 0; i < usuariosSize; i++) {
                texto += usuariosParticipaSeleccionados.get(i).getNombre();
                texto += (i == usuariosSize - 1) ? "" : ", ";
            }
        }

        return texto;
    }

    private List<Usuario> getUsuariosConCantidadNoNula(List<Usuario> usuarios){
        ArrayList<Usuario> us = new ArrayList<Usuario>();

        for(Usuario u: usuarios){
            if(u.getCantidadAux() != null)
                us.add(u);
        }

        return us;
    }

    private double getTotalCompra(List<Usuario> usuarios){
        double c = 0.0;

        for(Usuario u: usuarios){
            c += u.getCantidadAux();
        }

        return c;
    }
}