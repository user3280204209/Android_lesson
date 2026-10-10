package cn.edu.sicnu.cs.LHM
class ProgramExpert {
    fun getLanguage(featureIndex: Int): Int =
        when (featureIndex) {
            0 -> R.string.lang_c
            1 -> R.string.lang_python
            2 -> R.string.lang_kotlin
            3 -> R.string.lang_java
            else -> R.string.lang_unknown
        }
}
