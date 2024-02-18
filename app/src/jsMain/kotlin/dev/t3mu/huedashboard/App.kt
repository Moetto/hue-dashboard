package dev.t3mu.huedashboard

import dev.t3mu.huedashboard.pages.renderPage

fun main() {
    js("""require("./app.css")""")
    renderPage()
}
