package lourdes8122.radiotaller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class dialogo_acerca_de extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedIntanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Acerca de");
        builder.setMessage("App creada por Alexis Mandracchia, Julian Sanchez y Gisel Casco. ©2019. Todos los derechos reservados.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancelar", null);

        return builder.create();
    }
}
