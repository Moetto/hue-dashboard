package dev.t3mu.huedashboard.models

import dev.fritz2.core.Lenses
import kotlinx.serialization.Serializable

@Serializable
@Lenses
data class Configuration(
    val logins: LoginInformation? = null,
) {
    companion object
}

@Lenses
data class LoginInput(
    val bridgeAddress: String? = null,
    val username: String? = null,
) {
    companion object
}

@Serializable
@Lenses
data class LoginInformation(
    val bridgeAddress: String,
    val username: String,
    val clientKey: String,
) {
    companion object
}
