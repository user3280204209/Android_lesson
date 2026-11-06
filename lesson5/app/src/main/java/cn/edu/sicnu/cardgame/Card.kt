package cn.edu.sicnu.cardgame

/**
 * 模型：一张扑克牌。
 *
 * 实验五把游戏扩展为“一次选三张牌”：三张牌全部同点数返回 4 分，
 * 全部同花色返回 1 分，否则返回 0 分。
 */
class Card(
    private var suit: String,
    private var rank: String,
    var isChosen: Boolean = false,
    var isMatched: Boolean = false
) {
    companion object {
        val rankStrings = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    /**
     * 计算当前牌与 [otherCards] 能否匹配。
     *
     * 两张牌时沿用实验四的规则；三张牌时必须三张全部同点数或全部同花色。
     */
    fun match(otherCards: Array<Card>): Int {
        val allCards = listOf(this) + otherCards
        if (allCards.size < 2) {
            return 0
        }

        val allSameRank = allCards.all { it.rank == rank }
        val allSameSuit = allCards.all { it.suit == suit }
        return when {
            allSameRank -> 4
            allSameSuit -> 1
            else -> 0
        }
    }
}
