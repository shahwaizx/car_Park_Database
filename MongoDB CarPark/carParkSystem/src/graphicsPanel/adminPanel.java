package graphicsPanel;
import Connection.connection_database;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


import com.mongodb.client.*;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;



public class adminPanel extends JFrame implements ActionListener {
    private JPanel panel;
    private JLabel usernameLabel;
    private JTextField usernameTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordTextField;
    private JButton loginButton;
    private Font myFont = new Font("Segoe UI", Font.PLAIN, 14);
    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public adminPanel() {
        super("ADMIN LOGIN WINDOW");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        usernameLabel = new JLabel("USERNAME:");
        usernameTextField = new JTextField();
        usernameLabel.setBounds(100, 80, 80, 20);
        usernameTextField.setBounds(180, 80, 100, 20);

        passwordLabel = new JLabel("PASSWORD:");
        passwordTextField = new JPasswordField();
        passwordLabel.setBounds(100, 120, 100, 20);
        passwordTextField.setBounds(180, 120, 100, 20);

        loginButton = new JButton("LOGIN");
        loginButton.setBounds(140, 240, 100, 20);
        loginButton.addActionListener(this);

        add(usernameLabel);
        add(usernameTextField);
        add(passwordLabel);
        add(passwordTextField);
        add(loginButton);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = usernameTextField.getText();
        char[] pass = passwordTextField.getPassword();
        String password = String.valueOf(pass);
        if (str.isEmpty() || password.isEmpty()) {
            String soundPath = "error.wav";
            try {
                File soundFile = new File(soundPath);
                AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(ai);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(new JFrame(), "Empty Field Entry");
        } else {
                MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
                MongoCollection<Document> collection = mongoDatabase.getCollection("admin");
                Document adminDocument = collection.find(Filters.eq("username",str)).first();


                if (adminDocument != null & Objects.equals(adminDocument.get("password"),password)) {
                    new adminFunctions();
                    dispose();
                } else {
                    String soundPath = "error.wav";
                    try {
                        File soundFile = new File(soundPath);
                        AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(ai);
                        clip.start();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(new JFrame(), "WRONG CREDENTIALS");
                }

        }
    }
}
