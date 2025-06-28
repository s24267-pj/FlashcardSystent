// Pakiet danych – zawiera repozytoria, DAO i modele powiązane z bazą danych
package com.example.flashcardsystent.data;

// Importy potrzebne do działania repozytorium
import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Klasa `CardRepository` pełni rolę pośrednika (warstwy pośredniej) pomiędzy bazą danych
 * a ViewModelem lub UI. Umożliwia bezpieczny, asynchroniczny dostęp do danych fiszek.
 *
 * Jest to część tzw. architektury MVVM – Model-View-ViewModel.
 */
public class CardRepository {

    /**
     * Obiekt DAO odpowiedzialny za bezpośredni dostęp do danych fiszek w bazie danych.
     * Zawiera metody takie jak insert, update, delete, query...
     */
    private final CardDao cardDao;

    /**
     * ExecutorService to narzędzie służące do wykonywania zadań w tle (na osobnym wątku).
     * W tym przypadku – do wykonywania operacji na bazie danych poza głównym wątkiem UI.
     * Używamy `singleThreadExecutor()`, co oznacza jeden wątek roboczy w tle.
     */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Konstruktor repozytorium – tworzy dostęp do bazy danych na podstawie kontekstu aplikacji.
     * Dzięki temu unika się wycieków pamięci (bo nie używa się kontekstu Activity).
     *
     * @param app obiekt aplikacji – wykorzystywany do zainicjowania bazy danych
     */
    public CardRepository(Application app) {
        // Pobieramy instancję bazy danych
        AppDatabase db = AppDatabase.getInstance(app);

        // Z tej instancji pobieramy DAO do fiszek
        cardDao = db.cardDao();
    }

    /**
     * Zwraca obserwowalną listę fiszek przypisanych do danego zestawu (deckId).
     * `LiveData` pozwala komponentom UI reagować automatycznie na zmiany danych.
     *
     * @param deckId identyfikator zestawu, którego fiszki chcemy pobrać
     * @return LiveData z listą fiszek z danego zestawu
     */
    public LiveData<List<Card>> getCardsByDeck(int deckId) {
        return cardDao.getCardsByDeck(deckId);
    }

    /**
     * Wstawia fiszkę do bazy danych, wykonując operację w tle.
     * Dzięki temu nie blokujemy głównego wątku aplikacji (czyli UI).
     *
     * @param card obiekt fiszki do dodania
     */
    public void insert(Card card) {
        executor.execute(() -> cardDao.insert(card));
    }

    /**
     * Aktualizuje dane fiszki w bazie, również w tle.
     *
     * @param card obiekt fiszki zawierający zaktualizowane dane
     */
    public void update(Card card) {
        executor.execute(() -> cardDao.update(card));
    }

    /**
     * Usuwa wskazaną fiszkę z bazy danych w tle.
     *
     * @param card obiekt fiszki do usunięcia
     */
    public void delete(Card card) {
        executor.execute(() -> cardDao.delete(card));
    }
}
