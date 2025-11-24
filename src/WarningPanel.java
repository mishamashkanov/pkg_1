import javax.swing.*;
import java.awt.*;

public class WarningPanel extends JPanel {
    private JLabel warningLabel;

    public WarningPanel() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        setBackground(new Color(255, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        warningLabel = new JLabel(" ");
        warningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        warningLabel.setForeground(new Color(200, 0, 0));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(warningLabel, BorderLayout.CENTER);
    }

    public void showWarning(String message) {
        warningLabel.setText("⚠ " + message);
        setBackground(new Color(255, 230, 230));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 100, 100), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    public void clearWarning() {
        warningLabel.setText(" ");
        setBackground(new Color(240, 240, 245));
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}