package cn.edu.sicnu.cardgame

/**
 * 三张牌翻牌游戏 Fragment。
 */
class ThreeCardGameFragment : CardGameFragment() {
    override val cardsPerTurn = 3
    override val cardCount = 15
    override val gridColumns = 5
    override val hintTextRes = R.string.game_hint_three
}
