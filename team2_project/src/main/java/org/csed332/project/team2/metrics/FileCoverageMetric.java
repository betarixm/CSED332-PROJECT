/*******************************************************************************
 * Copyright (c) 2009, 2021 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.csed332.project.team2.metrics;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICoverageVisitor;
import org.jacoco.core.data.ExecutionDataStore;

/**
 * This example reads Java class files, directories or JARs given as program
 * arguments and dumps information about the classes.
 */
public final class FileCoverageMetric implements ICoverageVisitor {

    // streaming object for printing metric results
    private final PrintStream out;

    // jacoco analyzer which is attached to the JVM
    private final Analyzer analyzer;

    // history container for the future usage
    // private final ArrayList<HashMap<String, String>> history;
    /**
     * Creates a new example instance printing to the given stream.
     *
     * @param out
     *            stream for outputs
     */
    public FileCoverageMetric(final PrintStream out) {
        this.out = out;
        analyzer = new Analyzer(new ExecutionDataStore(), this);
        // history = new ArrayList<>();
    }

    /**
     * Run this example with the given parameters.
     *
     * @param args
     *            command line parameters
     * @throws IOException
     *             in case of error reading a input file
     */
    public void execute(final String[] args) throws IOException {
//        history.clear();
        for (final String file : args) {
            System.out.println(analyzer.analyzeAll(new File(file)));
        }
    }

    // visitor method for execution
    public void visitCoverage(final IClassCoverage coverage) {
        out.printf("class name:   %s%n", coverage.getName());
        out.printf("class id:     %016x%n", Long.valueOf(coverage.getId()));
        out.printf("instructions: %s%n", Integer
                .valueOf(coverage.getInstructionCounter().getTotalCount()));
        out.printf("branches:     %s%n",
                Integer.valueOf(coverage.getBranchCounter().getTotalCount()));
        out.printf("lines:        %s%n",
                Integer.valueOf(coverage.getLineCounter().getTotalCount()));
        out.printf("methods:      %s%n",
                Integer.valueOf(coverage.getMethodCounter().getTotalCount()));
        out.printf("complexity:   %s%n%n", Integer
                .valueOf(coverage.getComplexityCounter().getTotalCount()));
        HashMap<String, String> tmp = new HashMap<>();
        tmp.put("id", Long.valueOf(coverage.getId()).toString());
        tmp.put("instructions", Long.valueOf(coverage.getBranchCounter().getTotalCount()).toString());
        tmp.put("branches", Long.valueOf(coverage.getBranchCounter().getTotalCount()).toString());
        tmp.put("lines", Long.valueOf(coverage.getLineCounter().getTotalCount()).toString());
        tmp.put("methods", Long.valueOf(coverage.getMethodCounter().getTotalCount()).toString());
        tmp.put("complexity", Long.valueOf(coverage.getComplexityCounter().getTotalCount()).toString());

        // history.add(tmp);
    }

    // This is for debuggin purpose only
    //    /**
    //     * Entry point to run this examples as a Java application.
    //     *
    //     * @param args
    //     *            list of program arguments
    //     * @throws IOException
    //     *             in case of errors executing the example
    //     */
    //    public static void main(final String[] args) throws IOException {
    //        new ClassInfo(System.out).execute(new String[]{"/Users/anna2/subin/lectures_loc/2021F/csed332/project/sample/jacoco/gradle/wrapper"});
    //        new ClassInfo(System.out).execute(new String[]{"/Users/anna2/subin/lectures_loc/2021F/csed332/submitted_hw"});
    //    }

    // automated runner for a gradle script
    // still working ...
//    public void runTest(String fileName){
//        GradleConnector connector = GradleConnector.newConnector();
//        connector.forProjectDirectory(new File(fileName));
//        ProjectConnection connection = connector.connect();
//        try {
//            BuildLauncher launcher = connection.newBuild();
//            launcher.forTasks("build");
//            launcher.run();
//        } finally {
//            connection.close();
//        }
//    }

}