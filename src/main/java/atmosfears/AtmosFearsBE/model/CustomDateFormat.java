package atmosfears.AtmosFearsBE.model;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum CustomDateFormat {
    day(ChronoUnit.DAYS, 1),
    month( ChronoUnit.MONTHS, 1),
    year( ChronoUnit.YEARS, 3);


    private final TemporalUnit unit;
    private final int amount;
    CustomDateFormat(TemporalUnit unit, int amount) {
        this.unit = unit;
        this.amount = amount;
    }

    public TemporalUnit getUnit() {
        return unit;
    }

    public int getAmount() {
        return amount;
    }

    public static CustomDateFormat fromString(String name){
        CustomDateFormat format = null;
        try{
            format = CustomDateFormat.valueOf(name);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  format;
    }


}
