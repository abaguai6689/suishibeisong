package com.suishibeisong.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suishibeisong.flashcards.data.FlashcardRepository
import com.suishibeisong.flashcards.data.ProgressRules
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GroupViewModel(
    repo: FlashcardRepository,
    private val group: String,
) : ViewModel() {
    val items: StateFlow<List<DeckItem>> = repo.state
        .map { state ->
            state.decks.filter { it.group == group }.map { deck ->
                DeckItem(
                    deck = deck,
                    cards = deck.cards.size,
                    known = ProgressRules.deckKnown(deck, state.stats),
                    due = ProgressRules.deckDue(deck, state.stats),
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
