package jazzyweb.tekila.widget;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.model.Usuario;

public class SelectUsuariosYCantidadAdapter extends ArrayAdapter<Usuario>{


    private static final String TXT_TODOS = "Todos";
    private static final String TXT_CURRENCY = "€";

    private List<Usuario> usuariosSeleccionados;

    public SelectUsuariosYCantidadAdapter(Context context, int resource, List<Usuario> usuarios, List<Usuario> ususSelect) {
        super(context, resource, usuarios);
        usuariosSeleccionados = (ususSelect == null)?  new ArrayList<Usuario>() : ususSelect;
    }

    public List<Usuario> getUsuariosSeleccionados() {
        return usuariosSeleccionados;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Usuario usuario = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_usuarios_y_cantidad_item, parent, false);
        }
        // Lookup view for data population
        TextView txtUsuario = (TextView) convertView.findViewById(R.id.txtUsuariosYCantidadUsuario);
        final CheckBox chkUsuario = (CheckBox) convertView.findViewById(R.id.chkUsuariosYCantidadUsuario);
        final EditText etxtCantidad = (EditText) convertView.findViewById(R.id.etxtUsuariosYCantidadCantidad);

        initializeWidgets(txtUsuario, chkUsuario, etxtCantidad, usuario);

        chkUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkUsuario.isChecked()) {
                    etxtCantidad.setVisibility(View.VISIBLE);
                    etxtCantidad.requestFocus();
                    usuariosSeleccionados.add(usuario);

                }else{
                    etxtCantidad.setText("");
                    etxtCantidad.setVisibility(View.INVISIBLE);
                    usuariosSeleccionados.remove(usuario);
                }
            }
        });

        etxtCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String strCantidad = etxtCantidad.getText().toString();
                if(!strCantidad.matches("")) {
                    Double cantidad = Double.parseDouble(String.valueOf(etxtCantidad.getText()));
                    Usuario u = getUsuarioEqualTo(usuario);
                    u.setCantidadAux(cantidad);
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private void initializeWidgets(TextView txtUsuario, CheckBox chkUsuario, EditText etxtCantidad, Usuario usuario){
        txtUsuario.setText(usuario.getNombre());
        etxtCantidad.setVisibility(View.INVISIBLE);
        if(userIsInSelectedList(usuario)){
            usuario.setCantidadAux(usuariosSeleccionados.get(usuariosSeleccionados.indexOf(usuario)).getCantidadAux());
            chkUsuario.setChecked(true);
            etxtCantidad.setText(String.valueOf(usuario.getCantidadAux()));
            etxtCantidad.setVisibility(View.VISIBLE);

        }else{
            chkUsuario.setChecked(false);
            etxtCantidad.setText("");
        }
    }


    private boolean userIsInSelectedList(Usuario usuario){
        for(Usuario u: usuariosSeleccionados){
            if(usuario.getId() == u.getId()){
                return true;
            }
        }
        return false;
    }

    protected Usuario getUsuarioEqualTo(Usuario u){
        for(Usuario usu: usuariosSeleccionados){
            if (usu.equals(u)){
                return  usu;
            }
        }
        return null;
    }

}
