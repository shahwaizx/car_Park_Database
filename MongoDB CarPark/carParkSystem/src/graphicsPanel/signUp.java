package graphicsPanel;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import Connection.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

public class signUp extends JFrame implements ActionListener {
    private JLabel nameLabel, phoneNoLabel, cnicLabel, usernameLabel, passwordLabel,phoneNoInstruct,cnicInstruct;
    private JTextField nameTextfield, phoneNoTextfield, cnicTextfield, usernameTextField;
    private JPasswordField passwordTextField;
    private JButton regButton;
    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public signUp() {
        super("SIGN UP PAGE");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(100, 80, 70, 20);
        nameTextfield = new JTextField();
        nameTextfield.setBounds(180, 80, 100, 20);
        add(nameLabel);
        add(nameTextfield);

        phoneNoLabel = new JLabel("Phone No. :");
        phoneNoLabel.setBounds(100, 140, 70, 20);
        phoneNoInstruct = new JLabel("(03*********)");
        phoneNoInstruct.setBounds(180,160,100,20);
        phoneNoTextfield = new JTextField();
        phoneNoTextfield.setBounds(180, 140, 100, 20);
        add(phoneNoLabel);
        add(phoneNoInstruct);
        add(phoneNoTextfield);

        cnicLabel = new JLabel("CNIC:");
        cnicLabel.setBounds(100, 200, 70, 20);
        cnicInstruct = new JLabel("(Without Dashes)");
        cnicInstruct.setBounds(180,220,100,20);
        cnicTextfield = new JTextField();
        cnicTextfield.setBounds(180, 200, 100, 20);
        add(cnicLabel);
        add(cnicInstruct);
        add(cnicTextfield);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(100, 260, 70, 20);
        usernameTextField = new JTextField();
        usernameTextField.setBounds(180, 260, 100, 20);
        add(usernameLabel);
        add(usernameTextField);


        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(100, 320, 70, 20);
        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(180, 320, 100, 20);
        add(passwordLabel);
        add(passwordTextField);

        regButton = new JButton("Register");
        regButton.setBounds(150, 390, 100, 30);
        regButton.setFocusable(false);
        regButton.addActionListener(this);
        add(regButton);


        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == regButton) {

            char[] pass = passwordTextField.getPassword();
            String password = String.valueOf(pass);
            if (nameTextfield.getText().isEmpty() || phoneNoTextfield.getText().isEmpty() || cnicTextfield.getText().isEmpty() || usernameTextField.getText().isEmpty() || password.isEmpty()) {
                String soundPath = "error.wav";
                try {
                    File soundFile = new File(soundPath);
                    AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ai);
                    clip.start();
                }
                catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex){
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(new JFrame(), "Empty Field Entry");

            } else {
                if (phoneNoTextfield.getText().length() != 11) {
                    String soundPath = "error.wav";
                    try {
                        File soundFile = new File(soundPath);
                        AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(ai);
                        clip.start();
                    }
                    catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex){
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(new JFrame(), "Invalid PhoneNo");

                }
                if (!isValidPhoneNumber(phoneNoTextfield.getText())){
                    String soundPath = "error.wav";
                    try {
                        File soundFile = new File(soundPath);
                        AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(ai);
                        clip.start();
                    }
                    catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex){
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(new JFrame(),"Only Use Numbers for PhoneNo");
                }
                if (cnicTextfield.getText().length() != 13) {
                    String soundPath = "error.wav";
                    try {
                        File soundFile = new File(soundPath);
                        AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(ai);
                        clip.start();
                    }
                    catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex){
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(new JFrame(), "Invalid CNIC");
                }
                else {
                    MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
                    MongoCollection<Document> collection = mongoDatabase.getCollection("customer");
                    String usernameTocheck = usernameTextField.getText();
                    Document existingUser = collection.find(Filters.eq("username",usernameTocheck)).first();
                    if (existingUser != null) {
                        // Username is already taken, handle accordingly (e.g., display an error message)
                        String soundPath = "error.wav";
                        try {
                            File soundFile = new File(soundPath);
                            AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                            Clip clip = AudioSystem.getClip();
                            clip.open(ai);
                            clip.start();
                        }
                        catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex){
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(new JFrame(),"USERNAME IS ALREADY TAKEN");
                    }
                    else {
                        Document lastcustomerID = collection.find()
                                .sort(Sorts.descending("customer_id"))
                                .limit(1)
                                .first();
                        int lastCustomerId = lastcustomerID.getInteger("customer_id");
                        System.out.println("last id: " + lastCustomerId);

                        int newCustomerId = lastCustomerId + 1;
                        System.out.println("new id: " + newCustomerId);
                        Document customerDocument = new Document("customer_id",newCustomerId)
                                .append("name", nameTextfield.getText())
                                .append("cnic", cnicTextfield.getText())
                                .append("phone_no", phoneNoTextfield.getText())
                                .append("username", usernameTextField.getText())
                                .append("password", password);
                        collection.insertOne(customerDocument);
                    JOptionPane.showMessageDialog(new JFrame(), "ACCOUNT REGISTERED SUCCESSFULLY");
                    dispose();
                }
                }

            }
        }
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
