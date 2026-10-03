// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import i18n.appLanguage
import i18n.str
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.SnackbarDuration
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.SnackbarResult
import top.yukonga.miuix.kmp.basic.TextButton

fun LazyListScope.snackbarSection(snackbarHostState: SnackbarHostState) {
    item(key = "snackbar") {
        SmallTitle(text = str("Snackbar"))
        val scope = rememberCoroutineScope()
        Card(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(
                        text = str("Dismiss oldest"),
                        onClick = {
                            scope.launch {
                                snackbarHostState.oldestSnackbarData()?.dismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(
                        text = str("Dismiss newest"),
                        onClick = {
                            scope.launch {
                                snackbarHostState.newestSnackbarData()?.dismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(
                        text = str("Short (4s)"),
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(str("This message stays for 4 seconds."))
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(
                        text = str("Long (10s)"),
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = str("This is a longer message that stays for 10 seconds."),
                                    duration = SnackbarDuration.Long,
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(
                        text = str("Custom (2s)"),
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = str("This message uses a custom 2-second duration."),
                                    duration = SnackbarDuration.Custom(2000L),
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                    var text by remember(appLanguage) { mutableStateOf(str("Action")) }
                    TextButton(
                        text = text,
                        onClick = {
                            scope.launch {
                                text = str("Action: Alive")
                                val result = snackbarHostState.showSnackbar(
                                    message = str("This message has an action button."),
                                    actionLabel = str("Undo"),
                                    duration = SnackbarDuration.Short,
                                )
                                text = when (result) {
                                    SnackbarResult.ActionPerformed -> str("Action: Undo")
                                    else -> str("Action: Expired")
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColorsPrimary(),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(
                        text = str("Dismissible"),
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = str("Tap the close button to dismiss this message."),
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Long,
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(
                        text = str("Indefinite"),
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = str("This message stays until you dismiss it manually."),
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Indefinite,
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(
                        text = str("Action + Close"),
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = str("This message has both an action and a close button."),
                                    actionLabel = str("Undo"),
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Long,
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
