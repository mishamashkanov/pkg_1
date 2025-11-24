import java.awt.*;
import java.text.DecimalFormat;

public class ColorModelConverter {
    private Color currentColor = Color.RED;
    private boolean updating = false;
    private DecimalFormat df = new DecimalFormat("0.##");

    private WarningPanel warningPanel;
    private ControlPanel controlPanel;
    private ColorPanel colorPanel;

    public void setWarningPanel(WarningPanel warningPanel) {
        this.warningPanel = warningPanel;
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public void setColorPanel(ColorPanel colorPanel) {
        this.colorPanel = colorPanel;
    }

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
        if (colorPanel != null) {
            colorPanel.updateColorDisplay();
        }
    }

    // Основные методы преобразования цветов
    public void updateColorFromCMYK(int c, int m, int y, int k) {
        try {
            double cyan = c / 100.0;
            double magenta = m / 100.0;
            double yellow = y / 100.0;
            double black = k / 100.0;

            int rgb = cmykToRgb(cyan, magenta, yellow, black);
            currentColor = new Color(rgb);

            updating = true;
            if (controlPanel != null) {
                controlPanel.updateHLSFromRGB(rgb);
                controlPanel.updateXYZFromRGB(rgb);
            }
            updating = false;

            if (colorPanel != null) {
                colorPanel.updateColorDisplay();
            }
            clearWarning();
        } catch (Exception e) {
            showWarning("Ошибка в преобразовании CMYK");
        }
    }

    public void updateColorFromHLS(int h, int l, int s) {
        try {
            double hue = h;
            double lightness = l / 100.0;
            double saturation = s / 100.0;

            int rgb = hslToRgb(hue, saturation, lightness);
            currentColor = new Color(rgb);

            updating = true;
            if (controlPanel != null) {
                controlPanel.updateCMYKFromRGB(rgb);
                controlPanel.updateXYZFromRGB(rgb);
            }
            updating = false;

            if (colorPanel != null) {
                colorPanel.updateColorDisplay();
            }
            clearWarning();
        } catch (Exception e) {
            showWarning("Ошибка в преобразовании HLS");
        }
    }

    public void updateColorFromXYZ(double x, double y, double z) {
        try {
            int rgb = xyzToRgb(x, y, z);
            currentColor = new Color(rgb);

            updating = true;
            if (controlPanel != null) {
                controlPanel.updateCMYKFromRGB(rgb);
                controlPanel.updateHLSFromRGB(rgb);
            }
            updating = false;

            if (colorPanel != null) {
                colorPanel.updateColorDisplay();
            }
            clearWarning();
        } catch (Exception e) {
            showWarning("Цвет вне диапазона XYZ - применено округление");
        }
    }

    // Методы преобразования между цветовыми моделями
    public int cmykToRgb(double c, double m, double y, double k) {
        double r = 255 * (1 - c) * (1 - k);
        double g = 255 * (1 - m) * (1 - k);
        double b = 255 * (1 - y) * (1 - k);

        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        return new Color((int)r, (int)g, (int)b).getRGB();
    }

    public int hslToRgb(double h, double s, double l) {
        h = h % 360.0;
        if (h < 0) h += 360;

        double c = (1 - Math.abs(2 * l - 1)) * s;
        double x = c * (1 - Math.abs((h / 60) % 2 - 1));
        double m = l - c / 2;

        double r, g, b;

        if (h >= 0 && h < 60) {
            r = c; g = x; b = 0;
        } else if (h >= 60 && h < 120) {
            r = x; g = c; b = 0;
        } else if (h >= 120 && h < 180) {
            r = 0; g = c; b = x;
        } else if (h >= 180 && h < 240) {
            r = 0; g = x; b = c;
        } else if (h >= 240 && h < 300) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }

        int red = (int)Math.round((r + m) * 255);
        int green = (int)Math.round((g + m) * 255);
        int blue = (int)Math.round((b + m) * 255);

        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new Color(red, green, blue).getRGB();
    }

    public int xyzToRgb(double x, double y, double z) {
        // Нормализация значений XYZ
        x /= 100.0;
        y /= 100.0;
        z /= 100.0;

        // Матрица преобразования XYZ to RGB (sRGB)
        double r = 3.2406 * x - 1.5372 * y - 0.4986 * z;
        double g = -0.9689 * x + 1.8758 * y + 0.0415 * z;
        double b = 0.0557 * x - 0.2040 * y + 1.0570 * z;

        // Гамма коррекция
        r = (r > 0.0031308) ? 1.055 * Math.pow(r, 1/2.4) - 0.055 : 12.92 * r;
        g = (g > 0.0031308) ? 1.055 * Math.pow(g, 1/2.4) - 0.055 : 12.92 * g;
        b = (b > 0.0031308) ? 1.055 * Math.pow(b, 1/2.4) - 0.055 : 12.92 * b;

        // Масштабирование и ограничение
        int red = (int)Math.round(r * 255);
        int green = (int)Math.round(g * 255);
        int blue = (int)Math.round(b * 255);

        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new Color(red, green, blue).getRGB();
    }

    public double[] rgbToXYZ(Color color) {
        double r = color.getRed() / 255.0;
        double g = color.getGreen() / 255.0;
        double b = color.getBlue() / 255.0;

        // Гамма коррекция
        r = (r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
        g = (g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
        b = (b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

        // Преобразование в XYZ
        double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
        double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
        double z = r * 0.0193 + g * 0.1192 + b * 0.9505;

        // Масштабирование
        x *= 100;
        y *= 100;
        z *= 100;

        return new double[]{x, y, z};
    }

    public String formatDouble(double value) {
        return df.format(value);
    }

    private void showWarning(String message) {
        if (warningPanel != null) {
            warningPanel.showWarning(message);
        }
    }

    private void clearWarning() {
        if (warningPanel != null) {
            warningPanel.clearWarning();
        }
    }
}