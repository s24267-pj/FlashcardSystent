package com.example.flashcardsystent.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;

public class BrowseSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button playAgain = view.findViewById(R.id.button_play_again);
        Button goHome = view.findViewById(R.id.button_go_home);

        playAgain.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_browseSummaryFragment_to_browseSetListFragment));
        goHome.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_home));
    }
}