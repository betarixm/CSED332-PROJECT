package org.csed332.project.team2;

import java.util.Optional;

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
        threshold = value;
    }
    public void setTeta(double value){
        teta = value;
    }
    public Boolean shouldWarn(double value){
        return true;
    }
}
