package dev.t3mu.huedashboard.pages

import dev.fritz2.core.Store
import dev.fritz2.core.render
import dev.fritz2.routing.Route
import dev.fritz2.routing.Router
import dev.t3mu.huedashboard.models.Configuration

enum class Page {
    login,
    home,
}

class Route(private val route: Page? = null) : Route<Page> {
    override val default: Page
        get() = route ?: Page.login

    override fun deserialize(hash: String) = Page.entries.find { it.name == hash } ?: Page.login

    override fun serialize(route: Page) = route.name
}

fun renderPage(router: Router<Page>, configuration: Store<Configuration>) {
    render {
        router.data.render { page ->
            when (page) {
                Page.login -> {
                    loginPage(router, configuration)
                }

                Page.home -> {
                    home(router, configuration)
                }
            }
        }
    }
}
