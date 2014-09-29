package jazzyweb.tekila.main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.model.Usuario;

public class UsuariosResumenAdapter extends ArrayAdapter<Usuario>{

    private static final String TXT_CURRENCY = "€";
    private Long idGrupo;

    public UsuariosResumenAdapter(Context context, int resource, List<Usuario> usuarios, Long idG) {
        super(context, resource, usuarios);
        idGrupo = idG;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Usuario usuario = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_usuarios_resumen, parent, false);
        }
        // Lookup view for data population
        TextView txtUsuario       = (TextView) convertView.findViewById(R.id.txtUsuario);
        TextView txtDeuda         = (TextView) convertView.findViewById(R.id.txtDeuda);
        TextView txtPagadoGastado = (TextView) convertView.findViewById(R.id.txtPagadoGastado);

        // Populate the data into the template view using the data object

        Double deuda = usuario.getDeuda(idGrupo);
        txtUsuario.setText(usuario.getNombre());
        txtDeuda.setText(getDeuda(usuario));

        int color = (deuda < 0)? R.color.red : R.color.green;
        txtDeuda.setTextColor(parent.getResources().getColor(color));

        txtPagadoGastado.setText(getPagadoGastado(usuario));

        // Return the completed view to render on screen
        return convertView;
    }

    private String getDeuda(Usuario usuario){
        String texto = String.format("%.2f",usuario.getDeuda(idGrupo)) + " " + TXT_CURRENCY;

        return texto;
    }

    private String getPagadoGastado(Usuario usuario){

        Double pagado = usuario.getPagado(idGrupo);
        Double gastado = usuario.getGastado(idGrupo);



        String texto = "Ha pagado: " + String.format("%.2f", usuario.getPagado(idGrupo))
                + " , ha gastado: " + String.format("%.2f", usuario.getGastado(idGrupo));

        return texto;
    }
}
