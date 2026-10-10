package cn.edu.sicnu.cs.lhm

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//Gradle 自动生成的类,类里面封装了你布局 xml 所有带 id 控件的引用
import cn.edu.sicnu.cs.lhm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //成员变量声明,不在声明时赋值，等到onCreate里面再初始化
    // 整个 Activity 里所有函数都可以访问`binding.xxx`拿到控件
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //加载 xml 布局，自动完成所有控件`findViewById`，生成 binding 实例
        //替换原来的一行旧代码 `setContentView(R.layout.activity_main)`
        binding = ActivityMainBinding.inflate(layoutInflater)
        //把加载好的根布局交给页面显示
        setContentView(binding.root)
        //业务代码
        binding.button.setOnClickListener {
            binding.textView.text = resources.getString(R.string.clicked)
        }
        //已经用 ViewBinding 加载布局了，这里Listener后面不要再写 `findViewById(R.id.main)`
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}