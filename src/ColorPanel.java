import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel {
    private ColorModelConverter converter;
    private JPanel colorDisplay;
    private JLabel hexLabel;

    public ColorPanel(ColorModelConverter converter) {
        this.converter = converter;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        setBackground(new Color(240, 240, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        colorDisplay = new JPanel();
        colorDisplay.setPreferredSize(new Dimension(300, 120));
        colorDisplay.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 210), 2),
                BorderFactory.createLineBorder(new Color(230, 230, 235), 4)
        ));
        colorDisplay.setBackground(Color.RED);

        hexLabel = new JLabel("#FF0000", JLabel.CENTER);
        hexLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        hexLabel.setForeground(Color.BLACK);
    }

    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Текущий цвет", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 70, 90));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel displayContainer = new JPanel();
        displayContainer.setBackground(new Color(240, 240, 245));
        displayContainer.setLayout(new BoxLayout(displayContainer, BoxLayout.Y_AXIS));
        displayContainer.add(Box.createVerticalStrut(10));
        displayContainer.add(colorDisplay);

        colorDisplay.setLayout(new GridBagLayout());
        colorDisplay.add(hexLabel);

        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(displayContainer);
    }

    public void updateColorDisplay() {
        Color currentColor = converter.getCurrentColor();
        colorDisplay.setBackground(currentColor);

        // Автоматическое изменение цвета текста для лучшей читаемости
        int brightness = (int)(currentColor.getRed() * 0.299 +
                currentColor.getGreen() * 0.587 +
                currentColor.getBlue() * 0.114);
        Color textColor = brightness > 186 ? Color.BLACK : Color.WHITE;

        hexLabel.setForeground(textColor);
        hexLabel.setText(String.format("#%02X%02X%02X",
                currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue()));

        repaint();
    }
}