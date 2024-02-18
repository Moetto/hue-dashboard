package dev.t3mu.huedashboard.pages

import dev.fritz2.core.RenderContext
import dev.fritz2.core.Store
import dev.fritz2.routing.Router
import dev.t3mu.huedashboard.load
import dev.t3mu.huedashboard.logout
import dev.t3mu.huedashboard.models.Configuration

fun RenderContext.content(router: Router<Page>, configuration: Store<Configuration>) {
    button("bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
        +"Logout"
        clicks.handledBy {
            logout()
            router.update(Page.login)
            configuration.update(load())
        }
    }
}
