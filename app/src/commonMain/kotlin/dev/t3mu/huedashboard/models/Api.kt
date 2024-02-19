package dev.t3mu.huedashboard.models

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.t3mu.openhue.apis.AuthApi
import dev.t3mu.openhue.models.AuthenticateRequest
import io.ktor.client.engine.HttpClientEngine

sealed interface Error
data object InvalidInput : Error
data object ApiError : Error

suspend fun login(input: LoginInput, httpClientEngine: HttpClientEngine? = null): Either<Error, LoginInformation> {
    if (input.bridgeAddress.isNullOrBlank() || input.username.isNullOrBlank()) {
        return InvalidInput.left()
    }

    return AuthApi(baseUrl = "http://${input.bridgeAddress}", httpClientEngine = httpClientEngine).authenticate(
        AuthenticateRequest(input.username, true)
    )
        .body()
        .first()
        .let { responseInner ->
            responseInner.success?.let {
                LoginInformation("http://${input.bridgeAddress}", it.username!!, it.clientkey!!).right()
            } ?: ApiError.left()
        }
}
