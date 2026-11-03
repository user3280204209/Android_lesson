package cn.edu.sicnu.cardgame

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CardTest {

    @Test
    fun sameRankScoresFour() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♠", rank = "A")

        assertEquals(4, first.match(arrayOf(second)))
    }

    @Test
    fun sameSuitScoresOne() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♥", rank = "K")

        assertEquals(1, first.match(arrayOf(second)))
    }

    @Test
    fun differentRankAndSuitScoresZero() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♣", rank = "K")

        assertEquals(0, first.match(arrayOf(second)))
    }

    @Test
    fun matchRequiresExactlyOneOtherCard() {
        val first = Card(suit = "♥", rank = "A")
        val second = Card(suit = "♠", rank = "A")
        val third = Card(suit = "♦", rank = "A")

        assertEquals(0, first.match(emptyArray()))
        assertEquals(0, first.match(arrayOf(second, third)))
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
    fun newGameStartsWithZeroScoreAndTwelveCards() {
        val game = CardMatchingGame(12)

        assertEquals(0, game.score)
        assertEquals(12, game.numberOfCards)
    }

    @Test
    fun choosingCardCostsOnePointAndCanBeUndone() {
        val game = CardMatchingGame(12)

        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)

        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun matchingTwoCardsAddsBonusAndMarksBothMatched() {
        val game = CardMatchingGame(12)

        var first = -1
        var second = -1
        outer@ for (i in 0 until game.numberOfCards) {
            for (j in i + 1 until game.numberOfCards) {
                if (game.cardAtIndex(i).match(arrayOf(game.cardAtIndex(j))) > 0) {
                    first = i
                    second = j
                    break@outer
                }
            }
        }

        assertTrue("12 张牌中必定存在花色相同的牌", first >= 0)
        game.chooseCardAtIndex(first)
        val scoreBeforeMatch = game.score
        game.chooseCardAtIndex(second)

        assertTrue(game.score > scoreBeforeMatch)
        assertTrue(game.cardAtIndex(first).isMatched)
        assertTrue(game.cardAtIndex(second).isMatched)
    }
}
