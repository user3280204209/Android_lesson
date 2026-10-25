package cn.edu.sicnu.cs.LHM

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat
import cn.edu.sicnu.cs.LHM.databinding.ActivityThirdBinding

/**
 * 第3个二级 Activity：采用隐式 Intent 调用系统相机拍照，并把照片显示在 ImageView 中。
 */
class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    // 运行时申请相机权限
    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                dispatchTakePictureIntent()
            }
        }

    // 处理拍照返回结果
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val extras = result.data?.extras
                val bitmap = extras?.let { BundleCompat.getParcelable(it, "data", Bitmap::class.java) }
                binding.photoImage.setImageBitmap(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTakePhoto.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // 通过隐式 Intent 启动系统相机
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            takePicture.launch(takePictureIntent)
        } else {
            Toast.makeText(this, getString(R.string.no_camera), Toast.LENGTH_SHORT).show()
        }
    }
}
