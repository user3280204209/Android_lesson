package cn.edu.sicnu.cs.LHM

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.LHM.databinding.ActivitySecondBinding

/**
 * 第2个二级 Activity：输入内容后通过 setResult 把数据返回给 MainActivity。
 */
class SecondActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirm.setOnClickListener {
            val content = binding.inputText.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, content)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
