package ba.unsa.etf.ra;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) throws IOException {

        Scanner ulaz = new Scanner(System.in);

        System.out.println("Unesite put do datoteke");
        String put = ulaz.nextLine();

        ArrayList<Instruction> sekvenca = dajSekvencuIzDatoteke(put);

        if(provjeriPotrebuInstrZadrske(sekvenca)==false){
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("instrukcijeZadrske.txt"));
                bw.write("S obzirom da nema niti jedne instrukcije grananja, instrukcije zadrške nisu potrebne.");
                bw.close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<Instruction, Instruction> parovi = dajInstrukcijeZadrske(sekvenca);

        zapisiInstrukcijeZadrske(parovi);

        System.out.println("Odredišna datoteka je: instrukcijeZadrske.txt");
    }

    private static void zapisiInstrukcijeZadrske(Map<Instruction,Instruction> parovi) {
        if (parovi != null) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("instrukcijeZadrske.txt"));
                for (Map.Entry<Instruction, Instruction> par : parovi.entrySet()) {
                    Instruction inst = par.getKey();
                    bw.write(inst.getCijelaInstrukcija());

                    if (par.getValue() == null) {
                        bw.write("-----> nema instrukciju zadrske");
                    } else
                        bw.write("----->" + par.getValue().getCijelaInstrukcija());

                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static Map<Instruction, Instruction> dajInstrukcijeZadrske(ArrayList<Instruction> sekvenca) {
        Map<Instruction, Instruction> parovi = new HashMap<Instruction, Instruction>();
        Instruction zadrska=null;
        for(Instruction in : sekvenca){
            if(in.getNaziv().equals("BEQ") || in.getNaziv().equals("BNE")){
                zadrska = dajInstrNakonInstrGrananjaZadrska(in, sekvenca);
                if (zadrska!=null) parovi.put(in,zadrska);
                if(zadrska==null){ //slucaj B
                    //zadrska = funkcijaKojaDajeInstrZaSlucajB
                    if(zadrska!=null) parovi.put(in,zadrska);
                }
                if(zadrska==null){
                    //zadrska= funkcijaKojaDajeInstrZaSlucajA
                    if(zadrska!=null) parovi.put(in,zadrska);
                }
                if(zadrska==null){
                    //Ako je nakon 3 ispitana slucaja nije moguce naci zadrsku onda napisati u datoteku da za tu instrukciju
                    //nije moguce naci zadrsku, trebalo bi znaci par instrucija, i instrukcija zadrske u kojoj pise da nije moguce naci zadrsku
                }
            }
        }
        return parovi;
    }
    private static Instruction dajInstrNakonInstrGrananjaZadrska(Instruction instrukcija, ArrayList<Instruction> sekvenca){
        Instruction instr = null;
        for(int i=0;i<sekvenca.size();i++){
            if(sekvenca.get(i).getCijelaInstrukcija().equals(instrukcija.getCijelaInstrukcija())){
                if(i+1==sekvenca.size()) return null; //ako je zadnja instrukcija, onda nema instrukcije iza nje
                //ako je iza beq ili bne onda one ne bismo trebali koristiti kao instrukcije zadrske
                if(sekvenca.get(i+1).getNaziv().equals("BEQ") || sekvenca.get(i+1).getNaziv().equals("BNE")) return null;
                instr = sekvenca.get(i+1);
                int j=0;
                while(j<sekvenca.size()){
                    if(sekvenca.get(j).getLabel()==null){
                        j++;// && sekvenca.get(j).getLabel()!=null || !sekvenca.get(j).getLabel().equals(instrukcija.getIzvorni2()) )) j++;
                    }
                    else if(sekvenca.get(j).getLabel().equals(instrukcija.getIzvorni2())) break;
                    else j++;
                }
                if(j==sekvenca.size()) return null; //dosli smo do kraja, a nema labele
                while(j<sekvenca.size() && sekvenca.get(j).getCijelaInstrukcija()!=instrukcija.getCijelaInstrukcija()){
                    if( (sekvenca.get(j).getIzvorni1()!=null && sekvenca.get(j).getIzvorni1().equals(instr.getOdredisni())) || (
                            sekvenca.get(j).getIzvorni2()!=null && sekvenca.get(j).getIzvorni2().equals(instr.getOdredisni()) )) return null;
                    j++;
                }
                if(j==sekvenca.size()) i=sekvenca.size();
            }
        }
        return instr;
    }

    private static ArrayList<Instruction> dajSekvencuIzDatoteke(String put) throws IOException {

        ArrayList<Instruction> sekvenca = new ArrayList<>();
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(put));
            String red = br.readLine();
            while(red!=null) {
                sekvenca.add(dajInstrukcijuIzStringa(red));
                red = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
            br.close();
        }
        br.close();
        return sekvenca;

    }

    private static Instruction dajInstrukcijuIzStringa(String red) {
        String label=null, naziv=null, odredisni=null, izvorni1=null, izvorni2=null, imm=null, tip=null;

        List<String> dijelovi = Arrays.asList(red.split(" "));

        if(dijelovi.get(0).contains(":")){
            label = dijelovi.get(0).toUpperCase().substring(0,dijelovi.get(0).length()-1);
            naziv = dijelovi.get(1).trim().toUpperCase();//sve u velika tako da je podrzano citanje instrukcija i sa malim i sa velikim slovima
            if(rTip.contains(naziv)){  //ako je R tip u pitanju
                odredisni = dijelovi.get(2).trim().toUpperCase().substring(0,dijelovi.get(2).length()-1);
                izvorni1 = dijelovi.get(3).trim().toUpperCase().substring(0,dijelovi.get(3).length()-1);
                izvorni2 = dijelovi.get(4).trim().toUpperCase();
                imm=null;
                tip = "R";
            }
            else if(iTip.contains(naziv)){ // ako je I tip u pitanju
                if(naziv.toUpperCase().equals("LW") || naziv.toUpperCase().equals("SW")){
                    odredisni=null;
                    izvorni1=dijelovi.get(2).trim().toUpperCase().substring(0,dijelovi.get(2).length()-1);
                    izvorni2= dajRegistarLwSw(dijelovi.get(3).toUpperCase());
                }
                else{
                    odredisni = dijelovi.get(2).trim().toUpperCase().substring(0,dijelovi.get(2).length()-1);
                    izvorni1 = dijelovi.get(3).trim().toUpperCase().substring(0,dijelovi.get(3).length()-1);
                    izvorni2=null;
                    imm = dijelovi.get(4).trim().toUpperCase();
                    if(naziv.equals("BEQ") || naziv.equals("BNE")){
                        izvorni2=dijelovi.get(4).trim().toUpperCase();
                        imm=null;
                    }
                }
                tip="I";
            }
            else if(jTip.contains(naziv)){ // ako je J tip u pitanju
                odredisni = dijelovi.get(2).trim().toUpperCase().substring(0,dijelovi.get(2).length()-1);
                izvorni1=null;
                izvorni2=null;
                imm=null;
                tip = "J";
            }
            else{ // AKO NEMA INSTRUKCIJE NAPISATI U DATOTEKU DA NE VALJA SEKVENCA I PREKINUTI SA RADOM
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("instrukcijeZadrske.txt"));
                    bw.write("S obzirom da nema niti jedne instrukcije grananja, instrukcije zadrške nisu potrebne.");
                    bw.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {//ako prvi dio nema dvotacku onda nemamo labelu i prvi dio je onda ustvari naziv instrukcije
            label = null;
            naziv = dijelovi.get(0).toUpperCase();
            if(rTip.contains(naziv)){  //ako je R tip u pitanju
                odredisni = dijelovi.get(1).trim().toUpperCase().substring(0,dijelovi.get(1).length()-1);
                izvorni1 = dijelovi.get(2).trim().toUpperCase().substring(0,dijelovi.get(2).length()-1);
                izvorni2 = dijelovi.get(3).trim().toUpperCase();
                imm=null;
                tip = "R";
            }
            else if(iTip.contains(naziv)){ // ako je I tip u pitanju
                if(naziv.toUpperCase().equals("LW") || naziv.toUpperCase().equals("SW")){
                    odredisni=null;
                    izvorni1=dijelovi.get(1).trim().toUpperCase().substring(0,dijelovi.get(1).length()-1);
                    izvorni2= dajRegistarLwSw(dijelovi.get(2).toUpperCase());
                }
                else{
                    odredisni = dijelovi.get(1).trim().toUpperCase().substring(0,dijelovi.get(1).length()-1);
                    izvorni1 = dijelovi.get(2).trim().toUpperCase().substring(0,dijelovi.get(2).length()-1);
                    izvorni2=null;
                    imm = dijelovi.get(3).trim().toUpperCase();
                    if(naziv.equals("BEQ") || naziv.equals("BNE")){
                        izvorni2=dijelovi.get(3).trim().toUpperCase();
                        imm=null;
                    }
                }
                tip="I";
            }
            else if(jTip.contains(naziv)){ // ako je J tip u pitanju
                odredisni = dijelovi.get(1).trim().toUpperCase().substring(0,dijelovi.get(1).length()-1);
                izvorni1=null;
                izvorni2=null;
                imm=null;
                tip = "J";
            }
            else{ // AKO NEMA INSTRUKCIJE NAPISATI U DATOTEKU DA NE VALJA SEKVENCA I PREKINUTI SA RADOM
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("instrukcijeZadrske.txt"));
                    bw.write("S obzirom da nema niti jedne instrukcije grananja, instrukcije zadrške nisu potrebne.");
                    bw.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new Instruction(label, naziv, odredisni, izvorni1, izvorni2, imm, tip, red.toUpperCase());
    }

    public static String dajRegistarLwSw(String nizSlova){
        String registar="";
        for(int i=0;i<nizSlova.length();i++){
            if(nizSlova.charAt(i)=='('){
                i++;
              while(nizSlova.charAt(i)!=')'){
                  registar+=nizSlova.charAt(i);
                  i++;
              }
            }
        }
        return registar.toUpperCase();
    }

    public static boolean provjeriPotrebuInstrZadrske(ArrayList<Instruction> sekvenca){
        for(Instruction instr : sekvenca){
            if(instr.getNaziv().equals("BEQ") || instr.getNaziv().equals("BNE"))
                return true;
        }
        return false;
    }

    //R TIP
    public static ArrayList<String> rTip1 = new ArrayList<>() {
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
    public static ArrayList<String> rTip = rTip1.stream().map(e -> e.toUpperCase()).collect(Collectors.toCollection(ArrayList::new));
    //I TIP
    public static ArrayList<String> iTip1 = new ArrayList<>() {
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
    public static ArrayList<String> iTip = iTip1.stream().map(e -> e.toUpperCase()).collect(Collectors.toCollection(ArrayList::new));
    //J TIP
    public static ArrayList<String> jTip1 = new ArrayList<>() {
        {
            add("j");
            add("jal");

        }
    };
    public static ArrayList<String> jTip = jTip1.stream().map(e -> e.toUpperCase()).collect(Collectors.toCollection(ArrayList::new));
}
