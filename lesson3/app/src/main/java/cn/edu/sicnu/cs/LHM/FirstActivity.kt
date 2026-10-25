package cn.edu.sicnu.cs.LHM

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.LHM.databinding.ActivityFirstBinding

/**
 * 第1个二级 Activity：接收 MainActivity 正向传递的参数并显示。
 */
class FirstActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
    }

    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        binding.receiveText.text = getString(R.string.received_param, message ?: "")
    }
}
