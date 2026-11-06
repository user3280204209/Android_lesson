package cn.edu.sicnu.cardgame

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * 对象序列化存储工具。
 *
 * 写文件使用 openFileOutput + ObjectOutputStream，
 * 读文件使用 openFileInput + ObjectInputStream。
 */
object GameStorage {

    private const val PROFILE_FILE = "profile.dat"
    private const val GAME_TWO_FILE = "game_two.dat"
    private const val GAME_THREE_FILE = "game_three.dat"

    fun saveProfile(context: Context, profile: Profile) {
        writeObject(context, PROFILE_FILE, profile)
    }

    fun loadProfile(context: Context): Profile? {
        return readObject(context, PROFILE_FILE) as? Profile
    }

    fun saveGame(context: Context, cardsPerTurn: Int, game: CardMatchingGame) {
        writeObject(context, gameFile(cardsPerTurn), game)
    }

    fun loadGame(context: Context, cardsPerTurn: Int): CardMatchingGame? {
        val game = readObject(context, gameFile(cardsPerTurn)) as? CardMatchingGame
        return if (game?.cardsPerTurn == cardsPerTurn) game else null
    }

    fun clearAll(context: Context) {
        listOf(PROFILE_FILE, GAME_TWO_FILE, GAME_THREE_FILE).forEach {
            context.deleteFile(it)
        }
    }

    private fun gameFile(cardsPerTurn: Int): String {
        return if (cardsPerTurn == 2) GAME_TWO_FILE else GAME_THREE_FILE
    }

    private fun writeObject(context: Context, fileName: String, data: Any) {
        try {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { file ->
                ObjectOutputStream(file).use { it.writeObject(data) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readObject(context: Context, fileName: String): Any? {
        return try {
            context.openFileInput(fileName).use { file ->
                ObjectInputStream(file).use { it.readObject() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
