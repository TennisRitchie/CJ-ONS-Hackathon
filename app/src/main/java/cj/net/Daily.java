package cj.net;

public class Daily {
    private double weight;
    private double muscle;
    private double fat;
    private double sleep;

    public Daily( double weight,double muscle,double fat,double sleep){
        this.weight = weight;
        this.muscle = muscle;
        this.fat = fat;
        this.sleep = sleep;
    }

    public double getWeight() {
        return weight;
    }

    public double getMuscle() {
        return muscle;
    }

    public double getFat() {
        return fat;
    }

    public double getSleep() {
        return sleep;
    }

}
