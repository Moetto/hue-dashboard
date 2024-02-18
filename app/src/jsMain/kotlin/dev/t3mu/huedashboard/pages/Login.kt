package dev.t3mu.huedashboard.pages

import dev.fritz2.core.*
import dev.fritz2.routing.Router
import dev.t3mu.huedashboard.models.Configuration
import dev.t3mu.huedashboard.models.Credentials
import dev.t3mu.huedashboard.models.bridgeAddress
import dev.t3mu.huedashboard.models.credentials
import kotlinx.coroutines.flow.map

fun RenderContext.login(router: Router<Page>, configuration: Store<Configuration>) {
    val bridgeAddress = configuration.map(Configuration.bridgeAddress())
    div("max-w-xs") {
        label("block text-gray-700 text-sm font-bold mb-2") {
            `for`(bridgeAddress.id)
            +"Bridge address"
        }
        input(
            "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline",
            id = bridgeAddress.id
        ) {
            value(bridgeAddress.data.map { it ?: "" })
            placeholder("192.168.1.100")
            inputs.values().handledBy(bridgeAddress.update)
        }
    }
    button("disabled:bg-slate-800 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
        +"Login"
        bridgeAddress.data.handledBy {
            disabled(it.isNullOrBlank())
        }
        clicks.handledBy {
            configuration.map(Configuration.credentials()).update(Credentials("hello", "world"))
            router.update(Page.content)
        }
    }
}
