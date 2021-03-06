package jazzyweb.tekila.main;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.db.ModelManager;
import jazzyweb.tekila.model.Usuario;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResumenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResumenFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ResumenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idGrupo";

    // TODO: Rename and change types of parameters
    private Long idGrupo;
    private ModelManager modelManager;
    private List<Usuario> usuarios;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idGrupo Parameter 1.
     * @return A new instance of fragment ResumenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResumenFragment newInstance(Long idGrupo) {
        ResumenFragment fragment = new ResumenFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, idGrupo);
        fragment.setArguments(args);
        return fragment;
    }
    public ResumenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idGrupo = getArguments().getLong(ARG_PARAM1);
            modelManager = new ModelManager(getActivity());
            usuarios = modelManager.getUsuariosFromGrupo(idGrupo);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_resumen, container, false);

        ListView lstCompras = (ListView) rootView.findViewById(R.id.lstUsuariosResumen);

        UsuariosResumenAdapter adapter = new UsuariosResumenAdapter(getActivity(), R.layout.item_compra, usuarios, idGrupo);

        lstCompras.setAdapter(adapter);

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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

}
