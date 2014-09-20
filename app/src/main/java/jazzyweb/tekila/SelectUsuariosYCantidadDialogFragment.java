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

    private static final String ID_GRUPO = "idGrupo";
    private static final String TITLE = "title";
    private static final String MODO = "modo";
    private static final String USUARIOS_SELCCIONADOS = "usuariosSeleccionados";

    public static final int MODO_PAGO = 0;
    public static final int MODO_PARTICIPACION = 1;

    private Long idGrupo;
    private String title;
    private int modo;
    List<Usuario> usuariosSeleccionados;
    OnUsuariosSelectedChangeListener mCallback;

    private OnFragmentInteractionListener mListener;

    public interface OnUsuariosSelectedChangeListener{
        public void onUsuariosPagoSelectedChange(List<Usuario> result);
        public void onUsuariosParticipaSelectedChange(List<Usuario> result);
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
    public static SelectUsuariosYCantidadDialogFragment newInstance(Long idGrupo,
                                                                    List<Usuario> uSeleccionados,
                                                                    String title,
                                                                    int modo) {
        SelectUsuariosYCantidadDialogFragment fragment = new SelectUsuariosYCantidadDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ID_GRUPO, idGrupo);
        args.putString(TITLE, title);
        args.putInt(MODO, modo);
        fragment.setUsuariosSeleccionados(uSeleccionados);
        fragment.setArguments(args);
        return fragment;
    }
    public SelectUsuariosYCantidadDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        idGrupo = getArguments().getLong(ID_GRUPO);
        title = getArguments().getString(TITLE);
        modo = getArguments().getInt(MODO);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogQuienParticipa = inflater.inflate(R.layout.dialog_usuarios_y_cantidad, null);
        builder.setView(dialogQuienParticipa);

        List<Usuario> usuarios = getUsuariosFromGrupo(idGrupo);
        final ListView lstQuienPaga = (ListView) dialogQuienParticipa.findViewById(R.id.lstUsuariosYCantidad);

        final SelectUsuariosYCantidadAdapter adapter =
                new SelectUsuariosYCantidadAdapter(getActivity(), R.layout.dialog_usuarios_y_cantidad, usuarios, usuariosSeleccionados);

        lstQuienPaga.setAdapter(adapter);

        builder.setMessage(title)
                .setPositiveButton(R.string.action_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (modo){
                            case MODO_PAGO:
                                mCallback.onUsuariosPagoSelectedChange(adapter.getUsuariosSeleccionados());
                                break;
                            case MODO_PARTICIPACION:
                            default:
                                mCallback.onUsuariosParticipaSelectedChange(adapter.getUsuariosSeleccionados());

                        }
                        SelectUsuariosYCantidadDialogFragment.this.dismiss();
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
