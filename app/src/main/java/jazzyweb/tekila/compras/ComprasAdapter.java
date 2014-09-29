package jazzyweb.tekila.compras;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jazzyweb.tekila.R;
import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.model.Pago;
import jazzyweb.tekila.model.Participacion;

public class ComprasAdapter extends ArrayAdapter<Compra>{

    private int numParticipantes;

    private static final String TXT_TODOS = "Todos";
    private static final String TXT_CURRENCY = "€";

    public ComprasAdapter(Context context, int resource, List<Compra> compras, int numP) {
        super(context, resource, compras);
        numParticipantes = numP;
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
        String texto = "";
        List<Pago> pagos = compra.getPagos();

        int pagosSize = pagos.size();
        for(int i = 0; i < pagosSize ; i++){
            texto += pagos.get(i).getUsuario().getNombre();
            texto += (i == pagosSize - 1)? "" : ", ";
        }

        return texto;
    }

    private String getCantidad(Compra compra){
        String texto = "";
        texto = TXT_CURRENCY + " " + String.valueOf(compra.getCantidad());

        return texto;
    }

    private String getConcepto(Compra compra){
        String texto = compra.getNombre();

        return texto;
    }

    private String getParticipantes(Compra compra){
        String text = "Para: " + TXT_TODOS;

        List<Participacion> participaciones = compra.getParticipaciones();

        int participacionesSize = participaciones.size();
        if(participacionesSize != numParticipantes){
            text = "Para: ";
            for(int i = 0; i < participaciones.size(); i++){
                text += participaciones.get(i).getUsuario().getNombre();
                text += (i == participacionesSize - 1)? "" : ", ";
            }
        }
        return text;
    }

    private String getFecha(Compra compra){
        String texto = "";

        Long timestamp = compra.getDatetime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        texto =  sdf.format(new Date(timestamp));

        return texto;
    }
}
