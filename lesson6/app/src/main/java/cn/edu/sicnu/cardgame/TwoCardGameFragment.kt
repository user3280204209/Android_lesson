package cn.edu.sicnu.cardgame

/**
 * 两张牌翻牌游戏 Fragment。
 */
class TwoCardGameFragment : CardGameFragment() {
    override val cardsPerTurn = 2
    override val cardCount = 12
    override val gridColumns = 4
    override val hintTextRes = R.string.game_hint_two
}
