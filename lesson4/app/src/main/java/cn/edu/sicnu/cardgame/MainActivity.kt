package cn.edu.sicnu.cardgame

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cardgame.databinding.ActivityMainBinding

/**
 * 主界面：显示玩家基本信息和游戏最高分，并提供进入信息修改、翻牌游戏的入口。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var profile: Profile

    private val infoEditLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                @Suppress("DEPRECATION")
                val updated = result.data?.getSerializableExtra(InfoEditActivity.EXTRA_PROFILE) as? Profile
                if (updated != null) {
                    profile = updated
                    renderProfile()
                }
            }
        }

    private val cardGameLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val newHighScore = result.data?.getIntExtra(
                    CardGameActivity.EXTRA_HIGH_SCORE,
                    profile.highScore
                ) ?: profile.highScore
                if (newHighScore > profile.highScore) {
                    profile.highScore = newHighScore
                    renderProfile()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        profile = savedInstanceState?.getSerializable(KEY_PROFILE) as? Profile
            ?: Profile.default(this)

        binding.btnEditInfo.setOnClickListener {
            val intent = Intent(this, InfoEditActivity::class.java)
            intent.putExtra(InfoEditActivity.EXTRA_PROFILE, profile)
            infoEditLauncher.launch(intent)
        }

        binding.btnPlayGame.setOnClickListener {
            val intent = Intent(this, CardGameActivity::class.java)
            intent.putExtra(CardGameActivity.EXTRA_HIGH_SCORE, profile.highScore)
            cardGameLauncher.launch(intent)
        }

        renderProfile()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_PROFILE, profile)
    }

    private fun renderProfile() {
        binding.tvHeaderName.text = profile.name
        binding.tvHeaderStudentId.text =
            getString(R.string.student_id_format, profile.studentId)

        binding.tvNameValue.text = profile.name
        binding.tvGenderValue.text = profile.gender
        binding.tvStudentIdValue.text = profile.studentId
        binding.tvPhoneValue.text = profile.phone
        binding.tvEmailValue.text = profile.email
        binding.tvHobbiesValue.text =
            profile.hobbies.joinToString(getString(R.string.hobby_separator))
        binding.tvHighScoreValue.text = profile.highScore.toString()
    }

    private companion object {
        const val KEY_PROFILE = "key_profile"
    }
}
