package cn.edu.sicnu.cardgame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.edu.sicnu.cardgame.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * 主 Activity：只负责底部导航和 Fragment 的切换。
 *
 * 三个 Fragment 分别是个人信息、两张牌翻牌游戏、三张牌翻牌游戏。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.app_name)

        if (savedInstanceState == null) {
            showFragment(ProfileFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    showFragment(ProfileFragment())
                    true
                }

                R.id.nav_game_two -> {
                    showFragment(TwoCardGameFragment())
                    true
                }

                R.id.nav_game_three -> {
                    showFragment(ThreeCardGameFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
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
}
