package jazzyweb.tekila.usuarios;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.model.Grupo;
import jazzyweb.tekila.model.Usuario;

public class UsuarioAdapter extends ArrayAdapter<Usuario>{


    public UsuarioAdapter(Context context, int resource, List<Usuario> usuarios) {
        super(context, resource, usuarios);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Usuario usuario = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_usuarios, parent, false);
        }
        // Lookup view for data population
        TextView lblUsuarioNombre       = (TextView) convertView.findViewById(R.id.lblUsuarioItemNombre);

        // Populate the data into the template view using the data object

        lblUsuarioNombre.setText(usuario.getNombre());


        // Return the completed view to render on screen
        return convertView;
    }

}
