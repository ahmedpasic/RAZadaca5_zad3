package ba.unsa.etf.ra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner ulaz = new Scanner(System.in);

        System.out.println("Unesite put do datoteke");
        String put = ulaz.nextLine();

        ArrayList<Instruction> sekvenca = dajSekvencuIzDatoteke(put);
    }

    private static ArrayList<Instruction> dajSekvencuIzDatoteke(String put) {

        ArrayList<Instruction> sekvenca = new ArrayList<>();

        try {

            BufferedReader br = new BufferedReader(new FileReader(put));
            String red = br.readLine();
            sekvenca.add(dajInstrukcijuIzStringa(red));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Instruction dajInstrukcijuIzStringa(String red) {
        String label, naziv, odredisni, izvorni1, izvorni2, imm;
        InstructionType instructionType;

        ArrayList<String> dijelovi = (ArrayList<String>) Arrays.asList(red.split(" "));

        if(dijelovi.get(0).contains(":")){
            label = dijelovi.get(0);
            naziv = dijelovi.get(1).trim().toUpperCase();//sve u velika tako da je podrzano citanje instrukcija i sa malim i sa velikim slovima
        }
        else {//ako prvi dio nema dvotacku onda nemamo labelu i prvi dio je onda ustvari naziv instrukcije
            label = null;
            naziv = dijelovi.get(0);
        }





        return new Instruction(label, naziv, odredisni, izvorni1, izvorni2, imm);
    }
}
