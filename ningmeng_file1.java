package Stroke;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // ========= 第 1 步：创建两个病人和他们的检查 =========
        List<Patient> patients = new ArrayList<>();

        // ---- 病人 1：John Smith ----
        Patient p1 = new Patient(
                // 类型名  变量名  = 初始值;
                "John Smith",
                20,
                "http://martinh.netfirms.com/BIOE60010/DaphneVonOram.jpg" // 头像
        );

        MRIExamination p1Mri = new MRIExamination(
                // MRI 图像 URL
                "http://martinh.netfirms.com/BIOE60010/mri1.jpg",
                2,
                LocalDate.of(2023, 10, 13)
        );

        BPExamination p1Bp = new BPExamination(
                120,
                80,
                LocalDate.of(2023, 10, 14),
                "VST"   // 测量方式
        );

        // addExamination 方法在 Patient 类里定义
        // 它的作用是把 Examination 对象放进 Patient 对象的 examinations 列表
        p1.addExamination(p1Mri);  // 这里的 p1Mri 是 MRIExamination 对象，是 Examination 的子类
        p1.addExamination(p1Bp);
        patients.add(p1);          // 把病人放进病人列表

        // ---- 病人 2：Jill Brown ----
        Patient p2 = new Patient(
                "Jill Brown",
                30,
                "http://martinh.netfirms.com/BIOE60010/SebastianCompton.jpg"
        );

        MRIExamination p2Mri = new MRIExamination(
                "http://martinh.netfirms.com/BIOE60010/mri2.jpg",
                3,
                LocalDate.of(2023, 11, 15)
        );

        BPExamination p2Bp = new BPExamination(
                130,
                70,
                LocalDate.of(2023, 11, 15),
                "ST"
        );

        p2.addExamination(p2Mri);
        p2.addExamination(p2Bp);
        patients.add(p2);


        // ========= 第 2 步：管理员视角，在控制台打印技术 log =========
        System.out.println("=== Admin log ===");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // 对列表里的每个病人，依次打印他们的检查内容
        for (Patient p : patients) {
            MRIExamination mri = null;
            BPExamination bp = null;
            // Patient 类之中的方法：
            // public List<Examination> getExaminations() { return examinations; }
            // 把这个病人所有的检查列表（MRI + BP）拿出来给调用者
            for (Examination exam : p.getExaminations()) {
                if (exam instanceof MRIExamination) {
                    // instanceof 会返回一个 boolean（true / false）
                    // 如果 exam 是 MRIExamination，就赋值给 mriExam
                    mri = (MRIExamination) exam;
                } else if (exam instanceof BPExamination) {
                    bp = (BPExamination) exam;
                }
            }
            // 用“最朴素”的字符串拼接方式来生成这一行日志
            String line =
                    "Patient: " + p.getName()
                            + "  MRI: " + mri.getFieldStrengthTesla() + " Tesla, "
                            + mri.getDate().format(fmt)
                            + "  BP: " + bp.getSystolicPressure() + " over " + bp.getDiastolicPressure() + ", "
                            + bp.getDate().format(fmt);

            System.out.println(line); // 只需要这一行
        }



// === 医生视角（超级精简版） ===
// 创建窗口
        JFrame frame = new JFrame("Stroke Investigation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

// 每个病人一行，每行 3 列
        // 每个病人一行，每行 3 列（头像+信息 / MRI 图 / 血压文字）
        frame.setLayout(new GridLayout(patients.size(), 3));



        // 直接默认所有数据都存在：
        // 每个病人 examinations.get(0) = MRI, get(1) = BP
        for (Patient p : patients) {
            // ===== 左列：头像 + 姓名 + 年龄 =====
            JLabel Label = new JLabel("", SwingConstants.CENTER);
            ImageIcon icon = loadIconFromUrl(p.getPictureUrl(), 150, 150);
            Label.setIcon(icon);
            Label.setText("<html><b>Name: </b>" + p.getName() +
                    "<br><b>Age: </b>" + p.getAge() + "</html>");
            Label.setHorizontalTextPosition(SwingConstants.CENTER);
            Label.setVerticalTextPosition(SwingConstants.BOTTOM);
            frame.add(Label);

            // ===== 中列：MRI 图像（直接取第 0 个）=====
            MRIExamination mri = (MRIExamination) p.getExaminations().get(0);
            JLabel mriLabel = new JLabel("", SwingConstants.CENTER);
            ImageIcon mriIcon = loadIconFromUrl(mri.getImageUrl(), 150, 150);
            mriLabel.setIcon(mriIcon);
            frame.add(mriLabel);

            // ===== 右列：BP 文本输出（直接取第 1 个）=====
            BPExamination bp = (BPExamination) p.getExaminations().get(1);
            JLabel bpLabel = new JLabel(
                    "<html><b>" + bp.getDoctorOutputText() + "</b></html>",
                    SwingConstants.CENTER
            );
            frame.add(bpLabel);
        }

        // 显示窗口
        frame.setVisible(true);
    }

    // ========= 小工具函数：从 URL 加载并缩放图片 =========
    private static ImageIcon loadIconFromUrl(String urlString, int w, int h) {
        try {
            URL url = new URL(urlString);                 // 把字符串变成 URL
            ImageIcon icon = new ImageIcon(url);          // 从网络加载图片
            Image img = icon.getImage()
                    .getScaledInstance(w, h, Image.SCALE_SMOOTH); // 按 w×h 缩放
            return new ImageIcon(img);                    // 再包成新的 ImageIcon 返回
        } catch (Exception e) {
            System.out.println("Failed to load image: " + urlString);
            System.out.println("  Reason: " + e.getClass().getSimpleName()
                    + " - " + e.getMessage());
            return null;  // 出错时返回 null
        }
    }
} //从这开始 1.service
import java.util.ArrayList;
import java.util.List;

public abstract class Service {
    protected String name;
    protected String bay;
    protected boolean act;
    protected int dur;
    protected int rem;
    protected List<String> waitBays = new ArrayList<>();

    public Service(String name, String bay, int dur) {
        this.name = name;
        this.bay = bay;
        this.dur = dur;
        this.rem = 0;
        this.act = false;
    }

    public String getName() { return name; }
    public String getBay() { return bay; }
    public boolean isActive() { return act; }

    public void tickHour() {
        if (act) {
            rem--;
            if (rem <= 0) {
                rem = 0;
                act = false;
            }
        }
    }

    public void startAt(String bay) {
        this.bay = bay;
        this.act = true;
        this.rem = dur;
    }

    public void addWait(String bay) { waitBays.add(bay); }
    public boolean hasWait() { return !waitBays.isEmpty(); }
    public String nextWaitBay() { return waitBays.remove(0); }

    public String statusText() {
        if (act) return name + " (active, " + rem + "h left)";
        return name + " (idle)";
    }
}


 //到这结束

import java.time.LocalDate;

public abstract class Examination {
    private LocalDate d;

    public Examination(LocalDate d) {
        this.d = d;
    }

    public LocalDate getDate() {
        return d;
    }

    public abstract String docText();  // 给医生看的描述（用在界面上）
    public abstract String logText();  // 给管理员看的日志（打印到控制台）
}
///ICUProject/
    Main.java
    Service.java
    Ultrasound.java
    ECG.java
    Dialysis.java
    Massage.java
    Bay.java
    HospitalInformation.java     ← 老师的
    MedicalNotification.java      ← 老师的
