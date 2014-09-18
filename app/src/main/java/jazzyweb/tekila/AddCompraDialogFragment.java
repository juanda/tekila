package jazzyweb.tekila;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Usuario;
import jazzyweb.tekila.widget.ComprasAdapter;
import jazzyweb.tekila.widget.QuienPagaAdapter;

public class AddCompraDialogFragment extends DialogFragment {

    private Long idGrupo;
    List<Usuario> usuariosSeleccionados;

    OnAddCompraDialogResult dialogResult;

    public interface OnAddCompraDialogResult{
        void finish(List<Usuario> result);
    }

    public AddCompraDialogFragment(){

    }

    public void setUsuariosSeleccionados(List<Usuario> usuarios){
        usuariosSeleccionados = usuarios;
    }

    public void setDialogResult(OnAddCompraDialogResult dr){
        dialogResult = dr;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        idGrupo = getArguments().getLong("idGrupo");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogQuienPaga = inflater.inflate(R.layout.dialog_quien_paga, null);
        builder.setView(dialogQuienPaga);


        List<Usuario> usuarios = getUsuariosFromGrupo(idGrupo);
        ListView lstQuienPaga = (ListView) dialogQuienPaga.findViewById(R.id.lstQuienPaga);

        final QuienPagaAdapter adapter =
                new QuienPagaAdapter(getActivity(), R.layout.dialog_quien_paga, usuarios, usuariosSeleccionados);

        lstQuienPaga.setAdapter(adapter);

        builder.setMessage(R.string.label_compra_quien_paga)
                .setPositiveButton(R.string.action_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if( dialogResult != null ){
                            dialogResult.finish(adapter.getUsuariosSeleccionados());
                        }
                        AddCompraDialogFragment.this.dismiss();
                    }
                })
                .setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    private List<Usuario> getUsuariosFromGrupo(Long idGrupo){
        DataBaseManager dataBaseManager = new DataBaseManager(getActivity());
        dataBaseManager.open();

        List<Usuario> usuarios = dataBaseManager.getUsuariosFromGrupo(idGrupo);

        return usuarios;
    }
}
