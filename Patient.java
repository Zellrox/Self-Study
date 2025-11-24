package Stroke;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Patient {

    private String name;
    private int age;
    private String pictureUrl;              // 可以为 null / ""，表示没有头像
    private List<Examination> examinations; // 病人的所有检查

    public Patient(String name, int age, String pictureUrl) {
        this.name = name;
        this.age = age;
        this.pictureUrl = pictureUrl;
        this.examinations = new ArrayList<>();
    }

    // ===== 基础 getter（main 和其他类会用到） =====
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public List<Examination> getExaminations() {
        return examinations;
    }

    // 往病人里加一项检查
    public void addExamination(Examination exam) {
        if (exam != null) {
            examinations.add(exam);
        }
    }

    // 让 JList<Patient> 直接显示名字
    @Override
    public String toString() {
        return name;
    }

    /**
     * 保留一个简单的 GUI 方法，方便 main 直接用：
     * 创建一个显示病人基本信息（头像 + 姓名 + 年龄）的 JPanel。
     * 如果没图或加载失败，就只显示文字。
     */
    public JPanel createDisplayPanel(int imgWidth, int imgHeight) {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));

        // 基本文字信息
        String text = "<html><b>Name: </b>" + name +
                "<br><b>Age: </b>" + age + "</html>";
        label.setText(text);

        // 尝试加载图片（如果有 pictureUrl）
        if (pictureUrl != null && !pictureUrl.isBlank()) {
            try {
                URL url = new URL(pictureUrl);
                ImageIcon icon = new ImageIcon(url);

                if (imgWidth > 0 && imgHeight > 0) {
                    Image scaled = icon.getImage()
                            .getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaled);
                }

                label.setIcon(icon);
                label.setHorizontalTextPosition(SwingConstants.CENTER);
                label.setVerticalTextPosition(SwingConstants.BOTTOM);
            } catch (Exception e) {
                System.out.println("Patient image load failed: " + pictureUrl);
                // 图片失败就保留文字就行，不抛异常
            }
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
