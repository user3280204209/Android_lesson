# 实验四 翻牌游戏应用（Lesson 4）

本项目是 Android 移动应用开发实验四的简单实现，包含 3 个 Activity：

| Activity | 作用 |
| --- | --- |
| `MainActivity` | 主界面，显示姓名、性别、学号、电话、邮箱、爱好、游戏最高分 |
| `InfoEditActivity` | 修改个人信息；保存后通过 Activity Result 返回主界面（按要求不写入文件） |
| `CardGameActivity` | 翻牌游戏；12 张牌、3 列网格、计分、重置、退出 |

## 运行环境

- Android Studio（AGP 9.3.3 / Gradle 9.5.0）
- compileSdk 37，minSdk 24，targetSdk 34
- 模拟器：Medium_Phone（API 37），也可使用任意外接设备/模拟器

> `targetSdk` 使用 34 是为了保留 ActionBar 的经典布局行为。targetSdk 35+ 会强制 edge-to-edge，ActionBar 可能覆盖页面顶部的头像/姓名，需要额外处理 WindowInsets。实验工程选择更简单、稳定的 targetSdk 34。

## 打开与运行

1. 用 Android Studio 打开本目录（`C:\Android\bzdmtc\android_lesson4`）。
2. 等待 Gradle Sync 完成。
3. 启动模拟器或连接手机，点击 Run。

命令行编译：

```bash
./gradlew :app:assembleDebug
```

APK 输出：`app/build/outputs/apk/debug/app-debug.apk`。

## 测试

单元测试（JUnit4，9 个用例）：

```bash
./gradlew :app:testDebugUnitTest
```

UI 测试（Espresso，5 个用例）：

- 在 Android Studio 中右键 `app/src/androidTest/java/cn/edu/sicnu/cardgame/MainActivityTest.kt`
  或 `CardGameActivityTest.kt`，选择 Run。
- 也可以先用 Android Studio 的 Device Manager 启动模拟器，然后执行：

```bash
./gradlew :app:connectedDebugAndroidTest
```

如果所在网络无法访问 Maven Central（`connectedDebugAndroidTest` 会下载测试平台依赖），
可以直接使用已经编译好的测试 APK：

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb shell am instrument -w -r cn.edu.sicnu.cardgame.test/androidx.test.runner.AndroidJUnitRunner
```

运行 Espresso 前建议关闭模拟器动画：

```bash
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0
```

测试结果：单元测试 9/9 通过，UI 测试 5/5 通过（详见 `test-results/`）。

## 工程结构

```text
app/src/main/java/cn/edu/sicnu/cardgame/
├── Card.kt                 # 模型：牌、匹配规则
├── Deck.kt                 # 模型：52 张牌牌堆、随机发牌
├── CardMatchingGame.kt     # 模型：计分与匹配逻辑
├── Profile.kt              # 个人信息（Serializable，用于 Activity 间传递）
├── MainActivity.kt         # 主界面控制器
├── InfoEditActivity.kt     # 信息修改控制器
└── CardGameActivity.kt     # 翻牌游戏控制器
```

## 作业要求对应关系

| 要求 | 实现 |
| --- | --- |
| 1 个主 Activity + 2 个二级 Activity | `MainActivity`、`InfoEditActivity`、`CardGameActivity` |
| 主界面显示个人信息与最高分 | 头像、姓名、性别、学号、电话、邮箱、爱好、游戏最高分 |
| 信息修改，无需保存到文件 | 编辑后用 `Intent` + `Serializable` 返回主界面 |
| 翻牌游戏界面规整美观 | 3×4 网格、圆角牌面、牌背/选中/匹配三种样式 |
| 游戏模型参照附件 | 保留 `Card` / `Deck` / `CardMatchingGame`，仅增加只读牌数属性 |
| 控制器接受交互并同步界面 | `CardGameActivity` 负责点击、计分、刷新、重置、退出 |
| 可重新开始一局 | “重置游戏”重新创建 `CardMatchingGame` |
| 单元测试和 UI 测试 | JUnit 4（9 个）+ Espresso（5 个） |
| APP 标题为姓名+学号 | `app_name = 李浩铭2024080616` |
| 不直接出现字符串 | 所有界面文字均来自 `strings.xml` |

## 实验报告材料

- `实验报告填写指南.md`：按报告各栏目给出的可直接填写文字、代码说明与截图位置。
- `实验四+翻牌游戏应用-填写参考.docx`：已排版的参考稿，可把内容合并到学院模板。
- `screenshots/`：6 张真实运行截图。
- `test-results/`：单元测试和 UI 测试输出。
