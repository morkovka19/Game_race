
import lab_7.Controller.Preloader;
import lab_7.Logger.BaseLogger;
import lab_7.Logger.Logger;

import javax.swing.*;

import java.io.IOException;
import java.util.Properties;

import static java.lang.Thread.sleep;


public class Main {
    static public BaseLogger logger;

    public static void main(String[] args) throws IOException {
        logger = new Logger("logs.txt");
        try {
            //работа с конфигурацией

            Properties prop = new Properties();

            Preloader PRL = new Preloader("settings.properties", prop);

            System.out.println("Добро пожаловать, " + prop.getProperty("LOGIN"));
            System.out.println("Group " + prop.getProperty("GROUP"));
            System.out.println("LR " + prop.getProperty("NUMBER_LR"));
            boolean is_logger_needed = true;


            //внешний вид по умолчанию

            JFrame.setDefaultLookAndFeelDecorated(true);
            logger.log("Запуск приложения");

            //запуск фрейма

            MainFrame.start_frame();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.log(ex.getMessage());
        }
    }
}