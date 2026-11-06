package cn.edu.sicnu.cardgame

import java.io.Serializable

/**
 * 模型：翻牌游戏。
 *
 * [cardsPerTurn] 为 2 表示两张牌玩法，为 3 表示三张牌玩法。
 * 实现 Serializable 后，整个游戏对象可以写入文件，实现历史进度恢复。
 */
class CardMatchingGame(
    val count: Int,
    val cardsPerTurn: Int
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    var score = 0
        private set

    private val cards = mutableListOf<Card>()

    val numberOfCards: Int
        get() = cards.size

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    init {
        dealCards()
    }

    /**
     * 重置游戏：重新发牌并把分数清零。
     */
    fun reset() {
        score = 0
        dealCards()
    }

    fun cardAtIndex(index: Int): Card {
        return cards[index]
    }

    /**
     * 选择一张牌。
     *
     * 已选中的牌可以再次点击取消选择。当已选中的牌达到 [cardsPerTurn] 张时，
     * 这些牌一起参与匹配判断：成功则标记为已匹配，失败则取消选择。
     */
    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched) {
            return
        }

        if (card.isChosen) {
            card.isChosen = false
            return
        }

        card.isChosen = true
        score -= COST_TO_CHOOSE

        val chosenCards = cards.filter { it.isChosen && !it.isMatched }
        if (chosenCards.size == cardsPerTurn) {
            val matchScore = chosenCards[0].match(chosenCards.drop(1).toTypedArray())
            if (matchScore > 0) {
                score += matchScore * MATCH_BONUS
                for (chosenCard in chosenCards) {
                    chosenCard.isMatched = true
                    chosenCard.isChosen = false
                }
            } else {
                score -= MISMATCH_PENALTY
                for (chosenCard in chosenCards) {
                    chosenCard.isChosen = false
                }
            }
        }
    }

    private fun dealCards() {
        cards.clear()
        val deck = Deck()
        repeat(count) {
            val card = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }
}
