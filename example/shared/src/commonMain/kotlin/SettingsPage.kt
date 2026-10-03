// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

@file:OptIn(ExperimentalScrollBarApi::class)

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import i18n.AppLanguage
import i18n.appLanguage
import i18n.str
import misc.VersionInfo
import navigation.Route
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeColorSpec
import top.yukonga.miuix.kmp.theme.ThemePaletteStyle
import utils.AdaptiveTopAppBar
import utils.BlurredBar
import utils.pageContentPadding
import utils.pageScrollModifiers
import utils.rememberBlurBackdrop

private val NavigationBarDisplayModeOptions = listOf("IconAndText", "IconOnly", "IconWithSelectedLabel")
private val FloatingNavigationBarStyleOptions = listOf("Default", "iOS-like")
private val FloatingNavigationBarPositionOptions = listOf("Center", "Start", "End")
private val FloatingToolbarPositionOptions =
    listOf(str("TopStart"), str("CenterStart"), str("BottomStart"), str("TopEnd"), str("CenterEnd"), str("BottomEnd"), str("TopCenter"), str("BottomCenter"))
private val FloatingToolbarOrientationOptions = listOf("Horizontal", "Vertical")
private val FabPositionOptions = listOf("Start", "Center", "End", "EndOverlay")
private val ColorModeOptions = listOf("System", "Light", "Dark", "MonetSystem", "MonetLight", "MonetDark")
private val NavTransitionStyleOptions = listOf("Miuix", "AOSP")
private val BlurStyleOptions = listOf("Gaussian", "Progressive")
private val PaletteStyleOptions = ThemePaletteStyle.entries.map { it.name }
private val ColorSpecOptions = ThemeColorSpec.entries.map { it.name }
private val KeyColorOptions = listOf("Default") + ui.KeyColors.map { it.first }
private val PagerGestureModeOptions = listOf("Default", "Cross-Axis", "iOS-like")

@Composable
fun SettingsPage(
    padding: PaddingValues,
) {
    val appState = LocalAppState.current
    val isWideScreen = LocalIsWideScreen.current
    val backdrop = rememberBlurBackdrop()
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface
    val topAppBarScrollBehavior = MiuixScrollBehavior()

    Scaffold(
        topBar = {
            BlurredBar(backdrop, blurActive) {
                AdaptiveTopAppBar(
                    title = str("Settings"),
                    showTopAppBar = appState.showTopAppBar,
                    isWideScreen = isWideScreen,
                    scrollBehavior = topAppBarScrollBehavior,
                    color = barColor,
                    subtitle = "v${VersionInfo.VERSION_NAME} (${VersionInfo.VERSION_CODE})",
                )
            }
        },
    ) { innerPadding ->
        SettingsContent(
            padding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            ),
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            backdrop = backdrop,
        )
    }
}

@Composable
private fun SettingsContent(
    padding: PaddingValues,
    topAppBarScrollBehavior: ScrollBehavior,
    backdrop: LayerBackdrop?,
) {
    val appState = LocalAppState.current
    val isWideScreen = LocalIsWideScreen.current
    val updateAppState = LocalUpdateAppState.current
    val navigator = LocalNavigator.current
    val lazyListState = rememberLazyListState()

    val contentPadding = pageContentPadding(padding, padding, isWideScreen)
    Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.pageScrollModifiers(
                appState.enableScrollEndHaptic,
                appState.showTopAppBar,
                topAppBarScrollBehavior,
            ),
            contentPadding = contentPadding,
        ) {
            item(key = "settingsUi") {
                Card(
                    modifier = Modifier.padding(12.dp),
                ) {
                    OverlayDropdownPreference(
                        title = str("Language"),
                        items = AppLanguage.entries.map { it.displayName },
                        selectedIndex = appLanguage.ordinal,
                        onSelectedIndexChange = { appLanguage = AppLanguage.fromOrdinal(it) },
                    )
                    SwitchPreference(
                        title = str("Show FPS Monitor"),
                        checked = appState.showFPSMonitor,
                        onCheckedChange = { updateAppState { state -> state.copy(showFPSMonitor = it) } },
                    )
                    OverlayDropdownPreference(
                        title = str("Color Mode"),
                        items = ColorModeOptions.map { str(it) },
                        selectedIndex = appState.colorMode,
                        onSelectedIndexChange = { updateAppState { state -> state.copy(colorMode = it) } },
                    )
                    AnimatedVisibility(visible = appState.colorMode in 3..5) {
                        OverlayDropdownPreference(
                            title = str("Key Color"),
                            items = KeyColorOptions.map { str(it) },
                            selectedIndex = appState.seedIndex,
                            onSelectedIndexChange = { updateAppState { state -> state.copy(seedIndex = it) } },
                        )
                    }
                    AnimatedVisibility(visible = appState.colorMode in 3..5 && appState.seedIndex > 0) {
                        Column {
                            OverlayDropdownPreference(
                                title = str("Palette Style"),
                                items = PaletteStyleOptions,
                                selectedIndex = appState.paletteStyle,
                                onSelectedIndexChange = { updateAppState { state -> state.copy(paletteStyle = it) } },
                            )
                            OverlayDropdownPreference(
                                title = str("Color Spec"),
                                items = ColorSpecOptions,
                                selectedIndex = appState.colorSpec,
                                onSelectedIndexChange = { updateAppState { state -> state.copy(colorSpec = it) } },
                            )
                        }
                    }
                    AnimatedVisibility(visible = isRuntimeShaderSupported()) {
                        SwitchPreference(
                            title = str("Enable Squircle Shapes"),
                            checked = appState.enableSquircle,
                            onCheckedChange = { updateAppState { state -> state.copy(enableSquircle = it) } },
                        )
                    }
                    AnimatedVisibility(visible = isRuntimeShaderSupported()) {
                        SwitchPreference(
                            title = str("Enable Blur Effect"),
                            checked = appState.enableBlur,
                            onCheckedChange = { updateAppState { state -> state.copy(enableBlur = it) } },
                        )
                    }
                    SwitchPreference(
                        title = str("Enable Scroll End Haptic"),
                        checked = appState.enableScrollEndHaptic,
                        onCheckedChange = { updateAppState { state -> state.copy(enableScrollEndHaptic = it) } },
                    )
                    SwitchPreference(
                        title = str("Enable Page User Scroll"),
                        checked = appState.enablePageUserScroll,
                        onCheckedChange = { updateAppState { state -> state.copy(enablePageUserScroll = it) } },
                    )
                    AnimatedVisibility(visible = appState.enablePageUserScroll) {
                        OverlayDropdownPreference(
                            title = str("Pager Gesture Mode"),
                            items = PagerGestureModeOptions.map { str(it) },
                            selectedIndex = appState.pagerInterceptionMode,
                            onSelectedIndexChange = { updateAppState { state -> state.copy(pagerInterceptionMode = it) } },
                        )
                    }
                    SwitchPreference(
                        title = str("Show TopAppBar"),
                        checked = appState.showTopAppBar,
                        onCheckedChange = { updateAppState { state -> state.copy(showTopAppBar = it) } },
                    )
                    AnimatedVisibility(visible = appState.showTopAppBar && appState.enableBlur && isRuntimeShaderSupported()) {
                        OverlayDropdownPreference(
                            title = str("TopAppBar Blur Style"),
                            items = BlurStyleOptions.map { str(it) },
                            selectedIndex = appState.blurStyle,
                            onSelectedIndexChange = { updateAppState { state -> state.copy(blurStyle = it) } },
                        )
                    }
                    SwitchPreference(
                        title = if (isWideScreen) str("Show NavigationRail") else str("Show NavigationBar"),
                        checked = appState.showNavigationBar,
                        onCheckedChange = { updateAppState { state -> state.copy(showNavigationBar = it) } },
                    )
                    AnimatedVisibility(visible = appState.showNavigationBar) {
                        SwitchPreference(
                            title = str("Show Navigation Badge"),
                            checked = appState.showNavigationBarBadge,
                            onCheckedChange = { updateAppState { state -> state.copy(showNavigationBarBadge = it) } },
                        )
                    }
                    AnimatedVisibility(visible = appState.showNavigationBar && !isWideScreen && !appState.useFloatingNavigationBar) {
                        OverlayDropdownPreference(
                            title = str("NavigationBar Mode"),
                            items = NavigationBarDisplayModeOptions.map { str(it) },
                            selectedIndex = appState.navigationBarMode,
                            onSelectedIndexChange = { updateAppState { state -> state.copy(navigationBarMode = it) } },
                        )
                    }
                    AnimatedVisibility(visible = appState.showNavigationBar && !isWideScreen) {
                        Column {
                            SwitchPreference(
                                title = str("Use FloatingNavigationBar"),
                                checked = appState.useFloatingNavigationBar,
                                onCheckedChange = { updateAppState { state -> state.copy(useFloatingNavigationBar = it) } },
                            )
                            AnimatedVisibility(visible = appState.useFloatingNavigationBar) {
                                Column {
                                    OverlayDropdownPreference(
                                        title = str("FloatingNavigationBar Style"),
                                        items = FloatingNavigationBarStyleOptions.map { str(it) },
                                        selectedIndex = appState.floatingNavigationBarStyle,
                                        onSelectedIndexChange = { updateAppState { state -> state.copy(floatingNavigationBarStyle = it) } },
                                    )
                                    AnimatedVisibility(visible = appState.floatingNavigationBarStyle == 0) {
                                        Column {
                                            OverlayDropdownPreference(
                                                title = str("FloatingNavigationBar Position"),
                                                items = FloatingNavigationBarPositionOptions.map { str(it) },
                                                selectedIndex = appState.floatingNavigationBarPosition,
                                                onSelectedIndexChange = { updateAppState { state -> state.copy(floatingNavigationBarPosition = it) } },
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    SwitchPreference(
                        title = str("Show FloatingToolbar"),
                        checked = appState.showFloatingToolbar,
                        onCheckedChange = { updateAppState { state -> state.copy(showFloatingToolbar = it) } },
                    )
                    AnimatedVisibility(visible = appState.showFloatingToolbar) {
                        Column {
                            OverlayDropdownPreference(
                                title = str("FloatingToolbar Position"),
                                items = FloatingToolbarPositionOptions.map { str(it) },
                                selectedIndex = appState.floatingToolbarPosition,
                                onSelectedIndexChange = { updateAppState { state -> state.copy(floatingToolbarPosition = it) } },
                            )
                            OverlayDropdownPreference(
                                title = str("FloatingToolbar Orientation"),
                                items = FloatingToolbarOrientationOptions.map { str(it) },
                                selectedIndex = appState.floatingToolbarOrientation,
                                onSelectedIndexChange = { updateAppState { state -> state.copy(floatingToolbarOrientation = it) } },
                            )
                        }
                    }
                    SwitchPreference(
                        title = str("Show FloatingActionButton"),
                        checked = appState.showFloatingActionButton,
                        onCheckedChange = { updateAppState { state -> state.copy(showFloatingActionButton = it) } },
                    )
                    AnimatedVisibility(visible = appState.showFloatingActionButton) {
                        OverlayDropdownPreference(
                            title = str("FloatingActionButton Position"),
                            items = FabPositionOptions.map { str(it) },
                            selectedIndex = appState.floatingActionButtonPosition,
                            onSelectedIndexChange = { updateAppState { state -> state.copy(floatingActionButtonPosition = it) } },
                        )
                    }
                }
            }
            item(key = "settingsTransition") {
                SmallTitle(str("Navigation"))
                Card(
                    modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 12.dp),
                ) {
                    OverlayDropdownPreference(
                        title = str("Transition Style"),
                        items = NavTransitionStyleOptions.map { str(it) },
                        selectedIndex = appState.navTransitionStyle,
                        onSelectedIndexChange = { updateAppState { state -> state.copy(navTransitionStyle = it) } },
                    )
                    SwitchPreference(
                        title = str("Enable Corner Clip"),
                        summary = str("Clip the top scene with rounded corners during transitions"),
                        checked = appState.enableCornerClip,
                        onCheckedChange = { updateAppState { state -> state.copy(enableCornerClip = it) } },
                    )
                    SwitchPreference(
                        title = str("Enable Dim"),
                        summary = str("Dim the scene behind during transitions"),
                        checked = appState.enableDim,
                        onCheckedChange = { updateAppState { state -> state.copy(enableDim = it) } },
                    )
                    SwitchPreference(
                        title = str("Block Input During Transition"),
                        summary = str("Block touch input on the non-target scene"),
                        checked = appState.blockInputDuringTransition,
                        onCheckedChange = { updateAppState { state -> state.copy(blockInputDuringTransition = it) } },
                    )
                    SwitchPreference(
                        title = str("Enable Swipe Back"),
                        summary = "Swipe a pushed page to pop it; direction follows layout",
                        checked = appState.enableSwipeBack,
                        onCheckedChange = { updateAppState { state -> state.copy(enableSwipeBack = it) } },
                    )
                }
            }
            item(key = "settingsAbout") {
                SmallTitle(str("Other"))
                Card(
                    modifier = Modifier.padding(horizontal = 12.dp),
                ) {
                    ArrowPreference(
                        title = str("About"),
                        summary = str("About this example App"),
                        onClick = { navigator.push(Route.About) },
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
        VerticalScrollBar(
            adapter = rememberScrollBarAdapter(lazyListState),
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            trackPadding = contentPadding,
        )
    }
}
