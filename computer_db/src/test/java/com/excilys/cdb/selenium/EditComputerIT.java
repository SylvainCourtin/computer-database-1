package com.excilys.cdb.selenium;

import static org.testng.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.ServiceException;

public class EditComputerIT extends SeleniumSuite {

    private Computer edit;

    private static final String NAMEINVALID = "";

    /** Beforeclass.
     * @throws DAOConfigurationException configuration exception
     * @throws ServiceException service exception
     */
    @BeforeClass
    public void beforeClass() throws DAOConfigurationException, ServiceException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=list_computers&page=1");
        List<WebElement> elem = driver.findElements(By.xpath("//table/tbody/tr/td/a"));
        elem.get(0).click();
        String idS = driver.getCurrentUrl().replace("http://localhost:9090/computer_db/computer?action=edit_form_computer&id=", "");
        Integer id = Integer.valueOf(idS.trim());
        driver.findElement(By.id("computerName")).getAttribute("value")
        je.executeScript("return document.getElementById('introduced').value;")
        edit = new Computer(setId(id), );companyId
        System.out.println("Val: " + id);
    }

    /**
     */
    @AfterClass
    public void afterClass() {
        this.closeInstance();
    }

    /** Method used in editComputer.
     * @param name name à insérer
     */
    private void editTextComputerName(String name) {
        driver.findElement(By.id("computerName")).sendKeys(name);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        assertEquals(name, driver.findElement(By.id("computerName")).getAttribute("value"));

    }

    /** Method used in editComputer.
     * @param introduced la date à insérer
     * @param je javascript executor
     */
    private void editTextIntroduced(JavascriptExecutor je, LocalDateTime introduced) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = introduced.format(formatter);
        je.executeScript("return document.getElementById('introduced').value = '" + date + "';");
        Assert.assertEquals((String) je.executeScript("return document.getElementById('introduced').value;"), date);
    }

    /** Method used in editComputer.
     * @param discontinued date discontinued à insérer.
     * @param je javascript executor
     */
    public void editTextDiscontinued(JavascriptExecutor je, LocalDateTime discontinued) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = discontinued.format(formatter);
        je.executeScript("return document.getElementById('discontinued').value = '" + date + "';");
        Assert.assertEquals((String) je.executeScript("return document.getElementById('discontinued').value;"), date);
    }

    /** Method used in editComputer.
     * @param company la company lié à choisir.
     */
    private void editTextCompanyIdName(Company company) {

        if (company != null) {
            WebElement select = driver.findElement(By.id("companyId"));
            List<WebElement> options = select.findElements(By.tagName("option"));
            for (WebElement option : options) {
                if (option.getText().equals(company.getName())) {
                    option.click();
                    break;
                }
            }
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            Assert.assertEquals(new Long(company.getId()), Long.valueOf(driver.findElement(By.id("companyId")).getAttribute("value")));

        } else {
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            Assert.assertEquals(new Long(-1), Long.valueOf(driver.findElement(By.id("companyId")).getAttribute("value")));
        }

    }

    /**Verify click du bouton + edit fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyEditerbuttonEditOk() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=edit_form_computer&id=" + edit.getId());
        JavascriptExecutor je = (JavascriptExecutor) driver;
        this.editTextComputerName(edit.getName());
        this.editTextIntroduced(je, edit.getIntroduced());
        this.editTextDiscontinued(je, edit.getDiscontinued());
        this.editTextCompanyIdName(edit.getCompany());
        driver.findElement(By.id("buttonEdit")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("success")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result, "Create Computer " + edit.getName() + " success.");
    }

    /**Verify click du bouton + edit fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyEditerbuttonEditFailForName() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=edit_form_computer&id=" + edit.getId());
        JavascriptExecutor je = (JavascriptExecutor) driver;
        this.editTextComputerName(NAMEINVALID);
        this.editTextIntroduced(je, edit.getIntroduced());
        this.editTextDiscontinued(je, edit.getDiscontinued());
        this.editTextCompanyIdName(edit.getCompany());
        driver.findElement(By.id("buttonEdit")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("error")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result, "Le nom : " + NAMEINVALID + "est invalid car : Le nom est trop court 3 lettres minimum");
    }

    /**Verify click du bouton + edit fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyEditerbuttonEditFailForIntroduced() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=edit_form_computer&id=" + edit.getId());
        JavascriptExecutor je = (JavascriptExecutor) driver;
        this.editTextComputerName(edit.getName());
        this.editTextIntroduced(je, edit.getIntroduced().minus(Period.ofYears(100)));
        this.editTextDiscontinued(je, edit.getDiscontinued());
        this.editTextCompanyIdName(edit.getCompany());
        driver.findElement(By.id("buttonEdit")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("error")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result, "La date est invalid car : introduced date n'est pas comprise entre 1972-12-31 et 2030-01-01");
    }

    /**Verify click du bouton + edit fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyEditerbuttonEditFailForDiscontinued() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=edit_form_computer&id=" + edit.getId());
        JavascriptExecutor je = (JavascriptExecutor) driver;
        this.editTextComputerName(edit.getName());
        this.editTextIntroduced(je, edit.getIntroduced());
        this.editTextDiscontinued(je, edit.getDiscontinued().minus(Period.ofYears(100)));
        this.editTextCompanyIdName(edit.getCompany());
        driver.findElement(By.id("buttonEdit")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("error")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result, "La date est invalid car : discontinued date n'est pas comprise entre 1972-12-31 et 2030-01-01");
    }

    /**Verify click du bouton + edit fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyEditerbuttonEditFailForDiscontinuedBeforeIntroduced() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=edit_form_computer&id=" + edit.getId());
        JavascriptExecutor je = (JavascriptExecutor) driver;
        this.editTextComputerName(edit.getName());
        this.editTextIntroduced(je, edit.getDiscontinued());
        this.editTextDiscontinued(je, edit.getIntroduced());
        this.editTextCompanyIdName(edit.getCompany());
        driver.findElement(By.id("buttonEdit")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("error")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Assert.assertEquals(result, "La date est invalid car : Computer introduced is after computer discontinued: " + edit.getIntroduced().format(formatter));
    }

}