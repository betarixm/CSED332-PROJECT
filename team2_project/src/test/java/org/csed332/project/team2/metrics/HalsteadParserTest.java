package org.csed332.project.team2.metrics;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.metrics.halstead.HalsteadParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HalsteadParserTest {
    private static final String testPath = "testdata/singleFiles";
    private static FixtureHelper helperMainClass;
    private static FixtureHelper helperSimpleAddition;

    private static FixtureHelper setUpFixture(String fileName) throws Exception {
        FixtureHelper fixtureHelper = new FixtureHelper(testPath);
        fixtureHelper.setUp();
        fixtureHelper.configure(fileName);
        return fixtureHelper;
    }

    @BeforeAll
    public static void initialize() throws Exception {
        helperSimpleAddition = setUpFixture( "SimpleAddition.java");
        helperMainClass = setUpFixture("MainClass.java");
    }

    @Test
    public void parseMethodSimpleAddition() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperSimpleAddition.getFixture().getProject();
                            final PsiClass psiClass = helperSimpleAddition.getFirstPsiClass();

                            PsiMethod psiMethod = psiClass.getAllMethods()[0];
                            HalsteadParser halsteadParser = new HalsteadParser();
                            halsteadParser.parse(psiMethod);

                            Assertions.assertEquals(0, 0);
                        });
    }

    @Test
    public void parseMethodMainClass() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();

                            PsiMethod psiMethod = psiClass.getAllMethods()[0];
                            HalsteadParser halsteadParser = new HalsteadParser();
                            halsteadParser.parse(psiMethod);

                            Assertions.assertEquals(0, 0);
                        });
    }
}