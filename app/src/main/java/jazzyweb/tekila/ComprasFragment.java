package jazzyweb.tekila;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import jazzyweb.tekila.db.DataBaseManager;
import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.widget.ComprasAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComprasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComprasFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ComprasFragment extends Fragment {
    private static final String ARG_PARAM1 = "idGrupo";

    private Long idGrupo;
    private DataBaseManager dataBaseManager;
    private List<Compra> compras;
    private int numParticipantes;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idGrupo Parameter 1.
     * @return A new instance of fragment ComprasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComprasFragment newInstance(Long idGrupo) {
        ComprasFragment fragment = new ComprasFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, idGrupo);
        fragment.setArguments(args);
        return fragment;
    }

    public ComprasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            idGrupo = getArguments().getLong(ARG_PARAM1);
            dataBaseManager = new DataBaseManager(getActivity());
            dataBaseManager.open();
            compras = dataBaseManager.getComprasFromGrupo(idGrupo);
            numParticipantes = dataBaseManager.getNumParticipantes(idGrupo);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_compras, container, false);

        ListView lstCompras = (ListView) rootView.findViewById(R.id.lstCompras);

        ComprasAdapter adapter = new ComprasAdapter(getActivity(), R.layout.item_compra, compras, numParticipantes);

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
