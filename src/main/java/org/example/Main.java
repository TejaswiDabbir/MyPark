package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Boolean confirmRegistration = true;//Set to false if you only want to test if the details are filled correctly

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String driverPath = properties.getProperty("driverPath");
        String propertyName = properties.getProperty("propertyName");
        String userApt = properties.getProperty("userApt");
        String userCarMake = properties.getProperty("userCarMake");
        String userCarModel = properties.getProperty("userCarModel");
        String userCarLicense = properties.getProperty("userCarLicense");
        String userEmail = properties.getProperty("userEmail");

        //TODO: Add config file values validation

        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            driver.get("https://www.register2park.com/register");

            //Part 1 : Enter property name
            var propertyNameInputField = driver.findElement(By.id("propertyName"));
            propertyNameInputField.sendKeys(propertyName);
            var nextButton = driver.findElement(By.id("confirmProperty"));
            nextButton.click();

            //Part 2 : Confirm property
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//b[contains(text(), '%s')]", propertyName))));
            WebElement radioButton = driver.findElement(By.xpath(String.format("//b[contains(text(), '%s')]/preceding-sibling::input[@type='radio']", propertyName)));
            radioButton.click();

            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("confirmPropertySelection")));
            confirmButton.click();

            //Part 3 : Choose visitor parking option
            WebElement visitorParkingButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("registrationTypeVisitor")));
            visitorParkingButton.click();

            //Part 4 : Enter user values into input fields
            WebElement aptInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleApt")));
            aptInput.sendKeys(userApt);
            var makeInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleMake")));
            makeInput.sendKeys(userCarMake);
            var modelInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleModel")));
            modelInput.sendKeys(userCarModel);
            var licenseInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleLicensePlate")));
            licenseInput.sendKeys(userCarLicense);
            var licenseConfirmInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleLicensePlateConfirm")));
            licenseConfirmInput.sendKeys(userCarLicense);

            if(confirmRegistration)
            {
                WebElement confirmVehicleButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("vehicleInformation")));
                confirmVehicleButton.click();

                //Part 5 : Send email confirmation
                WebElement emailButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email-confirmation")));
                emailButton.click();

                WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("emailConfirmationEmailView")));
                emailInput.sendKeys(userEmail);

                var sendEmailButton = driver.findElement(By.id("email-confirmation-send-view"));
                sendEmailButton.click();
            }


        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
        finally {
            //Comment below line if you want to check the inputs entered and confirm the registration
            driver.quit();
        }
    }
}