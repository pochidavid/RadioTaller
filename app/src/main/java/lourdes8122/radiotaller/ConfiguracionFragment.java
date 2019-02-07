package lourdes8122.radiotaller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;




public class ConfiguracionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idUsuario"; //Por defecto el Id de Usuario será 1 (administrador)
    private static final String ARG_PARAM2 = "menu";
    private static final String ARG_PARAM3 = "perfil";
    private static final String ARG_PARAM4 = "apariencia";

    private int mParam1;
    private String mParam2;

    private Button btnPerfil;
    private Button btnApariencia;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    public static ConfiguracionFragment newInstance() {
        ConfiguracionFragment fragment = new ConfiguracionFragment();
       Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_configuracion, container, false);
        btnPerfil = rootView.findViewById(R.id.perfil_usuario);
        btnApariencia = rootView.findViewById(R.id.apariencia);

       btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), ConfigUsuarioActivity.class);
                startActivity(i);
                }
       });
        btnApariencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ConfigUsuarioActivity.class);
                startActivity(i);
            }

        });

        return rootView;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
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
        void onFragmentInteraction(Uri uri);
    }
}
