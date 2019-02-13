package lourdes8122.radiotaller;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import lourdes8122.radiotaller.model.Horario;

public class HorarioViewHolder extends ProgramacionViewHolder {

    private TextView mTextDia;
    private TextView mTextHora;
    private CheckBox mCheckNotification;

    public HorarioViewHolder(@NonNull View itemView) {
        super(itemView);

        mTextDia = itemView.findViewById(R.id.tv_dia);
        mTextHora = itemView.findViewById(R.id.tv_hora);
        mCheckNotification = itemView.findViewById(R.id.chk_notification);
    }

    public void bindViewHorarioList(Horario horario) {
        mTextDia.setText(horario.getDayOfWeekString());
        mTextHora.setText(horario.getTimeString());

        mCheckNotification.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mCheckNotification.isChecked()){

                        }else{

                        }
                    }
                }
        );
    }
}
