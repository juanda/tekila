package jazzyweb.tekila.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import jazzyweb.tekila.R;
import jazzyweb.tekila.compras.ComprasAdapter;
import jazzyweb.tekila.db.ModelManager;
import jazzyweb.tekila.grupos.GrupoAdapter;
import jazzyweb.tekila.model.Grupo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChoiceGrupoDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChoiceGrupoDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ChoiceGrupoDialogFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private String title;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChoiceGrupoDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoiceGrupoDialogFragment newInstance(String title) {
        ChoiceGrupoDialogFragment fragment = new ChoiceGrupoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
    public ChoiceGrupoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogChoiceGrupo = inflater.inflate(R.layout.fragment_choice_grupo_dialog, null);
        builder.setView(dialogChoiceGrupo);

        builder.setTitle(getResources().getString(R.string.label_selecciona_grupo));

        final ListView lstGrupo = (ListView) dialogChoiceGrupo.findViewById(R.id.lstChoiceGrupoGrupos);

        final GrupoAdapter adapter = new GrupoAdapter(getActivity(), R.layout.fragment_choice_grupo_dialog, getGrupos());

        lstGrupo.setAdapter(adapter);

        lstGrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Grupo grupo = adapter.getItem(i);
                SharedPreferences settings = getActivity().getPreferences(0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putLong("idGrupo", grupo.getId());
                editor.commit();

                getActivity().recreate();

                ChoiceGrupoDialogFragment.this.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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

    public List<Grupo> getGrupos(){
        ModelManager modelManager = new ModelManager(getActivity());

        List<Grupo> grupos = modelManager.getGrupos();

        return grupos;
    }

}
