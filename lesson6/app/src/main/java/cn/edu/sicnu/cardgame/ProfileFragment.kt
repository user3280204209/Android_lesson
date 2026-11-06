package cn.edu.sicnu.cardgame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import cn.edu.sicnu.cardgame.databinding.FragmentProfileBinding

/**
 * 个人信息 Fragment。
 *
 * 信息从 profile.dat 读取（对象反序列化），修改后重新写入文件，
 * 因此退出应用再打开仍能恢复。
 */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profile: Profile

    private val infoEditLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                @Suppress("DEPRECATION")
                val updated =
                    result.data?.getSerializableExtra(InfoEditActivity.EXTRA_PROFILE) as? Profile
                if (updated != null) {
                    profile = updated
                    GameStorage.saveProfile(requireContext(), profile)
                    renderProfile()
                    Toast.makeText(
                        requireContext(),
                        R.string.profile_saved,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile = GameStorage.loadProfile(requireContext()) ?: Profile.default(requireContext())
        renderProfile()

        binding.btnEditInfo.setOnClickListener {
            val intent = Intent(requireContext(), InfoEditActivity::class.java)
            intent.putExtra(InfoEditActivity.EXTRA_PROFILE, profile)
            infoEditLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            // 从游戏页面返回时，刷新最高分等信息。
            profile = GameStorage.loadProfile(requireContext()) ?: profile
            renderProfile()
        }
    }

    override fun onPause() {
        super.onPause()
        if (_binding != null) {
            GameStorage.saveProfile(requireContext(), profile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.tvHighScoreTwo.text = profile.highScoreTwo.toString()
        binding.tvHighScoreThree.text = profile.highScoreThree.toString()
    }
}
