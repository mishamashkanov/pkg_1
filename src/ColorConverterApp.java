import javax.swing.*;
import java.awt.*;

public class ColorConverterApp extends JFrame {
    private ColorModelConverter converter;
    private ColorPanel colorPanel;
    private ControlPanel controlPanel;
    private WarningPanel warningPanel;

    public ColorConverterApp() {
        setTitle("Конвертер цветовых моделей - CMYK ↔ HLS ↔ XYZ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        setupLayout();

        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 700));
    }

    private void initializeComponents() {
        converter = new ColorModelConverter();
        colorPanel = new ColorPanel(converter);
        controlPanel = new ControlPanel(converter, colorPanel);
        warningPanel = new WarningPanel();

        converter.setWarningPanel(warningPanel);
        converter.setControlPanel(controlPanel);
        converter.setColorPanel(colorPanel);
    }

    private void setupLayout() {
        // Основная панель с отступами
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 245));

        // Заголовок
        JLabel titleLabel = new JLabel("Конвертер цветовых моделей", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Подзаголовок
        JLabel subtitleLabel = new JLabel("CMYK ↔ HLS ↔ XYZ", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 120));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(colorPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(warningPanel, BorderLayout.SOUTH);
    }
}