package dev.t3mu.huedashboard.pages

import dev.fritz2.core.render
import dev.fritz2.core.storeOf
import dev.fritz2.routing.Route
import dev.fritz2.routing.Router
import dev.t3mu.huedashboard.load
import dev.t3mu.huedashboard.save
import kotlinx.coroutines.Job

enum class Page {
    login,
    content,
}

class Route(private val route: Page? = null) : Route<Page> {
    override val default: Page
        get() = route ?: Page.login

    override fun deserialize(hash: String) = Page.entries.find { it.name == hash } ?: Page.login

    override fun serialize(route: Page) = route.name
}

fun renderPage() {
    render {
        val configuration = storeOf(load())
        configuration.data.handledBy(::save)
        val router = Router(Route(), Job())

        router.data.render { page ->
            when (page) {
                Page.login -> {
                    login(router, configuration)
                }

                Page.content -> {
                    content(router, configuration)
                }
            }
        }
    }
}