package cn.edu.sicnu.cs.LHM
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.LHM.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val expert = ProgramExpert()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (i in 1..30) {
            val textView = TextView(this)
            textView.text = getString(R.string.dynamic_text_view, i)
            textView.setPadding(0, 16, 0, 16)
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            binding.dynamicContainer.addView(textView)
        }

        binding.queryButton.setOnClickListener {
            val index = binding.spinner.selectedItemPosition
            binding.resultText.setText(expert.getLanguage(index))
        }
    }
}
