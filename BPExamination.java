package Stroke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BPExamination extends Examination {
    private int systolicpressure;
    private int diastolicpressure;
    private String durationType;

    public BPExamination(int systolicpressure, int diastolicpressure, LocalDate scanDate, String durationType) {
        super(scanDate);
        this.systolicpressure = systolicpressure;
        this.diastolicpressure = diastolicpressure;
        this.durationType = durationType;
    }


    public int getSystolicPressure() {
        return systolicpressure;
    }
    public int getDiastolicPressure() {
        return diastolicpressure;
    }
    public String getDurationType() {
        return durationType;
    }


    @Override
    public String getDoctorOutputText() {
        return "Blood pressure" + systolicpressure+ " over "+ diastolicpressure;
    }
    @Override
    public String getAdminDetails() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return "BP: " + durationType + ", " + getDate().format(fmt);
    }

}
