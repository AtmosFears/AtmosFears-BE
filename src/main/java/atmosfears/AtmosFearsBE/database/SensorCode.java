package atmosfears.AtmosFearsBE.database;

public enum SensorCode {
    MpKrakAlKras("al. Krasińskiego"),
    MpKrakBujaka("ul. Bujaka"),
    MpKrakBulwar("ul. Bulwarowa"),
    MpKrakDietla("ul. Dietla"),
    MpKrakOsPias("os. Piastów"),
    MpKrakowWIOSPrad6115("ul. Prądnicka"),
    MpKrakowWSSEKapi6108("ul. Swoszowice Kąpielowa 70"),
    MpKrakowWSSEPrad6102("ul. Prądnicka 76"),
    MpKrakowWSSERPod6113("ul. Rynek Podgórski"),
    MpKrakSwoszo("ul. Lusińska"),
    MpKrakTelime("ul. Telimeny 9"),
    MpKrakWadow("os. Wadów"),
    MpKrakZloRog("ul. Złoty Róg");

    public final String address;

    SensorCode(String address) {
        this.address = address;
    }
}
