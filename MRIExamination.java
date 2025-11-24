package Stroke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MRIExamination extends Examination {
    private String imageUrl;
    private int fieldStrengthTesla;

    public MRIExamination(String imageUrl, int fieldStrengthTesla, LocalDate scanDate) {
        super(scanDate);  //super要写在前面
        this.imageUrl = imageUrl;
        this.fieldStrengthTesla = fieldStrengthTesla;
    }

    public String getImageUrl() {return imageUrl;}
    public double getFieldStrengthTesla() {return fieldStrengthTesla;}




    @Override
    public String getAdminDetails() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return "MRI: " + fieldStrengthTesla + " Tesla, " + getDate().format(fmt);
    }

    // 对医生来说，MRI 的“输出”是图像，不是文字，所以可以返回空字符串
    @Override
    public String getDoctorOutputText() {
        return "";
    }
}
