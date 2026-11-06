package cn.edu.sicnu.cardgame

/**
 * 模型：翻牌游戏。负责发牌、计分与三张牌的匹配判断。
 */
class CardMatchingGame(private val count: Int) {

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
     * 实验五新增：重置游戏，重新发牌并把分数清零。
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
     * 已选中的牌可以再次点击取消选择。当桌面上已有两张未匹配的选中牌时，
     * 新选中的牌作为第三张，三张牌一起参与匹配判断。
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
        if (chosenCards.size == 3) {
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
