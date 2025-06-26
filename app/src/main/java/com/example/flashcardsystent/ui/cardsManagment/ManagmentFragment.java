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

/**
 * Fragment listing all flashcard decks. Users can tap a deck to manage its cards,
 * or long press to rename or delete the deck.
 */
public class ManagmentFragment extends Fragment {

    /** ViewModel providing access to deck operations */
    private DeckViewModel deckViewModel;

    /** Adapter displaying all decks in a list */
    private DeckAdapter adapter;

    /**
     * Inflates the layout containing the deck list and "add deck" button.
     *
     * @param inflater LayoutInflater used to inflate views
     * @param container Optional parent view
     * @param savedInstanceState previous saved state, if any
     * @return the root view of the fragment
     */
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
                        .setItems(new CharSequence[]{getString(R.string.rename), getString(R.string.delete_deck)}, (dialog, which) -> {
                            if (which == 0) {
                                showRenameDialog(deck);
                            } else {
                                new AlertDialog.Builder(requireContext())
                                        .setTitle(getString(R.string.delete_deck))
                                        .setMessage(getString(R.string.confirm_delete_deck, deck.name))
                                        .setPositiveButton(R.string.yes, (d, w) -> {
                                            deckViewModel.delete(deck);
                                            Toast.makeText(requireContext(), R.string.deck_deleted, Toast.LENGTH_SHORT).show();
                                        })
                                        .setNegativeButton(R.string.no, null)
                                        .show();
                            }
                        })
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), adapter::submitList);

        root.findViewById(R.id.button_add_deck)
                .setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.action_navigation_dashboard_to_addDeckFragment)
                );

        return root;
    }

    /**
     * Displays a dialog that allows the user to rename a deck.
     *
     * @param deck the deck to rename
     */
    private void showRenameDialog(Deck deck) {
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(deck.name);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.rename_deck)
                .setView(input)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (newName.isEmpty()) {
                        Toast.makeText(getContext(), R.string.name_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    } else {
                        deck.name = newName;
                        deckViewModel.update(deck);
                        Toast.makeText(requireContext(), getString(R.string.deck_renamed_to, newName), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
