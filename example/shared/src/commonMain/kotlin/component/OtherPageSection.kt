// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package component
import i18n.str

import LocalNavigator
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navigation.Route
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.preference.ArrowPreference
import kotlin.random.Random

fun LazyListScope.otherPageSection() {
    item(key = "other") {
        val navigator = LocalNavigator.current
        SmallTitle(text = str("Other"))
        Card(
            modifier = Modifier
                .padding(horizontal = 12.dp),
        ) {
            ArrowPreference(
                title = str("PullToRefresh Test"),
                summary = str("Navigate to a PullToRefresh Page"),
                onClick = {
                    navigator.push(Route.PullToRefresh)
                },
            )
            ArrowPreference(
                title = str("Navigation test"),
                summary = str("Navigate to a Navigation Page"),
                onClick = { navigator.push(Route.Navigation(Random.nextLong().toString())) },
            )
            ArrowPreference(
                title = str("MultiScaffold Test"),
                summary = str("Navigate to a MultiScaffold Page"),
                onClick = { navigator.push(Route.MultiScaffold) },
            )
            ArrowPreference(
                title = str("Nested Navigation Test"),
                summary = str("A NavDisplay nested inside an entry"),
                onClick = { navigator.push(Route.NestedNav) },
            )
            ArrowPreference(
                title = str("Overscroll + Load More Test"),
                summary = str("Fling to the bottom, then fling again"),
                onClick = { navigator.push(Route.OverscrollLoadMore) },
            )
        }
    }
}
