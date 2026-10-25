package cn.edu.sicnu.cs.LHM

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.LHM.databinding.ActivityFourthBinding

/**
 * 第4个二级 Activity：秒表（StopWatch）。
 * 通过 onSaveInstanceState 保存计时状态，保证横竖屏切换时计时不中断。
 */
class FourthActivity : AppCompatActivity() {

    companion object {
        private const val KEY_RUNNING = "key_running"
        private const val KEY_ELAPSED = "key_elapsed"
    }

    private lateinit var binding: ActivityFourthBinding

    private var running = false
    private var elapsedBefore = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 恢复横竖屏切换前保存的计时状态
        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean(KEY_RUNNING)
            elapsedBefore = savedInstanceState.getLong(KEY_ELAPSED)
            binding.chronometer.base = SystemClock.elapsedRealtime() - elapsedBefore
            if (running) {
                binding.chronometer.start()
            } else {
                binding.chronometer.stop()
            }
        }

        binding.btnStart.setOnClickListener {
            if (!running) {
                binding.chronometer.base = SystemClock.elapsedRealtime() - elapsedBefore
                binding.chronometer.start()
                running = true
            }
        }

        binding.btnPause.setOnClickListener {
            if (running) {
                elapsedBefore = SystemClock.elapsedRealtime() - binding.chronometer.base
                binding.chronometer.stop()
                running = false
            }
        }

        binding.btnReset.setOnClickListener {
            binding.chronometer.base = SystemClock.elapsedRealtime()
            elapsedBefore = 0L
            running = false
            binding.chronometer.stop()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (running) {
            elapsedBefore = SystemClock.elapsedRealtime() - binding.chronometer.base
        }
        outState.putBoolean(KEY_RUNNING, running)
        outState.putLong(KEY_ELAPSED, elapsedBefore)
    }
}
