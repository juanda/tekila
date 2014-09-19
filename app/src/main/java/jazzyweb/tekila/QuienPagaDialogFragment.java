package jazzyweb.tekila;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Usuario;
import jazzyweb.tekila.widget.SelectUsuariosYCantidadAdapter;

public class QuienPagaDialogFragment extends DialogFragment {

    private Long idGrupo;
    List<Usuario> usuariosSeleccionados;
    OnUsuariosPagoSelectedChangeListener mCallback;

    public interface OnUsuariosPagoSelectedChangeListener{
        public void onUsuariosPagoSelectedChange(List<Usuario> result);
    }

    public QuienPagaDialogFragment(){

    }

    public void setUsuariosSeleccionados(List<Usuario> usuarios){
        usuariosSeleccionados = usuarios;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        idGrupo = getArguments().getLong("idGrupo");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogQuienPaga = inflater.inflate(R.layout.dialog_quien_paga, null);
        builder.setView(dialogQuienPaga);

        List<Usuario> usuarios = getUsuariosFromGrupo(idGrupo);
        final ListView lstQuienPaga = (ListView) dialogQuienPaga.findViewById(R.id.lstQuienPaga);

        final SelectUsuariosYCantidadAdapter adapter =
                new SelectUsuariosYCantidadAdapter(getActivity(), R.layout.dialog_quien_paga, usuarios, usuariosSeleccionados);

        lstQuienPaga.setAdapter(adapter);

        builder.setMessage(R.string.label_compra_quien_paga)
                .setPositiveButton(R.string.action_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    mCallback.onUsuariosPagoSelectedChange(adapter.getUsuariosSeleccionados());
                    QuienPagaDialogFragment.this.dismiss();
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

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnUsuariosPagoSelectedChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUsuariosPagoSelectedChangeListener");
        }
    }

}
