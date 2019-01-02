package main.Tools;

import main.ast.CodeGenerationVisitor;
import main.ast.Type.ArrayType.ArrayType;
import main.ast.Type.PrimitiveType.BooleanType;
import main.ast.Type.PrimitiveType.IntType;
import main.ast.Type.PrimitiveType.StringType;
import main.ast.Type.Type;
import main.ast.Type.UserDefinedType.UserDefinedType;
import main.ast.node.declaration.ClassDeclaration;
import main.ast.node.declaration.MethodDeclaration;
import main.ast.node.declaration.VarDeclaration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class CodeGenerator {
    private static final String stackSize = "100";
    private static final String outputPath = "./output/";
    public static String generateCode(ClassDeclaration classDeclaration)
    {
        String code = ".class public " + classDeclaration.getName().getName() + "\n";
        code += ".super java/lang/Object\n\n";

        ArrayList<VarDeclaration> varDeclarations = classDeclaration.getVarDeclarations();
        for (VarDeclaration varDeclaration : varDeclarations) {
            code += varDeclaration.getCode() + "\n";
        }
        code += "\n";
        code += ".method public <init>()V\n" +
                "   aload_0 ; push this\n" +
                "   invokespecial java/lang/Object/<init>()V ; call super\n" +
                "   return\n" +
                ".end method";

        ArrayList<MethodDeclaration> methodDeclarations = classDeclaration.getMethodDeclarations();
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            code += "\n\n" + methodDeclaration.getCode() + "\n\n";
        }

        classDeclaration.setCode(code);
        return code;
    }
    public static String generateCode(VarDeclaration varDeclaration)
    {
        String code = ".field protected " + varDeclaration.getIdentifier().getName()
                + " " + generateCode(varDeclaration.getType()) ;

        varDeclaration.setCode(code);
        return code ;
    }
    public static String generateCode(MethodDeclaration methodDeclaration)
    {

        String returnTypeCode = generateCode(methodDeclaration.getReturnType());
        String methodName = methodDeclaration.getName().getName();
        String args = "";
        String staticy = "";
        if(CodeGenerationVisitor.inMain)
        {
            returnTypeCode = "V";
            args = "[Ljava/lang/String;";
            staticy = "static ";
        }
        String code = ".method public " + staticy + methodName +"("+args+ ")" + returnTypeCode +"\n";
        code += "   .limit stack " + stackSize  +"\n" +
                "   .limit locals "+ stackSize  +"\n";

        // TODO : Statements
        code += "   return\n";
        code += ".end method\n";

        //TODO : adding arguments and var declarations

        methodDeclaration.setCode(code);
        return code;
    }
    private static String generateCode(Type type)
    {
        String code = "";
        if (type instanceof StringType)
            code = "Ljava/lang/String;";
        else if (type instanceof IntType)
            code = "I";
        else if (type instanceof BooleanType)
            code = "Z";
        else if (type instanceof ArrayType)
            code = "[I";
        else if (type instanceof UserDefinedType)
            code = "L"+((UserDefinedType) type).getName().getName();
        type.setCode(code);
        return code;
    }
    public static void jasminFileCreator(String code, String className)
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + className + ".j"));

            writer.write(code);
            writer.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

    }

}
