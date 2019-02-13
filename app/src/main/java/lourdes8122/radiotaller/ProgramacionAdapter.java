package lourdes8122.radiotaller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lourdes8122.radiotaller.model.Horario;
import lourdes8122.radiotaller.model.Programa;

public class ProgramacionAdapter extends RecyclerView.Adapter<ProgramacionViewHolder> {

    private List<Integer> itemType = new ArrayList<>();
    private List<Object> itemValue = new ArrayList<>();

    private static final int PROGRAMA_ITEM_VIEW = 1;
    private static final int HORARIO_ITEM_VIEW = 2;

    List<Programa> listaProgramacion;
    Context context;

    public ProgramacionAdapter(Context context,List<Programa> listaProgramacion) {
        this.context = context;
        this.listaProgramacion = listaProgramacion;
    }

    @NonNull
    @Override
    public ProgramacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if(viewType == PROGRAMA_ITEM_VIEW){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_programa, parent, false);
            ProgramaViewHolder vh = new ProgramaViewHolder(v);
            return vh;
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
            HorarioViewHolder vh = new HorarioViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramacionViewHolder holder, int position) {

        try {
            if(holder instanceof ProgramaViewHolder) {
                ProgramaViewHolder vh = (ProgramaViewHolder) holder;

                if(itemValue.get(position) instanceof  Programa){
                    Programa p = (Programa) itemValue.get(position);
                    vh.bindViewProgramaList(p);
                }

            } else if(holder instanceof HorarioViewHolder) {
                HorarioViewHolder vh = (HorarioViewHolder) holder;

                if(itemValue.get(position) instanceof Horario){
                    Horario h = (Horario) itemValue.get(position);
                    vh.bindViewHorarioList(h);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        int count = listaProgramacion.size();

        for(Programa p: listaProgramacion){

            count += p.getHorarios().size();


            itemType.add(PROGRAMA_ITEM_VIEW);
            itemValue.add(p);
            for (Horario h: p.getHorarios()){
                itemType.add(HORARIO_ITEM_VIEW);
                itemValue.add(h);
            }
        }

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return itemType.get(position);
    }

}
