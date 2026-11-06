package cn.edu.sicnu.cardgame

import java.io.Serializable

/**
 * 模型：一张扑克牌。
 *
 * 实现 Serializable 后，整副牌可以随 [CardMatchingGame] 一起写入文件，
 * 退出应用后再次打开仍能恢复牌面状态。
 */
class Card(
    private var suit: String,
    private var rank: String,
    var isChosen: Boolean = false,
    var isMatched: Boolean = false
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L

        val rankStrings =
            arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    /**
     * 计算当前牌与 [otherCards] 能否匹配。
     *
     * 两张牌玩游戏时传入 1 张牌，三张牌玩游戏时传入 2 张牌：
     * 全部同点数返回 4 分，全部同花色返回 1 分，否则返回 0 分。
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
