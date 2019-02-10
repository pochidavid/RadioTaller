package lourdes8122.radiotaller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import lourdes8122.radiotaller.repository.EnviarMail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.mail.MessagingException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnviarComentariosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnviarComentariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnviarComentariosFragment extends Fragment {

    Button btnEnviar;
    EditText comentarios;

    public EnviarComentariosFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EnviarComentariosFragment newInstance(String param1, String param2) {
        EnviarComentariosFragment fragment = new EnviarComentariosFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enviar_comentarios, container, false);

        btnEnviar = (Button) rootView.findViewById(R.id.btnEnviar);
        comentarios = (EditText) rootView.findViewById(R.id.textComentario);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnviarMail gestor = new EnviarMail();
                String asunto = "Prueba";
                String contenido = String.valueOf(comentarios.getText());
                try {
                    gestor.main();
                    gestor.enviarMensaje(asunto, contenido);
                    Toast.makeText(getContext(), "Comentarios enviados, gracias.,", Toast.LENGTH_LONG).show();
                } catch (MessagingException e) {
                    Toast.makeText(getContext(), "Ocurrio un error en el envio.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
