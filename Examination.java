package Stroke;

import java.time.LocalDate;

public abstract class Examination {

    private LocalDate date;

    public Examination(LocalDate date){
        this.date=date;
    }

    public LocalDate getDate() {
        return date;
    }

    public abstract String getDoctorOutputText();
    public abstract String getAdminDetails();
    //先规定有这个方法，但现在不写具体内容，强制子类去实现。
    //你现在还不知道具体每种情况的文案长什么样
    //但你知道：每一种 Alert 类型，都一定要能输出两种文字：
    //给医生看的报告（getDoctorReport()）
    //给管理员 log 的内容（getAdminLogEntry()）
}
