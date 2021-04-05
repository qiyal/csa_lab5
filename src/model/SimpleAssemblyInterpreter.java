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
    private Set<String> varIsStringSet;
    private List<String> storeCommands;

    private String lastVarValue;
    private boolean isStop;

    private void init() {
        reader = new BufferedReader(new InputStreamReader(System.in));

        dbHash = new HashMap<>();
        dwHash = new HashMap<>();
        ddHash = new HashMap<>();
        varIsStringSet = new HashSet<>();
        storeCommands = new ArrayList<>();
    }

    private void inputs() throws IOException {
        String str = null;
        System.out.println("\t\t\t--- Write Assembler code ---\n");

        do {
            if (str != null) {
                storeCommands.add(str.trim());
            }
            str = reader.readLine();
        } while (!str.equalsIgnoreCase("end"));
    }

    private boolean checkVariableName(String varName) {
        if (varName.length() > 31) {
            System.out.println("Length of variable must be 1 - 31!");
            return false;
        }

        if (varName.startsWith(".") && varName.substring(1).contains(".") || !varName.startsWith(".") && varName.substring(1).contains(".")) {
            System.out.println("Period(.) must be first character!");
            return false;
        }

        if (varName.charAt(0) <= '0' && varName.charAt(0) >= '9') {
            System.out.println("May not begin with a digit!");
            return false;
        }

        if (varName.contains("&")) {
            System.out.println("may consist of special characters.");
            return false;
        }

        if (dwHash.containsKey(varName) || ddHash.containsKey(varName) || dbHash.containsKey(varName)) {
            System.out.println("Such a variable already exists! (" + varName + ")");
            return false;
        }

        return true;
    }

    private String defineValueVar(String [] strs) {
        String res = "";

        if (strs.length == 3 && strs[2].startsWith("'") && strs[2].endsWith("'")) {
            varIsStringSet.add(strs[0]);
            return strs[2].substring(1, strs[2].length() - 1);
        }

        if (strs.length == 3) {
            return strs[2];
        }

        for (int i = 2; i < strs.length; i++) {
            String s = strs[i];

            if (s.startsWith("'") && s.endsWith("'")) {
                res += s.substring(1, s.length() - 1);
            } else if (s.startsWith("'") && s.endsWith("',")) {
                res += s.substring(1, s.length() - 2);
            } else if (!s.startsWith("'") && s.endsWith("',")) {
                res += " " + s.substring(0, s.length() - 2);
            } else if (!s.startsWith("'") && s.endsWith(",")) {
                char ch = (char) getVarValueByDecimal(s.substring(0, s.length() - 1));
                res += ch;
            } else if (s.startsWith("'")) {
                res += s.substring(1);
            } else if (s.endsWith("'")) {
                res += " " + s.substring(0, s.length() - 1);
            } else {
                char ch = (char) getVarValueByDecimal(s);
                res += ch;
            }
        }

        varIsStringSet.add(strs[0]);
        return res;
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

    private void createVariable(String varName, String value, String map) {
        if (checkVariableName(varName)) {

            if (map.equals("DB")) {
                dbHash.put(varName, value);
            } else if (map.equals("DW")) {
                dwHash.put(varName, value);
            } else {
                ddHash.put(varName, value);
            }

            lastVarValue = value;
        } else {
            isStop = true;
        }
    }

    private void addVariable(String varName, String type, String value) {
        if (type.equalsIgnoreCase("DB")) {
            createVariable(varName, value, "DB");
        } else if (type.equalsIgnoreCase("DW")) {
            createVariable(varName, value, "DW");
        } else if (type.equalsIgnoreCase("DD")) {
            createVariable(varName, value, "DD");
        } else {
            System.out.println("Error, not found type! (" + type + ")");
            isStop = true;
        }
    }

    private void doAdd(String varNameA, String varNameB) {
        String valueVarA, valueVarB;
        int typeA;
        int varA = -1, varB = -1;
        int res = -1;
        boolean isStringA = false;
        boolean isStringB = false;
        boolean havVarA = true;
        boolean havVarB = true;

        if (dbHash.containsKey(varNameA.substring(0, varNameA.length() - 1))) {
            typeA = 1;
            valueVarA = dbHash.get(varNameA.substring(0, varNameA.length() - 1));
        } else if (dwHash.containsKey(varNameA.substring(0, varNameA.length() - 1))) {
            typeA = 2;
            valueVarA = dwHash.get(varNameA.substring(0, varNameA.length() - 1));
        } else if (ddHash.containsKey(varNameA.substring(0, varNameA.length() - 1))) {
            typeA = 3;
            valueVarA = ddHash.get(varNameA.substring(0, varNameA.length() - 1));
        } else if (varNameA.startsWith("'") && varNameA.endsWith("',")) {
            isStringA = true;
            valueVarA = varNameA.substring(1, varNameA.length() - 2);
            havVarA = false;
            typeA = 0;
        } else {
            valueVarA = varNameA.substring(0, varNameA.length() - 1);
            havVarA = false;
            typeA = 0;
        }

        if (!isStringA && !varIsStringSet.contains(varNameA.substring(0, varNameA.length() - 1))) {
            varA = getVarValueByDecimal(valueVarA);
        } else {
            isStringA = true;
        }

        if (dbHash.containsKey(varNameB)) {
            valueVarB = dbHash.get(varNameB);
        } else if (dwHash.containsKey(varNameB)) {
            valueVarB = dwHash.get(varNameB);
        } else if (ddHash.containsKey(varNameB)) {
            valueVarB = ddHash.get(varNameB);
        } else if (varNameB.startsWith("'") && varNameB.endsWith("'")) {
            havVarB = false;
            isStringB = true;
            valueVarB = varNameB.substring(1, varNameB.length() - 1);
        } else {
            havVarB = false;
            valueVarB = varNameB;
        }

        if (!isStringB && !varIsStringSet.contains(valueVarB)) {
            varB = getVarValueByDecimal(valueVarB);
        } else {
            isStringB = true;
        }

        if (isStringA || isStringB) {
            if (isStringA && isStringB) {
                valueVarA += valueVarB;
            } else if (isStringA) {
                valueVarA += varB;
            } else if (isStringB) {
                valueVarA = varA + valueVarB;
            }
            varIsStringSet.add(varNameA.substring(0, varNameA.length() - 1));
        } else {
            res = varA + varB;
        }

        if (typeA == 1) {
            lastVarValue = convertDecimalByOtherDataTypes(res, defineDataType(dbHash.get(varNameA.substring(0, varNameA.length() - 1))));
            dbHash.put(varNameA.substring(0, varNameA.length() - 1), isStringA || isStringB ? valueVarA : lastVarValue);
//            System.out.println("ADD: " + lastVarValue);
//            System.out.println("GET: " + dbHash.get(varNameA.substring(0, varNameA.length() - 1)));
//            System.out.println("NAME" + varNameA);
        } else if (typeA == 2) {
            lastVarValue = convertDecimalByOtherDataTypes(res, defineDataType(dwHash.get(varNameA.substring(0, varNameA.length() - 1))));
            dwHash.put(varNameA.substring(0, varNameA.length() - 1), isStringA || isStringB ? valueVarA : lastVarValue);
        } else if (typeA == 3) {
            lastVarValue = convertDecimalByOtherDataTypes(res, defineDataType(ddHash.get(varNameA.substring(0, varNameA.length() - 1))));
            ddHash.put(varNameA.substring(0, varNameA.length() - 1), isStringA || isStringB ? valueVarA : lastVarValue);
        } else {
            if (isStringA || isStringB) {
                lastVarValue = valueVarA;
            } else {
                lastVarValue = convertDecimalByOtherDataTypes(res, defineDataType(varNameA.substring(0, varNameA.length() - 1)));
            }
        }
    }

    private void doSubOrMulOrDiv(String varNameA, String varNameB, String type) {
        String valueVarA, valueVarB;
        int typeA;
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
            typeA = 0;
            valueVarA = varNameA;
        }
        varA = getVarValueByDecimal(valueVarA);

        if (dbHash.containsKey(varNameB)) {
            valueVarB = dbHash.get(varNameB);
        } else if (dwHash.containsKey(varNameB)) {
            valueVarB = dwHash.get(varNameB);
        } else if (ddHash.containsKey(varNameB)) {
            valueVarB = ddHash.get(varNameB);
        } else {
            valueVarB = varNameB;
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
        } else if (typeA == 3){
            ddHash.put(varNameA, convertDecimalByOtherDataTypes(res, defineDataType(valueVarA)));
        } else {
            lastVarValue = convertDecimalByOtherDataTypes(res, defineDataType(varNameA));
            return;
        }

        lastVarValue = convertDecimalByOtherDataTypes(res, defineDataType(valueVarA));
    }

    private void doMov(String varNameA, String varNameB) {
        String valueVarB;
        boolean isVarALetterOrString = varIsStringSet.contains(varNameA);
        boolean isVarBLetterOrString = false;

        if (dbHash.containsKey(varNameB)) {
            valueVarB = dbHash.get(varNameB);
            isVarBLetterOrString = varIsStringSet.contains(varNameB);
        } else if (dwHash.containsKey(varNameB)) {
            valueVarB = dwHash.get(varNameB);
            isVarBLetterOrString = varIsStringSet.contains(varNameB);
        } else if (ddHash.containsKey(varNameB)) {
            valueVarB = ddHash.get(varNameB);
            isVarBLetterOrString = varIsStringSet.contains(varNameB);
        } else if (varNameB.startsWith("\'") && varNameB.endsWith("\'")) {
            valueVarB = varNameB.substring(1, varNameB.length() - 1);
            isVarBLetterOrString = true;
        } else {
            valueVarB = varNameB;
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

        if (isVarALetterOrString && !isVarBLetterOrString) {
            // S -> N
            varIsStringSet.remove(varNameA);
        }

        if (isVarALetterOrString && !isVarBLetterOrString) {
            // N -> S
            varIsStringSet.add(varNameA);
        }

        lastVarValue = valueVarB;
    }

    private void doXchg(String varNameA, String varNameB) {
        String valueVarA, valueVarB;
        int typeA, typeB;
        boolean isVarALetterOrString = varIsStringSet.contains(varNameA);
        boolean isVarBLetterOrString = varIsStringSet.contains(varNameB);

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

        // for varA
        if (isVarALetterOrString && !isVarBLetterOrString) {
            // S -> N
            varIsStringSet.remove(varNameA);
        }

        if (isVarALetterOrString && !isVarBLetterOrString) {
            // N -> S
            varIsStringSet.add(varNameA);
        }

        // for varB
        if (isVarBLetterOrString && !isVarALetterOrString) {
            varIsStringSet.remove(varNameB);
        }

        if (isVarBLetterOrString && !isVarALetterOrString) {
            varIsStringSet.add(varNameB);
        }

        lastVarValue = valueVarB;
    }

    private void doDeclareVarOrArithmeticOrDataTransfer(String [] strs) {
        switch (strs[0].toUpperCase()) {
            case "ADD":
                doAdd(strs[1], strs[2]);
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
                addVariable(strs[0], strs[1], defineValueVar(strs));
        }
    }

    private void defineDoIncOrDec(String varName, char type) {
        int a;
        String res = null;

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

        lastVarValue = res;
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

    public void run() throws IOException {
        init();
        inputs();

        for (String s : storeCommands) {
            String [] strs = s.split(" ");

            if (strs.length >= 3) {
                doDeclareVarOrArithmeticOrDataTransfer(strs);
            } else if (strs.length == 2){
                doIncOrDec(strs);
            } else {
                System.out.println("Error, Str length not  == 2, >= 3!");
            }

            if (isStop) {
                break;
            }
        }

        System.out.println(lastVarValue);
    }
}
