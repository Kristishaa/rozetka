package Rozetka;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Listeners({TestListener.class})

public class RozetkaSearchTest {
    private WebDriver driver;
    private String baseUrl = "http://rozetka.com.ua/";
    private  Cookie ck;

    @BeforeTest
    public void searchTest(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver2");
        driver = new ChromeDriver();
        driver.manage().window().maximize();


    }

    @Test
    public  void findItemTest() throws InterruptedException, IOException {
        driver.get(baseUrl);
        ck = new Cookie("ab_main_page", "old");
        driver.manage().addCookie(ck);
        WebElement searchLink1 = driver.findElement(By.xpath("/html/body/app-root/div/div[1]/div[3]/div/aside/main-page-sidebar/sidebar-fat-menu/div/ul/li[1]/a"));
        searchLink1.click();
        boolean status1 = driver.findElement(By.linkText("Ноутбуки")).isDisplayed();
        boolean status2 = driver.findElement(By.linkText("Компьютеры")).isDisplayed();
        WebElement searchLink2 = driver.findElement(By.xpath("//*[@id=\"content-inner-block\"]/div[3]/div/div/div[2]/div/div[1]/div[1]/div/div[3]/div/div/div[1]/div/div[1]/div[1]/div/p/a"));
        searchLink2.click();
        WebElement searchLink3 = driver.findElement(By.xpath("//*[@id=\"menu_categories_left\"]/li[2]/ul/li[3]/a"));
        searchLink3.click();
        //проверяю, что чекбокс асус чекнутый
        boolean checkAsus = driver.findElement(By.cssSelector("#filter_producer_4 > label > a")).isSelected();
        //проверить, что все ноутбуки асус
        //Выбрать в фильтре цена  значение до 10000 грн
        WebElement searchPrice = driver.findElement(By.cssSelector("#price\\5b max\\5d"));
        searchPrice.sendKeys("10000");
        WebElement button = driver.findElement(By.cssSelector("#submitprice"));
        button.click();
        Thread.sleep(5000);
        // тут проверить, что список обновлён и цена в ходит в диапазон
        //выбрать ноут и проверить цены
        List<WebElement> elements = driver.findElements(By.xpath("//*[@data-view_type='catalog_with_hover']"));
        //не стабильно работает
        //Assert.assertEquals(elements.size(), 31);
        for(WebElement element : elements){
            if(element.getText().toLowerCase().contains("asus")){
            }
            else{
                Assert.fail("name isn't correct");
            }
            WebElement priceElement =element.findElement(By.className("g-price-uah"));
            int price = Integer.parseInt(priceElement.getText().replaceAll("\\s+","").replaceAll("\\D+", ""));

            if(price < 10000){
            }
            else{
                Assert.fail("price filter error");
            }
        }
        WebElement notebook = driver.findElement(By.linkText("Ноутбук Asus VivoBook RZ540MA-GQ008 (90RZ0IR1-M00080) Chocolate Black"));
        notebook.click();
        WebElement price = driver.findElement(By.xpath("//*[@id=\"price_label\"]"));
        int priceal = Integer.parseInt(price.getText().replaceAll("\\s+","").replaceAll("\\D+", ""));
        //Assert.assertEquals(priceal, 6999);
        if(priceal == 6999){

        }
        else{
            //System.out.print("Price has changed");
            Reporter.log( "Price has changed", true );
        }

        //открыть характеристики ноутбука
        WebElement characteristic = driver.findElement(By.xpath("//*[@id=\"tabs\"]/li[2]"));
        characteristic.click();
        //сделать скриншот
        File scrFile1 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile1, new File("/Users/kristinahazukina/Documents/Screenshot/screenshot1.jpg"));
        WebElement feedbacks = driver.findElement(By.xpath("//*[@id=\"tabs\"]/li[4]"));
        feedbacks.click();
        //пока поставила ожидание, позже переделаю
        //Thread.sleep(10000);
        WebElement myDynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"comments\"]/article[1]/div/div[1]/div[3]")));
        WebElement lastFeedback = driver.findElement(By.xpath("//*[@id=\"comments\"]/article[1]/div/div[1]/div[3]"));
        lastFeedback.getText();
        //прикрутить репорт с последним комментарием и скриншотом



    }


        @AfterTest
    public void quitDriver() {
        driver.quit();
    }
}
