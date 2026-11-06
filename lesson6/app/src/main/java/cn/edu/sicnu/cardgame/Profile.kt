package cn.edu.sicnu.cardgame

import android.content.Context
import java.io.Serializable

/**
 * 玩家个人信息。实现 Serializable 后可以写入文件，
 * 退出应用后再次打开仍能恢复个人信息。
 */
data class Profile(
    var name: String,
    var gender: String,
    var studentId: String,
    var phone: String,
    var email: String,
    var hobbies: ArrayList<String>,
    var highScoreTwo: Int = 0,
    var highScoreThree: Int = 0
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L

        fun default(context: Context): Profile {
            return Profile(
                name = context.getString(R.string.default_name),
                gender = context.getString(R.string.gender_male),
                studentId = context.getString(R.string.default_student_id),
                phone = context.getString(R.string.default_phone),
                email = context.getString(R.string.default_email),
                hobbies = arrayListOf(
                    context.getString(R.string.hobby_travel),
                    context.getString(R.string.hobby_reading)
                )
            )
        }
    }
}
