import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainFrame {
    //фрейм
    private static JFrame frame; //основной фрейм

    public static JFrame getFrame() {
        return frame;
    }//геттер

    //состаляюшие начального фрейма
    private static JTextField race_amount;//вопрос о кол-ве игроков
    private static JPanel buttons_panel;//панель с кнопками о кол-ве гонок
    private static JButton[] buttons_start;//массив кнопок на начальной фрейме
    private static JPanel contents_panel;

    //составляющие фрейма с гонкой
    private static ArrayList<RaceButton> races_threads; //массив потоков
    private static ArrayList<JButton> races_buttons; //массив в кнопками-гонками
    private static JButton run_button = new JButton();//кнопка старта
    private static JButton reset_button = new JButton(); //кнопка окончания
    private static JPanel panel; //панель с гонками

    //геттеры
    public static JPanel get_panel() {
        return panel;
    }

    public static JButton get_run_button() {
        return run_button;
    }

    //stats
    private static int raceWidth = 100;
    private static int raceHeight = 30;
    private static Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA};
    private static String[] colorsName = new String[]{"red", "blue", "green", "yellow", "magenta"};
    public static final int width = 1000;
    public static final int indent = 150; //отступ
    private static int amount_races;//количество игроков


    //запуск фрейма
    public static void start_frame() {
        //инициализация основного фрейма
        MainFrame.main_frame();

        //реализация  Runnable

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            //прключение событий к кнопкам
            public void run() {
                try {
                    MainFrame.run_listen();
                }catch (IOException ex) {
                }
                }
        });
    }


    //запуск основого фрейма и инициализация всех компонентов
    private static void main_frame() {

        WinFrame.newRace();
        frame = new JFrame("Гонка");

        //фрейм с вопросом о количестве гонок

        race_amount = new JTextField("Сколько кнопок в гонке?");
        race_amount.setFont(new Font("Arial", Font.PLAIN, 20));
        race_amount.setEditable(false);

        //панель с кнопками о кол-ве гонок

        buttons_panel = new JPanel(new GridLayout(2, 2));
        buttons_start = new JButton[4];
        //добавление кнопок на панель
        for (int i = 0; i < buttons_start.length; i++) {
            buttons_start[i] = new JButton(i + 2 + "");
            buttons_panel.add(buttons_start[i]);
        }

        races_threads = new ArrayList<>();
        races_buttons = new ArrayList<>();
        run_button = new JButton("Run");
        reset_button = new JButton("Reset");
        for (int i = 0; i < 5; i++) {
            //определяем надпись и цвет по номеру в массивах
            JButton temp_race = new JButton(colorsName[i]);
            temp_race.setBounds(10, (raceHeight + 5) * (i + 1), raceWidth, raceHeight);
            temp_race.setBackground(colors[i]);
            races_buttons.add(temp_race);
        }

        //добавленние на панель вопроса и кнопок
        contents_panel = new JPanel(new GridLayout(2, 1));
        contents_panel.add(race_amount);
        contents_panel.add(buttons_panel);

        //установление параметров фрейма

        frame.setContentPane(contents_panel);

        //устанавливается оптимальный размер
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    }

    //второй фрейм. после оределения количества игроков
    public static void race_frame(int race_Amount) {
        amount_races = race_Amount;
        //определение размеров фрейма по кол-ву игроков
        frame.setSize(width, race_Amount * raceHeight + 5 * (race_Amount - 1) + 3 * (raceHeight + 5));

        panel = new JPanel();
        panel.setLayout(null);

        //добавление гонок из массива на панель
        for (int i = 0; i < race_Amount; i++) {
            panel.add(races_buttons.get(i));
        }
        //установление параметров старта и окончания
        run_button.setBounds(870, frame.getHeight() / 4, 100, frame.getHeight() / 5);
        run_button.setVisible(true);
        panel.add(run_button);
        reset_button.setBounds(870, frame.getHeight() / 2, 100, frame.getHeight() / 5);
        reset_button.setVisible(true);
        panel.add(reset_button);


        //добавляет линию финиша
        panel.add(new Finish());

        frame.setContentPane(panel);
        frame.setLocation(200, 200);
    }

    //определение потоков
    //создание потока для каждой кнопки и добавление в общий массив потоков
    private static void threads() throws IOException {
        try {
            for (int i = 0; i < amount_races; i++) {
                RaceButton temp = new RaceButton(races_buttons.get(i));
                temp.setFinish(width - indent);
                temp.setRedrawer(new Redrawer());
                races_threads.add(temp);
                races_threads.get(i).start();
            }
        } catch (Exception e) {
            Main.logger.log(e.getMessage());
        }
    }

    //события для кнопок
    public static void run_listen()throws IOException {

        //добавление обработчика событий для кнопок с кол-вом гонок
        for (JButton button : buttons_start) {
            button.addActionListener(new ActionListener() {
                @Override
                //обработчик событий для кнопок главного фрейма
                public void actionPerformed(ActionEvent e) {
                    try {

                        //переопределение фрейма в вид гонки
                        race_frame(Integer.parseInt(button.getText()));
                    }catch (Exception ex) {}
                }

            });
        }

        //добавление обработчика событий для кнопки старта
        //если флаг ожидания true, поменять надпить на continue, иначе на stop
        run_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //меняет режим ожидания
                    RaceButton.invertWait();
                    if (RaceButton.isWait()) {
                        run_button.setText("Continue");
                    } else {
                        run_button.setText("Stop");
                    }
                    //сбрасывает настройки
                    RaceButton.dropHardReset();
                    //добавляет потоки на кнопки
                    threads();
                }catch (IOException ex) {}
            }
        });

        //добавляет обработчик событий на кнопку reset
        reset_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RaceButton.setHardReset();
                for (RaceButton race : races_threads) {
                    //меняет координаты кнопок по х yf yfxfkmyst
                    race.getButton().getBounds().x = width;
                }
                //делает основное фрейм невидимым
                frame.setVisible(false);
                //обнуляет все места
                WinFrame.resetWinPlace();
                //запускает начальный фрейм заново
                start_frame();
            }
        });

        //добавляет обработчик событий на кнопки гонок
        for (int i = 0; i < races_buttons.size(); i++) {
            int finalI = i;
            races_buttons.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //меняет флаг движение кнопки
                    races_threads.get(finalI).invertRun();
                    //увеличивает скорость
                    races_threads.get(finalI).incSpeed();
                }
            });
        }
    }
}






//рисование линии финиша
class Finish extends JComponent{

    public Finish(){
        super();
        setBounds(MainFrame.width-MainFrame.indent,0,MainFrame.width-MainFrame.indent, MainFrame.getFrame().getHeight());
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.black);
        g2.drawLine(0, 0, 0, MainFrame.getFrame().getHeight());
    }
}

//класс для изменения цвета фона с переопределенным методом интерфейса BackRedrawer
class Redrawer implements BackRedrawer{
    @Override
    public void redrawBack(Color color) {
        //меняет цвет панели
        MainFrame.get_panel().setBackground(color);
        //меняет надпись на кнопке старта на продолжить
        MainFrame.get_run_button().setText("Continue");
    }
}