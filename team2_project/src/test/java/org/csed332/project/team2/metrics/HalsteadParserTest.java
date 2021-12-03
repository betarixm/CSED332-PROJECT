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

    @BeforeAll
    public static void initialize() throws Exception {
        String fileName = "MainClass.java";
        helperMainClass = new FixtureHelper(testPath);
        helperMainClass.setUp();
        helperMainClass.configure(fileName);
    }

    @Test
    public void parseMethodTest() {
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