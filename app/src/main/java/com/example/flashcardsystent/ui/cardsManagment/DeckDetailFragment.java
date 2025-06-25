package com.example.flashcardsystent.ui.cardsManagment;

/**
 * Fragment showing details for a specific deck. It lists all cards in the deck
 * and allows users to swipe to delete or tap to edit. Comments describe the
 * setup of the RecyclerView and swipe actions.
 */

// Carries data needed when recreating the fragment
import android.os.Bundle;
// Used to inflate XML layout resources
import android.view.LayoutInflater;
// Base class for all visual components
import android.view.View;
// Container holding child views
import android.view.ViewGroup;
// Displays text strings
import android.widget.TextView;
// Shows short popup messages
import android.widget.Toast;

// Parameter cannot be null
import androidx.annotation.NonNull;
// Parameter may be null
import androidx.annotation.Nullable;
// Portion of UI contained in an activity
import androidx.fragment.app.Fragment;
// Factory for ViewModel instances
import androidx.lifecycle.ViewModelProvider;
// Utility for navigating between fragments
import androidx.navigation.Navigation;
// Adds support for swipe gestures on RecyclerView items
import androidx.recyclerview.widget.ItemTouchHelper;
// Positions items in a vertical scrolling list
import androidx.recyclerview.widget.LinearLayoutManager;
// RecyclerView displays scrollable lists of items
import androidx.recyclerview.widget.RecyclerView;

// Resource identifiers from XML
import com.example.flashcardsystent.R;
// Adapter used to present cards
import com.example.flashcardsystent.adapter.CardAdapter;
// Entity representing a card
import com.example.flashcardsystent.data.Card;
// ViewModel providing card database operations
import com.example.flashcardsystent.viewmodel.CardViewModel;

public class DeckDetailFragment extends Fragment {

    // ViewModel for interacting with card data
    private CardViewModel cardViewModel;
    // Adapter feeding cards into the RecyclerView
    private CardAdapter cardAdapter;
    // Identifier of the deck being displayed
    private int deckId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the deck detail layout
        return inflater.inflate(R.layout.fragment_deck_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get arguments specifying which deck to show
        Bundle args = getArguments();
        if (args != null) {
            deckId = args.getInt("deckId");
            String deckName = args.getString("deckName", "");
            ((TextView) view.findViewById(R.id.text_deck_name)).setText(deckName);
        }

        // Obtain ViewModel shared with the activity
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        // Set up RecyclerView to show the list of cards
        RecyclerView rv = view.findViewById(R.id.rv_cards);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Handle clicks to edit individual cards
        cardAdapter = new CardAdapter(card -> {
            Bundle bundle = new Bundle();
            bundle.putInt("cardId", card.id);
            bundle.putInt("deckId", deckId);
            Navigation.findNavController(view)
                    .navigate(R.id.action_deckDetailFragment_to_editCardFragment, bundle);
        });
        rv.setAdapter(cardAdapter);

        // Observe the deck's cards and display them
        cardViewModel.getCardsByDeck(deckId)
                .observe(getViewLifecycleOwner(), cardAdapter::submitList);

        // Allow swiping right to delete a card
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView rv,
                                          @NonNull RecyclerView.ViewHolder vh,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int direction) {
                        // Remove the swiped card from the database
                        int pos = vh.getAdapterPosition();
                        Card toDelete = cardAdapter.getItemAt(pos);
                        cardViewModel.delete(toDelete);
                        Toast.makeText(requireContext(),
                                R.string.card_deleted,
                                Toast.LENGTH_SHORT).show();
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rv);

        // Launch the bottom sheet to add new cards to this deck
        view.findViewById(R.id.button_add_cards)
                .setOnClickListener(v ->
                        AddCardsBottomSheet
                                .newInstance(deckId)
                                .show(getChildFragmentManager(), "ADD_CARDS")
                );
    }
}