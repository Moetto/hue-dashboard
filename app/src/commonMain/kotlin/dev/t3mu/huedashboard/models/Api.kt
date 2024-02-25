package dev.t3mu.huedashboard.models

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.t3mu.openhue.apis.AuthApi
import dev.t3mu.openhue.models.AuthenticateRequest
import io.ktor.client.engine.HttpClientEngine
import kotlin.jvm.JvmInline

sealed interface Error
data object ApiError : Error

@JvmInline
value class BridgeAddress(val s: String)

@JvmInline
value class UserName(val s: String)

suspend fun login(
    url: BridgeAddress,
    username: UserName,
    httpClientEngine: HttpClientEngine? = null
): Either<Error, LoginInformation> {
    return AuthApi(baseUrl = url.s, httpClientEngine = httpClientEngine).authenticate(
        AuthenticateRequest(username.s, true)
    )
        .body()
        .first()
        .let { responseInner ->
            responseInner.success?.let {
                LoginInformation(url.s, it.username!!, it.clientkey!!).right()
            } ?: ApiError.left()
        }
}
