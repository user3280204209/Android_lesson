package cn.edu.sicnu.cardgame

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cardgame.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * 主界面：显示玩家基本信息和游戏最高分，提供信息修改、翻牌游戏入口，
 * 并通过选项菜单的“关于”项弹出关于对话框。
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                showAboutDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_about_title)
            .setMessage(getString(R.string.dialog_about_message, getString(R.string.app_name)))
            .setPositiveButton(R.string.dialog_about_ok, null)
            .show()
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
