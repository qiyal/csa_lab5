package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SimpleAssemblyInterpreter {
    private BufferedReader reader;

    private Map<String, String> dbHash;
    private Map<String, String> dwHash;
    private Map<String, String> ddHash;
    private List<String> storeCommands;

    private String lastVarName;
    private boolean isStop;

    private void init() {
        reader = new BufferedReader(new InputStreamReader(System.in));

        dbHash = new HashMap<>();
        dwHash = new HashMap<>();
        ddHash = new HashMap<>();
        storeCommands = new ArrayList<>();
    }

    private void inputs() throws IOException {
        String str = null;
        System.out.println("\t\t\t--- Write Assembler code ---\n");
//        int index = 1;
        do {
            if (str != null) {
                storeCommands.add(str.trim());
            }
//            System.out.print("LINE-" + (index++) + ", input: ");
            str = reader.readLine();
        } while (!str.equalsIgnoreCase("end"));
    }

    private boolean checkVariableName(String varName) {
        if (varName.length() > 31) {
            System.out.println("Length of variable must be 1 - 31!");
            return false;
        }

        if (varName.startsWith(".") && varName.substring(1).contains(".")) {
            System.out.println("Period(.) must be first character!");
            return false;
        }

        if (varName.charAt(0) <= '0' && varName.charAt(0) >= '9') {
            System.out.println("May not begin with a digit!");
            return false;
        }

        if (dwHash.containsKey(varName) || ddHash.containsKey(varName) || dbHash.containsKey(varName)) {
            System.out.println("Such a variable already exists! (" + varName + ")");
            return false;
        }

        return true;
    }

    private int getVarValueByDecimal(String value) {

        if (value.endsWith("h")) {
            return Integer.parseInt(value.substring(0, value.length() - 1), 16);
        } else if (value.endsWith("q")) {
            return Integer.parseInt(value.substring(0, value.length() - 1), 8);
        } else if (value.endsWith("b")) {
            return Integer.parseInt(value.substring(0, value.length() - 1), 2);
        } else if (value.endsWith("d")) {
            return Integer.parseInt(value.substring(0, value.length() - 1), 10);
        } else {
            return Integer.parseInt(value);
        }
    }

    private char defineDataType(String value) {
        if (value.endsWith("h")) {
            return 'h';
        } else if (value.endsWith("q")) {
            return 'q';
        } else if (value.endsWith("b")) {
            return 'b';
        } else if (value.endsWith("d")) {
            return 'd';
        } else {
            return 'D';
        }
    }

    private String convertDecimalByOtherDataTypes(int value, char type) {
        String res;

        if (type == 'h') {
            res = Integer.toString(value, 16) + "h";

            if (res.charAt(0) >= 'A' && res.charAt(0) <= 'F') {
                res = "0" + res;
            }
        } else if (type == 'q') {
            res = Integer.toString(value, 8) + "q";
        } else if (type == 'b') {
            res = Integer.toString(value, 2) + "b";
        } else if (type == 'd') {
            res = value + "d";
        } else {
            res = Integer.toString(value);
        }

        return res;
    }

    private void createVariableDB(String varName, String value) {
        if (checkVariableName(varName)) {
            dbHash.put(varName, value);
            lastVarName = varName;
        } else {
            isStop = true;
        }
    }

    private void createVariableDW(String varName, String value) {
        if (checkVariableName(varName)) {
            dwHash.put(varName, value);
            lastVarName = varName;
        } else {
            isStop = true;
        }
    }

    private void createVariableDD(String varName, String value) {
        if (checkVariableName(varName)) {
            ddHash.put(varName, value);
            lastVarName = varName;
        } else {
            isStop = true;
        }
    }

    private void addVariable(String varName, String type, String value) {
        if (type.equalsIgnoreCase("DB")) {
            createVariableDB(varName, value);
        } else if (type.equalsIgnoreCase("DW")) {
            createVariableDW(varName, value);
        } else if (type.equalsIgnoreCase("DD")) {
            createVariableDD(varName, value);
        } else {
            System.out.println("Error, not found type! (" + type + ")");
            isStop = true;
        }
    }

    private void doAdd(String [] strs) {

    }

    private void doSubOrMulOrDiv(String varNameA, String varNameB, String type) {
        String valueVarA, valueVarB;
        int typeA, typeB;
        int varA, varB;
        int res;

        if (dbHash.containsKey(varNameA)) {
            typeA = 1;
            valueVarA = dbHash.get(varNameA);
        } else if (dwHash.containsKey(varNameA)) {
            typeA = 2;
            valueVarA = dwHash.get(varNameA);
        } else if (ddHash.containsKey(varNameA)) {
            typeA = 3;
            valueVarA = ddHash.get(varNameA);
        } else {
            System.out.println("Error, not found variable! (" + varNameA + ")");
            isStop = true;
            return;
        }
        varA = getVarValueByDecimal(valueVarA);

        if (dbHash.containsKey(varNameB)) {
            typeB = 1;
            valueVarB = dbHash.get(varNameB);
        } else if (dwHash.containsKey(varNameB)) {
            typeB = 2;
            valueVarB = dwHash.get(varNameB);
        } else if (ddHash.containsKey(varNameB)) {
            typeB = 3;
            valueVarB = ddHash.get(varNameB);
        } else {
            System.out.println("Error, not found variable! (" + varNameB + ")");
            isStop = true;
            return;
        }
        varB = getVarValueByDecimal(valueVarB);

        switch (type) {
            case "SUB":
                res = varA - varB;
                break;
            case "MUL":
                res = varA * varB;
                break;
            default:
                res = varA / varB;
        }

        if (typeA == 1) {
            dbHash.put(varNameA, convertDecimalByOtherDataTypes(res, defineDataType(valueVarA)));
        } else if (typeA == 2) {
            dwHash.put(varNameA, convertDecimalByOtherDataTypes(res, defineDataType(valueVarA)));
        } else {
            ddHash.put(varNameA, convertDecimalByOtherDataTypes(res, defineDataType(valueVarA)));
        }

        lastVarName = varNameA;
    }

    private void doMov(String varNameA, String varNameB) {
        String valueVarB;

        if (dbHash.containsKey(varNameB)) {
            valueVarB = dbHash.get(varNameB);
        } else if (dwHash.containsKey(varNameB)) {
            valueVarB = dwHash.get(varNameB);
        } else if (ddHash.containsKey(varNameB)) {
            valueVarB = ddHash.get(varNameB);
        } else {
            if (varNameB.startsWith("\'") && varNameB.endsWith("\'")) {
                valueVarB = varNameB.substring(1);
                valueVarB = valueVarB.substring(0, valueVarB.length() - 1);
            } else {
                valueVarB = varNameB;
            }
        }

        if (dbHash.containsKey(varNameA.substring(0, varNameA.length() - 1))) {
            dbHash.put(varNameA, valueVarB);
        } else if (dwHash.containsKey(varNameA.substring(0, varNameA.length() - 1))) {
            dwHash.put(varNameA, valueVarB);
        } else if (ddHash.containsKey(varNameA.substring(0, varNameA.length() - 1))) {
            ddHash.put(varNameA, valueVarB);
        } else {
            System.out.println("Error, not found variable! (" + varNameA + ")");
            isStop = true;
            return;
        }
        
        lastVarName = varNameA;
    }

    private void doXchg(String varNameA, String varNameB) {
        String valueVarA, valueVarB;
        int typeA, typeB;

        if (dbHash.containsKey(varNameA)) {
            typeA = 1;
            valueVarA = dbHash.get(varNameA);
        } else if (dwHash.containsKey(varNameA)) {
            typeA = 2;
            valueVarA = dwHash.get(varNameA);
        } else if (ddHash.containsKey(varNameA)) {
            typeA = 3;
            valueVarA = ddHash.get(varNameA);
        } else {
            System.out.println("Error, not found variable! (" + varNameA + ")");
            isStop = true;
            return;
        }

        if (dbHash.containsKey(varNameB)) {
            typeB = 1;
            valueVarB = dbHash.get(varNameB);
        } else if (dwHash.containsKey(varNameB)) {
            typeB = 2;
            valueVarB = dwHash.get(varNameB);
        } else if (ddHash.containsKey(varNameB)) {
            typeB = 3;
            valueVarB = ddHash.get(varNameB);
        } else {
            System.out.println("Error, not found variable! (" + varNameB + ")");
            isStop = true;
            return;
        }

        if (typeA == 1) {
            dbHash.put(varNameA, valueVarB);
        } else if (typeA == 2) {
            dwHash.put(varNameA, valueVarB);
        } else {
            ddHash.put(varNameA, valueVarB);
        }

        if (typeB == 1) {
            dbHash.put(varNameB, valueVarA);
        } else if (typeA == 2) {
            dwHash.put(varNameB, valueVarA);
        } else {
            ddHash.put(varNameB, valueVarA);
        }

        lastVarName = varNameA;
    }

    private void doDeclareVarOrArithmeticOrDataTransfer(String [] strs) {
        switch (strs[0].toUpperCase()) {
            case "ADD":
                doAdd(strs);
                break;
            case "SUB":
                doSubOrMulOrDiv(strs[1].substring(0, strs[1].length() - 1), strs[2], "SUB");
                break;
            case "MUL":
                doSubOrMulOrDiv(strs[1].substring(0, strs[1].length() - 1), strs[2], "MUL");
                break;
            case "DIV":
                doSubOrMulOrDiv(strs[1].substring(0, strs[1].length() - 1), strs[2], "DIV");
                break;
            case "MOV":
                doMov(strs[1], strs[2]);
                break;
            case "XCHG":
                doXchg(strs[1].substring(0, strs[1].length() - 1), strs[2]);
                break;
            default:
                addVariable(strs[0], strs[1], strs[2]);
        }
    }

    private void defineDoIncOrDec(String varName, char type) {
        int a;
        String res;

        if (dbHash.containsKey(varName)) {
            a = type == '+' ? getVarValueByDecimal(dbHash.get(varName)) + 1 : getVarValueByDecimal(dbHash.get(varName)) - 1;
            res = convertDecimalByOtherDataTypes(a, defineDataType(dbHash.get(varName)));
            dbHash.put(varName, res);
        } else if (dwHash.containsKey(varName)) {
            a = type == '+' ? getVarValueByDecimal(dwHash.get(varName)) + 1 : getVarValueByDecimal(dwHash.get(varName)) - 1;
            res = convertDecimalByOtherDataTypes(a, defineDataType(dwHash.get(varName)));
            dwHash.put(varName, res);
        } else if (ddHash.containsKey(varName)) {
            a = type == '+' ? getVarValueByDecimal(ddHash.get(varName)) + 1 : getVarValueByDecimal(ddHash.get(varName)) - 1;
            res = convertDecimalByOtherDataTypes(a, defineDataType(ddHash.get(varName)));
            ddHash.put(varName, res);
        } else {
            System.out.println("Error, not found variable! (" + varName + ")");
            isStop = true;
        }

        lastVarName = varName;
    }

    private void doIncOrDec(String [] strs) {
        if (strs[0].equalsIgnoreCase("INC")) {
            defineDoIncOrDec(strs[1], '+');
        } else if (strs[0].equalsIgnoreCase("DEC")) {
            defineDoIncOrDec(strs[1], '-');
        } else {
            System.out.println("Error");
        }
    }

    private void showResult() {
        String result;

        if (dbHash.containsKey(lastVarName)) {
            result = dbHash.get(lastVarName);
        } else if (dwHash.containsKey(lastVarName)) {
            result = dwHash.get(lastVarName);
        } else {
            result = ddHash.get(lastVarName);
        }

        System.out.println(result);
    }

    public void run() throws IOException {

        init();
        inputs();

        for (String s : storeCommands) {
            String [] strs = s.split(" ");

            if (strs.length == 3) {
                doDeclareVarOrArithmeticOrDataTransfer(strs);
            } else if (strs.length == 2){
                doIncOrDec(strs);
            } else {
                System.out.println("Str length no 2, 3");
            }

            if (isStop) {
                break;
            }
        }

        showResult();
    }
}
