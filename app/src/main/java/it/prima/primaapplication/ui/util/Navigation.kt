package it.prima.primaapplication.ui.util

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

@Suppress("RegExpRedundantEscape")
private val ARG_MATCHING_REGEX = "\\{.+\\}".toRegex()

fun NavController.navigate(
    route: String,
    vararg args: String,
    builder: NavOptionsBuilder.() -> Unit
) {
    var newRoute = route
    for (arg in args) {
        newRoute = route.replaceFirst(ARG_MATCHING_REGEX, arg)
    }

    navigate(newRoute, builder)
}