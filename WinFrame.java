import javax.swing.*;
import java.awt.*;

//фрейм для соообщения о победителе
public class WinFrame extends JFrame {
    private static int winPlace = 1; //место
    private static int race_num = 0; //номер гонки

    //обнуляет все результаты
    public static void resetWinPlace(){ winPlace = 1;}

    //изменяет номер гонки
    public static void newRace(){ race_num++;}

    //конструктор. Принимает как параметр кнопку победителя
    public WinFrame(RaceButton winner){

        //устанавливает фон фрейма с сообщением по цвету победителя
        super(race_num+"."+winPlace+"");
        setBackground(winner.getButton().getBackground());

        //надпись о победе
        JTextField msg = new JTextField(winner.getButton().getText() + " занял " + winPlace + " место");
        msg.setEditable(false);

        winPlace++;

        //устанавливает все параметры фрейма и добавляет элементы
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(msg);
        panel.setBackground(winner.getButton().getBackground());
        setContentPane(panel);

        pack();
        setResizable(false);
        //смещение для расположения нескольких сообщение о победителях
        setLocation(200*winPlace, 100);
        setVisible(true);
    }
}
