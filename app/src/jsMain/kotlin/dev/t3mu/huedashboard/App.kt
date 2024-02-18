package dev.t3mu.huedashboard

import dev.fritz2.core.*
import dev.fritz2.routing.Router
import dev.t3mu.huedashboard.Page.content
import dev.t3mu.huedashboard.Page.login
import dev.t3mu.huedashboard.models.Configuration
import dev.t3mu.huedashboard.models.Credentials
import dev.t3mu.huedashboard.models.bridgeAddress
import dev.t3mu.huedashboard.models.credentials
import kotlinx.browser.localStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val storageKey = "huedashboard-configuration"
fun save(configuration: Configuration) {
    console.log("Saving new configuration")
    localStorage.setItem(storageKey, Json.encodeToString(configuration))
}

fun load(): Configuration {
    console.log("Loading new configuration")
    return localStorage.getItem(storageKey)?.let {
        Json.decodeFromString<Configuration>(it)
    } ?: Configuration()
}

fun logout() {
    localStorage.clear()
    load()
}

fun main() {
    js("""require("./app.css")""")
    render {
        val configuration = storeOf(load())
        configuration.data.handledBy(::save)
        val router = Router(Route(), Job())

        router.data.render { page ->
            when (page) {
                login -> {
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
                            router.update(content)
                        }
                    }
                }

                content -> {
                    button("bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                        +"Logout"
                        clicks.handledBy {
                            logout()
                            router.update(login)
                            configuration.update(load())
                        }
                    }
                }
            }
        }
    }
}
