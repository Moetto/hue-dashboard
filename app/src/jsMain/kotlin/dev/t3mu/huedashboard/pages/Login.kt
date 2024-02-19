package dev.t3mu.huedashboard.pages

import dev.fritz2.core.*
import dev.fritz2.routing.Router
import dev.t3mu.huedashboard.models.*
import kotlinx.browser.document
import kotlinx.coroutines.flow.map

fun RenderContext.loginPage(router: Router<Page>, configuration: Store<Configuration>) {
    val formInput = storeOf(LoginInput())
    document.title = "Hue dashboard - login"
    div("flex items-center justify-center") {
        form("max-w-s") {
            formField("Bridge address", "192.168.1.100", formInput.map(LoginInput.bridgeAddress()), "bridgeAddress")
            formField("App name", "Dashboard light control", formInput.map(LoginInput.username()), "appName")
            input("disabled:bg-slate-800 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded", "login") {
                type("submit")
                value("Login")
                formInput.data.handledBy {
                    disabled(it.username.isNullOrBlank() || it.bridgeAddress.isNullOrBlank())
                }
                formMethod("dialog")
                clicks.map { formInput.current } handledBy {
                    login(it).fold({
                        console.log("Failed to login")
                    }, { response ->
                        configuration.map(Configuration.logins()).update(response)
                        router.update(Page.home)
                    })
                }
            }
        }
    }
}

fun RenderContext.formField(label: String, placeHolder: String, field: Store<String?>, id: String = field.id) {
    label("block text-gray-700 text-sm font-bold mb-2") {
        `for`(id)
        +label
    }
    input(
        "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline",
        id = id
    ) {
        value(field.data.map { it ?: "" })
        placeholder(placeHolder)
        inputs.values().handledBy(field.update)
        required(true)
    }
}
