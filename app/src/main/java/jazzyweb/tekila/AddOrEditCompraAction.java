package jazzyweb.tekila;

import android.app.ActionBar;
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
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jazzyweb.tekila.db.ModelManager;
import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.model.Pago;
import jazzyweb.tekila.model.Participacion;
import jazzyweb.tekila.model.Usuario;
import jazzyweb.tekila.widget.ParticipantesAdapter;
import jazzyweb.tekila.widget.SelectUsuariosYCantidadAdapter;

public class AddOrEditCompraAction extends Activity {

    private List<Usuario> usuariosParaPagos;
    private List<Usuario> usuariosParaParticipaciones;
    private List<Usuario> usuariosPagosSeleccionados;
    private List<Usuario> usuariosPagosSeleccionadosPrev;
    private List<Usuario> usuariosParticipaSeleccionados;
    private List<Usuario> usuariosParticipaSeleccionadosPrev;

    private Long idGrupo;
    private Long idCompra;

    private EditText etxtConcepto;
    private TextView lblQuienPaga;
    private TextView lblQuienParticipa;
    private TextView lblTotalCompra;
    private TextView lblDate;
    private TextView lblTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compra);

        Bundle b = getIntent().getExtras();
        idGrupo = b.getLong("idGrupo");
        idCompra = b.getLong("idCompra");

        usuariosParaPagos = getUsuariosFromGrupo(idGrupo);
        usuariosParaParticipaciones = Usuario.clone(usuariosParaPagos);
        usuariosParticipaSeleccionados = Usuario.clone(usuariosParaPagos);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        etxtConcepto = (EditText) findViewById(R.id.etxtConcepto);
        lblQuienPaga = (TextView) findViewById(R.id.lblQuienPaga);
        lblQuienParticipa = (TextView) findViewById(R.id.lblQuienParticipa);
        lblTotalCompra = (TextView) findViewById(R.id.lblTotalCompra);

        lblDate = (TextView) findViewById(R.id.lblDate);
        lblTime = (TextView) findViewById(R.id.lblTime);

        initializeDataWidgets();

        lblDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();

                datePickerFragment.setTextView(lblDate);

                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        lblTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();

                timePickerFragment.setTextView(lblTime);

                timePickerFragment.show(getFragmentManager(), "timePicker");

            }
        });


        lblQuienPaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getResources().getString(R.string.label_compra_quien_paga);

                SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener listener = new SelectUsuariosYCantidadDialogFragment.OnUsuariosSelectedChangeListener() {
                    @Override
                    public void onUsuariosSelectedChange(List<Usuario> result) {
                        setTextViewPagadores(result);
                    }

                    public void resetUsuariosSeleccionados(){
                        resetTextViewPagadores();
                    }

                };

                SelectUsuariosYCantidadAdapter adapter = new  SelectUsuariosYCantidadAdapter(AddOrEditCompraAction.this, R.layout.dialog_usuarios_y_cantidad, usuariosParaPagos, usuariosPagosSeleccionados);

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
                                setTextViewParticipantes(result);
                            }

                            public void resetUsuariosSeleccionados(){
                                resetTextViewParticipantes();
                            }
                        };
                ParticipantesAdapter adapter = new ParticipantesAdapter(AddOrEditCompraAction.this, R.layout.dialog_usuarios_y_cantidad, usuariosParaParticipaciones, usuariosParticipaSeleccionados);

                SelectUsuariosYCantidadDialogFragment dialog =
                        SelectUsuariosYCantidadDialogFragment.newInstance(title, listener, adapter);

                dialog.show(getFragmentManager(),"tag");
            }
        });
    }

    protected void setTextViewPagadores(List<Usuario> usuarios){
        usuariosPagosSeleccionados = getUsuariosConCantidadNoNula(usuarios);
        usuariosPagosSeleccionadosPrev = Usuario.clone(usuariosPagosSeleccionados);
        lblQuienPaga.setText(createTextPagadores());
        lblTotalCompra.setText(String.valueOf(getTotalCompra(usuariosPagosSeleccionados)));
    }

    protected void resetTextViewPagadores(){
        usuariosPagosSeleccionados = Usuario.clone(usuariosPagosSeleccionadosPrev);
        lblQuienPaga.setText(createTextPagadores());
    }

    protected void setTextViewParticipantes(List<Usuario> usuarios){
        usuariosParticipaSeleccionados = usuarios;
        usuariosParticipaSeleccionadosPrev = Usuario.clone(usuariosParticipaSeleccionados);
        lblQuienParticipa.setText(createTextParticipantes());
    }

    protected void resetTextViewParticipantes(){
        usuariosParticipaSeleccionados = Usuario.clone(usuariosParticipaSeleccionadosPrev);
        lblQuienParticipa.setText(createTextParticipantes());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_compra, menu);
        if(idCompra == 0){
            MenuItem menuItem = (MenuItem) menu.findItem(R.id.action_delete_compra);
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
        if (id == R.id.action_save_compra) {
            if(isValid()){ // Esta forma de validar no me mola nada, en realidad esta función también crea los avisos de error
                String concepto = etxtConcepto.getText().toString();
                String date = lblDate.getText().toString();
                String time = lblTime.getText().toString();
                String cantidad = lblTotalCompra.getText().toString();
                persistData(idGrupo, idCompra, concepto, cantidad, date, time, usuariosPagosSeleccionados, usuariosParticipaSeleccionados);
                returnToMain();
            }
            return true;
        }else if(id == R.id.action_cancel_add_compra){
            returnToMain();
            return true;
        }else if(id == R.id.action_delete_compra){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            ModelManager modelManager = new ModelManager(getBaseContext());
                            modelManager.deleteCompra(idCompra);
                            modelManager.deletePagos(idCompra);
                            modelManager.deleteParticipaciones(idCompra);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void returnToMain(){
        Intent intent = new Intent(this, MainAction.class);
        Bundle b = new Bundle();
        b.putLong("idGrupo", idGrupo );
        intent.putExtras(b);
        startActivity(intent);
    }

    private List<Usuario> getUsuariosFromGrupo(Long idGrupo){
        ModelManager modelManager = new ModelManager(this);

        List<Usuario> usuarios = modelManager.getUsuariosFromGrupo(idGrupo);

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

    private String getActualDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String strDate = dateFormat.format(date);
        return strDate;
    }

    private String getActualTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);
        return strDate;
    }

    private Boolean isValid(){
        Boolean bConcepto = true;
        Boolean bPagadores = true;
        Boolean bParticipantes = true;
        String errors = "";

        EditText etxtConcepto = (EditText) findViewById(R.id.etxtConcepto);
        if( etxtConcepto.getText().toString().length() == 0 ){
            errors += getResources().getString(R.string.label_compra_error_concepto_vacio);
            bConcepto = false;
        }

        if( usuariosPagosSeleccionados == null || usuariosPagosSeleccionados.size() == 0 ){
            errors += "\n" + getResources().getString(R.string.label_compra_error_pagadores_vacio);
            bPagadores = false;
        }

        if( usuariosParticipaSeleccionados == null || usuariosParticipaSeleccionados.size() == 0 ){
            errors += "\n" + getResources().getString(R.string.label_compra_error_participantes_vacio);
            bParticipantes = false;
        }

        Boolean result = bConcepto && bPagadores && bParticipantes;

        if(!result){
            Toast.makeText(this, errors, Toast.LENGTH_LONG).show();
        }

        return result;
    }

    protected void persistData(Long idGrupo,
                               Long idCompra,
                               String concepto,
                               String cantidad,
                               String date,
                               String time,
                               List<Usuario> usuariosPagosSeleccionados,
                               List<Usuario> usuariosParticipaSeleccionados){

        ModelManager modelManager = new ModelManager(this);


        Long datetime = getTimeStamp(date, time);
        if(idCompra == 0){
            idCompra = modelManager.createCompra(concepto, Double.valueOf(cantidad), idGrupo, datetime);
        }else{
            modelManager.updateCompra(idCompra, concepto, Double.valueOf(cantidad), idGrupo, datetime);
            modelManager.deleteParticipaciones(idCompra);
            modelManager.deletePagos(idCompra);
        }


        for(Usuario u: usuariosPagosSeleccionados){
            Long idPago = modelManager.createPago(u.getCantidadAux(), u.getId(), idCompra);
        }

        int nParticipantes = usuariosParticipaSeleccionados.size();
        Double porcentaje = Double.valueOf(cantidad) / Double.valueOf(nParticipantes);
        for(Usuario u: usuariosParticipaSeleccionados){
            Long idParticipacion = modelManager.createParticipacion(porcentaje, u.getId(), idCompra);
        }

    }

    protected Long getTimeStamp(String date, String time){
        String datetime = date + " " + time;
        Long timestamp = null;

        try {
            Date fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(datetime);
            timestamp = fecha.getTime();
        }catch (Exception e){
            throw new Error("No he podido convertir la fecha a timestamp");
        }

        return timestamp;
    }

    protected void initializeDataWidgets(){
        if(idCompra == 0){
            lblDate.setText(getActualDate());
            lblTime.setText(getActualTime());
        }else{
            ModelManager modelManager = new ModelManager(this);
            Compra compra = modelManager.getCompra(idCompra);

            List<Participacion> participaciones = modelManager.getParticipacionesFromCompra(idCompra);
            List<Pago> pagos = modelManager.getPagosFromCompra(idCompra);

            List<Usuario> _usuariosPagosSeleccionados = new ArrayList<Usuario>();
            for(Pago p: pagos){
                Usuario u = new Usuario();
                u.setId(p.getUsuario().getId());
                u.setNombre(p.getUsuario().getNombre());
                u.setCantidadAux(p.getCantidad());
                _usuariosPagosSeleccionados.add(u);
            }

            List<Usuario> _usuariosParticipaSeleccionados = new ArrayList<Usuario>();
            for(Participacion pa: participaciones){
                Usuario u = new Usuario();
                u.setId(pa.getUsuario().getId());
                u.setNombre(pa.getUsuario().getNombre());
                u.setCantidadAux(pa.getPorcentaje());
                _usuariosParticipaSeleccionados.add(u);
            }

            Long timestamp = compra.getDatetime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String date =  dateFormat.format(new Date(timestamp));
            String time =  timeFormat.format(new Date(timestamp));

            setTextViewPagadores(_usuariosPagosSeleccionados);
            setTextViewParticipantes(_usuariosParticipaSeleccionados);

            etxtConcepto.setText(compra.getNombre());
            lblDate.setText(date);
            lblTime.setText(time);
        }
    }
}
