package dev.t3mu.huedashboard

import dev.t3mu.huedashboard.models.Configuration
import kotlinx.browser.localStorage
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
