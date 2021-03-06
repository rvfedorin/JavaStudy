package gui;

import actions.GetStatus;
import actions.StartSession;
import actions.StopSession;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import tools.Config;

import static tools.Config.ISG_1;
import static tools.Config.ISG_2;
import static tools.CryptDecrypt.getEncrypt;
import tools.ExtendedTextField;
import tools.InLoad;

public class MainFrame extends JFrame {

    private HashMap<String, JTextField> labelsWithInputs;
    private char[] key;
    private MainMenu menu;
    public ResultWindow resultFrame;

    public MainFrame() {
        super();
        Dimension DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
        Double X_SCREEN_SIZE = DIMENSION.getWidth();
        Double Y_SCREEN_SIZE = DIMENSION.getHeight();
        int x = (int) (X_SCREEN_SIZE - getWidth()) / 3;
        int y = (int) (Y_SCREEN_SIZE - getHeight()) / 3;
        setLocation(x, y);

    } // ** constructor

    public void createGUI() {
        this.setTitle("Homes customers");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        labelsWithInputs = new HashMap<>();
        String[] labels = new String[]{"Mnemokod: ", "IP: ", "Pass: "};
        String[] ipIsg = null;
        try {
            ipIsg = new String[]{getEncrypt(new String(key), ISG_1), getEncrypt(new String(key), ISG_2)};
        } catch (NullPointerException ex) {
            System.exit(0);
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel panelLabels = new JPanel(new GridLayout(labels.length, 1, 1, 8));
        JPanel panelInputs = new JPanel(new GridLayout(labels.length, 1, 1, 1));
        inputPanel.add(panelLabels);
        inputPanel.add(panelInputs);

        for (String name : labels) {
            JTextField input = new ExtendedTextField(18);
            labelsWithInputs.put(name, input);

            panelLabels.add(new JLabel(name + "          "));
            panelInputs.add(input);
        }

        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createTitledBorder("ISG ")
        ));
        ButtonGroup buttonGroup = new ButtonGroup();
        boolean selected = true;
        for (String name : ipIsg) {
            JRadioButton tempButton = new JRadioButton(name, selected);
            tempButton.setBorder(BorderFactory.createEmptyBorder());
            tempButton.setActionCommand(name);

            buttonGroup.add(tempButton);
            radioButtonPanel.add(tempButton);
            selected = false;
        } // for

        JButton getStatusButton = new JButton("Узнать статус");
        getStatusButton.addActionListener(e -> {
            String mnemokod = getData("Mnemokod: ").getText();
            String ipClient = getData("IP: ").getText();
            String passClient = getData("Pass: ").getText();
            String ipISG = buttonGroup.getSelection().getActionCommand();

            if (mnemokod != null && ipClient != null && passClient != null && ipISG != null) {
                new Thread(
                        new GetStatus(mnemokod,
                                ipClient,
                                passClient,
                                ipISG,
                                key,
                                this)).start();
            } else {
                System.out.println("Error getStatusButton.addActionListener");
            }
        });

        JButton runSessionButton = new JButton("Запустить сессию"); //StartSession
        runSessionButton.addActionListener(e -> {
            String mnemokod = getData("Mnemokod: ").getText().trim();
            String ipClient = getData("IP: ").getText().trim();
            String passClient = getData("Pass: ").getText().trim();

            new Thread(new StartSession(mnemokod, ipClient, passClient, key)).start();
        });

        JButton detailSessionButton = new JButton("Параметры сессии"); //GetInfoSession
        detailSessionButton.addActionListener(e -> {
            String mnemokod = getData("Mnemokod: ").getText();
            String ipClient = getData("IP: ").getText();
            String passClient = getData("Pass: ").getText();
            String ipISG = buttonGroup.getSelection().getActionCommand();

            if (mnemokod != null && ipClient != null && passClient != null && ipISG != null) {
                new Thread(new GetStatus(mnemokod, ipClient, passClient, ipISG, key, this).detailed(true)).start();
            } else {
                System.out.println("Error detailSessionButton.addActionListener");
            }
        });

        JButton stopSessionButton = new JButton("Остановить сессию"); //StopSession
        stopSessionButton.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        stopSessionButton.addActionListener(e -> {
            String mnemokod = getData("Mnemokod: ").getText();
            String ipISG = buttonGroup.getSelection().getActionCommand();

            if (mnemokod != null && mnemokod.length() > 3 && ipISG != null) {
                new Thread(new StopSession(mnemokod, ipISG, key, this)).start();
            } else {
                System.out.println("[Error] stopSessionButton.addActionListener");
                System.out.println("[Error] mnemokod != null && mnemokod.length() > 3 && ipISG != null");
            }
        });

        JButton clearTextFieldsButton = new JButton("Очистить поля");
        clearTextFieldsButton.addActionListener(e -> {
            for (String label : labels) {
                labelsWithInputs.get(label).setText("");
            }
        });
        JPanel clearTextFieldsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clearTextFieldsPanel.add(clearTextFieldsButton);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 0, 4));
        buttonsPanel.add(getStatusButton);
        buttonsPanel.add(runSessionButton);
        buttonsPanel.add(detailSessionButton);
        buttonsPanel.add(stopSessionButton);

        mainPanel.add(inputPanel);
        mainPanel.add(clearTextFieldsPanel);
        mainPanel.add(radioButtonPanel);
        mainPanel.add(buttonsPanel);

        menu = new MainMenu(this);
        add(ProgressBar.progressBar, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame = new ResultWindow(this);

        InLoad in = new InLoad();
        try {
            Class<?> cl = in.loadClass(in.getN());
            Field f = cl.getField("TO_CLOSE");
            Config.TO_CLOSE = (String) f.get(null);
        } catch (Throwable t) {
            String error = "[ERROR] Can't load TO_CLOSE " + t;
            System.out.println(error);
            resultFrame.showResult(error);
        };
    }

    public void setKey(final char[] key) {
        if (key.length < 4) {
            System.exit(0);
        }
        this.key = key;
    }

    public JTextField getData(String name) {
        return labelsWithInputs.getOrDefault(name, null);
    }

} // ** class MainFrame
