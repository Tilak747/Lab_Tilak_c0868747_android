package c0868747.tilak.labtest.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import c0868747.tilak.labtest.R;
import c0868747.tilak.labtest.databinding.FragmentPlaceListBinding;
import c0868747.tilak.labtest.model.FavLocation;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlacesListFragment extends Fragment {

    FragmentPlaceListBinding binding = null;
    MainViewModel viewModel;
    FavPlacesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(binding == null){
            binding = FragmentPlaceListBinding.inflate(inflater,container,false);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FavPlaceListener listener = new FavPlaceListener() {
            @Override
            public void viewLocation(FavLocation location, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",location);
                Navigation.findNavController(requireActivity(), R.id.fragContainerView)
                        .navigate(R.id.action_placeListFragment_to_pickPlaceFragment,bundle);
            }

            @Override
            public void delete(FavLocation location, int position) {
                viewModel.deleteLocation(location);
            }
        };


        adapter = new FavPlacesAdapter(requireContext(),listener,new ArrayList<>());
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadData(s.toString());
            }
        });

        loadData("");

//        ArrayList<FavLocation> dummy = new ArrayList<>();
//        dummy.add(new FavLocation(1,0.0,0.0,"Home","Abc",100));
//        dummy.add(new FavLocation(2,0.0,0.0,"Work","asdas",100));
//        dummy.add(new FavLocation(3,0.0,0.0,"Other","bngh",100));
//        adapter.updateData(dummy);
//        adapter.notifyDataSetChanged();



    }

    String lastFilter = "";
    public void loadData(String filter){
        if(viewModel.getAllLocations(lastFilter).hasObservers()){
            viewModel.getAllLocations(lastFilter).removeObservers(requireActivity());
        }
        lastFilter = filter;
        viewModel.getAllLocations(filter).observe(requireActivity(), favLocations -> {
            adapter.updateData(favLocations);
            adapter.notifyDataSetChanged();
        });
    }

    private final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            requireActivity().finish();
        }
    };
    @Override
    public void onAttach(@NonNull Context context) {
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        callback.setEnabled(true);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        callback.setEnabled(false);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        callback.setEnabled(false);
        super.onDetach();
    }



}
