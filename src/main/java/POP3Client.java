import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.mail.*;

public class POP3Client extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextArea emailArea;
    private Store store;
    public POP3Client() {
        setTitle("POP3 Client");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailArea = new JTextArea();
        emailArea.setEditable(false);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());

        JButton listButton = new JButton("LIST");
        listButton.addActionListener(new ListAction());

        JButton retrButton = new JButton("RETR");
        retrButton.addActionListener(new RetrAction());

        JButton quitButton = new JButton("QUIT");
        quitButton.addActionListener(new QuitAction());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(listButton);
        panel.add(retrButton);
        panel.add(quitButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(emailArea), BorderLayout.CENTER);
    }

    private class LoginAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String host = "pop.gmail.com";
                String user = emailField.getText();
                String password = new String(passwordField.getPassword());
                Properties properties = new Properties();
                properties.put("mail.pop3.host", host);
                properties.put("mail.pop3.port", "995");
                properties.put("mail.pop3.ssl.enable", "true");
                Session session = Session.getInstance(properties);
                store = session.getStore("pop3s");
                store.connect(host, user, password);
                emailArea.append("Login successful!\n");
            } catch (Exception ex) {
                emailArea.append("Login failed: " + ex.getMessage() + "\n");
            }
        }
    }

    private class ListAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                Message[] messages = folder.getMessages();
                java.util.List<Message> messageList = Arrays.asList(messages);
                Collections.reverse(messageList);
                emailArea.append("Number of messages: " + messageList.size() + "\n");
                for (int i = 0; i < messageList.size(); i++) {
                    emailArea.append("Email " + (i + 1) + ": " + messageList.get(i).getSubject() + "\n");
                }
                folder.close(false);
            } catch (Exception ex) {
                emailArea.append("Error listing messages: " + ex.getMessage() + "\n");
            }
        }
    }

    private class RetrAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);

                Message[] messages = folder.getMessages();
                if (messages.length > 0) {
                    // Lấy thư mới nhất (thư cuối cùng trong danh sách)
                    Message latestMessage = messages[messages.length - 1];
                    Object content = latestMessage.getContent();

                    if (content instanceof String) {
                        emailArea.append("Content of the latest email: " + content + "\n");
                    } else {
                        emailArea.append("Content of the latest email is not plain text.\n");
                    }
                } else {
                    emailArea.append("No emails found.\n");
                }

                folder.close(false);
            } catch (Exception ex) {
                emailArea.append("Error retrieving message: " + ex.getMessage() + "\n");
            }
        }
    }

    private class QuitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (store != null) {
                    store.close();
                    emailArea.append("Disconnected from server.\n");
                }
            } catch (Exception ex) {
                emailArea.append("Error disconnecting: " + ex.getMessage() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            POP3Client client = new POP3Client();
            client.setVisible(true);
        });
    }
}
