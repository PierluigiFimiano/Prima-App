package it.prima.primaapplication.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow
import it.prima.primaapplication.util.Event
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull

@Composable
fun <T : Any> CollectEvent(
    key: State<Event<T>?>,
    action: suspend (value: T) -> Unit
) {
    LaunchedEffect(key) {
        snapshotFlow { key.value }
            .mapNotNull { event ->
                event?.getContentIfNotHandled()
            }
            .collect(action)
    }
}