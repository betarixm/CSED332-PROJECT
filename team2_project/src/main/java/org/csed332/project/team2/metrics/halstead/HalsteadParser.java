package org.csed332.project.team2.metrics.halstead;

import com.intellij.psi.PsiMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

// TODO: parse method... with ASTParser
//  then visit all nodes to count the number of total operators
public class HalsteadParser {
    private HalsteadVisitor halsteadVisitor;

    public HalsteadParser() {
        this.halsteadVisitor = new HalsteadVisitor();
    }

    public void parse(PsiMethod method) {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        String fakeClassText = "public class A {" + method.getText() + "}";
        parser.setSource(fakeClassText.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        cu.accept(this.halsteadVisitor);
    }


    public HalsteadVisitor getHalsteadVisitor() {
        return halsteadVisitor;
    }
}
