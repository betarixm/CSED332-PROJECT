package org.csed332.project.team2.metrics.halstead;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.FixtureHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HalsteadParserTest {
    private static final String testPath = "TestProjects/SingleFiles";
    private static FixtureHelper helperMainClass;

    @BeforeAll
    public static void initialize() throws Exception {
        helperMainClass = FixtureHelper.getInstance();
        helperMainClass.changeFile(testPath);
        helperMainClass.setUp();
    }

    @AfterAll
    public static void dispose() throws Exception {
        helperMainClass.tearDown();
    }

    private void configureFixture(String fileName) throws Exception {
        helperMainClass.configure(fileName);
    }

    @Test
    public void parseMethodOneOperator() throws Exception {
        configureFixture("SimpleInt.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();

                            PsiMethod psiMethod = psiClass.getAllMethods()[0];
                            HalsteadParser halsteadParser = new HalsteadParser();
                            halsteadParser.parse(psiMethod);

                            Assertions.assertEquals(8, halsteadParser.getHalsteadVisitor().getNumberOfTotalOperands());
                            Assertions.assertEquals(8, halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperands());
                            Assertions.assertEquals(2, halsteadParser.getHalsteadVisitor().getNumberOfTotalOperators());
                            Assertions.assertEquals(1, halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperators());
                        });
    }

    @Test
    public void parseMethodSimpleAddition() throws Exception {
        configureFixture("SimpleAddition.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();

                            PsiMethod psiMethod = psiClass.getAllMethods()[0];
                            HalsteadParser halsteadParser = new HalsteadParser();
                            halsteadParser.parse(psiMethod);

                            Assertions.assertEquals(11, halsteadParser.getHalsteadVisitor().getNumberOfTotalOperands());
                            Assertions.assertEquals(8, halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperands());
                            Assertions.assertEquals(4, halsteadParser.getHalsteadVisitor().getNumberOfTotalOperators());
                            Assertions.assertEquals(2, halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperators());
                        });
    }

    @Test
    public void parseMethodMainClass() throws Exception {
        configureFixture("MainClass.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();

                            PsiMethod psiMethod = psiClass.getAllMethods()[0];
                            HalsteadParser halsteadParser = new HalsteadParser();
                            halsteadParser.parse(psiMethod);

                            Assertions.assertEquals(8, halsteadParser.getHalsteadVisitor().getNumberOfTotalOperands());
                            Assertions.assertEquals(8, halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperands());
                            Assertions.assertEquals(0, halsteadParser.getHalsteadVisitor().getNumberOfTotalOperators());
                            Assertions.assertEquals(0, halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperators());
                        });
    }
}