package org.csed332.project.team2;

public class WarningCondition {

    public enum Mode {INCREASE,DECREASE,MORE_THAN, LESS_THAN}

    private double threshold;
    private double teta;
    private Mode mode;

    public WarningCondition(Mode mode){
        setMode(mode);
        teta = 0;
        threshold = 0;
    }
    public WarningCondition(Mode mode, double threshold){
        this(mode);
        setThreshold(threshold);
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }
    public void setThreshold(double value){
        this.threshold = value;
    }
    public void setTeta(double value){
        this.teta = value;
    }
    public Boolean shouldWarn(double oldValue, double newValue){
        switch (mode){
            case DECREASE: return oldValue - newValue >= teta;
            case INCREASE: return newValue - oldValue >= teta;
            case LESS_THAN: return newValue <= threshold;
            case MORE_THAN: return newValue >= threshold;
            default: return false;
        }
    }
}
