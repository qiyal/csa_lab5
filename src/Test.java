public class Test {
    public static void main(String[] args) {
        String s = "VAR1 DD 48h, 45h, 4Ch, 4Ch, 4Fh, 0Ah, 0Dh, 24h";

        System.out.println(defineValueVar(s.split(" ")));
    }

    public static String defineValueVar(String [] strs) {
        String res = "";

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

        return res;
    }

    public static int getVarValueByDecimal(String value) {

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
}
