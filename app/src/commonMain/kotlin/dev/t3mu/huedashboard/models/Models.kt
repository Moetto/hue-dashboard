package dev.t3mu.huedashboard.models

import dev.fritz2.core.Lenses
import kotlinx.serialization.Serializable

@Serializable
@Lenses
data class Configuration(
    val bridgeAddress: String? = null,
) {
    companion object
}
