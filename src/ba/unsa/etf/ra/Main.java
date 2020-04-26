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



    //R TIP
    public static ArrayList<String> rTip = new ArrayList<>() {
        {
            add("add");
            add("addu");
            add("and");
            add("break");
            add("div");
            add("divu");
            add("jalr");
            add("jr");
            add("mfhi");
            add("mfl");
            add("mthi");
            add("mtlo");
            add("mult");
            add("multu");
            add("nor");
            add("or");
            add("sll");
            add("sllv");
            add("slt");
            add("sltu");
            add("sra");
            add("srav");
            add("srl");
            add("srlv");
            add("sub");
            add("subu");
            add("syscall");
            add("xor");
        }
    };

    //I TIP
    public static ArrayList<String> iTip = new ArrayList<>() {
        {
            add("addi");
            add("addiu");
            add("andi");
            add("beq");
            add("bgez");
            add("bgtz");
            add("blez");
            add("bltz");
            add("bne");
            add("lb");
            add("lbu");
            add("lh");
            add("lhu");
            add("lui");
            add("lw");
            add("lwc1");
            add("ori");
            add("sb");
            add("slti");
            add("sltiu");
            add("sh");
            add("sw");
            add("swc1");
            add("xori");
        }
    };

    //J TIP
    public static ArrayList<String> jTip = new ArrayList<>() {
        {
            add("j");
            add("jal");

        }
    };
}
