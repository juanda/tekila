package jazzyweb.tekila;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.concurrent.Callable;

import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Usuario;
import jazzyweb.tekila.widget.SelectUsuariosYCantidadAdapter;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link jazzyweb.tekila.SelectUsuariosYCantidadDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link jazzyweb.tekila.SelectUsuariosYCantidadDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SelectUsuariosYCantidadDialogFragment extends DialogFragment {

    private String title;
    List<Usuario> usuarios;
    OnUsuariosSelectedChangeListener mCallback;
    SelectUsuariosYCantidadAdapter adapter;

    public interface OnUsuariosSelectedChangeListener{
        public void onUsuariosSelectedChange(List<Usuario> result);
        public void resetUsuariosSeleccionados();
    }

    public void setUsuarios(List<Usuario> usuarios){
        this.usuarios = usuarios;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setmCallback(OnUsuariosSelectedChangeListener callback){
        this.mCallback = callback;
    }

    public void setAdapter(SelectUsuariosYCantidadAdapter adapter){
        this.adapter = adapter;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param usuarios Parameter 1.
     * @return A new instance of fragment SelectUsuariosYCantidadDialogFragment.
     */
    public static SelectUsuariosYCantidadDialogFragment newInstance(List<Usuario> usuarios,
                                                                    String title,
                                                                    OnUsuariosSelectedChangeListener callback,
                                                                    SelectUsuariosYCantidadAdapter adapter
                                                                    ) {
        SelectUsuariosYCantidadDialogFragment fragment = new SelectUsuariosYCantidadDialogFragment();
        fragment.setUsuarios(usuarios);
        fragment.setTitle(title);
        fragment.setmCallback(callback);
        fragment.setAdapter(adapter);

        return fragment;
    }
    public SelectUsuariosYCantidadDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogUsuariosYCantidad = inflater.inflate(R.layout.dialog_usuarios_y_cantidad, null);
        builder.setView(dialogUsuariosYCantidad);

        final ListView lstUsuarios = (ListView) dialogUsuariosYCantidad.findViewById(R.id.lstUsuariosYCantidad);

        lstUsuarios.setAdapter(adapter);

        builder.setMessage(title)
                .setPositiveButton(R.string.action_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCallback.onUsuariosSelectedChange(adapter.getUsuariosSeleccionados());

                        SelectUsuariosYCantidadDialogFragment.this.dismiss();
                    }
                })
                .setNegativeButton(R.string.action_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCallback.resetUsuariosSeleccionados();

                        SelectUsuariosYCantidadDialogFragment.this.dismiss();
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        if(mCallback == null){
            throw new ClassCastException(activity.toString()
                    + " must implement OnUsuariosSelectedChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
