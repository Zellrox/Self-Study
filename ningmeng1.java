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
} //从这开始  main.java
//
package icu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // ---- base classes ----

    // 抽象服务类：封装名称、所在 bay、是否在用、剩余时间、总时长
    static abstract class Service {
        protected String name;
        protected String bay;
        protected boolean act;
        protected int dur;   // 总时长（小时）
        protected int rem;   // 剩余小时
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

        public void addWait(String bay) {
            waitBays.add(bay);
        }

        public boolean hasWait() {
            return !waitBays.isEmpty();
        }

        public String nextWaitBay() {
            return waitBays.remove(0);
        }

        public String statusText() {
            if (act) {
                return name + " (active, " + rem + "h left)";
            } else {
                return name + " (idle)";
            }
        }
    }

    static class Ultrasound extends Service {
        public Ultrasound(String bay) {
            super("Ultrasound", bay, 3);
        }
    }

    static class ECG extends Service {
        public ECG(String bay) {
            super("ECG", bay, 1);
        }
    }

    static class Dialysis extends Service {
        public Dialysis(String bay) {
            super("Dialysis", bay, 4);
        }
    }

    static class Massage extends Service {
        public Massage(String bay) {
            super("Massage", bay, 2);
        }
    }

    // ICU 病房格子
    static class Bay {
        String name;
        JLabel lb;

        public Bay(String name) {
            this.name = name;
            this.lb = new JLabel();
        }
    }

    // ---- data ----

    private static List<Bay> bays = new ArrayList<>();
    private static List<Service> svcs = new ArrayList<>();

    // ---- main ----

    public static void main(String[] args) {

        makeData();
        makeGui();

        // 主循环：每个循环 = 1 小时
        while (true) {
            MedicalNotification n = HospitalInformation.getMedicalNotificationFromDoctor();
            handleNotification(n);

            // 时间推进一小时
            for (Service s : svcs) {
                s.tickHour();
            }

            // 看有没有刚空闲下来的服务需要去下一个 bay
            for (Service s : svcs) {
                if (!s.isActive() && s.hasWait()) {
                    String b = s.nextWaitBay();
                    moveAndStart(s, b);
                }
            }

            updateGrid();

            HospitalInformation.waitAnHour();

            int h = HospitalInformation.getHourCount();
            if (h % 24 == 0) {
                printReport();
            }
        }
    }

    // ---- setup ----

    private static void makeData() {
        // 8 个 bay
        bays.add(new Bay("Nightingale"));
        bays.add(new Bay("Barton"));
        bays.add(new Bay("Seacole"));
        bays.add(new Bay("Dix"));
        bays.add(new Bay("Henderson"));
        bays.add(new Bay("Cavell"));
        bays.add(new Bay("Breckinridge"));
        bays.add(new Bay("Sanger"));

        // 初始位置（题目给定），全部 idle
        svcs.add(new Ultrasound("Nightingale"));
        svcs.add(new ECG("Seacole"));
        svcs.add(new Massage("Seacole"));
        svcs.add(new Dialysis("Henderson"));
    }

    private static void makeGui() {
        JFrame f = new JFrame("ICU Ward Service Scheduling");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 600);

        f.setLayout(new GridLayout(2, 4));

        for (Bay b : bays) {
            b.lb.setVerticalAlignment(SwingConstants.TOP);
            f.add(b.lb);
        }

        updateGrid();

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    // ---- logic ----

    private static void handleNotification(MedicalNotification n) {
        String bay = n.getBay();
        String svcName = n.getService();

        if (svcName.equals("None")) return;

        Service s = findService(svcName);
        if (s == null) return;

        if (!s.isActive()) {
            moveAndStart(s, bay);
        } else {
            s.addWait(bay);
        }
    }

    private static Service findService(String name) {
        for (Service s : svcs) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    private static void moveAndStart(Service s, String bay) {
        // 通知搬运工
        HospitalInformation.requestPorter(bay, s.getName());
        // 更新自己状态
        s.startAt(bay);
    }

    private static void updateGrid() {
        for (Bay b : bays) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append(b.name).append("<br>");

            for (Service s : svcs) {
                if (s.getBay().equals(b.name)) {
                    sb.append(s.statusText()).append("<br>");
                }
            }
            sb.append("</html>");
            b.lb.setText(sb.toString());
        }
    }

    private static void printReport() {
        System.out.println("=== 24 hour service report ===");
        for (Service s : svcs) {
            System.out.println(
                    s.getName() + " in " + s.getBay()
                            + " | " + (s.isActive() ? "active" : "idle")
            );
        }
        System.out.println();
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
//只用 一个 Main.java 文件（另外两个是老师给的 HospitalInformation.java 和 MedicalNotification.java）

变量名用简写（bay、svc、dur、rem 等）

方法名正常英文

不写多余的错误检查和复杂结构

用继承 / 抽象 / 多态（有抽象 Service，下面 4 种具体服务）