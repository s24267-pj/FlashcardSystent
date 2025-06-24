package com.example.flashcardsystent.ui.cardsManagment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.DeckAdapter;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class ManagmentFragment extends Fragment {

    private DeckViewModel deckViewModel;
    DeckAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cards_managment, container, false);

        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        RecyclerView recyclerView = root.findViewById(R.id.deck_list);
        adapter = new DeckAdapter(new DeckAdapter.OnDeckClickListener() {
            @Override
            public void onDeckClick(Deck deck) {
                Bundle bundle = new Bundle();
                bundle.putInt("deckId", deck.id);
                bundle.putString("deckName", deck.name);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_dashboard_to_deckDetailFragment, bundle);
            }

            @Override
            public void onDeckLongClick(Deck deck) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(deck.name)
                        .setItems(new CharSequence[]{"Zmień nazwę", "Usuń zestaw"}, (dialog, which) -> {
                            if (which == 0) {
                                showRenameDialog(deck);
                            } else {
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("Usuń zestaw")
                                        .setMessage("Czy na pewno chcesz usunąć zestaw \"" + deck.name + "\"?")
                                        .setPositiveButton("Tak", (d, w) -> {
                                            deckViewModel.delete(deck);
                                            Toast.makeText(getContext(), "Zestaw usunięty", Toast.LENGTH_SHORT).show();
                                        })
                                        .setNegativeButton("Nie", null)
                                        .show();
                            }
                        })
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);
        deckViewModel.getAllDecks()
                .observe(getViewLifecycleOwner(), adapter::submitList);

        root.findViewById(R.id.button_add_deck)
                .setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.action_navigation_dashboard_to_addDeckFragment)

                );

        return root;
    }

    private void showRenameDialog(Deck deck) {
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(deck.name);

        new AlertDialog.Builder(requireContext())
                .setTitle("Zmień nazwę zestawu")
                .setView(input)
                .setPositiveButton("Zapisz", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (newName.isEmpty()) {
                        Toast.makeText(getContext(), "Nazwa nie może być pusta", Toast.LENGTH_SHORT).show();
                    } else {
                        deck.name = newName;
                        deckViewModel.update(deck);
                        Toast.makeText(getContext(), "Zmieniono nazwę na \"" + newName + "\"", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Anuluj", null)
                .show();
    }
}
