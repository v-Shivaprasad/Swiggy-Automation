//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.time.Duration;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) throws InterruptedException {
//        // Step 1: Setup
//        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Dell\\Downloads\\Swiggy Automation\\chromedriver-win64\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-notifications");
//        WebDriver driver = new ChromeDriver(options);
//        driver.manage().window().maximize();
//
//        // Step 2: Open Swiggy
//        driver.get("https://www.swiggy.com/");
//        Thread.sleep(2000);
//
//        // Step 3: Print Title & URL
//        System.out.println("Page Title: " + driver.getTitle());
//        System.out.println("Page URL: " + driver.getCurrentUrl());
//
//        // Step 4: Click SignInBtn
//        WebElement SignInBtn = driver.findElement(By.xpath("//a[contains(text(),'Sign in')]"));
//        SignInBtn.click();
//
//        // Step 5: Enter Phone Number
//        WebElement phoneInput = driver.findElement(By.id("mobile"));
//        phoneInput.sendKeys("7993421044");
//        WebElement sendOtpBtn = driver.findElement(By.xpath("//a[contains(text(),'Login')]"));
//        sendOtpBtn.click();
//        // Manual OTP Step
//        System.out.println("Please enter OTP manually...");
////        Thread.sleep(7000);  // 20 seconds to enter OTP manually
//        //verify Otp
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Press Enter after Entering OTp");
//        String s = sc.nextLine();
//        WebElement verifyOtpBtn = driver.findElement(By.xpath("//a[contains(text(),'VERIFY OTP')]"));
//        verifyOtpBtn.click();
//        // Continue after login
//        // Step 6: Enter location
//        WebElement locationInput = driver.findElement(By.id("location"));
//        locationInput.sendKeys("Hyderabad");
//        Thread.sleep(3000); // wait for suggestions
//        locationInput.sendKeys(Keys.ARROW_DOWN);
//        locationInput.sendKeys(Keys.ENTER);
//        Thread.sleep(3000);
//
//        // Step 7: navigate to searchbox page it reloadszzzz.... check
//        WebElement searchBox = driver.findElement(By.xpath("//div[@type='button' and contains(.,'Search for restaurant, item or more')]"));
//        searchBox.click();
//        Thread.sleep(3000);
//
//        //step 8: search food
//        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Search for restaurants and food']"));
//        searchInput.sendKeys("Domino's Pizza");
//        Thread.sleep(3000);
//        searchInput.sendKeys(Keys.ENTER);
//        Thread.sleep(5000);
//
//        // Step 8: Select restaurant
////        WebElement dominosCard = driver.findElement(By.xpath("//a[contains(@aria-label, \"Restaurant name: Domino's Pizza\")]"));
////        System.out.println("Selected Restaurant: Domino's Pizza");
////        dominosCard.click();
////        Thread.sleep(5000);
//
//        // Wait for Domino's card to be present
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement dominosCard = wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.xpath("//a[contains(@aria-label, \"Restaurant name: Domino's Pizza\")]")
//        ));
//
//// Scroll + JS Click
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", dominosCard);
//        Thread.sleep(500);
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dominosCard);
//        System.out.println("✅ Selected Restaurant: Domino's Pizza");
//
//        // 8.5 step: log foodName
//        WebElement foodName = driver.findElement(By.xpath("(//div[contains(@class,'dwSeRx')])[1]"));
//        System.out.println("Selected Food Item: " + foodName.getText());
//        //  Step 9: Add to cart
//        WebElement secondAddBtn = driver.findElement(By.xpath("(//button[contains(@class,'add-button-center-container')])[2]"));
//        secondAddBtn.click();
//        System.out.println("Clicked on Add button for second food item.");
//        Thread.sleep(3000);
//
//        // Step 10: Continue Button
//        WebElement continueBtn = driver.findElement(By.xpath("//button[@data-testid='menu-customize-continue-button']"));
//        continueBtn.click();
//        System.out.println("Clicked on 'Continue' in customization step");
//        Thread.sleep(3000);
//
//        // Step 11: Click "Add Item to cart" button
//        WebElement addToCartBtn = driver.findElement(By.xpath("//button[@data-cy='customize-footer-add-button']"));
//        addToCartBtn.click();
//        System.out.println("Clicked: Add Item to cart");
//        Thread.sleep(7000);  // Wait for cart to update
//
//
//        // Step 12: Click "View Cart" button
////        WebElement viewCartBtn = driver.findElement(By.xpath("//button[@id='view-cart-btn']"));
////        viewCartBtn.click();
////        System.out.println("Clicked: View Cart");
////        Thread.sleep(4000);
//
//        // Step: Click View Cart button safely
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement viewCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("view-cart-btn")));
//
//// Scroll into view to ensure visibility
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", viewCartBtn);
//        Thread.sleep(500); // optional buffer after scroll
//
//// Click using JavaScript to avoid overlays/toasts blocking native click
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewCartBtn);
//
//        System.out.println("✅ Clicked: View Cart");
//        Thread.sleep(3000);  // Wait for cart page to load (optional)
//
//
//        // Step 13: New Address popup opener
//        WebElement addAddressBtn = driver.findElement(By.xpath("//div[contains(text(),'Add new Address')]"));
//        addAddressBtn.click();
//        System.out.println("Clicked: Add New Address");
//        Thread.sleep(3000);
//        // Step 13.1  : Fill in Address
//        // Step 18: Fill Address Fields
//        WebElement flatNo = driver.findElement(By.id("building"));
//        flatNo.sendKeys("H.No 8-123");
//
//        WebElement landmark = driver.findElement(By.id("landmark"));
//        landmark.sendKeys("Dr. Krishna Nagar, Moula Ali");
//        WebElement homeBtn = driver.findElement(By.xpath("//div[contains(text(),'Home')]"));
//        homeBtn.click();
//        System.out.println("Selected address label: Home");
//        Thread.sleep(1000);
//
//        WebElement saveBtn = driver.findElement(By.xpath("//button[contains(text(),'Save')]"));
//        saveBtn.click();
//        System.out.println("Clicked: Save Address");
//        Thread.sleep(5000);
//
//        WebElement Click2Proceed = driver.findElement(By.xpath("//button[contains(text(),'Proceed to Pay')]"));
//        Click2Proceed.click();
//        System.out.println("✅ Clicked: Proceed to Pay");
//        // Close browser at the end
//        // driver.quit();
//    }
//}