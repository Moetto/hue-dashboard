package dev.t3mu.huedashboard

import dev.fritz2.core.storeOf
import dev.fritz2.routing.Router
import dev.t3mu.huedashboard.pages.Page
import dev.t3mu.huedashboard.pages.Route
import dev.t3mu.huedashboard.pages.renderPage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main() {
    val router = Router(Route(), Job())
    val configuration = storeOf(load(), Job())
    GlobalScope.launch {
        configuration.data.collect {
            save(it)
        }
    }
    configuration.current.logins?.run {
        console.log("There's a saved login")
        router.update(Page.home)
    }
    renderPage(router, configuration)
}
