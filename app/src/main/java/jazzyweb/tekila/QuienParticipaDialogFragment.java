package jazzyweb.tekila;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Usuario;
import jazzyweb.tekila.widget.QuienPagaAdapter;
import jazzyweb.tekila.widget.QuienParticipaAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuienParticipaDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuienParticipaDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class QuienParticipaDialogFragment extends DialogFragment {

    private static final String ID_GRUPO = "idGrupo";
    private static final String USUARIOS_SELCCIONADOS = "usuariosSeleccionados";

    private Long idGrupo;
    List<Usuario> usuariosSeleccionados;
    OnUsuariosSelectedChangeListener mCallback;

    private OnFragmentInteractionListener mListener;

    public interface OnUsuariosSelectedChangeListener{
        public void onUsuariosSelectedChange(List<Usuario> result);
    }

    public void setUsuariosSeleccionados(List<Usuario> usuariosSeleccionados) {
        this.usuariosSeleccionados = usuariosSeleccionados;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idGrupo Parameter 1.
     * @param uSeleccionados Parameter 2.
     * @return A new instance of fragment QuienParticipaDialog.
     */
    public static QuienParticipaDialogFragment newInstance(Long idGrupo, List<Usuario> uSeleccionados) {
        QuienParticipaDialogFragment fragment = new QuienParticipaDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ID_GRUPO, idGrupo);
        fragment.setUsuariosSeleccionados(uSeleccionados);
        fragment.setArguments(args);
        return fragment;
    }
    public QuienParticipaDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        idGrupo = getArguments().getLong(ID_GRUPO);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogQuienParticipa = inflater.inflate(R.layout.dialog_quien_participa, null);
        builder.setView(dialogQuienParticipa);

        List<Usuario> usuarios = getUsuariosFromGrupo(idGrupo);
        final ListView lstQuienPaga = (ListView) dialogQuienParticipa.findViewById(R.id.lstQuienParticipa);

        final QuienParticipaAdapter adapter =
                new QuienParticipaAdapter(getActivity(), R.layout.dialog_quien_participa, usuarios, usuariosSeleccionados);

        lstQuienPaga.setAdapter(adapter);

        builder.setMessage(R.string.label_compra_quien_participa)
                .setPositiveButton(R.string.action_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCallback.onUsuariosSelectedChange(adapter.getUsuariosSeleccionados());
                        QuienParticipaDialogFragment.this.dismiss();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnUsuariosSelectedChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUsuariosSelectedChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    private List<Usuario> getUsuariosFromGrupo(Long idGrupo){
        DataBaseManager dataBaseManager = new DataBaseManager(getActivity());
        dataBaseManager.open();

        List<Usuario> usuarios = dataBaseManager.getUsuariosFromGrupo(idGrupo);

        return usuarios;
    }
}
