package lourdes8122.radiotaller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import lourdes8122.radiotaller.repository.EnviarMail;
import xdroid.toaster.Toaster;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView nombre;

    SharedPreferences myPreferences;

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
        comentarios = (EditText) rootView.findViewById(R.id.editText);
        final Boolean[] envio = {true};

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Enviando...", Toast.LENGTH_SHORT).show();
                final Boolean[] envio = {true};
                String coment = String.valueOf(comentarios.getText());
                comentarios.setText("");
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        EnviarMail gestor = new EnviarMail();
                        String asunto = "Nuevo Comentario desde la App - Radio Taller";
                        String contenido = "<p>Nombre: " + myPreferences.getString("NOMBRE", "No hay usuario registrado") + "</p>"
                                + "<p>Apellido: " + myPreferences.getString("APELLIDO", "Apellido") + "</p>"
                                + "<p>Pais: " + myPreferences.getString("PAIS", "Pais") + "</p>"
                                + "<p>Provincia: " + myPreferences.getString("PROVINCIA", "Provincia") + "</p>"
                                + "<p>Ciudad: " + myPreferences.getString("CIUDAD", "Ciudad") + "</p>"
                                + "<p>Email: " + myPreferences.getString("EMAIL", "Email") + "</p>"
                                + "<p>Comentarios: " + coment + "</p><p></p><p>NO CONTESTAR ESTE MAIL</p>";
                        try {
                            gestor.main();
                            gestor.enviarMensaje(asunto, contenido);
                            //Toast.makeText(getContext(), "Comentarios enviados, gracias.", Toast.LENGTH_LONG).show();
                            Toaster.toast("Comentarios enviados, gracias.");

                        } catch (MessagingException e) {
                            e.printStackTrace();
                            envio[0] = false;
                        }
                    }
                };
                Thread t1 = new Thread(r);
                t1.start();


            }
        });

        nombre = (TextView) rootView.findViewById(R.id.textNombre);

        myPreferences = PreferenceManager.getDefaultSharedPreferences(container.getContext());
        nombre.setText(myPreferences.getString("NOMBRE", "No hay usuario registrado"));


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
