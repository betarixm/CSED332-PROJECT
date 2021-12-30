package org.csed332.project.team2.metrics.halstead;

import com.intellij.psi.PsiMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Map;

// TODO: parse method... with ASTParser
//  then visit all nodes to count the number of total operators
public class HalsteadParser {
    private HalsteadVisitor halsteadVisitor;

    public HalsteadParser() {
        this.halsteadVisitor = new HalsteadVisitor();
    }

    public void parse(PsiMethod method) {
        ASTParser parser = ASTParser.newParser(AST.JLS14);
        // TODO: use another "unique" className that's hopefully not used, e.g. methodName + randomId
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


    public HalsteadVisitor getHalsteadVisitor() {
        return halsteadVisitor;
    }
}
