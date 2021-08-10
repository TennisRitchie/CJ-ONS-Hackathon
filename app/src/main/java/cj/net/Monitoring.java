package cj.net;
public class Monitoring {
    private double FHR;
    private double UC;
    private double FM;
    private double glucose;
    private double pressure;
    private double harmness;

    public double getFHR() {
        return FHR;
    }

    public double getUC() {
        return UC;
    }

    public double getFM() {
        return FM;
    }

    public double getGlucose() {
        return glucose;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHarmness() {
        return harmness;
    }

    public Monitoring( double FHR,double UC,double FM,double glucose,double pressure,double harmness) {
        this.FHR = FHR;
        this.UC = UC;
        this.FM = FM;
        this.glucose = glucose;
        this.pressure = pressure;
        this.harmness = harmness;
    }


    public String toString(){
        return "FHR :"+FHR + " UC : "+UC + FM + glucose + pressure + harmness;
    }
}
