package lourdes8122.radiotaller;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import lourdes8122.radiotaller.model.Programa;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramacionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ProgramacionViewModel viewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProgramacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgramacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgramacionFragment newInstance(String param1, String param2) {
        ProgramacionFragment fragment = new ProgramacionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
                //update UI
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programacion, container, false);
    }

}
