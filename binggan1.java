package Lion;

public class AlartSystem {

    private double feedingRadius;

    public AlartSystem(double feedingRadius) {
        this.feedingRadius = feedingRadius;
    }



    public void checkFeeding(Keeper keeper, Lion lion) {
        double d = lion.getPosition().distance(keeper.getPosition());
        if (d <= feedingRadius) {
            System.out.println("Dangerous");



        }
    }
}
   














   package stroke;

import java.time.LocalDate;

public abstract class Examination {
    private LocalDate date;

    public Examination(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    // 给医生看的文本描述
    public abstract String getDoctorDescription();

    // 给管理员看的文本描述（包含日期等技术细节）
    public abstract String getAdminLog();
}
