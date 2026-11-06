package cn.edu.sicnu.cardgame

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.sicnu.cardgame.databinding.FragmentCardGameBinding

/**
 * 翻牌游戏 Fragment 的公共实现。
 *
 * 子类只负责给出牌数、每轮翻牌张数和列数；保存与恢复逻辑统一在这里完成。
 * 离开页面时（onStop）自动把游戏对象序列化到文件，再次进入时自动恢复。
 */
abstract class CardGameFragment : Fragment() {

    /** 每轮翻牌张数：2 或 3。 */
    protected abstract val cardsPerTurn: Int

    /** 初始牌数。 */
    protected abstract val cardCount: Int

    /** 网格列数。 */
    protected abstract val gridColumns: Int

    /** 玩法提示文字。 */
    protected abstract val hintTextRes: Int

    private var _binding: FragmentCardGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var game: CardMatchingGame
    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedGame = GameStorage.loadGame(requireContext(), cardsPerTurn)
        game = savedGame ?: CardMatchingGame(cardCount, cardsPerTurn)
        binding.tvSaveState.setText(
            if (savedGame != null) R.string.state_restored else R.string.state_new_game
        )

        cardAdapter = CardAdapter(::chooseCard)
        binding.cardList.layoutManager = GridLayoutManager(requireContext(), gridColumns)
        binding.cardList.setHasFixedSize(true)
        binding.cardList.adapter = cardAdapter

        binding.gameHint.setText(hintTextRes)
        binding.btnReset.setOnClickListener {
            game.reset()
            binding.tvSaveState.setText(R.string.state_new_game)
            updateUI()
        }
        binding.btnSave.setOnClickListener {
            saveGame()
            Toast.makeText(
                requireContext(),
                R.string.progress_saved,
                Toast.LENGTH_SHORT
            ).show()
            binding.tvSaveState.setText(R.string.state_restored)
        }

        updateUI()
    }

    private fun chooseCard(index: Int) {
        game.chooseCardAtIndex(index)
        updateUI()
    }

    private fun updateUI() {
        binding.scoreText.text = getString(R.string.score_format, game.score)
        val cards = (0 until game.numberOfCards).map { game.cardAtIndex(it) }
        cardAdapter.submitCards(cards)
    }

    private fun saveGame() {
        val context = context ?: return
        GameStorage.saveGame(context, cardsPerTurn, game)
        updateHighScore(context)
    }

    private fun updateHighScore(context: Context) {
        val profile = GameStorage.loadProfile(context) ?: Profile.default(context)
        if (cardsPerTurn == 2) {
            profile.highScoreTwo = maxOf(profile.highScoreTwo, game.score)
        } else {
            profile.highScoreThree = maxOf(profile.highScoreThree, game.score)
        }
        GameStorage.saveProfile(context, profile)
    }

    override fun onStop() {
        if (_binding != null) {
            saveGame()
        }
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
