package com.example.flashcardsystent.ui.cardsManagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.CardAdapter;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;

/**
 * Fragment displaying all flashcards from a specific deck.
 * Allows swiping to delete and tapping to edit individual cards.
 */
public class DeckDetailFragment extends Fragment {

    /** ViewModel for managing card data */
    private CardViewModel cardViewModel;

    /** Adapter for displaying the list of cards */
    private CardAdapter cardAdapter;

    /** ID of the deck being shown */
    private int deckId;

    /**
     * Inflates the layout for the deck detail screen.
     *
     * @param inflater LayoutInflater used to inflate the view
     * @param container Optional parent view
     * @param savedInstanceState Previous saved state
     * @return root view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_detail, container, false);
    }

    /**
     * Called after the view has been created. Sets up RecyclerView, swipe logic,
     * and button listeners.
     *
     * @param view root view of the fragment
     * @param savedInstanceState previous state if any
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            deckId = args.getInt("deckId");
            String deckName = args.getString("deckName", "");
            ((TextView) view.findViewById(R.id.text_deck_name)).setText(deckName);
        }

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        RecyclerView rv = view.findViewById(R.id.rv_cards);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        cardAdapter = new CardAdapter(card -> {
            Bundle bundle = new Bundle();
            bundle.putInt("cardId", card.id);
            bundle.putInt("deckId", deckId);
            Navigation.findNavController(view)
                    .navigate(R.id.action_deckDetailFragment_to_editCardFragment, bundle);
        });
        rv.setAdapter(cardAdapter);

        cardViewModel.getCardsByDeck(deckId)
                .observe(getViewLifecycleOwner(), cardAdapter::submitList);

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
                        int pos = vh.getAdapterPosition();
                        Card toDelete = cardAdapter.getItemAt(pos);
                        cardViewModel.delete(toDelete);
                        Toast.makeText(requireContext(),
                                R.string.card_deleted,
                                Toast.LENGTH_SHORT).show();
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rv);

        view.findViewById(R.id.button_add_cards)
                .setOnClickListener(v ->
                        AddCardsBottomSheet
                                .newInstance(deckId)
                                .show(getChildFragmentManager(), "ADD_CARDS"));
    }
}
