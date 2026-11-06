package cn.edu.sicnu.cardgame

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CardTest {

    @Test
    fun twoCardsSameRankScoresFour() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♠", rank = "A")

        assertEquals(4, first.match(arrayOf(second)))
    }

    @Test
    fun twoCardsSameSuitScoresOne() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♥", rank = "K")

        assertEquals(1, first.match(arrayOf(second)))
    }

    @Test
    fun twoDifferentCardsScoreZero() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♣", rank = "K")

        assertEquals(0, first.match(arrayOf(second)))
    }

    @Test
    fun threeCardsSameRankScoresFour() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♠", rank = "A")
        val third = Card(suit = "♦", rank = "A")

        assertEquals(4, first.match(arrayOf(second, third)))
    }

    @Test
    fun threeCardsSameSuitScoresOne() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♥", rank = "K")
        val third = Card(suit = "♥", rank = "Q")

        assertEquals(1, first.match(arrayOf(second, third)))
    }

    @Test
    fun threeCardsWithoutCommonRankOrSuitScoreZero() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♠", rank = "A")
        val third = Card(suit = "♦", rank = "K")

        assertEquals(0, first.match(arrayOf(second, third)))
    }

    @Test
    fun matchWithNoOtherCardsScoresZero() {
        val card = Card(suit = "♥", rank = "A")

        assertEquals(0, card.match(emptyArray()))
    }
}

class DeckTest {

    @Test
    fun deckHasFiftyTwoDistinctCards() {
        val deck = Deck()
        val drawn = (1..52).mapNotNull { deck.drawRandomCard() }

        assertEquals(52, drawn.size)
        assertEquals(52, drawn.map { it.toString() }.distinct().size)
    }

    @Test
    fun drawingFromEmptyDeckReturnsNull() {
        val deck = Deck()
        repeat(52) { deck.drawRandomCard() }

        assertNull(deck.drawRandomCard())
    }
}

class CardMatchingGameTest {

    @Test
    fun newGameStartsWithZeroScoreAndTwentyFourCards() {
        val game = CardMatchingGame(24)

        assertEquals(0, game.score)
        assertEquals(24, game.numberOfCards)
    }

    @Test
    fun choosingCardCostsOnePointAndCanBeUndone() {
        val game = CardMatchingGame(24)

        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)

        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun resetRestoresZeroScoreAndClearsAllCards() {
        val game = CardMatchingGame(24)

        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)

        game.reset()

        assertEquals(0, game.score)
        assertEquals(24, game.numberOfCards)
        for (index in 0 until game.numberOfCards) {
            assertFalse(game.cardAtIndex(index).isChosen)
            assertFalse(game.cardAtIndex(index).isMatched)
        }
    }
}
