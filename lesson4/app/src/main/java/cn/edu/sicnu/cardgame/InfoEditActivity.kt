package cn.edu.sicnu.cardgame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cardgame.databinding.ActivityInfoEditBinding

/**
 * 信息修改界面：读取主界面传入的个人信息，修改后通过 Activity Result 返回。
 */
class InfoEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoEditBinding
    private lateinit var profile: Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.title_info_edit)

        @Suppress("DEPRECATION")
        profile = intent.getSerializableExtra(EXTRA_PROFILE) as? Profile
            ?: Profile.default(this)

        showProfile()
        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun showProfile() {
        binding.etName.setText(profile.name)
        binding.etStudentId.setText(profile.studentId)
        binding.etPhone.setText(profile.phone)
        binding.etEmail.setText(profile.email)

        val isMale = profile.gender == getString(R.string.gender_male)
        binding.rbMale.isChecked = isMale
        binding.rbFemale.isChecked = !isMale

        binding.cbTravel.isChecked = profile.hobbies.contains(getString(R.string.hobby_travel))
        binding.cbReading.isChecked = profile.hobbies.contains(getString(R.string.hobby_reading))
        binding.cbGame.isChecked = profile.hobbies.contains(getString(R.string.hobby_game))
        binding.cbShopping.isChecked = profile.hobbies.contains(getString(R.string.hobby_shopping))
    }

    private fun saveProfile() {
        val name = binding.etName.text?.toString()?.trim().orEmpty()
        val phone = binding.etPhone.text?.toString()?.trim().orEmpty()

        if (name.isEmpty()) {
            binding.etName.error = getString(R.string.error_name_empty)
            return
        }
        if (phone.isEmpty()) {
            binding.etPhone.error = getString(R.string.error_phone_empty)
            return
        }
        if (!phone.matches(Regex("\\d{11}"))) {
            binding.etPhone.error = getString(R.string.error_phone_invalid)
            return
        }

        val updated = profile.copy(
            name = name,
            gender = getString(
                if (binding.rbFemale.isChecked) R.string.gender_female else R.string.gender_male
            ),
            studentId = binding.etStudentId.text?.toString()?.trim().orEmpty(),
            phone = phone,
            email = binding.etEmail.text?.toString()?.trim().orEmpty(),
            hobbies = selectedHobbies()
        )

        setResult(
            RESULT_OK,
            Intent().putExtra(EXTRA_PROFILE, updated)
        )
        finish()
    }

    private fun selectedHobbies(): ArrayList<String> {
        val hobbies = ArrayList<String>()
        if (binding.cbTravel.isChecked) hobbies.add(getString(R.string.hobby_travel))
        if (binding.cbReading.isChecked) hobbies.add(getString(R.string.hobby_reading))
        if (binding.cbGame.isChecked) hobbies.add(getString(R.string.hobby_game))
        if (binding.cbShopping.isChecked) hobbies.add(getString(R.string.hobby_shopping))
        return hobbies
    }

    companion object {
        const val EXTRA_PROFILE = "extra_profile"
    }
}
