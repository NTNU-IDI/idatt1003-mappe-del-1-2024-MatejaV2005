package Oeving_6;
import java.lang.Math;

public class F1 {
    public static void main(String[] args) {
        Tabell a = new Tabell();
        int[] antall = a.tabell();

        for (int i = 0; i < 1000; i++) {
            int randNum = a.Tall();
            antall[randNum]++;
        }

        for (int j = 0; j < antall.length; j++) {
            int num = Math.round((float) antall[j] / 10);
            System.out.println(j + ": " + antall[j] + " " + ("*").repeat(num)); // skjer en avrundingsfeil for en eller annen grunn

        }

    }
}

