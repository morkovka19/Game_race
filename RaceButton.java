import javax.swing.*;
import java.awt.*;
import java.util.Random;

//класс кнопки, наследник Thread
public class RaceButton extends Thread implements BackRedrawer{


    public static boolean hardReset = false;
    private static boolean wait = true; //флаг ожидания
    private static double speed_с = 0.1; //коэф изменения скорости
    private int finish; //параметры координат
    private BackRedrawer redrawer;//элемент, для изменения фона

    private int speed; //скорость гонки
    public boolean isRun; //флаг движения
    private JButton button; //кнопка гонки


    //геттер кнопки
    public JButton getButton() { return button; }

    //сеттер, возвращает координаты финиша по ОХ
    public void setFinish(int finish) { this.finish = finish; }

    //установить перерисовщик для шаблона делегата в запуске метода
    public void setRedrawer(BackRedrawer redrawer) { this.redrawer = redrawer; }

    //увеличивает скорость
    public void incSpeed() { speed++; }

    //получатель флага ожидания
    public static boolean isWait() { return wait; }

    //меняет флаг ожидания
    public static void invertWait(){ wait = !wait;}

    //меняет флаг движения
    public void invertRun(){ isRun = !isRun;}

    public static void setHardReset() {hardReset = true;}

    //сбросить настройки
    public static void dropHardReset() {hardReset = false;}


    //конструктор. Принимает кнопку гонки
    public RaceButton(JButton button){
        super();
        this.button = button;
        //рандомно определяет скорость
        speed = (new Random()).nextInt(3)+3;
        //устанавливает флаг движения
        isRun = true;
    }

    //делает шаг. если коэф > 1 приравнивает его к 0.1, иначе увеличивает на 0.1
    private void step() {
        speed_с = (speed_с > 1) ? 0.1 : speed_с + 0.1;
        button.setBounds(button.getBounds().x + speed * (int) Math.round(speed_с), button.getBounds().y, button.getBounds().width, button.getBounds().height);
    }

    //запуск потока. Перегруженный метод от потока
    @Override
    public void run() {
        try {
            //если координаты меньше финиша и настройки false. делает шаг
            while(button.getBounds().x + button.getWidth() < finish && !hardReset){
                if(!wait && isRun){
                    step();
                }
                //засывает на 50
                sleep(50);
            }
            //если настройки на false, скрывет кнопку, меняет цвет фона и выводит сообщение о победителе
            if(!hardReset) {
                button.setVisible(false);
                new WinFrame(this);
                redrawBack(button.getBackground());

            }wait = true;
        } catch (InterruptedException e) {
            return;
        }
    }

    //перегруженный метод от интерфейса
    @Override
    public void redrawBack(Color color) {
        redrawer.redrawBack(color);
    }
}
