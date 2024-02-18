package dev.t3mu.huedashboard

import dev.fritz2.core.*
import dev.t3mu.huedashboard.models.Configuration
import dev.t3mu.huedashboard.models.bridgeAddress
import kotlinx.browser.localStorage
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

        val bridgeAddress = configuration.map(Configuration.bridgeAddress())
        label {
            `for`(bridgeAddress.id)
            +"Bridge address"
        }
        input(id = bridgeAddress.id) {
            value(bridgeAddress.data.map { it ?: "" })
            placeholder("192.168.1.100")
            changes.values().handledBy(bridgeAddress.update)
        }
        button {
            +"Logout"
            clicks.handledBy {
                logout()
                configuration.update(load())
            }
        }
    }
}
