package cn.edu.sicnu.cs.LHM

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.LHM.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 用于接收第2个二级 Activity 返回数据的启动器
    private val secondLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getStringExtra(SecondActivity.EXTRA_RESULT)
                binding.resultText.text = getString(R.string.return_result, data ?: "")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 按钮1：启动第1个二级 Activity 并正向传参
        binding.btnFirst.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            intent.putExtra(FirstActivity.EXTRA_MESSAGE, getString(R.string.forward_value))
            startActivity(intent)
        }

        // 按钮2：启动第2个二级 Activity，接收其返回的数据
        binding.btnSecond.setOnClickListener {
            secondLauncher.launch(Intent(this, SecondActivity::class.java))
        }

        // 按钮3：启动第3个二级 Activity（相机拍照）
        binding.btnThird.setOnClickListener {
            startActivity(Intent(this, ThirdActivity::class.java))
        }

        // 按钮4：启动第4个二级 Activity（秒表计时）
        binding.btnFourth.setOnClickListener {
            startActivity(Intent(this, FourthActivity::class.java))
        }
    }
}
