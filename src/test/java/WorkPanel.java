import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;
import java.util.List;

public class WorkPanel extends JPanel {

    public static final int LENGTH_FORM1 = 12;
    public static final int LENGTH_FORM2 = 10;


    public static final String FORMAT1 = "972";
    public static final String FORMAT2 = "05";

    private ImageIcon background;
    private JTextField phoneNumber;
    private JTextField text;
    private JLabel phoneNumerText, textLabe;

    public WorkPanel(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
        this.setDoubleBuffered(true);
        this.setLayout(null);
        this.setRequestFocusEnabled(true);
        this.background = new ImageIcon("whatsapp2.jpeg");
        JButton start = new JButton();
        phoneNumber = new JTextField();
        text = new JTextField();

        Color color = new Color(200, 200, 200);
        start.setBackground(color);
        start.setVisible(true);
        this.add(start);
        start.setBounds(320, 240, 100, 50);
        phoneNumber.setBounds(400, 150, 150, 50);
        phoneNumber.setVisible(true);
        this.add(phoneNumber);
        text.setBounds(200, 150, 150, 50);
        text.setVisible(true);
        this.add(text);


        start.addActionListener((event) -> {
            if (phoneNumber.getText().isEmpty() || text.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "One or more text box is empty",
                        "",
                        JOptionPane.ERROR_MESSAGE);
            } else if (!validPhoneForm(phoneNumber.getText())) {
                JOptionPane.showMessageDialog(null,
                        "wrong number",
                        "phone value missing",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    actionPerformed(event);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        });


    }

    private void actionPerformed(ActionEvent event) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ADMIN\\Downloads\\chromedriver_win32 (1)\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("C:\\Users\\ADMIN\\AppData\\Local\\Temp\\scoped_dir11132_1172441705\\Default");
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://web.whatsapp.com/");
        driver.manage().window().maximize();
        boolean isConnected = false;
        while (!isConnected) {
            List<WebElement> elementAfterConnection = driver.findElements(By.id("side"));
            if (!elementAfterConnection.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "you are in",
                        "connection succses",
                        JOptionPane.INFORMATION_MESSAGE);
                isConnected = true;
            }
        }
        afterConnection(driver);
    }

    public void copyMessage(ChromeDriver driver) {
        boolean inChat = false;
        while (!inChat) {
            List<WebElement> newWebElement = driver.findElements
                    (By.xpath
                            ("/html/body/div[1]/div/div/div[4]/div/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div[2]"));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
            if (!newWebElement.isEmpty()) {
                newWebElement.get(0).sendKeys(text.getText());
                inChat = true;
            }
        }
    }

    public void sendMessage(ChromeDriver driver){
        boolean elementExists = false;
        while (!elementExists){
            List<WebElement> webElements = driver.findElements
                    (By.xpath("/html/body/div[1]/div/div/div[4]/div/footer/div[1]/div/span[2]/div/div[2]/div[2]/button"));
            if (!webElements.isEmpty()){
                webElements.get(0).click();
                elementExists = true;
            }
        }
    }

    public boolean msgCheck (ChromeDriver driver){
        boolean msgCheck = false;
        boolean msgWasRead = false;
            while (!msgCheck){
                List<WebElement> messageCheck = driver.findElements(By.className("_2wUmf _21bY5 message-out focusable-list-item"));
                if (!messageCheck.isEmpty()){
                    msgCheck = true;
                    msgWasRead = true;

                }
            }
        return msgWasRead;
    }

    public void afterConnection(ChromeDriver driver) {
        if (this.phoneNumber.getText().substring(0, 2).equals(FORMAT2)) {
            driver.get("https://web.whatsapp.com/send?phone=972" +
                    this.phoneNumber.getText().substring(1, this.phoneNumber.getText().length()));
            copyMessage(driver);
            sendMessage(driver);
            System.out.println(msgCheck(driver));
        }
        else {
            driver.get("https://web.whatsapp.com/send?phone=" + this.phoneNumber.getText());
            copyMessage(driver);
            sendMessage(driver);
            System.out.println(msgCheck(driver));
        }

    }


    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.background.paintIcon(this, graphics, 0, 0);
    }

    public boolean validPhoneForm(String phoneNum) {
        boolean validPhoneNum = false;

        try {
            if (phoneNum.substring(0, 3).equals(FORMAT1) && phoneNum.length() == 12) {
                validPhoneNum = true;
            } else if (phoneNum.substring(0, 2).equals(FORMAT2) &&
                    (phoneNum.length() == LENGTH_FORM2)) {
                validPhoneNum = true;
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("error");
        }
        return validPhoneNum;
    }
}

//