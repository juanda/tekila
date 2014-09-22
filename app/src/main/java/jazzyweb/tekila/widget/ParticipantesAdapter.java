package jazzyweb.tekila.widget;


import android.content.Context;

import java.util.List;

import jazzyweb.tekila.model.Usuario;

public class ParticipantesAdapter extends SelectUsuariosYCantidadAdapter{

    public ParticipantesAdapter(Context context, int resource, List<Usuario> usuarios, List<Usuario> ususSelect){
        super(context, resource, usuarios, ususSelect);
    }

}
