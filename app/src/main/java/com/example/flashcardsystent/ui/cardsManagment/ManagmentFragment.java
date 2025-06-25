package com.example.flashcardsystent.ui.cardsManagment;

/**
 * Fragment listing all existing decks. Users can tap a deck to view its cards
 * or long press to rename or delete it. Inline comments outline each setup
 * step and dialog interaction.
 */

// Bundle used to pass state information
import android.os.Bundle;
// Type constant for configuring EditText
import android.text.InputType;
// Inflates XML layouts
import android.view.LayoutInflater;
// Base UI element type
import android.view.View;
// Container for other views
import android.view.ViewGroup;
// Widget used for text input
import android.widget.EditText;
// Quick popup message to the user
import android.widget.Toast;

// Indicates parameters cannot be null
import androidx.annotation.NonNull;
// Dialog builder from the support library
import androidx.appcompat.app.AlertDialog;
// Fragment base class
import androidx.fragment.app.Fragment;
// Factory for acquiring ViewModel instances
import androidx.lifecycle.ViewModelProvider;
// Helper class for fragment navigation
import androidx.navigation.Navigation;
// Displays a list of scrollable items
import androidx.recyclerview.widget.RecyclerView;

// Resource identifiers
import com.example.flashcardsystent.R;
// Adapter presenting deck rows
import com.example.flashcardsystent.adapter.DeckAdapter;
// Entity representing a deck of cards
import com.example.flashcardsystent.data.Deck;
// ViewModel handling deck operations
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class ManagmentFragment extends Fragment {

    // ViewModel shared with the activity for deck data
    private DeckViewModel deckViewModel;
    // Adapter supplying deck items to the RecyclerView
    DeckAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout containing the RecyclerView
        View root = inflater.inflate(R.layout.fragment_cards_managment, container, false);

        // Obtain a ViewModel instance scoped to this fragment
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        // Set up the RecyclerView and its adapter
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
                // Show options to rename or delete the deck
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
        deckViewModel.getAllDecks()
                .observe(getViewLifecycleOwner(), adapter::submitList);

        // Navigate to the screen for creating a new deck
        root.findViewById(R.id.button_add_deck)
                .setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.action_navigation_dashboard_to_addDeckFragment)

                );

        return root;
    }

    // Helper method displaying a dialog where the user can rename a deck
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