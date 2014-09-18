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


    OnAddCompraDialogResult dialogResult;

    public AddCompraDialogFragment(){

    }

    public interface OnAddCompraDialogResult{
        void finish(List<Usuario> result);
    }

    public void setDialogResult(OnAddCompraDialogResult dr){
        dialogResult = dr;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogQuienPaga = inflater.inflate(R.layout.dialog_quien_paga, null);
        builder.setView(dialogQuienPaga);

        DataBaseManager dataBaseManager = new DataBaseManager(getActivity());
        dataBaseManager.open();

        List<Usuario> usuarios = dataBaseManager.getUsuariosFromGrupo(Long.valueOf(1));

        ListView lstQuienPaga = (ListView) dialogQuienPaga.findViewById(R.id.lstQuienPaga);

        final QuienPagaAdapter adapter = new QuienPagaAdapter(getActivity(), R.layout.dialog_quien_paga, usuarios);

        lstQuienPaga.setAdapter(adapter);

        builder.setMessage(R.string.label_compra_quien_paga)
                .setPositiveButton(R.string.action_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        TextView tv = (TextView) dialogQuienPaga.findViewById(R.id.txtQuienPaga);
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
}
