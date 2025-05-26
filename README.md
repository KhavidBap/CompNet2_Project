# POP3 Email Client

Team
- Ngo Duc Minh Khoa (10422036)
- Pham Luu Minh Nhu (10422063)
- Dang Phan Khanh Linh (10422106)

A simple Java Swing application for connecting to a POP3 email server, viewing, reading, deleting, and saving emails.  
Supports Yandex POP3 accounts and provides a user-friendly GUI.

## Features

- Login/logout to a POP3 mailbox (tested with Yandex)
- List all emails or filter by specific date
- View mailbox statistics (total messages, size, oldest/newest dates)
- Read latest email
- Read/Delete/Save email by ID

## Requirements

- Java 17 or higher (project uses Java 23 syntax, ensure your JDK supports it)
- Maven (for dependency management and building)
- Internet connection (for accessing the mail server)

## Dependencies

- `com.sun.mail:javax.mail` (JavaMail API)
- `jakarta.activation:jakarta.activation-api`
- `com.sun.activation:javax.activation`
- `javax.activation:javax.activation-api`

All dependencies are managed via Maven in `pom.xml`.

## Setup

1. **Clone the repository:**
   https://github.com/KhavidBap/CompNet2_Project.git
2. **Configure your email credentials:**
    - Use your Yandex email and an app password (not your main password).
    - Example credentials can be stored in `mail.txt` (not required for running).
3. **Build the project:**
```mvn clean package```
4. **Run the application:**
```mvn exec:java -Dexec.mainClass="POP3Client"```
or run the `POP3Client` class from your IDE

## Usage

- Enter your Yandex email and app password, then click **Login**.
- Use the left panel buttons to:
    - View statistics
    - List all emails or by date
    - Read, delete, or save emails by ID
    - Logout

## Notes

- Only plain text emails are fully supported for reading.
- The app uses a `JTextArea` for output; rich formatting is not supported.

## License

This project is for educational purposes.