import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private ColorModelConverter converter;
    private ColorPanel colorPanel;

    // Компоненты CMYK
    private JSlider cyanSlider, magentaSlider, yellowSlider, blackSlider;
    private JTextField cyanField, magentaField, yellowField, blackField;

    // Компоненты HLS
    private JSlider hueSlider, lightnessSlider, saturationSlider;
    private JTextField hueField, lightnessField, saturationField;

    // Компоненты XYZ
    private JTextField xField, yField, zField;

    private JButton colorChooserBtn;

    public ControlPanel(ColorModelConverter converter, ColorPanel colorPanel) {
        this.converter = converter;
        this.colorPanel = colorPanel;
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        setBackground(new Color(240, 240, 245));

        // Инициализация CMYK компонентов
        cyanSlider = createSlider(0, 100, 0, new Color(0, 255, 255));
        magentaSlider = createSlider(0, 100, 100, new Color(255, 0, 255));
        yellowSlider = createSlider(0, 100, 100, new Color(255, 255, 0));
        blackSlider = createSlider(0, 100, 0, Color.BLACK);

        cyanField = createTextField("0");
        magentaField = createTextField("100");
        yellowField = createTextField("100");
        blackField = createTextField("0");

        // Инициализация HLS компонентов
        hueSlider = createSlider(0, 360, 0, Color.RED);
        lightnessSlider = createSlider(0, 100, 50, Color.LIGHT_GRAY);
        saturationSlider = createSlider(0, 100, 100, Color.PINK);

        hueField = createTextField("0");
        lightnessField = createTextField("50");
        saturationField = createTextField("100");

        // Инициализация XYZ компонентов
        xField = createTextField("41.24");
        yField = createTextField("21.26");
        zField = createTextField("1.93");

        // Кнопка выбора цвета
        colorChooserBtn = new JButton("🎨 Выбрать цвет из палитры");
        colorChooserBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        colorChooserBtn.setBackground(new Color(70, 130, 180));
        colorChooserBtn.setForeground(Color.WHITE);
        colorChooserBtn.setFocusPainted(false);
        colorChooserBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 110, 160), 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
    }

    private JSlider createSlider(int min, int max, int value, Color trackColor) {
        JSlider slider = new JSlider(min, max, value);
        slider.setMajorTickSpacing((max - min) / 4);
        slider.setMinorTickSpacing((max - min) / 20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(new Color(250, 250, 255));

        // Кастомная отрисовка для цветных слайдеров
        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Rectangle trackBounds = trackRect;
                if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
                    g2d.setColor(new Color(220, 220, 225));
                    g2d.fillRoundRect(trackBounds.x, trackBounds.y + trackBounds.height/2 - 2,
                            trackBounds.width, 4, 4, 4);

                    g2d.setColor(trackColor);
                    int filledWidth = (int) (trackBounds.width * ((double)slider.getValue() / slider.getMaximum()));
                    g2d.fillRoundRect(trackBounds.x, trackBounds.y + trackBounds.height/2 - 2,
                            filledWidth, 4, 4, 4);
                }
            }
        });

        return slider;
    }

    private JTextField createTextField(String text) {
        JTextField field = new JTextField(text, 6);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Consolas", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 190)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Панель CMYK
        JPanel cmykPanel = createModelPanel("CMYK МОДЕЛЬ",
                new String[]{"Cyan", "Magenta", "Yellow", "Black"},
                new JSlider[]{cyanSlider, magentaSlider, yellowSlider, blackSlider},
                new JTextField[]{cyanField, magentaField, yellowField, blackField},
                new Color(220, 240, 255));

        // Панель HLS
        JPanel hlsPanel = createModelPanel("HLS МОДЕЛЬ",
                new String[]{"Hue", "Lightness", "Saturation"},
                new JSlider[]{hueSlider, lightnessSlider, saturationSlider},
                new JTextField[]{hueField, lightnessField, saturationField},
                new Color(255, 240, 245));

        // Панель XYZ
        JPanel xyzPanel = createModelPanel("XYZ МОДЕЛЬ",
                new String[]{"X", "Y", "Z"},
                null,
                new JTextField[]{xField, yField, zField},
                new Color(240, 255, 240));

        // Контейнер для панелей моделей
        JPanel modelsContainer = new JPanel(new GridLayout(1, 3, 15, 0));
        modelsContainer.setBackground(new Color(240, 240, 245));
        modelsContainer.add(cmykPanel);
        modelsContainer.add(hlsPanel);
        modelsContainer.add(xyzPanel);

        // Панель кнопки
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        buttonPanel.add(colorChooserBtn);

        add(modelsContainer);
        add(buttonPanel);
    }

    private JPanel createModelPanel(String title, String[] labels, JSlider[] sliders,
                                    JTextField[] fields, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 190), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Заголовок
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));

        for (int i = 0; i < labels.length; i++) {
            JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
            rowPanel.setBackground(bgColor);
            rowPanel.setMaximumSize(new Dimension(300, 45));

            // Метка
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setPreferredSize(new Dimension(70, 20));
            rowPanel.add(label, BorderLayout.WEST);

            // Слайдер (если есть)
            if (sliders != null && sliders[i] != null) {
                rowPanel.add(sliders[i], BorderLayout.CENTER);
            } else {
                rowPanel.add(Box.createHorizontalStrut(150), BorderLayout.CENTER);
            }

            // Поле ввода
            JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            fieldPanel.setBackground(bgColor);
            fieldPanel.add(fields[i]);

            // Единицы измерения
            JLabel unitLabel = new JLabel(getUnit(labels[i]));
            unitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            unitLabel.setForeground(Color.GRAY);
            fieldPanel.add(unitLabel);

            rowPanel.add(fieldPanel, BorderLayout.EAST);
            panel.add(rowPanel);

            if (i < labels.length - 1) {
                panel.add(Box.createVerticalStrut(8));
            }
        }

        return panel;
    }

    private String getUnit(String label) {
        switch (label) {
            case "Hue": return "°";
            case "Lightness": case "Saturation":
            case "Cyan": case "Magenta": case "Yellow": case "Black":
                return "%";
            default: return "";
        }
    }

    private void setupListeners() {
        // Слушатели для CMYK
        addSliderListener(cyanSlider, cyanField, "cmyk");
        addSliderListener(magentaSlider, magentaField, "cmyk");
        addSliderListener(yellowSlider, yellowField, "cmyk");
        addSliderListener(blackSlider, blackField, "cmyk");

        addFieldListener(cyanField, cyanSlider, "cmyk");
        addFieldListener(magentaField, magentaSlider, "cmyk");
        addFieldListener(yellowField, yellowSlider, "cmyk");
        addFieldListener(blackField, blackSlider, "cmyk");

        // Слушатели для HLS
        addSliderListener(hueSlider, hueField, "hls");
        addSliderListener(lightnessSlider, lightnessField, "hls");
        addSliderListener(saturationSlider, saturationField, "hls");

        addFieldListener(hueField, hueSlider, "hls");
        addFieldListener(lightnessField, lightnessSlider, "hls");
        addFieldListener(saturationField, saturationSlider, "hls");

        // Слушатели для XYZ
        addXYZFieldListener(xField);
        addXYZFieldListener(yField);
        addXYZFieldListener(zField);

        // Слушатель кнопки выбора цвета
        colorChooserBtn.addActionListener(e -> showColorChooser());

        // Добавляем hover эффект для кнопки
        colorChooserBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorChooserBtn.setBackground(new Color(65, 105, 225));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorChooserBtn.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private void addSliderListener(JSlider slider, JTextField field, String type) {
        slider.addChangeListener(e -> {
            if (!converter.isUpdating() && !slider.getValueIsAdjusting()) {
                converter.setUpdating(true);
                field.setText(String.valueOf(slider.getValue()));
                converter.setUpdating(false);

                switch (type) {
                    case "cmyk":
                        converter.updateColorFromCMYK(
                                cyanSlider.getValue(), magentaSlider.getValue(),
                                yellowSlider.getValue(), blackSlider.getValue()
                        );
                        break;
                    case "hls":
                        converter.updateColorFromHLS(
                                hueSlider.getValue(), lightnessSlider.getValue(),
                                saturationSlider.getValue()
                        );
                        break;
                }
            }
        });
    }

    private void addFieldListener(JTextField field, JSlider slider, String type) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void insertUpdate(DocumentEvent e) { update(); }

            private void update() {
                if (!converter.isUpdating()) {
                    try {
                        int value = Integer.parseInt(field.getText());
                        value = Math.max(slider.getMinimum(), Math.min(slider.getMaximum(), value));
                        converter.setUpdating(true);
                        slider.setValue(value);
                        converter.setUpdating(false);

                        switch (type) {
                            case "cmyk":
                                converter.updateColorFromCMYK(
                                        cyanSlider.getValue(), magentaSlider.getValue(),
                                        yellowSlider.getValue(), blackSlider.getValue()
                                );
                                break;
                            case "hls":
                                converter.updateColorFromHLS(
                                        hueSlider.getValue(), lightnessSlider.getValue(),
                                        saturationSlider.getValue()
                                );
                                break;
                        }
                    } catch (NumberFormatException ex) {
                        // Игнорируем некорректный ввод
                    }
                }
            }
        });
    }

    private void addXYZFieldListener(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void insertUpdate(DocumentEvent e) { update(); }

            private void update() {
                if (!converter.isUpdating()) {
                    try {
                        double x = Double.parseDouble(xField.getText());
                        double y = Double.parseDouble(yField.getText());
                        double z = Double.parseDouble(zField.getText());
                        converter.updateColorFromXYZ(x, y, z);
                    } catch (NumberFormatException ex) {
                        // Игнорируем некорректный ввод
                    }
                }
            }
        });
    }

    private void showColorChooser() {
        Color chosenColor = JColorChooser.showDialog(this, "Выберите цвет", converter.getCurrentColor());
        if (chosenColor != null) {
            converter.setCurrentColor(chosenColor);
            converter.setUpdating(true);
            updateCMYKFromRGB(chosenColor.getRGB());
            updateHLSFromRGB(chosenColor.getRGB());
            updateXYZFromRGB(chosenColor.getRGB());
            converter.setUpdating(false);
            colorPanel.updateColorDisplay();
        }
    }

    // Методы обновления из RGB
    public void updateCMYKFromRGB(int rgb) {
        Color color = new Color(rgb);
        double r = color.getRed() / 255.0;
        double g = color.getGreen() / 255.0;
        double b = color.getBlue() / 255.0;

        double k = 1 - Math.max(r, Math.max(g, b));
        double c = (k == 1) ? 0 : (1 - r - k) / (1 - k);
        double m = (k == 1) ? 0 : (1 - g - k) / (1 - k);
        double y = (k == 1) ? 0 : (1 - b - k) / (1 - k);

        cyanSlider.setValue((int)Math.round(c * 100));
        magentaSlider.setValue((int)Math.round(m * 100));
        yellowSlider.setValue((int)Math.round(y * 100));
        blackSlider.setValue((int)Math.round(k * 100));

        cyanField.setText(String.valueOf(cyanSlider.getValue()));
        magentaField.setText(String.valueOf(magentaSlider.getValue()));
        yellowField.setText(String.valueOf(yellowSlider.getValue()));
        blackField.setText(String.valueOf(blackSlider.getValue()));
    }

    public void updateHLSFromRGB(int rgb) {
        Color color = new Color(rgb);
        float[] hls = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        int hue = (int)(hls[0] * 360);
        int lightness = (int)(hls[2] * 100);
        int saturation = (int)(hls[1] * 100);

        hueSlider.setValue(hue);
        lightnessSlider.setValue(lightness);
        saturationSlider.setValue(saturation);

        hueField.setText(String.valueOf(hue));
        lightnessField.setText(String.valueOf(lightness));
        saturationField.setText(String.valueOf(saturation));
    }

    public void updateXYZFromRGB(int rgb) {
        Color color = new Color(rgb);
        double[] xyz = converter.rgbToXYZ(color);

        xField.setText(converter.formatDouble(xyz[0]));
        yField.setText(converter.formatDouble(xyz[1]));
        zField.setText(converter.formatDouble(xyz[2]));
    }
}