package jazzyweb.tekila;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddCompraDialogFragment extends DialogFragment {


    OnAddCompraDialogResult dialogResult;

    public AddCompraDialogFragment(){

    }

    public interface OnAddCompraDialogResult{
        void finish(String result);
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

        builder.setMessage(R.string.action_add_compra)
                .setPositiveButton(R.string.action_save_compra, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TextView tv = (TextView) dialogQuienPaga.findViewById(R.id.txtQuienPaga);
                        if( dialogResult != null ){
                            dialogResult.finish(String.valueOf(tv.getText()));
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
