package jazzyweb.tekila.widget;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jazzyweb.tekila.R;
import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.model.Pago;
import jazzyweb.tekila.model.Participacion;
import jazzyweb.tekila.model.Usuario;

public class QuienPagaAdapter extends ArrayAdapter<Usuario>{


    private static final String TXT_TODOS = "Todos";
    private static final String TXT_CURRENCY = "€";

    private ArrayList<Usuario> usuariosSeleccionados;

    public QuienPagaAdapter(Context context, int resource, List<Usuario> usuarios) {
        super(context, resource, usuarios);
        usuariosSeleccionados = new ArrayList<Usuario>();
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_quien_paga_item, parent, false);
        }
        // Lookup view for data population
        TextView txtQuienPaga = (TextView) convertView.findViewById(R.id.txtDialogQuienPagaUsuario);
        final CheckBox chkQuienPaga = (CheckBox) convertView.findViewById(R.id.chkDialogQuienPagaUsuario);
        final EditText etxtCantidad = (EditText) convertView.findViewById(R.id.etxtDialogQuienPagaCantidad);

        // Populate the data into the template view using the data object

        txtQuienPaga.setText(usuario.getNombre());
        etxtCantidad.setVisibility(View.INVISIBLE);
        chkQuienPaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkQuienPaga.isChecked()) {
                    etxtCantidad.setVisibility(View.VISIBLE);
                    etxtCantidad.requestFocus();
                    usuariosSeleccionados.add(usuario);

                }else{
                    etxtCantidad.setText("");
                    etxtCantidad.setVisibility(View.INVISIBLE);
                }
            }
        });

        etxtCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String strCantidad = etxtCantidad.getText().toString();
                if(!strCantidad.matches("")) {
                    Double cantidad = Double.parseDouble(String.valueOf(etxtCantidad.getText()));
                    usuario.setCantidadAux(cantidad);
                }
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }

}
