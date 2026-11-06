# 实验五 RecyclerView 翻牌游戏应用（Lesson 5）

本项目在实验四的基础上完成，使用 **RecyclerView + 适配器** 重写翻牌游戏，
并在主界面增加选项菜单和“关于”对话框。

| 功能 | 实现 |
| --- | --- |
| 主界面 | 沿用实验四：个人信息、最高分、信息修改、进入游戏 |
| 选项菜单 | `MainActivity` 右上角“⋮”→“关于” |
| 关于对话框 | `MaterialAlertDialogBuilder` 弹出实验与开发者信息 |
| 游戏界面 | `RecyclerView` + `GridLayoutManager`，牌面由 `item_card.xml` 描述 |
| 适配器 | `CardAdapter` 继承 `RecyclerView.Adapter`，通过回调把点击交还 Activity |
| 游戏模型 | `CardMatchingGame` 新增 `reset()`；实现一次选 3 张牌的匹配 |
| 横竖屏 | 竖屏 24 张（4 列 × 6 行）；横屏 30 张（15 列 × 2 行） |
| 字符串资源 | 布局与代码中不直接写界面文字，全部引用 `strings.xml` |
| APP 标题 | `李浩铭2024080616` |

## 三张牌游戏规则

1. 每轮依次翻开 3 张牌。
2. 三张牌**点数全部相同**：匹配，得 `4 × 4 = 16` 分。
3. 三张牌**花色全部相同**：匹配，得 `1 × 4 = 4` 分。
4. 三张牌既不全部同点数也不全部同花色：匹配失败，扣 2 分，三张牌重新盖住。
5. 每翻开一张牌扣 1 分；已翻开的牌可以再次点击取消选择。
6. 全部牌匹配完成或不存在可匹配的三张牌时给出提示，可点击“重置游戏”重新开始。

## 运行环境

- Android Studio（AGP 9.3.3 / Gradle 9.5.0）
- compileSdk 37，minSdk 24，targetSdk 34
- 模拟器：Medium_Phone（API 37），也可使用任意外接设备/模拟器

## 打开与运行

1. 用 Android Studio 打开本目录（`C:\Android\bzdmtc\android_lesson5`）。
2. 等待 Gradle Sync 完成。
3. 启动模拟器或连接手机，点击 Run。

命令行编译：

```bash
./gradlew :app:assembleDebug
```

APK 输出：`app/build/outputs/apk/debug/app-debug.apk`。

## 测试

单元测试（JUnit 4，12 个用例，全部通过）：

```bash
./gradlew :app:testDebugUnitTest
```

UI 测试（Espresso，6 个用例，全部通过）：

```bash
./gradlew :app:connectedDebugAndroidTest
```

如果网络无法下载测试平台依赖，可使用已编译好的测试 APK：

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

测试输出见 `test-results/`。

## 工程结构

```text
app/src/main/java/cn/edu/sicnu/cardgame/
├── Card.kt                 # 模型：牌；支持两/三张牌的匹配判断
├── Deck.kt                 # 模型：52 张牌牌堆、随机发牌
├── CardMatchingGame.kt     # 模型：发牌、计分、reset()、三张牌选择逻辑
├── Profile.kt              # 个人信息（Serializable）
├── CardAdapter.kt          # 适配器：RecyclerView.Adapter + 点击回调
├── MainActivity.kt         # 控制器：主界面、选项菜单、关于对话框
├── InfoEditActivity.kt     # 控制器：信息修改
└── CardGameActivity.kt     # 控制器：RecyclerView 游戏
```

关键资源：

```text
app/src/main/res/layout/activity_card_game.xml   # 得分 + RecyclerView + 按钮
app/src/main/res/layout/item_card.xml            # 单张牌面
app/src/main/res/menu/menu_main.xml              # “关于”菜单项
app/src/main/res/values/integers.xml             # 竖屏 24 张 / 4 列
app/src/main/res/values-land/integers.xml        # 横屏 30 张 / 15 列
app/src/main/res/values/dimens.xml               # 竖屏牌面尺寸
app/src/main/res/values-land/dimens.xml          # 横屏牌面尺寸
app/src/main/res/values/strings.xml              # 全部界面文字
```

## 作业要求对应关系

| 要求 | 实现 |
| --- | --- |
| 新建 Dev 分支并提交到 Gitee | 操作步骤见 `实验报告填写指南.md` 第九节 |
| 保护 Main 分支，使用 PR 合并 | Gitee“管理 → 分支设置 → 保护分支”+ Pull Request |
| 主界面菜单 + 关于对话框 | `menu_main.xml`、`onCreateOptionsMenu`、`showAboutDialog()` |
| (a) RecyclerView 24 张牌 | 竖屏 `RecyclerView` + `GridLayoutManager(4)`，24 张牌 |
| (b) 继承 RecyclerView.Adapter | `CardAdapter` 重写 `onCreateViewHolder` / `onBindViewHolder` / `getItemCount`，并处理点击回调 |
| (c) Model 增加 reset | `CardMatchingGame.reset()` |
| (d) 控制器与横竖屏牌数 | `CardGameActivity`；`values` / `values-land` 提供不同牌数和列数 |
| (5) 三张牌游戏 | `Card.match()` + `CardMatchingGame.chooseCardAtIndex()` |
| 主题补充说明 | `themes.xml` 使用 `Theme.MaterialComponents.DayNight.DarkActionBar.Bridge` |
| 禁止直接写字符串 | 全部文字来自 `strings.xml` |

## 实验报告材料

- `实验报告填写指南.md`：按实验报告各栏目给出的填写文字、核心代码说明、截图说明和 Gitee 操作步骤。
- `screenshots/`：6 张真实运行截图。
- `test-results/`：单元测试和 UI 测试输出。
