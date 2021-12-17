package org.csed332.project.team2.metrics.halstead;

import com.intellij.psi.PsiMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Map;

/**
 * Parser for Halstead metric.
 */
public class HalsteadParser {
    private HalsteadVisitor halsteadVisitor;

    /**
     * Instantiates a new HalsteadParser.
     */
    public HalsteadParser() {
        this.halsteadVisitor = new HalsteadVisitor();
    }

    /**
     * Parse given method code by using HalsteadVisitor.
     *
     * @param method the PsiMethod object
     */
    public void parse(PsiMethod method) {
        ASTParser parser = ASTParser.newParser(AST.JLS14);
        String fakeClassName = "A";
        String fakeClassString = "public class " + fakeClassName + " {" + method.getText() + "}";

        parser.setSource(fakeClassString.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        cu.accept(this.halsteadVisitor);
    }


    /**
     * Gets HalsteadVisitor.
     *
     * @return the HalsteadVisitor object
     */
    public HalsteadVisitor getHalsteadVisitor() {
        return halsteadVisitor;
    }
}
