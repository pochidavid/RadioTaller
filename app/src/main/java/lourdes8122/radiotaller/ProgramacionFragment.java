package lourdes8122.radiotaller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lourdes8122.radiotaller.model.Programa;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramacionFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ProgramacionViewModel viewModel;
    private RecyclerView programas_recycler;
    private RecyclerView.Adapter adapter;
    private List<Programa> programacion = new ArrayList<>();

    public ProgramacionFragment() {
        // Required empty public constructor
    }

    public static ProgramacionFragment newInstance(String param1, String param2) {
        ProgramacionFragment fragment = new ProgramacionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RadioTallerApplication.getApp().getDataComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProgramacionViewModel.class);
        viewModel.init();


        viewModel.getProgramas().observe(this, new Observer<List<Programa>>() {
            @Override
            public void onChanged(@Nullable List<Programa> programas) {
                programacion.clear();
                programacion.addAll(programas);

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programacion, container, false);

        adapter = new ProgramacionAdapter(getContext(),programacion);
        programas_recycler = view.findViewById(R.id.programas_recycler);
        programas_recycler.setAdapter(adapter);
        programas_recycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        return view;
    }

}
