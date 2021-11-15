package org.csed332.project.team2.metrics;

import java.io.IOException;
import java.io.FileReader;

public class ClassCodeLineMetric extends CodeLineMetric{

    public ClassCodeLineMetric(String path){
        super(path);
    }
    @Override
    public double calculate() {
        // TODO: implement with path of class & input stream
        try{
            int lines = 1;
            FileReader fileReader = new FileReader(this.path);
            int readByte;
            while((readByte = fileReader.read()) != -1){
                if((char)readByte == '\n') lines++;
            }
            fileReader.close();
            set(lines);

            return lines;
        }catch (IOException e){
            return -1;
        }
    }
}