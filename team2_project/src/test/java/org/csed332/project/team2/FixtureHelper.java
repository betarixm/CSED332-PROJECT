package org.csed332.project.team2;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.testFramework.fixtures.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FixtureHelper {
    private  String testPath;
    private CodeInsightTestFixture fixture;
    private static FixtureHelper Instance=null;

    private FixtureHelper() {}

    public static FixtureHelper getInstance() {
        if (Instance == null) {
            Instance = new FixtureHelper();
        }
        return Instance;
    }

    public void changeFile(String testPath) {
        this.testPath = new File("src/test/resources/" + testPath).getAbsolutePath();
    }

    public void setUp() {
        final TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder("test2");
        fixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.getFixture());
        try {
            fixture.setUp();
        } catch (Exception e) {
        }
        fixture.setTestDataPath(testPath);
    }

    public void tearDown() throws Exception {
        fixture.tearDown();
        fixture = null;
    }

    public PsiFile configure(String clsName) throws IOException {
        String content = getContent(clsName);
        return fixture.configureByText(clsName, content);
    }

    public String getTestDataPath(String clsName) {
        return testPath + "/" + clsName;
    }

    private String getContent(String clsName) throws IOException {
        return String.join(" ", Files.readAllLines(Paths.get(getTestDataPath(clsName))));
    }

    public CodeInsightTestFixture getFixture() {
        return fixture;
    }

    public PsiFile getPsiFile() {
        return fixture.getFile();
    }

    public PsiClass getFirstPsiClass() {
        PsiFile psiFile = this.getPsiFile();
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        final PsiClass[] classes = psiJavaFile.getClasses();
        final PsiClass psiClass = classes[0];
        return psiClass;
    }

    public PsiClass getPsiClass(String Name) {
        PsiFile psiFile = this.getPsiFile();
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        final PsiClass[] classes = psiJavaFile.getClasses();

        for (PsiClass aClass : classes) {
            if (aClass.getName().equals(Name)) {
                return aClass;
            }
        }
        return classes[0];
    }
}
