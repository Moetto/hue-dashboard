import arrow.core.right
import dev.t3mu.huedashboard.models.LoginInformation
import dev.t3mu.huedashboard.models.LoginInput
import dev.t3mu.huedashboard.models.login
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LoginTest {
    @Test
    fun loginWorksAsExpected() = runTest {
        val mockEngine = MockEngine { request ->
            request.url.fullPath shouldBe "/api"
            (request.body as TextContent).text shouldEqualJson """
                {
                  "devicetype": "my app",
                  "generateclientkey": true
                }
            """.trimIndent()
            respond(
                content = ByteReadChannel(
                    """
                    [
                      {
                        "success": {
                          "username": "username",
                          "clientkey": "clientkey"
                        }
                      }
                    ]
                """.trimIndent()
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val input = LoginInput("address", "my app")
        login("http://address", "my app", mockEngine) shouldBe LoginInformation(
            "http://address",
            "username",
            "clientkey"
        ).right()
    }
}
