package pages

import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class LoginPage(private val driver: RemoteWebDriver, url: String? = null) {
    init {
        url?.let { driver.get(it) }
        WebDriverWait(driver, Duration.ofSeconds(2)).until {
            driver.title.contains("login")
        }
        PageFactory.initElements(driver, this)
    }

    @FindBy(id = "bridgeAddress")
    private lateinit var addressField: WebElement

    @FindBy(id = "appName")
    private lateinit var appNameField: WebElement

    @FindBy(id = "login")
    private lateinit var loginButton: WebElement

    fun login(connectTo: String, appName: String): HomePage {
        addressField.sendKeys(connectTo)
        appNameField.sendKeys(appName)
        loginButton.click()
        return HomePage(driver)
    }
}
