package atmosfears.AtmosFearsBE.util;

import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.AirParticulatesWindroseInfo;
import atmosfears.AtmosFearsBE.model.Direction;

import java.util.Arrays;

public class AirParticulatesConverter {

    private static final double ANGLE_DIRECTION_WIDTH = 22.5;

    private static final int[] NO2_MAPPING = { 50, 100, 150, 200, 250, 300, 350 };
    private static final int[] SO2_MAPPING = { 7, 14, 21, 28, 35, 42, 49 };
    private static final int[] PM10_MAPPING = { 50, 100, 150, 200, 250, 300, 350 };
    private static final int[] PM2_5_MAPPING = { 25, 50, 75, 100, 125, 150, 175 };
    private static final int[] O3_MAPPING = { 25, 50, 75, 100, 125, 150, 175 };
    private static final double[] CO_MAPPING = { 0.5, 1, 1.5, 2, 2.5, 3, 3.5 };


    public static AirParticulatesWindroseInfo convertToWindroseParameters(
            AirParticulates particulates,
            Particulate particulate
    ) {
        AirParticulatesWindroseInfo windroseParameters = new AirParticulatesWindroseInfo();

        windroseParameters.setParticulate(particulate);
        windroseParameters.setDirection(getDirectionFromAngle(particulates.getWindDirection()));

        long particulateRange = switch (particulate) {
            case NO2 -> Arrays.stream(NO2_MAPPING)
                              .asDoubleStream()
                              .filter(range -> particulates.getNO2() != null &&
                                               particulates.getNO2() > range)
                              .count();
            case SO2 -> Arrays.stream(SO2_MAPPING)
                              .asDoubleStream()
                              .filter(range -> particulates.getSO2() != null &&
                                               particulates.getSO2() > range)
                              .count();
            case PM10 -> Arrays.stream(PM10_MAPPING)
                               .asDoubleStream()
                               .filter(range -> particulates.getPM10() !=
                                                null &&
                                                particulates.getPM10() > range)
                               .count();
            case PM25 -> Arrays.stream(PM2_5_MAPPING)
                               .asDoubleStream()
                               .filter(range -> particulates.getPM25() !=
                                                null &&
                                                particulates.getPM25() > range)
                               .count();
            case O3 -> Arrays.stream(O3_MAPPING)
                             .asDoubleStream()
                             .filter(range -> particulates.getO3() != null &&
                                              particulates.getO3() > range)
                             .count();
            default -> Arrays.stream(CO_MAPPING)
                             .filter(range -> particulates.getCO() != null &&
                                              particulates.getCO() > range)
                             .count();
        };

        windroseParameters.setSection((int) particulateRange);
        return windroseParameters;
    }

    private static Direction getDirectionFromAngle(double angle) {
        int angleDivided = (int) (((angle + 22.5) % 360) / ANGLE_DIRECTION_WIDTH);

        return Direction.values()[angleDivided];
    }
}
