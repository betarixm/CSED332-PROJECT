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
    private final String fakeClassName = "A";

    public HalsteadParser() {
        this.halsteadVisitor = new HalsteadVisitor();
    }

    public void parse(PsiMethod method) {
        ASTParser parser = ASTParser.newParser(AST.JLS13);
        String fakeClassString = "public class " + fakeClassName + " {" + method.getText() + "}";

        parser.setSource(fakeClassString.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        Map options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        cu.accept(this.halsteadVisitor);

        this.halsteadVisitor.operands.remove(fakeClassName);
        this.halsteadVisitor.operands.remove(method.getName());

        int distinctOperators = this.halsteadVisitor.operators.size();
        int distinctOperands = this.halsteadVisitor.operands.size();
    }


    public HalsteadVisitor getHalsteadVisitor() {
        return halsteadVisitor;
    }
}
