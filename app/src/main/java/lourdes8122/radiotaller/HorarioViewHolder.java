package lourdes8122.radiotaller;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import lourdes8122.radiotaller.model.Horario;

public class HorarioViewHolder extends ProgramacionViewHolder {

    private TextView mTextDia;
    private TextView mTextHora;

    public HorarioViewHolder(@NonNull View itemView) {
        super(itemView);

        mTextDia = itemView.findViewById(R.id.tv_dia);
        mTextHora = itemView.findViewById(R.id.tv_hora);
    }

    public void bindViewHorarioList(Horario horario) {
        mTextDia.setText(horario.getDayOfWeekString());
        mTextHora.setText(horario.getTimeString());
    }
}
