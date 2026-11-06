package cn.edu.sicnu.cardgame

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

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
    fun newTwoCardGameStartsWithZeroScoreAndTwelveCards() {
        val game = CardMatchingGame(12, 2)

        assertEquals(0, game.score)
        assertEquals(12, game.numberOfCards)
        assertEquals(2, game.cardsPerTurn)
    }

    @Test
    fun choosingCardCostsOnePointAndCanBeUndone() {
        val game = CardMatchingGame(12, 2)

        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)

        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun resetRestoresZeroScoreAndClearsAllCards() {
        val game = CardMatchingGame(15, 3)

        game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)

        game.reset()

        assertEquals(0, game.score)
        assertEquals(15, game.numberOfCards)
        for (index in 0 until game.numberOfCards) {
            assertFalse(game.cardAtIndex(index).isChosen)
            assertFalse(game.cardAtIndex(index).isMatched)
        }
    }

    @Test
    fun gameCanBeSerializedAndRestored() {
        val game = CardMatchingGame(15, 3)
        game.chooseCardAtIndex(0)

        val bytes = ByteArrayOutputStream().use { output ->
            ObjectOutputStream(output).use { it.writeObject(game) }
            output.toByteArray()
        }
        val restored = ObjectInputStream(ByteArrayInputStream(bytes)).use {
            it.readObject() as CardMatchingGame
        }

        assertEquals(game.score, restored.score)
        assertEquals(game.numberOfCards, restored.numberOfCards)
        assertEquals(game.cardsPerTurn, restored.cardsPerTurn)
        assertTrue(restored.cardAtIndex(0).isChosen)
    }
}

class ProfileTest {

    @Test
    fun profileCanBeSerializedAndRestored() {
        val profile = Profile(
            name = "测试同学",
            gender = "男",
            studentId = "2024080616",
            phone = "13800000000",
            email = "test@example.com",
            hobbies = arrayListOf("阅读", "游戏"),
            highScoreTwo = 12,
            highScoreThree = 20
        )

        val bytes = ByteArrayOutputStream().use { output ->
            ObjectOutputStream(output).use { it.writeObject(profile) }
            output.toByteArray()
        }
        val restored = ObjectInputStream(ByteArrayInputStream(bytes)).use {
            it.readObject() as Profile
        }

        assertEquals(profile, restored)
    }
}
