package com.example.flashcardsystent.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonModeClassic.setOnClickListener(v ->
                Toast.makeText(getContext(), "Tryb klasyczny (mock)", Toast.LENGTH_SHORT).show());

        binding.buttonQuizMode.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_quizSetListFragment);

        });

        binding.buttonModeTyping.setOnClickListener(v ->
                Toast.makeText(getContext(), "Tryb pisania (mock)", Toast.LENGTH_SHORT).show());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
