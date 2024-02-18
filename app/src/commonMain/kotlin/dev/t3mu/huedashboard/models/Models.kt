package dev.t3mu.huedashboard.models

import dev.fritz2.core.Lenses
import kotlinx.serialization.Serializable

@Serializable
@Lenses
data class Configuration(
    val bridgeAddress: String? = null,
    val credentials: Credentials? = null,
) {
    companion object
}

@Serializable
@Lenses
data class Credentials(
    val username: String,
    val clientKey: String,
) {
    companion object
}
