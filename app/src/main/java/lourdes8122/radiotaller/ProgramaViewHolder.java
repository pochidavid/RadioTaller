package lourdes8122.radiotaller;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import lourdes8122.radiotaller.model.Programa;

public class ProgramaViewHolder extends ProgramacionViewHolder {

    private TextView mTextPrograma;

    public ProgramaViewHolder(@NonNull View itemView) {
        super(itemView);

        mTextPrograma = itemView.findViewById(R.id.tv_programa);
    }

    public void bindViewProgramaList(Programa programa) {
        mTextPrograma.setText(programa.getNombre());
    }
}
