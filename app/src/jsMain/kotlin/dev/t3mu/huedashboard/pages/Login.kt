package dev.t3mu.huedashboard.pages

import dev.fritz2.core.*
import dev.fritz2.routing.Router
import dev.t3mu.huedashboard.models.*
import kotlinx.coroutines.flow.map

fun RenderContext.login(router: Router<Page>, configuration: Store<Configuration>) {
    val formInput = storeOf(LoginInput())
    form("max-w-xs") {
        formField("Bridge address", "192.168.1.100", formInput.map(LoginInput.bridgeAddress()))
        formField("App name", "Dashboard light control", formInput.map(LoginInput.username()))
        input("disabled:bg-slate-800 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
            type("submit")
            value("Login")
            formInput.data.handledBy {
                disabled(it.username.isNullOrBlank() || it.bridgeAddress.isNullOrBlank())
            }
            clicks.handledBy {
                // Do log in here and use the response
                configuration.map(Configuration.logins())
                    .update(
                        LoginInformation(
                            formInput.current.bridgeAddress!!,
                            formInput.current.username!!,
                            "generated"
                        )
                    )
                router.update(Page.content)
            }
        }
    }
}

fun RenderContext.formField(label: String, placeHolder: String, field: Store<String?>) {
    label("block text-gray-700 text-sm font-bold mb-2") {
        `for`(field.id)
        +label
    }
    input(
        "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline",
        id = field.id
    ) {
        value(field.data.map { it ?: "" })
        placeholder(placeHolder)
        inputs.values().handledBy(field.update)
        required(true)
    }
}