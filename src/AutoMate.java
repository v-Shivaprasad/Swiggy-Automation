import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

public class AutoMate {

    public static void captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            File screenshotsDir = new File("screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path destPath = Paths.get("screenshots", screenshotName + "_" + timestamp + ".png");

            Files.copy(sourceFile.toPath(), destPath);
            System.out.println("📸 Screenshot captured: " + destPath.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Step 1: Setup
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Dell\\Downloads\\Swiggy Automation\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Step 2: Open Swiggy
        driver.get("https://www.swiggy.com/");
        Thread.sleep(2000);

        // Step 3: Print Title & URL
        System.out.println("📋 Page Title: " + driver.getTitle());
        System.out.println("🌐 Page URL: " + driver.getCurrentUrl());

        // Step 4: Click Sign In
        driver.findElement(By.xpath("//a[contains(text(),'Sign in')]")).click();

        // Step 5: Enter Phone Number and Submit OTP
        driver.findElement(By.id("mobile")).sendKeys("7993421044");
        driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();

        System.out.println("Please enter OTP manually...");
        Scanner sc = new Scanner(System.in);
        System.out.println("Press Enter after entering OTP");
        sc.nextLine();

        driver.findElement(By.xpath("//a[contains(text(),'VERIFY OTP')]")).click();

        // Step 6: Enter Location
        WebElement locationInput = driver.findElement(By.id("location"));
        locationInput.sendKeys("Hyderabad");
        Thread.sleep(3000);
        locationInput.sendKeys(Keys.ARROW_DOWN);
        locationInput.sendKeys(Keys.ENTER);
        Thread.sleep(3000);

        // Step 7: Click Search Box
        driver.findElement(By.xpath("//div[@type='button' and contains(.,'Search for restaurant, item or more')]")).click();
        Thread.sleep(2000);

        // Step 8: Search for Domino's Pizza
        WebElement searchInput = driver.findElement(By.xpath("//input[@placeholder='Search for restaurants and food']"));
        searchInput.sendKeys("Burger King");
        Thread.sleep(2000);
        searchInput.sendKeys(Keys.ENTER);
        Thread.sleep(5000);

        // Step 9: Select Restaurant
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        WebElement ItemCard = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@aria-label, \"Restaurant name: Burger King\")]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", ItemCard);
        Thread.sleep(2000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ItemCard);
        System.out.println("✅ Selected Restaurant: Burger King");

        try {
            WebElement pageNotFound = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(text(), 'Page not found')]")
            ));

            // If the above didn't throw TimeoutException, then page not found is true
            System.out.println("❌ Page not found. Reloading...");
            driver.navigate().refresh(); // Perform reload
            Thread.sleep(7000); // Wait after reload
        } catch (TimeoutException e) {
            // Page loaded fine
            System.out.println("✅ Restaurant page loaded successfully.");
            Thread.sleep(7000);
        }

        // Step 9.5: Handle "Ordering from far away" popup if it appears
        try {
            WebDriverWait popupWait = new WebDriverWait(driver, Duration.ofSeconds(4));
            WebElement gotItBtn = popupWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[contains(text(),'Got it!')]]")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", gotItBtn);
            System.out.println("⚠️ Dismissed 'far away' notification popup.");
            Thread.sleep(2000);  // allow DOM to settle
        } catch (TimeoutException e) {
            System.out.println("✅ No 'far away' popup appeared — continuing normally.");
        }

        // Step 10: Select the 2nd Dish Card and Click Its 'Add' Button
        List<WebElement> dishCards = driver.findElements(By.cssSelector("div[data-testid='normal-dish-item']"));

        WebElement secondDish = dishCards.get(1); // index 1 = second item

        // Extract name (optional)
        String itemNameToIncrement = secondDish.findElement(By.cssSelector("div.dwSeRx")).getText();
        System.out.println("🍔 Selected Food Item: " + itemNameToIncrement);

        // Scroll to the dish
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", secondDish);
        Thread.sleep(1000);

        // Find and click 'Add' button
        WebElement addBtn = secondDish.findElement(By.cssSelector("button.add-button-center-container"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
        System.out.println("✅ Clicked 'Add' for: " + itemNameToIncrement);

        Thread.sleep(2000);

        // Step 12: Add Item to Cart
        try {
            // Check for cart reset confirmation
            WebElement resetCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Yes, start afresh')]")
            ));

            // Click the "Yes, start afresh" button
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", resetCartBtn);
            System.out.println("⚠️ Cart conflict detected. Clicked: Yes, start afresh");
            Thread.sleep(2000);
        } catch (TimeoutException e) {
            System.out.println("✅ No cart conflict popup appeared. Proceeding...");
        }

        Thread.sleep(2000);
        driver.findElement(By.xpath("//button[@data-cy='customize-footer-add-button']")).click();
        System.out.println("✅ Clicked: Add Item to Cart");
        Thread.sleep(5000);

        try {
            // Wait up to 5 seconds to check if customization popup appears
            WebElement addToCartPopupBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@data-cy='customize-footer-add-button']"))
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCartPopupBtn);
            Thread.sleep(300);  // allow scroll to finish

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartPopupBtn);
            System.out.println("✅ Customization popup found and 'Add Item to cart' clicked");
        } catch (TimeoutException e) {
            System.out.println("ℹ️ Customization popup did not appear. Continuing...");
        }

        // Step 13: Click View Cart (JS-based for safety)
        WebElement viewCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("view-cart-btn")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", viewCartBtn);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewCartBtn);
        System.out.println("✅ Clicked: View Cart");
        Thread.sleep(3000);

        // 📸 SCREENSHOT 1: After adding item to cart
        captureScreenshot(driver, "01_after_adding_item_to_cart");

        // Quantity selector
        List<WebElement> items = driver.findElements(By.cssSelector("div._38bXh"));

        for (WebElement item : items) {
            String itemText = item.getText().trim();
            System.out.println(itemText +" , "+ itemNameToIncrement );

            if (itemText.contains(itemNameToIncrement)) {
                // Scroll the item into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", item);
                System.out.println("🎯 Target item found: " + itemNameToIncrement);

                // Wait for the + button inside this item
                WebElement plusButton = item.findElement(By.cssSelector("div.BbiqG"));

                // Wait until clickable to avoid race issues
                WebDriverWait innerWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                innerWait.until(ExpectedConditions.elementToBeClickable(plusButton));

                // Click + twice (as needed)
                plusButton.click();
                Thread.sleep(500); // Optional: helps with animations
                ClickThePopup(wait, driver);

                plusButton.click();
                Thread.sleep(500);
                ClickThePopup(wait, driver);

                System.out.println("✅ Incremented item: " + itemNameToIncrement);
                break; // Done
            }
        }

        // 📸 SCREENSHOT 2: After increasing quantity
        captureScreenshot(driver, "02_after_increasing_quantity");

        // Step 14: Add Address
        WebElement addNewAddressBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Add new Address')]")
        ));
        addNewAddressBtn.click();
        System.out.println("✅ Clicked: Add New Address");

        driver.findElement(By.id("building")).sendKeys("H.No 8-123");
        driver.findElement(By.id("landmark")).sendKeys("Dr. Krishna Nagar, Moula Ali");

        // Home btn exception
        WebElement homeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='_1qiSu']//div[text()='Home']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", homeBtn);
        Thread.sleep(500);
        homeBtn.click();
        System.out.println("✅ Clicked: Home Button");

        System.out.println("Selected address type: Home");
        Thread.sleep(5000);

        // Step 15: Proceed to Pay
        WebElement saveAndProceedBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'SAVE ADDRESS')]")));
        saveAndProceedBtn.click();
        System.out.println("✅ Clicked: SAVE ADDRESS & PROCEED");

        // 📸 SCREENSHOT 3: After entering delivery address
        captureScreenshot(driver, "03_after_entering_delivery_address");

        WebElement proceedToPayBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'_4dnMB') and text()='Proceed to Pay']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", proceedToPayBtn);
        Thread.sleep(500);

        // Extract the total amount before proceeding to pay
        String totalAmount ="";
        try {
            WebElement amountElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@class='_33IW5']//div[@class='_1Qyhr']")
            ));

            totalAmount = amountElement.getText().trim();
            System.out.println("💰 Cart Total:" + totalAmount);

        } catch (TimeoutException e) {
            System.out.println("⚠️ Could not extract total amount - element not found");
        }

        proceedToPayBtn.click();
        System.out.println("✅ Clicked: Proceed to Pay");

        // 📸 SCREENSHOT 4: After clicking Proceed to Pay
        Thread.sleep(3000); // Wait for payment page to load
        captureScreenshot(driver, "04_after_proceed_to_pay");

        // Final Console Logs Summary
        System.out.println("\n" + "*".repeat(50));
        System.out.println("AUTOMATION SUMMARY");
        System.out.println("=".repeat(50));
        System.out.println("Page Title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Selected Restaurant: Burger King");
        System.out.println("Selected Food Item: " + itemNameToIncrement);
        System.out.println("Total Amount in Cart: "+ totalAmount);
        System.out.println("Delivery Address: H.No 8-123, Dr. Krishna Nagar, Moula Ali");
        System.out.println("Screenshots captured in 'screenshots' folder");
        System.out.println("Automation completed successfully!");
        System.out.println("*".repeat(50));
        String s = sc.nextLine();
        if(s.toLowerCase().equals("exit")) driver.quit();
        // Optional: Close browser
        // driver.quit();
    }

    public static void ClickThePopup(WebDriverWait wait, WebDriver driver) {
        try {
            // Wait for the popup and click "REPEAT LAST"
            WebElement repeatLastBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'REPEAT LAST')]")
            ));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", repeatLastBtn);
            System.out.println("✅ 'Repeat Last Customization' popup appeared. Clicked: REPEAT LAST");
            Thread.sleep(2000); // Let the action complete
        } catch (TimeoutException e) {
            System.out.println("✅ No 'Repeat Last Customization' popup. Continuing...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}