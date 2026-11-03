package cn.edu.sicnu.cardgame

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cardgame.databinding.ActivityCardGameBinding

/**
 * 翻牌游戏控制器：接收用户点击，调用 [CardMatchingGame] 模型并刷新界面。
 */
class CardGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardGameBinding
    private lateinit var game: CardMatchingGame
    private val cardViews = mutableListOf<TextView>()
    private var highestScore = 0
    private var gameOverAnnounced = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.title_card_game)

        highestScore = intent.getIntExtra(EXTRA_HIGH_SCORE, 0)
        game = CardMatchingGame(CARD_COUNT)

        createCardViews()
        binding.btnReset.setOnClickListener { resetGame() }
        binding.btnExit.setOnClickListener { finish() }

        updateUI()
    }

    private fun createCardViews() {
        val cardWidth = dp(90)
        val cardHeight = dp(118)
        val margin = dp(5)

        for (index in 0 until CARD_COUNT) {
            val cardView = TextView(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = cardWidth
                    height = cardHeight
                    setMargins(margin, margin, margin, margin)
                }
                gravity = Gravity.CENTER
                textSize = 22f
                setTypeface(typeface, Typeface.BOLD)
                isClickable = true
                isFocusable = true
                elevation = dp(2).toFloat()
                contentDescription = getString(R.string.card_description, index + 1)
                setOnClickListener { chooseCard(index) }
            }
            binding.cardGrid.addView(cardView)
            cardViews.add(cardView)
        }
    }

    private fun chooseCard(index: Int) {
        game.chooseCardAtIndex(index)
        highestScore = maxOf(highestScore, game.score)
        updateUI()

        if (!gameOverAnnounced && (0 until game.numberOfCards).all {
                game.cardAtIndex(it).isMatched
            }) {
            gameOverAnnounced = true
            Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetGame() {
        game = CardMatchingGame(CARD_COUNT)
        gameOverAnnounced = false
        updateUI()
    }

    private fun updateUI() {
        binding.scoreText.text = getString(R.string.score_format, game.score)

        for (index in cardViews.indices) {
            val card = game.cardAtIndex(index)
            val view = cardViews[index]
            when {
                card.isMatched -> {
                    view.text = card.toString()
                    view.setTextColor(getColor(R.color.text_primary))
                    view.setBackgroundResource(R.drawable.bg_card_matched)
                    view.alpha = 0.75f
                }
                card.isChosen -> {
                    view.text = card.toString()
                    view.setTextColor(getColor(R.color.text_primary))
                    view.setBackgroundResource(R.drawable.bg_card_chosen)
                    view.alpha = 1f
                }
                else -> {
                    view.text = getString(R.string.card_back)
                    view.setTextColor(Color.WHITE)
                    view.setBackgroundResource(R.drawable.bg_card_back)
                    view.alpha = 1f
                }
            }
        }

        setResult(
            RESULT_OK,
            Intent().putExtra(EXTRA_HIGH_SCORE, highestScore)
        )
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    companion object {
        const val EXTRA_HIGH_SCORE = "extra_high_score"
        private const val CARD_COUNT = 12
    }
}
