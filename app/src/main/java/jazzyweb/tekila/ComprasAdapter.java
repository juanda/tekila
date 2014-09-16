package jazzyweb.tekila;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.model.Pago;
import jazzyweb.tekila.model.Participacion;

public class ComprasAdapter extends ArrayAdapter<Compra>{

    public ComprasAdapter(Context context, ArrayList<Compra> compras) {
        super(context, R.layout.item_compra, compras);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Compra compra = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_compra, parent, false);
        }
        // Lookup view for data population
        TextView txtPagadores      = (TextView) convertView.findViewById(R.id.txtPagadores);
        TextView txtCantidad       = (TextView) convertView.findViewById(R.id.txtCantidad);
        TextView txtConcepto       = (TextView) convertView.findViewById(R.id.txtConcepto);
        TextView txtParticipantes  = (TextView) convertView.findViewById(R.id.txtParticipantes);
        TextView txtFecha          = (TextView) convertView.findViewById(R.id.txtFecha);
        // Populate the data into the template view using the data object

        txtPagadores.setText(getPagadores(compra));
        txtCantidad.setText(getCantidad(compra));
        txtConcepto.setText(getConcepto(compra));
        txtParticipantes.setText(getParticipantes(compra));
        txtFecha.setText(getFecha(compra));

        // Return the completed view to render on screen
        return convertView;
    }

    private String getPagadores(Compra compra){

        List<Pago> pagos = compra.getPagos();


        return "";
    }

    private String getCantidad(Compra compra){

        return "";
    }

    private String getConcepto(Compra compra){

        return "";
    }

    private String getParticipantes(Compra compra){

        return "";
    }

    private String getFecha(Compra compra){

        return "";
    }
}
