package org.csed332.project.team2.metrics.halstead;

public class HalsteadMetric {
    private int numberUniqueOperators = 0;
    private int numberUniqueOperands = 0;
    private int numberTotalOperators = 0;
    private int numberTotaleOperands = 0;

    public HalsteadMetric(int n1, int n2, int N1, int N2){
        numberUniqueOperators = n1;
        numberUniqueOperands = n2;
        numberTotalOperators = N1;
        numberTotaleOperands = N2;
    }

    public int getVocabulary() {
        return numberUniqueOperators + numberUniqueOperands;
    }

    public int getSize(){
        return numberTotalOperators + numberTotaleOperands;
    }

    public double getVolume() {
        return (numberTotalOperators + numberTotaleOperands)*(Math.log(numberUniqueOperators + numberUniqueOperands)/Math.log(2));
    }

    public double getDifficulty() {
        return ((double)numberUniqueOperators/2)*((double)numberTotalOperators/numberUniqueOperands);
    }

    public double getEfforts() {
        double volume = getVolume();
        double difficulty = getDifficulty();

        return volume * difficulty;
    }

    public double getErrors() {
        double volume = getVolume();
        return volume/3000.0;
    }

    public double getTestingTime() {
        double effort = getEfforts();
        // 18 seconds
        return effort/18;
    }
}
