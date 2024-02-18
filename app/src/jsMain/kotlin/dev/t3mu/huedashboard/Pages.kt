package dev.t3mu.huedashboard

import dev.fritz2.routing.Route

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
