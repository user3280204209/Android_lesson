package cn.edu.sicnu.cardgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.sicnu.cardgame.databinding.ActivityCardGameBinding

/**
 * 控制器：接收 RecyclerView 适配器回调的点击事件，调用 [CardMatchingGame]，
 * 再把最新状态交给适配器刷新界面。
 *
 * 牌数和列数来自资源文件：竖屏 24 张（4 列），横屏 30 张（10 列）。
 */
class CardGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardGameBinding
    private lateinit var game: CardMatchingGame
    private lateinit var cardAdapter: CardAdapter

    private var highestScore = 0
    private var gameOverAnnounced = false
    private var stuckAnnounced = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.title_card_game)

        highestScore = intent.getIntExtra(EXTRA_HIGH_SCORE, 0)

        val cardCount = resources.getInteger(R.integer.card_count)
        val columns = resources.getInteger(R.integer.grid_columns)

        game = CardMatchingGame(cardCount)
        cardAdapter = CardAdapter(::chooseCard)

        binding.cardList.layoutManager = GridLayoutManager(this, columns)
        binding.cardList.setHasFixedSize(true)
        binding.cardList.adapter = cardAdapter

        binding.btnReset.setOnClickListener { resetGame() }
        binding.btnExit.setOnClickListener { finish() }

        updateUI()
    }

    private fun chooseCard(index: Int) {
        game.chooseCardAtIndex(index)
        highestScore = maxOf(highestScore, game.score)
        updateUI()
    }

    private fun resetGame() {
        game.reset()
        gameOverAnnounced = false
        stuckAnnounced = false
        updateUI()
    }

    private fun updateUI() {
        binding.scoreText.text = getString(R.string.score_format, game.score)

        val cards = (0 until game.numberOfCards).map { game.cardAtIndex(it) }
        cardAdapter.submitCards(cards)

        setResult(
            RESULT_OK,
            Intent().putExtra(EXTRA_HIGH_SCORE, highestScore)
        )

        val allMatched = cards.all { it.isMatched }
        when {
            allMatched && !gameOverAnnounced -> {
                gameOverAnnounced = true
                Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show()
            }

            !allMatched && !stuckAnnounced && !hasAvailableMatch() -> {
                stuckAnnounced = true
                Toast.makeText(this, R.string.no_more_moves, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 判断剩余未匹配的牌中是否还存在可以匹配的三张牌。
     * 牌数最多 30 张，三重循环的开销可以忽略。
     */
    private fun hasAvailableMatch(): Boolean {
        val available = (0 until game.numberOfCards)
            .map { game.cardAtIndex(it) }
            .filter { !it.isMatched }

        for (i in available.indices) {
            for (j in i + 1 until available.size) {
                for (k in j + 1 until available.size) {
                    if (available[i].match(
                            arrayOf(available[j], available[k])
                        ) > 0
                    ) {
                        return true
                    }
                }
            }
        }
        return false
    }

    companion object {
        const val EXTRA_HIGH_SCORE = "extra_high_score"
    }
}
