package lourdes8122.radiotaller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import lourdes8122.radiotaller.model.Usuario;
import lourdes8122.radiotaller.repository.AppRepository;

public class ConfigUsuarioActivity extends FragmentActivity{

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

    SharedPreferences myPreferences;

    private AppRepository appRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_usuario);

        volver = (Button) findViewById(R.id.button_volver_usuario);
        guardar = (Button) findViewById(R.id.button_guardar_usuario);
        nombre = (EditText) findViewById(R.id.nombre_usuario);
        apellido = (EditText) findViewById(R.id.apellido_usuario);
        nacimiento = (EditText) findViewById(R.id.nacimiento_usuario);
        pais = (EditText) findViewById(R.id.pais_usuario);
        ciudad = (EditText) findViewById(R.id.ciudad_usuario);
        provincia = (EditText) findViewById(R.id.provincia_usuario);
        email = (EditText) findViewById(R.id.email_usuario);

        //seteo datos de usuario
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nombre.setText(myPreferences.getString("NOMBRE", "Nombre"));
        apellido.setText(myPreferences.getString("APELLIDO", "Apellido"));
        nacimiento.setText(myPreferences.getString("FECHA_NACIMIENTO", "Fecha de Nacimiento AAAA/MM/DD"));
        pais.setText(myPreferences.getString("PAIS", "Pais"));
        ciudad.setText(myPreferences.getString("CIUDAD", "Ciudad"));
        provincia.setText(myPreferences.getString("PROVINCIA", "Provincia"));
        email.setText(myPreferences.getString("EMAIL", "Email"));

        //int age = myPreferences.getInt("AGE", 0);
        //boolean isSingle = myPreferences.getBoolean("SINGLE?", false);


        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //finish();
              ConfigUsuarioActivity.super.onResume();
            }
        });


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = myPreferences.edit();

                    if(apellido.getText().toString().isEmpty()
                            || nombre.getText().toString().isEmpty()
                            || pais.getText().toString().isEmpty()
                            || ciudad.getText().toString().isEmpty()
                            || provincia.getText().toString().isEmpty()
                            || nacimiento.getText().toString().isEmpty()
                            || email.getText().toString().isEmpty())
                    {
                        Toast.makeText(ConfigUsuarioActivity.this,"Debe completar todos los campos", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        //validar formato de fecha
                        if(nacimiento.getText().toString().trim().length() != 10 || !nacimiento.getText().toString().contains("/")){
                            Toast.makeText(ConfigUsuarioActivity.this,"El Formato de Fecha debe ser AAAA/MM/DD", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //validar edad
                        if(edad()<EDAD_MINIMA){
                            Toast.makeText(ConfigUsuarioActivity.this,"Debe ser Mayor a " + EDAD_MINIMA +" Años", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //validar email
                        String mail = email.getText().toString().trim();
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if(!mail.matches(emailPattern) ){
                            Toast.makeText(ConfigUsuarioActivity.this,"Debe Ingresar un Correo Electrónico Válido",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ConfigUsuarioActivity.this,"Los datos se guardaron con éxito!",Toast.LENGTH_LONG).show();
                        finish();
                    }
            }
        });




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

}
