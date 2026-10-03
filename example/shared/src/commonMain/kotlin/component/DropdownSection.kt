// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import i18n.appLanguage
import i18n.str
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.DropdownEntry
import top.yukonga.miuix.kmp.basic.DropdownItem
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.WindowDropdownPreference

fun LazyListScope.dropdownSection() {
    item(key = "dropdown") {
        var overlayDropdownOptionSelected by remember { mutableIntStateOf(0) }
        var windowDropdownOptionSelected by remember { mutableIntStateOf(0) }
        var overlayExpanded by remember { mutableStateOf(false) }
        var windowExpanded by remember { mutableStateOf(false) }
        val dropdownOptions = remember(appLanguage) {
            listOf(str("Option 1"), str("Option 2"), str("Option 3"), str("Option 4"))
        }
        val dropdownLongOptions = remember(appLanguage) {
            listOf(
                str("Option 1"),
                str("Long Option 2"),
                str("Long Long Option 3"),
                str("Long Long Long Option 4"),
                str("Long Long Long Long Option 5"),
                str("Long Long Long Long Long Option 6"),
                str("Long Long Long Long Long Long Option 7"),
                str("Long Long Long Long Long Long Long Option 8"),
                str("Long Long Long Long Long Long Long Long Option 9"),
                str("Long Long Long Long Long Long Long Long Long Option 10"),
                str("Long Long Long Long Long Long Long Long Long Long Option 11"),
                str("Long Long Long Long Long Long Long Long Long Long Long Option 12"),
            )
        }

        var overlayGroupedExpanded by remember { mutableStateOf(false) }
        var overlayGroup1DropdownOptionSelected by remember { mutableIntStateOf(0) }
        var overlayGroup2DropdownOptionSelected by remember { mutableIntStateOf(0) }
        var overlayGroup3DropdownOptionSelected by remember { mutableIntStateOf(0) }
        val overlayMultiGroupOptions = remember(
            overlayGroup1DropdownOptionSelected,
            overlayGroup2DropdownOptionSelected,
            overlayGroup3DropdownOptionSelected,
        ) {
            listOf(
                DropdownEntry(
                    items = listOf("Option A-1", "Option A-2")
                        .mapIndexed { index, text ->
                            DropdownItem(
                                text = text,
                                selected = overlayGroup1DropdownOptionSelected == index,
                                onClick = { overlayGroup1DropdownOptionSelected = index },
                            )
                        },
                ),
                DropdownEntry(
                    items = listOf("Option B-1", "Option B-2", "Option B-3")
                        .mapIndexed { index, text ->
                            DropdownItem(
                                text = text,
                                selected = overlayGroup2DropdownOptionSelected == index,
                                onClick = { overlayGroup2DropdownOptionSelected = index },
                            )
                        },
                ),
                DropdownEntry(
                    items = listOf("Option C-1", "Option C-2", "Option C-3", "Option C-4")
                        .mapIndexed { index, string ->
                            DropdownItem(
                                text = string,
                                enabled = index % 2 == 0,
                                selected = overlayGroup3DropdownOptionSelected == index,
                                onClick = { overlayGroup3DropdownOptionSelected = index },
                            )
                        },
                ),
            )
        }

        var windowGroupedExpanded by remember { mutableStateOf(false) }
        var windowGroup1DropdownOptionSelected by remember { mutableIntStateOf(0) }
        var windowGroup2DropdownOptionSelected by remember { mutableIntStateOf(0) }
        var windowGroup3DropdownOptionSelected by remember { mutableIntStateOf(0) }
        val windowMultiGroupOptions = remember(
            windowGroup1DropdownOptionSelected,
            windowGroup2DropdownOptionSelected,
            windowGroup3DropdownOptionSelected,
        ) {
            listOf(
                DropdownEntry(
                    items = listOf("Option A-1", "Option A-2")
                        .mapIndexed { index, text ->
                            DropdownItem(
                                text = text,
                                selected = windowGroup1DropdownOptionSelected == index,
                                onClick = { windowGroup1DropdownOptionSelected = index },
                            )
                        },
                ),
                DropdownEntry(
                    items = listOf("Option B-1", "Option B-2", "Option B-3")
                        .mapIndexed { index, text ->
                            DropdownItem(
                                text = text,
                                selected = windowGroup2DropdownOptionSelected == index,
                                onClick = { windowGroup2DropdownOptionSelected = index },
                            )
                        },
                ),
                DropdownEntry(
                    items = listOf("Option C-1", "Option C-2", "Option C-3", "Option C-4")
                        .mapIndexed { index, string ->
                            DropdownItem(
                                text = string,
                                enabled = index % 2 == 0,
                                selected = windowGroup3DropdownOptionSelected == index,
                                onClick = { windowGroup3DropdownOptionSelected = index },
                            )
                        },
                ),
            )
        }

        SmallTitle(text = str("Dropdown"))
        Card(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
        ) {
            OverlayDropdownPreference(
                title = str("DropdownPref (O)"),
                summary = if (overlayExpanded) str("Expanded") else str("Collapsed"),
                items = dropdownOptions,
                selectedIndex = overlayDropdownOptionSelected,
                onSelectedIndexChange = { newOption ->
                    overlayDropdownOptionSelected = newOption
                },
                onExpandedChange = { overlayExpanded = it },
            )
            WindowDropdownPreference(
                title = str("DropdownPref (W)"),
                summary = if (windowExpanded) str("Expanded") else str("Collapsed"),
                items = dropdownLongOptions,
                selectedIndex = windowDropdownOptionSelected,
                onSelectedIndexChange = { newOption ->
                    windowDropdownOptionSelected = newOption
                },
                onExpandedChange = { windowExpanded = it },
            )
            OverlayDropdownPreference(
                title = str("Grouped DropdownPref (O)"),
                summary = if (overlayGroupedExpanded) str("Expanded") else str("Collapsed"),
                entries = overlayMultiGroupOptions,
                collapseOnSelection = false,
                onExpandedChange = { overlayGroupedExpanded = it },
            )
            WindowDropdownPreference(
                title = str("Grouped DropdownPref (W)"),
                summary = if (windowGroupedExpanded) str("Expanded") else str("Collapsed"),
                entries = windowMultiGroupOptions,
                collapseOnSelection = false,
                onExpandedChange = { windowGroupedExpanded = it },
            )
            OverlayDropdownPreference(
                title = str("Disabled DropdownPref (O)"),
                items = listOf("Option 1"),
                selectedIndex = 0,
                onSelectedIndexChange = {},
                enabled = false,
            )
            WindowDropdownPreference(
                title = str("Disabled DropdownPref (W)"),
                items = listOf("Option 1"),
                selectedIndex = 0,
                onSelectedIndexChange = {},
                enabled = false,
            )
        }
    }
}
