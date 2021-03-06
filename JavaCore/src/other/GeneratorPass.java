package other;

import java.util.*;

public class GeneratorPass {
    private String allSimb;

    public static void main(String[] args) {
        GeneratorPass pass = new GeneratorPass();
        GeneratorPass pass2 = new GeneratorPass("123");
        System.out.println(pass.getPass(12));
        System.out.println(pass2.getPass(12));
    }

    GeneratorPass(String str) {
        allSimb = str;
    }

    GeneratorPass(){
        allSimb = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    }

    public String getPass(int len) {
        StringBuilder sb = new StringBuilder();

        ArrayList<String> allSimblList =new ArrayList<>(Arrays.asList(allSimb.split("")));

        // if length string is less then length pass
        while (allSimblList.size() < len) {
            allSimblList.addAll(Arrays.asList(allSimb.split("")));
        }

        Collections.shuffle(allSimblList);

        for (String s: allSimblList.subList(0, len)) {
            sb.append(s);
        }

        return sb.toString();
    } // getPass
}
