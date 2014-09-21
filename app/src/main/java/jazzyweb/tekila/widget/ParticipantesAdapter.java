package jazzyweb.tekila.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.model.Usuario;

public class ParticipantesAdapter extends SelectUsuariosYCantidadAdapter{

    public ParticipantesAdapter(Context context, int resource, List<Usuario> usuarios, List<Usuario> ususSelect) {
        super(context, resource, usuarios, ususSelect);
    }

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
                    if(!etxtCantidad.getText().equals("")) usuariosSeleccionados.add(usuario);

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

    protected void initializeWidgets(TextView txtUsuario, CheckBox chkUsuario, EditText etxtCantidad, Usuario usuario){
        txtUsuario.setText(usuario.getNombre());
        etxtCantidad.setVisibility(View.INVISIBLE);
        if(userIsInSelectedList(usuario)){
            chkUsuario.setChecked(true);
        }else{
            chkUsuario.setChecked(false);
            etxtCantidad.setText("");
        }
    }
}
