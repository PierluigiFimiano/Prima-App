package it.prima.primaapplication.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.primarySurface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun rememberCollapsingToolbarState(
    toolbarHeight: Int = 0,
    toolbarOffset: Float = 0f
) = rememberSaveable(saver = CollapsingToolbarState.Saver) {
    CollapsingToolbarState(
        toolbarHeight,
        toolbarOffset
    )
}

class CollapsingToolbarState(
    toolbarHeight: Int = 0,
    toolbarOffset: Float = 0f
) {

    var toolbarHeight by mutableStateOf(toolbarHeight)
        private set

    var toolbarOffset by mutableStateOf(toolbarOffset)
        private set

    private fun updateOffset(offset: Float = toolbarOffset) {
        val newOffset = offset.coerceIn(-toolbarHeight.toFloat(), 0f)
        if (newOffset == toolbarOffset) {
            return
        }
        toolbarOffset = newOffset
    }

    internal fun updateHeightAfterMeasurement(height: Int) {
        if (toolbarHeight == height) {
            return
        }
        toolbarHeight = height
        updateOffset()
    }

    internal fun onPreScroll(delta: Float) {
        val newOffset = toolbarOffset + delta
        updateOffset(newOffset)
    }

    companion object {
        val Saver: Saver<CollapsingToolbarState, *> = listSaver(
            save = { listOf(it.toolbarHeight, it.toolbarOffset) },
            restore = {
                CollapsingToolbarState(
                    toolbarHeight = it[0].toInt(),
                    toolbarOffset = it[1].toFloat()
                )
            }
        )
    }
}


@Composable
fun CollapsingToolbarLayout(
    modifier: Modifier = Modifier,
    state: CollapsingToolbarState = rememberCollapsingToolbarState(),
    toolBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val nestedScrollConnection = remember(state) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                state.onPreScroll(available.y)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
    ) {
        content()

        Surface(
            content = toolBar,
            modifier = Modifier
                .offset { IntOffset(x = 0, y = state.toolbarOffset.roundToInt()) }
                .onGloballyPositioned { coordinates ->
                    state.updateHeightAfterMeasurement(coordinates.size.height)
                }
                .fillMaxWidth(),
            color = MaterialTheme.colors.primarySurface,
            elevation = AppBarDefaults.TopAppBarElevation
        )
    }
}