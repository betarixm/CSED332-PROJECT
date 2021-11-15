package org.csed332.project.team2.metrics;

import java.io.File;

public class ProjectCodeLineMetric extends CompositeCodeLineMetric{
    public ProjectCodeLineMetric(String path){
        super(path);
    }

    public ProjectCodeLineMetric(){
        this(new File("").getAbsolutePath() + "/src/main/java");
    }
}
