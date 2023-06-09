package atmosfears.AtmosFearsBE.database;

import lombok.Getter;

public enum SensorCode {
    MpKrakAlKras("al. Krasińskiego", 50.057678, 19.926189),
    MpKrakBujaka("ul. Bujaka", 50.010575, 19.949189),
    MpKrakBulwar("ul. Bulwarowa", 50.069308, 20.053492),
    MpKrakDietla("ul. Dietla", 50.057447, 19.946008),
    MpKrakOsPias("os. Piastów", 50.098508, 20.018269),
    MpKrakowWIOSPrad6115("ul. Prądnicka", 50.087500, 19.926111),
    MpKrakowWSSEKapi6108("ul. Swoszowice Kąpielowa 70", 49.991667, 19.935000),
    MpKrakowWSSEPrad6102("ul. Prądnicka 76", 50.087500, 19.938056),
    MpKrakowWSSERPod6113("ul. Rynek Podgórski", 50.040000, 19.943333),
    MpKrakSwoszo("ul. Lusińska", 49.991442, 19.936792),
    MpKrakTelime("ul. Telimeny 9", 50.019036, 20.016822),
    MpKrakWadow("os. Wadów", 50.100569, 20.122561),
    MpKrakZloRog("ul. Złoty Róg",50.081197, 19.895358);

    @Getter
    public final String address;
    @Getter
    public final double latitude;
    @Getter
    public final double longitude;


    SensorCode(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
