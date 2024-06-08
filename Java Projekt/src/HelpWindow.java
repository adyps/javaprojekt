import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelpWindow extends JFrame {
    public HelpWindow() {
        setTitle("Help Window");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        try {
            String content = new String(Files.readAllBytes(Paths.get("sugo.txt")));
            textArea.setText(content);
        } catch (IOException e) {
            textArea.setText("Failed to load.");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

}
