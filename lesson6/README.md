# 实验六 多 Fragment 应用（Lesson 6）

本项目在实验五工程的基础上完成，用 **Fragment + BottomNavigationView** 重构应用，
并通过**对象序列化 + 文件存储**保存个人信息和两个翻牌游戏的历史进度。

| 功能 | 实现 |
| --- | --- |
| 底部导航 | `BottomNavigationView` 切换 3 个 Fragment |
| 个人信息 | `ProfileFragment`，可修改姓名、学号、电话、邮箱、性别、爱好 |
| 两张牌翻牌游戏 | `TwoCardGameFragment`，12 张牌、4 列，每轮翻 2 张 |
| 三张牌翻牌游戏 | `ThreeCardGameFragment`，15 张牌、5 列，每轮翻 3 张 |
| 个人信息持久化 | `Profile` 实现 `Serializable`，保存到 `profile.dat` |
| 游戏进度持久化 | `Card`、`CardMatchingGame` 实现 `Serializable`，分别保存到 `game_two.dat`、`game_three.dat` |
| 文件读写 | `openFileOutput` + `ObjectOutputStream`；`openFileInput` + `ObjectInputStream` |
| 自动保存/恢复 | 离开页面时自动保存，重新进入或重启应用后自动恢复 |
| 最高分 | 两个游戏的最高分同步显示在个人信息页 |
| APP 标题 | `李浩铭2024080616` |

## 游戏规则

### 两张牌

1. 每轮依次翻开 2 张牌；
2. 两张牌**点数相同**：匹配，得 `4 × 4 = 16` 分；
3. 两张牌**花色相同**：匹配，得 `1 × 4 = 4` 分；
4. 两张牌点数、花色都不同：匹配失败，扣 2 分，两张牌重新盖住；
5. 每翻开一张牌扣 1 分；已翻开的牌可以再次点击取消选择。

### 三张牌

与两张牌规则相同，只是每轮同时翻开 3 张牌，要求三张牌全部同点数或全部同花色。

## 运行环境

- Android Studio（AGP 9.3.3 / Gradle 9.5.0）
- compileSdk 37，minSdk 24，targetSdk 34
- 模拟器：Medium_Phone（API 37），也可使用任意外接设备

## 打开与运行

1. 用 Android Studio 打开本目录（`C:\Android\bzdmtc\android_lesson6`）；
2. 等待 Gradle Sync 完成；
3. 启动模拟器或连接手机，点击 Run。

命令行编译：

```bash
./gradlew :app:assembleDebug
```

APK 输出：`app/build/outputs/apk/debug/app-debug.apk`。

## 持久化验证方法

1. 在“两张牌”页翻开若干张牌，观察得分变化；
2. 切到“个人信息”页，再切回“两张牌”页，页面下方显示“已恢复上次保存的游戏进度”，得分和牌面不变；
3. 完全退出应用（从最近任务中划掉）后重新打开，再次进入“两张牌”页，进度仍能恢复；
4. 在“个人信息”页修改姓名并保存，退出应用后重新打开，修改后的信息仍会显示。

## 工程结构

```text
app/src/main/java/cn/edu/sicnu/cardgame/
├── Card.kt                     # 模型：牌，实现 Serializable
├── Deck.kt                     # 模型：52 张牌牌堆
├── CardMatchingGame.kt         # 模型：2/3 张牌游戏，实现 Serializable
├── Profile.kt                  # 模型：个人信息，实现 Serializable
├── GameStorage.kt              # 对象序列化文件存储工具
├── CardAdapter.kt              # RecyclerView 适配器
├── MainActivity.kt             # 主 Activity：底部导航 + Fragment 切换
├── ProfileFragment.kt          # Fragment 1：个人信息
├── CardGameFragment.kt         # 游戏 Fragment 公共实现（保存/恢复）
├── TwoCardGameFragment.kt      # Fragment 2：两张牌游戏
├── ThreeCardGameFragment.kt    # Fragment 3：三张牌游戏
└── InfoEditActivity.kt         # 个人信息修改
```

## 测试

单元测试（13 个用例，全部通过）：

```bash
./gradlew :app:testDebugUnitTest
```

UI 测试（Espresso，3 个用例）：

```bash
./gradlew :app:connectedDebugAndroidTest
```

## 实验报告材料

- `实验报告填写指南.md`：按报告栏目给出的填写文字、核心代码说明、截图清单和 Gitee 操作步骤；
- `screenshots/`：真实运行截图，可直接插入报告。

## 作业要求对应关系

| 要求 | 实现 |
| --- | --- |
| 最少 3 个 Fragment | `ProfileFragment`、`TwoCardGameFragment`、`ThreeCardGameFragment` |
| 第 1 个：个人信息 | `fragment_profile.xml` + `ProfileFragment` |
| 第 2 个：两张牌游戏 | `fragment_card_game.xml` + `TwoCardGameFragment` |
| 第 3 个：三张牌游戏 | 复用游戏布局 + `ThreeCardGameFragment` |
| 个人信息序列化存储 | `Profile` + `GameStorage.saveProfile/loadProfile` |
| 游戏历史序列化存储 | `CardMatchingGame`、`Card` + `GameStorage.saveGame/loadGame` |
| openFileInput/openFileOutput | `GameStorage.kt` |
| ObjectInputStream/ObjectOutputStream | `GameStorage.kt` |
| Serializable | `Card`、`CardMatchingGame`、`Profile` 均实现 |
| APP 标题“姓名+学号” | `strings.xml` 中 `app_name` |
| Gitee 分支 Main/Dev/Frag | 操作步骤见 `实验报告填写指南.md` 第九节 |
