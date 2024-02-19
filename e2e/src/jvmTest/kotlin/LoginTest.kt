import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.mockserver.configuration.ConfigurationProperties
import org.mockserver.integration.ClientAndServer
import org.mockserver.mock.OpenAPIExpectation
import org.openqa.selenium.firefox.FirefoxDriver
import pages.HomePage
import pages.LoginPage

class LoginTest : StringSpec() {
    val hueSpec = "https://github.com/openhue/openhue-api/releases/download/0.12/openhue.yaml"
    private lateinit var driver: FirefoxDriver
    private lateinit var server: ClientAndServer
    private var site: String =
        "http://localhost:${System.getProperty("server.port") ?: System.getenv("SERVER_PORT") ?: 8000}"
    private lateinit var bridge: String

    private fun enableCors() {
        ConfigurationProperties.enableCORSForAllResponses(true);
        ConfigurationProperties.corsAllowMethods("CONNECT, DELETE, GET, HEAD, OPTIONS, POST, PUT, PATCH, TRACE");
        ConfigurationProperties.corsAllowHeaders("Allow, Content-Encoding, Content-Length, Content-Type, ETag, Expires, Last-Modified, Location, Server, Vary, Authorization");
        ConfigurationProperties.corsAllowCredentials(true);
        ConfigurationProperties.corsMaxAgeInSeconds(300);
    }

    private fun startServer(): ClientAndServer {
        enableCors()
        val server = ClientAndServer.startClientAndServer(0)
        server.upsert(OpenAPIExpectation.openAPIExpectation(hueSpec))
        return server
    }

    override suspend fun beforeEach(testCase: TestCase) {
        driver = FirefoxDriver()
        server = startServer()
        bridge = "localhost:${server.port}"
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        driver.quit()
        server.stop()
    }

    init {
        "user can log in and is redirected to home" {
            LoginPage(driver, site).login(bridge, "my app")
        }

        "logging out redirects to login page" {
            HomePage(driver, site).logout()
        }
    }
}
