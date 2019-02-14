package lourdes8122.radiotaller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ConfiguracionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Boolean PERFIL_USUARIO = false;
    private Boolean APARIENCIA = false;
    private LayoutInflater mInflater;
    private ViewGroup mContainer;
    private ViewGroup placeholder=null;
    private Button btnPerfil;
    private Button btnApariencia;

    //Perfil de Usuario:
    private static final int ID_USUARIO = 1;
    private static final int EDAD_MINIMA = 10;
    private EditText nombre;
    private EditText apellido;
    private EditText nacimiento;
    private EditText pais;
    private EditText ciudad;
    private EditText provincia;
    private EditText email;
    private Button volver;
    private Button guardar;
    private int edad;

    //Apariencia
    private ListView themes;
    private ArrayAdapter<String> adapter_themes;
    private String[] datos = new String[]{"Classic","Action","BarOverlay","PopupOverlay"};

    SharedPreferences myPreferences;

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
    public View onCreateView( LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;

        getDefaultView();
        return placeholder;
    }

    private void getPerfilUsuario(){
        View newView = mInflater.inflate(R.layout.activity_config_usuario, mContainer, false);
        placeholder.removeAllViews();
        placeholder.addView(newView);
        volver = newView.findViewById(R.id.button_volver_usuario);
        guardar =  newView.findViewById(R.id.button_guardar_usuario);
        nombre = newView.findViewById(R.id.nombre_usuario);
        apellido =  newView.findViewById(R.id.apellido_usuario);
        nacimiento =  newView.findViewById(R.id.nacimiento_usuario);
        pais =  newView.findViewById(R.id.pais_usuario);
        ciudad =  newView.findViewById(R.id.ciudad_usuario);
        provincia =  newView.findViewById(R.id.provincia_usuario);
        email =  newView.findViewById(R.id.email_usuario);

        //seteo datos de usuario
        nombre.setText(myPreferences.getString("NOMBRE", "Nombre"));
        apellido.setText(myPreferences.getString("APELLIDO", "Apellido"));
        nacimiento.setText(myPreferences.getString("FECHA_NACIMIENTO", "Fecha de Nacimiento AAAA/MM/DD"));
        pais.setText(myPreferences.getString("PAIS", "Pais"));
        ciudad.setText(myPreferences.getString("CIUDAD", "Ciudad"));
        provincia.setText(myPreferences.getString("PROVINCIA", "Provincia"));
        email.setText(myPreferences.getString("EMAIL", "Email"));

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                getDefaultView();
            }
        });


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarUsuario();
                getDefaultView();
            }
        });
    }
    private void getDefaultView(){
        View rootView = mInflater.inflate(R.layout.fragment_configuracion, mContainer, false);
        if(placeholder==null){
            placeholder = (ViewGroup) rootView;
        }
        else{
            placeholder.removeAllViews();
            placeholder.addView(rootView);
        }
        myPreferences = PreferenceManager.getDefaultSharedPreferences(mContainer.getContext());
        btnPerfil = rootView.findViewById(R.id.perfil_usuario);
        btnApariencia = rootView.findViewById(R.id.apariencia);

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPerfilUsuario();}
        });
        btnApariencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApariencia();
            }

        });
    }


    private void getApariencia(){

        View newView = mInflater.inflate(R.layout.config_apariencia, mContainer, false);
        placeholder.removeAllViews();
        placeholder.addView(newView);
        volver=(Button) newView.findViewById(R.id.btn_volver);
        themes = (ListView) newView.findViewById(R.id.list_themes);
        adapter_themes = new ArrayAdapter (getContext(), android.R.layout.simple_list_item_1,datos);
        themes.setAdapter(adapter_themes);
        SharedPreferences.Editor editor = myPreferences.edit();

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                getDefaultView();
            }
        });
        themes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (adapter_themes.getItem(position).toString()){
                    case "Classic":
                        editor.putInt("THEME", R.style.AppTheme);
                        break;
                    case "Action":
                        editor.putInt("THEME", R.style.NoActionBar);
                        break;
                    case "BarOverlay":
                        editor.putInt("THEME", R.style.AppBarOverlay);
                        break;
                    case "PopupOverlay":
                        editor.putInt("THEME", R.style.PopupOverlay);
                        break;

                }editor.commit();
                Toast.makeText(getContext(),"El tema seleccionado se visualizara al abrir la aplicación",Toast.LENGTH_LONG).show();

            }
        });
    }
    private void actualizarUsuario(){

        SharedPreferences.Editor editor = myPreferences.edit();

        if(apellido.getText().toString().isEmpty()
                || nombre.getText().toString().isEmpty()
                || pais.getText().toString().isEmpty()
                || ciudad.getText().toString().isEmpty()
                || provincia.getText().toString().isEmpty()
                || nacimiento.getText().toString().isEmpty()
                || email.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(),"Debe completar todos los campos", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            //validar formato de fecha
            if(nacimiento.getText().toString().trim().length() != 10 || !nacimiento.getText().toString().contains("/")){
                Toast.makeText(getContext(),"El Formato de Fecha debe ser AAAA/MM/DD", Toast.LENGTH_LONG).show();
                return;
            }
            //validar edad
            if(edad()<EDAD_MINIMA){
                Toast.makeText(getContext(),"Debe ser Mayor a " + EDAD_MINIMA +" Años", Toast.LENGTH_LONG).show();
                return;
            }
            //validar email
            String mail = email.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(!mail.matches(emailPattern) ){
                Toast.makeText(getContext(),"Debe Ingresar un Correo Electrónico Válido",Toast.LENGTH_LONG).show();
                return;
            }

            editor.putString("NOMBRE", nombre.getText().toString());
            editor.putString("APELLIDO", apellido.getText().toString());
            editor.putString("FECHA_NACIMIENTO", nacimiento.getText().toString());
            editor.putString("PAIS", pais.getText().toString());
            editor.putString("CIUDAD", ciudad.getText().toString());
            editor.putString("PROVINCIA", provincia.getText().toString());
            editor.putString("EMAIL", email.getText().toString());
            //editor.putInt("AGE", 25);
            //editor.putBoolean("SINGLE?", true);
            editor.commit();
            Toast.makeText(getContext(),"Los datos se guardaron con éxito!",Toast.LENGTH_LONG).show();

        }
    }

    private int edad( ){
        //para restringir menore de 10 años como ejemplo
        String[] fecha_ingresada = nacimiento.getText().toString().split("/");

        int valor_anio = Integer.valueOf(fecha_ingresada[0]);
        int valor_mes = Integer.valueOf(fecha_ingresada[1]);
        int valor_dias = Integer.valueOf(fecha_ingresada[2]);

        if(fecha_ingresada.length == 3
                && valor_anio > 1900 && valor_anio <= Calendar.getInstance().get(Calendar.YEAR)
                && valor_mes > 1 && valor_mes < 13
                && valor_dias >1 && valor_dias < 32)
        {
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.set(valor_anio, valor_mes, valor_dias);
            int cumpleanio = dob.get(Calendar.DAY_OF_YEAR);
            int dia_hoy = today.get(Calendar.DAY_OF_YEAR);

            edad = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (dia_hoy < cumpleanio) {
                if ((cumpleanio - dia_hoy) > (today.getActualMaximum(Calendar.DAY_OF_YEAR) / 3)) {
                    edad--;
                }
            }
        }
        else {
            edad = 0;
        }

        return edad;
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
