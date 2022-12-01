package com.example.mobilehotelgroup2;
import java.io.Serializable;
public class Kamar implements Serializable{
    private String id="";
    private String kodehotel="";
    private String typekamar="";
    private String tglcheckin="";
    private String tglcheckout="";
    private String hargapermalam ="";

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodeHotel() {
        return kodehotel;
    }

    public void setKodeHotel(String kodehotelotel) {
        this.kodehotel = kodehotelotel;
    }

    public String getTypeKamar() {
        return typekamar;
    }

    public void setTypeKamar(String typekamar) {
        this.typekamar = typekamar;
    }

    public String getCheckin() {
        return tglcheckin;
    }

    public void setCheckin(String tglcheckin) {
        this.tglcheckin = tglcheckin;
    }

    public String getCheckOut() {
        return tglcheckout;
    }

    public void setCheckOut (String tglcheckout) {
        this.tglcheckout = tglcheckout;
    }

    public Double getHargaPerMalam() {
        return Double.valueOf(hargapermalam);
    }

    public void setHargaPerMalam(String hargapermalam) {
        this.hargapermalam = hargapermalam;
    }

    @Override
    public String toString() {
        return "Kamar{" +
                "id='" + id + '\'' +
                ", kodehotel='" + kodehotel + '\'' +
                ", typekamar='" + typekamar + '\'' +
                ", tglcheckin='" + tglcheckin + '\'' +
                ", tglcheckout='" +  tglcheckout + '\'' +
                ", hargapermalam='" + hargapermalam + '\'' +
                '}';
    }
}
