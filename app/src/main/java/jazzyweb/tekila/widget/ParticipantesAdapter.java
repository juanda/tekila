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

    public ParticipantesAdapter(Context context, int resource, List<Usuario> usuarios, List<Usuario> ususSelect){
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

        chkUsuario.setOnClickListener(new OnClickListener(chkUsuario, etxtCantidad, usuario));

        etxtCantidad.setOnFocusChangeListener(new OnFocusChangeListener(etxtCantidad, usuario));

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

    protected boolean userIsInSelectedList(Usuario usuario){
        for(Usuario u: usuariosSeleccionados){
            if(usuario.getId() == u.getId()){
                return true;
            }
        }
        return false;
    }

    protected class OnClickListener implements   View.OnClickListener {

        private CheckBox chkUsuario;
        private TextView etxtCantidad;
        private Usuario usuario;

        public OnClickListener(CheckBox chkUsuario, TextView etxtCantidad, Usuario usuario){
            this.chkUsuario = chkUsuario;
            this.etxtCantidad = etxtCantidad;
            this.usuario = usuario;
        }

        @Override
        public void onClick(View view) {
            if(chkUsuario.isChecked()) {
                usuariosSeleccionados.add(usuario);

            }else{
                usuariosSeleccionados.remove(usuario);
            }
        }
    }

}
