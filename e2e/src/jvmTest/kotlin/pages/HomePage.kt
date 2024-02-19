package pages

import dev.t3mu.huedashboard.models.Configuration
import dev.t3mu.huedashboard.models.LoginInformation
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class HomePage(private val driver: RemoteWebDriver, url: String? = null) {
    init {
        url?.let {
            driver.get(it)
            val l = LoginInformation("address", "username", "clientkey")
            val c = Configuration(l)
            driver.executeScript(
                """localStorage.setItem('hue-dashboard-configuration', '${Json.encodeToString(c)}')"""
            )
            driver.get(it)
        }
        WebDriverWait(driver, Duration.ofSeconds(2)).until {
            driver.title.contains("home")
        }
        PageFactory.initElements(driver, this)
    }

    @FindBy(id = "logout")
    lateinit var logoutButton: WebElement

    fun logout(): LoginPage {
        logoutButton.click()
        return LoginPage(driver)
    }
}
