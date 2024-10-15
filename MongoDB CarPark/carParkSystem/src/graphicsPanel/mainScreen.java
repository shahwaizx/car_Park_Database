package graphicsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainScreen extends JFrame implements ActionListener {
    private JLabel label;
    private Panel buttonPanel;
    private JLabel car;
    private JButton adminButton,userButton,exitButton,showOptions;
    private ImageIcon CarParkLogo = new ImageIcon("carPark.jpg");
    private ImageIcon gif = new ImageIcon("Spinning car.gif");
    private ImageIcon threeDots = new ImageIcon("threeDots.png");
    private Font myFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font myFont1 = new Font("Segoe UI", Font.PLAIN, 18);

    public mainScreen() {
        super("CAR PARKING SYSTEM");
        setSize(800, 600);
        setLocationRelativeTo(null);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        showOptions = new JButton(threeDots);
        showOptions.setFont(myFont);
        showOptions.setFocusable(false);
        showOptions.setBounds(650, 28, 64, 64);
        showOptions.addActionListener(this);
        showOptions.setOpaque(false);
        showOptions.setContentAreaFilled(false);
        showOptions.setBorderPainted(false);
        showOptions.setFocusPainted(false);
        add(showOptions);

        car = new JLabel(gif);
        car.setBounds(75, 15, 128, 128);

        label = new JLabel(CarParkLogo);
        label.setFont(myFont);
        label.add(car);
        add(label, BorderLayout.CENTER);

        adminButton = new JButton("ADMIN");
        adminButton.setFont(myFont);
        adminButton.setFocusable(false);
        adminButton.addActionListener(this);

        userButton = new JButton("USER");
        userButton.setFont(myFont);
        userButton.setFocusable(false);
        userButton.addActionListener(this);

        exitButton = new JButton("EXIT CAR");
        exitButton.setFont(myFont);
        exitButton.setFocusable(false);
        exitButton.addActionListener(this);


        buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);
        add(label);
        setVisible(true);
        buttonPanel.setVisible(false);

    }


    public static void main(String[] args) {

        new mainScreen();
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showOptions) {
            buttonPanel.setVisible(true);
            showOptions.setVisible(false);
        } else if (e.getSource() == adminButton) {
            new adminPanel();

        } else if (e.getSource() == userButton) {
            new userPanel();

        } else if (e.getSource() == exitButton) {
            new exitSignIn();
        }

    }
}
