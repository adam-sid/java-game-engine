package edu.uob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StrangeTest {

    static ArrayList<String> methodNames = new ArrayList();
    static ArrayList<String> classNames = new ArrayList();
    static String errorTag = "[\u001b[31mERROR\u001b[0m]";
    static String warningTag = "[\u001b[93mWARN\u001b[0m]";
    static String infoTag = "[\u001b[34mINFO\u001b[0m]";
    static String horizontalRule = " \u001b[1m" + "-".repeat(72) + "\u001b[0m";
    static String mavenUsageTag = "\u001b[36mmvnw exec:java@strange -Dexec.args=<source-file>\u001b[0m";
    static String javaUsageTag = "\u001b[36mjava Strange <source-file>\u001b[0m";
    static String rootPathTag;

    public static void execute(String[] var0) {
        if (var0.length != 1) {
            System.out.println(errorTag + " Please specify a source file by passing in a command line argument");
            System.out.println(errorTag + " If you are running strange via maven, please use: " + mavenUsageTag);
            System.out.println(errorTag + " If you are running strange with java, please use: " + javaUsageTag);
            System.exit(1);
        } else {
            String var1 = var0[0];
            System.out.println(infoTag + horizontalRule);
            System.out.println(infoTag + " Looking for illegal constructs in " + var1 + "...");
            System.out.println(infoTag + horizontalRule);
            int var3 = 1;
            boolean var4 = false;

            try {
                BufferedReader var5 = new BufferedReader(new FileReader(var1));

                String var2;
                while((var2 = var5.readLine()) != null) {
                    if (looksLikeClassDefinintion(var2)) {
                        classNames.add(extractClassName(var2));
                    }

                    if (looksLikeMethodDeclaration(var2)) {
                        methodNames.add(extractMethodName(var2));
                    }
                }

                var5.close();

                for(var5 = new BufferedReader(new FileReader(var1)); (var2 = var5.readLine()) != null; ++var3) {
                    if (var2.contains("interface ")) {
                        var4 = true;
                    }

                    if (var2.contains("}")) {
                        var4 = false;
                    }

                    if (!var4 && !var2.trim().startsWith("//")) {
                        var2 = scrub(var2);
                        if (var2.replace(" ", "").contains("\"+")) {
                            printWarning(var3, "possible string additions", var2);
                        }

                        if (var2.replace(" ", "").contains("+\"")) {
                            printWarning(var3, "possible string additions", var2);
                        }

                        if (var2.contains("+") && var2.contains("=") && var2.startsWith("String ")) {
                            printWarning(var3, "possible string additions", var2);
                        }

                        if (var2.contains("ArrayList")) {
                            printWarning(var3, "possible use of ArrayList", var2);
                        }

                        if (var2.matches(".*\\[\\d*\\].*") && !var2.contains("args")) {
                            printWarning(var3, "possible use of an array ", var2);
                        }

                        if (var2.matches(".*=.*\\?.*\\:.*")) {
                            printWarning(var3, "possible usage of ternary", var2);
                        }

                        if (var2.contains("->")) {
                            printWarning(var3, "possible usage of lambdas", var2);
                        }

                        if (isUnqualifiedMethodCall(var2)) {
                            printWarning(var3, "possible unqualified call", var2);
                        }
                    }
                }

                var5.close();
            } catch (IOException var7) {
                String var10001 = errorTag;
                System.out.println(var10001 + " " + String.valueOf(var7));
                System.out.println(errorTag + " Could not find the specified source code file: \u001b[36m" + var0[0] + "\u001b[0m");
                if (!var0[0].startsWith("src/main/java")) {
                    System.out.println(errorTag + " Specified file path should begin with: " + rootPathTag);
                }

                if (!var0[0].endsWith(".java")) {
                    System.out.println(errorTag + " Specified file should be a .java file");
                }

                System.exit(1);
            }
        }

    }

    public static boolean looksLikeClassDefinintion(String var0) {
        if (var0.trim().startsWith("//")) {
            return false;
        } else if (var0.trim().startsWith("/*")) {
            return false;
        } else {
            return var0.contains("class ");
        }
    }

    public static String extractClassName(String var0) {
        var0 = var0 + " ";
        int var1 = var0.indexOf("class ") + 6;
        int var2 = var0.indexOf(" ", var1);
        return var0.substring(var1, var2);
    }

    public static boolean isUnqualifiedMethodCall(String var0) {
        if (looksLikeMethodDeclaration(var0)) {
            return false;
        } else {
            for(int var1 = 0; var1 < methodNames.size(); ++var1) {
                if (!classNames.contains(methodNames.get(var1))) {
                    if (var0.startsWith((String)methodNames.get(var1) + "(")) {
                        return true;
                    }

                    if (var0.contains(" " + (String)methodNames.get(var1) + "(")) {
                        return true;
                    }

                    if (var0.contains("=" + (String)methodNames.get(var1) + "(")) {
                        return true;
                    }

                    if (var0.contains("+" + (String)methodNames.get(var1) + "(")) {
                        return true;
                    }

                    if (var0.contains("-" + (String)methodNames.get(var1) + "(")) {
                        return true;
                    }

                    if (var0.contains("->" + (String)methodNames.get(var1) + "(")) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static String scrub(String var0) {
        var0 = var0.trim();

        for(var0 = var0.replace("\t", " "); var0.contains("  "); var0 = var0.replace("  ", " ")) {
        }

        var0 = strikeOutStringLiterals(var0);
        var0 = var0.replace(" (", "(");
        var0 = var0.replace("++", "");
        var0 = var0.replace("+=", "");
        return var0;
    }

    public static String strikeOutStringLiterals(String var0) {
        var0 = var0.replace("\\\"", "");
        String[] var1 = var0.split("\"");

        for(int var2 = 1; var2 < var1.length; var2 += 2) {
            var1[var2] = "";
        }

        return String.join("\"", var1);
    }

    public static boolean looksLikeMethodDeclaration(String var0) {
        if (!var0.contains("(")) {
            return false;
        } else if (var0.endsWith(";")) {
            return false;
        } else if (var0.trim().startsWith("//")) {
            return false;
        } else if (var0.trim().startsWith("}")) {
            return false;
        } else if (var0.split("\\(").length > 2) {
            return false;
        } else {
            var0 = var0.replace("public ", " ");
            var0 = var0.replace("protected ", " ");
            var0 = var0.replace("private ", " ");
            var0 = var0.replace("final ", " ");
            var0 = var0.replace("static ", " ");
            var0 = var0.replace("return ", " ");

            for(var0 = var0.replace("\t", " "); var0.contains("  "); var0 = var0.replace("  ", " ")) {
            }

            var0 = var0.trim();
            String[] var1 = var0.split("\\(")[0].split(" ");
            if (var1.length > 2) {
                return false;
            } else if (var1.length < 2) {
                return false;
            } else if (var1[0].equals("new")) {
                return false;
            } else if (var1[1].length() < 3) {
                return false;
            } else {
                return !isInvalidMethodName(var1[1]);
            }
        }
    }

    public static boolean isInvalidMethodName(String var0) {
        var0 = var0.trim();
        if (!Character.isJavaIdentifierStart(var0.charAt(0))) {
            return true;
        } else {
            for(int var1 = 1; var1 < var0.length(); ++var1) {
                if (!Character.isJavaIdentifierPart(var0.charAt(var1))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static String extractMethodName(String var0) {
        while(var0.contains("  ")) {
            var0 = var0.replace("  ", " ");
        }

        var0 = var0.replace(" (", "(");
        int var1 = var0.indexOf("(");
        int var2 = var0.lastIndexOf(" ", var1);
        return var0.substring(var2 + 1, var1);
    }

    public static void printWarning(int var0, String var1, String var2) {
        for(var1 = var1 + ":"; var1.length() < 27; var1 = var1 + " ") {
        }

        if (var2.length() > 100) {
            var2 = var2.substring(0, 100) + "...";
        }

        System.out.println(warningTag + " Line " + var0 + ": " + var1 + var2.trim());
    }

    static {
        rootPathTag = "\u001b[36msrc" + File.separator + "main" + File.separator + "java" + File.separator + "...\u001b[0m";
    }
}
