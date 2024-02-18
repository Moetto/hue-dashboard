package dev.t3mu.huedashboard

import dev.fritz2.core.render

fun main()  {
    js("""require("./app.css")""")
    render {
        h1("text-3xl font-bold underline") {
            +"Hello world"
        }
    }
}
