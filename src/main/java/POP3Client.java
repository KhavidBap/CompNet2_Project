// Library import
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;

public class POP3Client extends JFrame {
    // Fields for email, password and email-viewing area
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextArea emailArea;

    private Store store;

    public POP3Client() {
        setTitle("POP3 Client");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        // GUI components initialization
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailArea = new JTextArea();
        emailArea.setEditable(false);
        emailArea.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        JButton quitButton = new JButton("Logout");
        quitButton.addActionListener(new QuitAction());
        JButton statsButton = new JButton("Statistics");
        statsButton.addActionListener(new StatsAction());
        JButton listButton = new JButton("List all emails");
        listButton.addActionListener(new ListAction());
        JButton listByDateButton = new JButton("List emails by date");
        listByDateButton.addActionListener(new ListByDateAction());
        JButton retrButton = new JButton("Read latest email content");
        retrButton.addActionListener(new RetrAction());
        JButton readByIdButton = new JButton("Read email content by ID");
        readByIdButton.addActionListener(new ReadByIdAction());
        JButton deleteByIdButton = new JButton("Delete email by ID");
        deleteByIdButton.addActionListener(new DeleteByIdAction());
        JButton saveByIdButton = new JButton("Save email by ID");
        saveByIdButton.addActionListener(new SaveByIdAction());


        // Fixing the height size of the components
        int fixedHeight = 30;
        Dimension emailFieldSize = new Dimension(emailField.getPreferredSize().width, fixedHeight);
        Dimension passwordFieldSize = new Dimension(passwordField.getPreferredSize().width, fixedHeight);
        Dimension buttonSize = new Dimension(120, fixedHeight);

        emailField.setPreferredSize(emailFieldSize);
        passwordField.setPreferredSize(passwordFieldSize);
        loginButton.setPreferredSize(buttonSize);
        quitButton.setPreferredSize(buttonSize);
        listButton.setPreferredSize(buttonSize);
        listByDateButton.setPreferredSize(buttonSize);
        retrButton.setPreferredSize(buttonSize);
        statsButton.setPreferredSize(buttonSize);
        readByIdButton.setPreferredSize(buttonSize);
        deleteByIdButton.setPreferredSize(buttonSize);
        saveByIdButton.setPreferredSize(buttonSize);


        // Layout setup
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        leftPanel.add(emailField, gbc);
        gbc.gridx = 2;
        leftPanel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        leftPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        leftPanel.add(passwordField, gbc);
        gbc.gridx = 2;
        leftPanel.add(quitButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(statsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        leftPanel.add(listButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        leftPanel.add(listByDateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        leftPanel.add(retrButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        leftPanel.add(readByIdButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        leftPanel.add(deleteByIdButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        leftPanel.add(saveByIdButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        leftPanel.add(Box.createVerticalGlue(), gbc);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(emailArea), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500);
        add(splitPane, BorderLayout.CENTER);
    }

    // Login to the POP3 server
    private class LoginAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // POP3 server details
                String host = "pop.yandex.com";
                String user = emailField.getText();
                String password = new String(passwordField.getPassword());
                Properties properties = new Properties();
                properties.put("mail.pop3.host", host);
                properties.put("mail.pop3.port", "995");
                properties.put("mail.pop3.socketFactory", 995);
                properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                properties.put("mail.pop3.ssl.enable", "true");
                Session session = Session.getInstance(properties);
                store = session.getStore("pop3s");

                // Connect to the POP3 server
                store.connect(host, user, password);
                emailArea.setText("");
                emailArea.append("Login successful!\n");

                // Error handling
            } catch (Exception ex) {
                emailArea.setText("");
                emailArea.append("Login failed: " + ex.getMessage() + "\n");
            }
        }
    }

    // Statistics mailbox
    private class StatsAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // Access the INBOX folder
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                int totalMessages = folder.getMessageCount();
                Message[] messages = folder.getMessages();

                // Get mail dates and sizes
                Date oldest = null;
                Date newest = null;
                int totalSize = 0;
                for (Message msg : messages) {
                    Date sentDate = msg.getSentDate();
                    if (sentDate != null) {
                        if (oldest == null || sentDate.before(oldest)) {
                            oldest = sentDate;
                        }
                        if (newest == null || sentDate.after(newest)) {
                            newest = sentDate;
                        }
                    }
                    String content = getTextFromMessage(msg);
                    totalSize += content.getBytes().length;
                }
                double totalSizeInKB = Math.round(totalSize * 1.0) / 100.0;

                // Display statistics
                emailArea.setText("");
                emailArea.append("Mailbox Statistics \n\n");
                emailArea.append("Total Messages: " + totalMessages + "\n");
                emailArea.append("Total Size (approx): " + totalSizeInKB + "kB\n\n");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                if (oldest != null) {
                    emailArea.append("Oldest message:  " + sdf.format(oldest) + "\n");
                }
                if (newest != null) {
                    emailArea.append("Newest message: " + sdf.format(newest) + "\n");
                }
                folder.close(false);

                // Error handling
            } catch (Exception ex) {
                emailArea.setText("");
                emailArea.append("Error retrieving stats: " + ex.getMessage() + "\n");
            }
        }
    }

    // List all emails in the inbox
    private class ListAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);

                // Get all messages in INBOX folder
                Message[] messages = folder.getMessages();
                java.util.List<Message> messageList = Arrays.asList(messages);
                Collections.reverse(messageList);
                emailArea.setText("");
                emailArea.append("Number of messages: " + messageList.size() + "\n");
                emailArea.append("\n");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                for (int i = 0; i < messageList.size(); i++) {
                    Date sentDate = messageList.get(i).getSentDate();
                    emailArea.append("Email " + (i + 1) + " (" + sdf.format(sentDate) + "): " + messageList.get(i).getSubject() + "\n");
                }
                folder.close(false);

                // Error handling
            } catch (Exception ex) {
                emailArea.setText("");
                emailArea.append("Error listing messages: " + ex.getMessage() + "\n");
            }
        }
    }

    // Dialog for date input
    class DateInputDialog extends JDialog {
        // Fields for day, month, year input
        private JTextField dayField = new JTextField(2);
        private JTextField monthField = new JTextField(2);
        private JTextField yearField = new JTextField(4);
        private boolean confirmed = false;

        public DateInputDialog(JFrame parent) {
            super(parent, "Enter Date", true);
            JPanel content = new JPanel(new GridLayout(4, 2, 5, 5));
            content.setBorder(new EmptyBorder(15, 20, 15, 20)); // top, left, bottom, right

            content.add(new JLabel("Day:"));
            content.add(dayField);
            content.add(new JLabel("Month:"));
            content.add(monthField);
            content.add(new JLabel("Year:"));
            content.add(yearField);
            content.add(new JLabel(""));
            JButton ok = new JButton("OK");
            ok.addActionListener(e -> { confirmed = true; setVisible(false); });
            content.add(ok);

            setContentPane(content);
            setPreferredSize(new Dimension(300, 180));
            setResizable(false);
            pack();
            setLocationRelativeTo(parent);
        }

        // Get input values
        public boolean isConfirmed() { return confirmed; }
        public int getDay() { return Integer.parseInt(dayField.getText().trim()); }
        public int getMonth() { return Integer.parseInt(monthField.getText().trim()); }
        public int getYear() { return Integer.parseInt(yearField.getText().trim()); }
    }

    // List emails by date
    private class ListByDateAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Display input dialog
            DateInputDialog dialog = new DateInputDialog(POP3Client.this);
            dialog.setVisible(true);

            if (!dialog.isConfirmed()) return;
            try {
                // Get input values
                int day = dialog.getDay();
                int month = dialog.getMonth();
                int year = dialog.getYear();

                // Validate date
                Calendar inputCal = Calendar.getInstance();
                inputCal.set(year, month - 1, day, 0, 0, 0);
                inputCal.set(Calendar.MILLISECOND, 0);

                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                Message[] messages = folder.getMessages();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                emailArea.setText("");
                emailArea.append("Emails on " + String.format("%02d/%02d/%04d", day, month, year) + ":\n\n");

                // Check if the date is valid
                int count = 0;
                for (int i = messages.length - 1; i >= 0; i--) {
                    Date sentDate = messages[i].getSentDate();
                    if (sentDate != null) {
                        Calendar msg = Calendar.getInstance();
                        msg.setTime(sentDate);
                        if (msg.get(Calendar.YEAR) == year && msg.get(Calendar.MONTH) == month - 1 && msg.get(Calendar.DAY_OF_MONTH) == day) {
                            emailArea.append("Email " + (messages.length - i) + " (" + sdf.format(sentDate) + "): " + messages[i].getSubject() + "\n");
                            count++;
                        }
                    }
                }

                // If no emails found on the date
                if (count == 0) {
                    emailArea.append("No emails found for this date.\n");
                }
                folder.close(false);

                // Error handling
            } catch (Exception ex) {
                emailArea.setText("Error: " + ex.getMessage() + "\n");
            }
        }
    }

    // Extract text content from a mail message
    private String getTextFromMessage(Message message) throws Exception {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    return (String) part.getContent();
                }
            }
        }
        return "";
    }

    // Read the latest email
    private class RetrAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                Message[] messages = folder.getMessages();

                // Display the latest email content
                int numMessages = messages.length;
                if (numMessages > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    emailArea.setText("");
                    Message latestMessage = messages[numMessages - 1];
                    Date sentDate = latestMessage.getSentDate();
                    emailArea.append("Content of the latest email: \n\n");
                    emailArea.append("Subject: " + latestMessage.getSubject() + "\n");
                    emailArea.append("Date: " + sdf.format(sentDate) + "\n");
                    emailArea.append("From: " +((InternetAddress) latestMessage.getFrom()[0]).getAddress() + "\n");
                    emailArea.append("To: " + ((InternetAddress) latestMessage.getRecipients(Message.RecipientType.TO)[0]).getAddress() + "\n\n");
                    emailArea.append("Content: \n");
                    emailArea.append(getTextFromMessage(latestMessage) + "\n");
                } else {
                    emailArea.setText("");
                    emailArea.append("No emails found.\n");
                }
                folder.close(false);

                // Error handling
            } catch (Exception ex) {
                emailArea.setText("");
                emailArea.append("Error retrieving message: " + ex.getMessage() + "\n");
            }
        }
    }

    // Read the specific email ID
    private class ReadByIdAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Display input dialog to get email ID
            String input = JOptionPane.showInputDialog(
                    POP3Client.this,
                    "Enter email ID (1 = newest, N = oldest):",
                    "Read Email by ID",
                    JOptionPane.PLAIN_MESSAGE
            );

            // Input is null
            if (input == null) return;
            try {
                int id = Integer.parseInt(input.trim());
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                Message[] messages = folder.getMessages();

                // If the ID is invalid
                if (id < 1 || id > messages.length) {
                    emailArea.setText("");
                    emailArea.setText("Invalid email ID.\n");

                // If the ID is valid
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Message msg = messages[(messages.length - (id - 1)) - 1];
                    Date sentDate = msg.getSentDate();
                    emailArea.setText("");
                    emailArea.append("Content of email " + id + ":\n\n");
                    emailArea.append("Subject: " + msg.getSubject() + "\n");
                    emailArea.append("Date: " + sdf.format(sentDate) + "\n");
                    emailArea.append("From: " + ((InternetAddress) msg.getFrom()[0]).getAddress() + "\n");
                    emailArea.append("To: " + ((InternetAddress) msg.getRecipients(Message.RecipientType.TO)[0]).getAddress() + "\n\n");
                    emailArea.append("Content: \n");
                    emailArea.append(getTextFromMessage(msg) + "\n");
                }
                folder.close(false);

                // Error handling
            } catch (NumberFormatException ex) {
                emailArea.setText("");
                emailArea.setText("Invalid input. Please enter a number.\n");
            } catch (Exception ex) {
                emailArea.setText("");
                emailArea.setText("Error retrieving message: " + ex.getMessage() + "\n");
            }
        }
    }

    // Delete mail based on the specific email ID
    private class DeleteByIdAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Display input dialog to get email ID
            String input = JOptionPane.showInputDialog(
                    POP3Client.this,
                    "Enter email ID to delete (1 = newest, N = oldest):",
                    "Delete Email by ID",
                    JOptionPane.PLAIN_MESSAGE
            );

            // Input is null
            if (input == null) return;
            try {
                int id = Integer.parseInt(input.trim());
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
                Message[] messages = folder.getMessages();

                // If the ID is invalid
                if (id < 1 || id > messages.length) {
                    emailArea.setText("");
                    emailArea.setText("Invalid email ID.\n");

                    // If the ID is valid
                } else {
                    Message msg = messages[(messages.length - (id - 1)) - 1];
                    msg.setFlag(Flags.Flag.DELETED, true);
                    folder.close(true); // expunge = true
                    emailArea.setText("");
                    emailArea.append("Email ID " + id + " deleted successfully.\n");
                }

                // Error handling
            } catch (NumberFormatException ex) {
                emailArea.setText("");
                emailArea.setText("Invalid input. Please enter a number.\n");
            } catch (Exception ex) {
                emailArea.setText("");
                emailArea.setText("Error deleting message: " + ex.getMessage() + "\n");
            }
        }
    }

    // Save the specific email ID as local file
    private class SaveByIdAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Display input dialog to get email ID
            String input = JOptionPane.showInputDialog(
                    POP3Client.this,
                    "Enter email ID to save (1 = newest, N = oldest):",
                    "Save Email by ID",
                    JOptionPane.PLAIN_MESSAGE
            );

            // Input is null
            if (input == null) return;
            try {
                int id = Integer.parseInt(input.trim());
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                Message[] messages = folder.getMessages();

                // If the ID is invalid
                if (id < 1 || id > messages.length) {
                    emailArea.setText("");
                    emailArea.setText("Invalid email ID.\n");

                    // If the ID is valid
                } else {
                    Message msg = messages[(messages.length - (id - 1)) - 1];
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Email");
                    int userSelection = fileChooser.showSaveDialog(POP3Client.this);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(fileChooser.getSelectedFile())) {
                            msg.writeTo(fos);
                            emailArea.setText("");
                            emailArea.setText("Email ID " + id + " saved successfully.\n");
                        }
                    }
                }
                folder.close(false);
            } catch (NumberFormatException ex) {
                emailArea.setText("");
                emailArea.setText("Invalid input. Please enter a number.\n");
            } catch (Exception ex) {
                emailArea.setText("");
                emailArea.setText("Error saving message: " + ex.getMessage() + "\n");
            }
        }
    }

    // Logout the server
    private class QuitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (store != null) {
                    store.close();
                    emailArea.setText("");
                    emailArea.append("Disconnected from server.\n");
                    emailArea.append("Exiting the app in 3 seconds...\n");
                }
            } catch (Exception ex) {
                emailArea.setText("");
                emailArea.append("Error disconnecting: " + ex.getMessage() + "\n");
            }
            new javax.swing.Timer(3000, evt -> System.exit(0)).start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            POP3Client client = new POP3Client();
            client.setVisible(true);
        });
    }
}
