package c0868747.tilak.labtest.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import c0868747.tilak.labtest.R;
import c0868747.tilak.labtest.databinding.FragmentSplashBinding;

public class SplashFragment extends Fragment {

    FragmentSplashBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Navigation.findNavController(requireActivity(), R.id.fragContainerView).navigate(R.id.action_splashFragment_to_placeListFragment);
        },500);
    }
}
