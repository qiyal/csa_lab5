import java.util.Scanner;


class xcng{
    float result1;
    float result2;
    xcng(float a,float b)
    {
        result1=b;
        result2=a;
    }
}

class assembler_calculator {
    public static float add(float x, float y)
    {
        float result = x + y;
        return result;
    }
    public static float mul(float x, float y)
    {
        float result = x * y;
        return result;
    }
    public static float sub(float x, float y)
    {
        float result = x - y;
        return result;
    }
    public static float div(float x, float y)
    {
        float result = x / y;
        return result;
    }
    public static float inc(float x, float y)
    {
        float result = x +1;
        return result;
    }
    public static float dec(float x, float y)
    {
        float result = x -1;
        return result;
    }
    public static float mov(float x, float y)
    {
        float result = x;
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String first;
        String second;

        String var1;
        String var2;

        String pseudo1;
        String pseudo2;

        String dw;
        String db;
        String dd;

        String madd;
        String mmul;
        String msub;
        String mdiv;
        String minc;
        String mdec;
        String mxcng;
        String mmov;

        System.out.println("Rules: VAR_NAME PSEUDO-OPS VARIABLE");

        System.out.println("Please enter first varible");
        first = scanner.nextLine();
        String[] firstnum = first.split(" ");

        System.out.println("Please enter second varible");
        second = scanner.nextLine();
        String[] secondnum = second.split(" ");

        var1=firstnum[0];
        var2=secondnum[0];

        pseudo1=firstnum[1];
        pseudo2=secondnum[1];

        dw = "DW";
        db = "DB";
        dd = "DD";

        madd = "ADD";
        mmul = "MUL";
        msub = "SUB";
        mdiv = "DIV";
        minc = "INC";
        mdec = "DEC";
        mxcng = "XCNG";
        mmov = "MOV";


        if (pseudo1.equals(dw) && pseudo2.equals(dw))
        {
            String methodname = "";

            int lengthfirst[] = new int[firstnum.length];
            lengthfirst[2] = Integer.parseInt(firstnum[2]);
            int lengthsecond[] = new int[secondnum.length];
            lengthsecond[2] = Integer.parseInt(secondnum[2]);

            while(!methodname.equals("END"))
            {
                int firstnumber = lengthfirst[2];
                int secondnumber = lengthsecond[2];

                System.out.println("Choose a method from given list");
                System.out.println("ADD,MUL,SUB,DIV,INC,DEC,XCNG,MOV");


                methodname = scanner.nextLine();
                String method[] = methodname.split(" ");



                if (madd.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(add(firstnumber,secondnumber));
                }
                if (mmul.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(mul(firstnumber,secondnumber));
                }
                if (msub.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(sub(firstnumber,secondnumber));
                }
                if (mdiv.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(div(firstnumber,secondnumber));
                }
                if (minc.equals(method[0]) && (method[1].equals(var1)) )
                {
                    System.out.println(inc(firstnumber,secondnumber));
                }
                if (minc.equals(method[0]) && (method[1].equals(var2)) )
                {
                    System.out.println(inc(secondnumber,firstnumber));
                }
                if (mdec.equals(method[0])&& (method[1].equals(var1)) )
                {
                    System.out.println(dec(firstnumber,secondnumber));
                }
                if (mdec.equals(method[0])&& (method[1].equals(var2)) )
                {
                    System.out.println(dec(secondnumber,firstnumber));
                }
                if (mxcng.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)))
                {
                    xcng c=new xcng(firstnumber,secondnumber);
                    System.out.println(var1+" is " +c.result1);
                    System.out.println(var2+" is " +c.result2);
                }
                if (mmov.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(mov(firstnumber,secondnumber));

                }
                if (mmov.equals(method[0])&& (method[1].equals(var2))&& (method[2].equals(var1)) )
                {
                    System.out.println(mov(secondnumber,firstnumber));

                }
            }

        }

        if (pseudo1.equals(db) && pseudo2.equals(db))
        {
            String changefirst[] = new String [firstnum.length];
            changefirst[2] = String.valueOf(firstnum[2]);
            String firsttype = new String(changefirst[2]);

            String changesecond[] = new String [secondnum.length];
            changesecond[2] = String.valueOf(secondnum[2]);
            String secondtype = new String(changesecond[2]);

            String type[] ={"H","B","Q","D"};

            int num1 = 0;
            int num2 = 0;

            if (firsttype.substring(firsttype.length() - 1).equals(type[0])&&secondtype.substring(secondtype.length() - 1).equals(type[0]) )
            {
                String onlynum1 = firsttype.substring(0, firsttype.length()-1);
                String onlynum2 = secondtype.substring(0, secondtype.length()-1);
                num1 = Integer.parseInt(onlynum1,16);
                num2 = Integer.parseInt(onlynum2,16);
            }

            if (firsttype.substring(firsttype.length() - 1).equals(type[1])&&secondtype.substring(secondtype.length() - 1).equals(type[1]))
            {
                String onlynum1 = firsttype.substring(0, firsttype.length()-1);
                String onlynum2 = secondtype.substring(0, secondtype.length()-1);
                num1 = Integer.parseInt(onlynum1,2);
                num2 = Integer.parseInt(onlynum2,2);
            }

            if (firsttype.substring(firsttype.length() - 1).equals(type[2])&&secondtype.substring(secondtype.length() - 1).equals(type[2]))
            {
                String onlynum1 = firsttype.substring(0, firsttype.length()-1);
                String onlynum2 = secondtype.substring(0, secondtype.length()-1);
                num1 = Integer.parseInt(onlynum1,8);
                num2 = Integer.parseInt(onlynum2,8);
            }

            if (firsttype.substring(firsttype.length() - 1).equals(type[3])&&secondtype.substring(secondtype.length() - 1).equals(type[3]))
            {
                String onlynum1 = firsttype.substring(0, firsttype.length()-1);
                String onlynum2 = secondtype.substring(0, secondtype.length()-1);
                num1 = Integer.parseInt(onlynum1,10);
                num2 = Integer.parseInt(onlynum2,10);
            }


            String methodname = "";


            while(!methodname.equals("END"))
            {
                int firstnumber = num1;
                int secondnumber = num2;

                System.out.println("Choose a method from given list");
                System.out.println("ADD,MUL,SUB,DIV,INC,DEC,XCNG,MOV");
                methodname = scanner.nextLine();
                String method[] = methodname.split(" ");

                if (madd.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(add(firstnumber,secondnumber));
                }
                if (mmul.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(mul(firstnumber,secondnumber));
                }
                if (msub.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(sub(firstnumber,secondnumber));
                }
                if (mdiv.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(div(firstnumber,secondnumber));
                }
                if (minc.equals(method[0]) && (method[1].equals(var1)) )
                {
                    System.out.println(inc(firstnumber,secondnumber));
                }
                if (minc.equals(method[0]) && (method[1].equals(var2)) )
                {
                    System.out.println(inc(secondnumber,firstnumber));
                }
                if (mdec.equals(method[0])&& (method[1].equals(var1)) )
                {
                    System.out.println(dec(firstnumber,secondnumber));
                }
                if (mdec.equals(method[0])&& (method[1].equals(var2)) )
                {
                    System.out.println(dec(secondnumber,firstnumber));
                }
                if (mxcng.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)))
                {
                    xcng c=new xcng(firstnumber,secondnumber);
                    System.out.println(var1+" is " +c.result1);
                    System.out.println(var2+" is " +c.result2);
                }
                if (mmov.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(mov(firstnumber,secondnumber));

                }
                if (mmov.equals(method[0])&& (method[1].equals(var2))&& (method[2].equals(var1)) )
                {
                    System.out.println(mov(secondnumber,firstnumber));

                }

            }
        }

        if (pseudo1.equals(dd) && pseudo2.equals(dd))
        {
            float changefirst[] = new float[firstnum.length];
            changefirst[2] = Float.parseFloat(firstnum[2]);
            float changesecond[] = new float[secondnum.length];
            changesecond[2] = Float.parseFloat(secondnum[2]);

            String methodname = "";


            while(!methodname.equals("END"))
            {
                float firstnumber = changefirst[2];
                float secondnumber = changesecond[2];

                System.out.println("Choose a method from given list");
                System.out.println("ADD,MUL,SUB,DIV,INC,DEC,XCNG,MOV");
                methodname = scanner.nextLine();
                String method[] = methodname.split(" ");


                if (madd.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(add(firstnumber,secondnumber));
                }
                if (mmul.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(mul(firstnumber,secondnumber));
                }
                if (msub.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(sub(firstnumber,secondnumber));
                }
                if (mdiv.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(div(firstnumber,secondnumber));
                }
                if (minc.equals(method[0]) && (method[1].equals(var1)) )
                {
                    System.out.println(inc(firstnumber,secondnumber));
                }
                if (minc.equals(method[0]) && (method[1].equals(var2)) )
                {
                    System.out.println(inc(secondnumber,firstnumber));
                }
                if (mdec.equals(method[0])&& (method[1].equals(var1)) )
                {
                    System.out.println(dec(firstnumber,secondnumber));
                }
                if (mdec.equals(method[0])&& (method[1].equals(var2)) )
                {
                    System.out.println(dec(secondnumber,firstnumber));
                }
                if (mxcng.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)))
                {
                    xcng c=new xcng(firstnumber,secondnumber);
                    System.out.println(var1+" is " +c.result1);
                    System.out.println(var2+" is " +c.result2);
                }
                if (mmov.equals(method[0])&& (method[1].equals(var1))&& (method[2].equals(var2)) )
                {
                    System.out.println(mov(firstnumber,secondnumber));

                }
                if (mmov.equals(method[0])&& (method[1].equals(var2))&& (method[2].equals(var1)) )
                {
                    System.out.println(mov(secondnumber,firstnumber));

                }

            }
        }
        else {
            System.out.println("Sorry NO similar to DD,DW,DB");
        }
    }
}
