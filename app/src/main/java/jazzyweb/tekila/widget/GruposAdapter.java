package jazzyweb.tekila.widget;


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

public class GruposAdapter extends ArrayAdapter<Grupo>{


    public GruposAdapter(Context context, int resource, List<Grupo> grupos) {
        super(context, resource, grupos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Grupo grupo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grupos, parent, false);
        }
        // Lookup view for data population
        TextView lblGrupoNombre       = (TextView) convertView.findViewById(R.id.lblGrupoItemNombre);

        // Populate the data into the template view using the data object

        lblGrupoNombre.setText(grupo.getNombre());


        // Return the completed view to render on screen
        return convertView;
    }

}
