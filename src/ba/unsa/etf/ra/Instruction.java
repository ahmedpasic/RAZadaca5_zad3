package ba.unsa.etf.ra;

public class Instruction {

    private String label, naziv, odredisni, izvorni1, izvorni2, imm, tip;
    private boolean taken;

    {
        taken = false;
    }

    public String getCijelaInstrukcija() {
        return cijelaInstrukcija;
    }

    public void setCijelaInstrukcija(String cijelaInstrukcija) {
        this.cijelaInstrukcija = cijelaInstrukcija;
    }

    private String cijelaInstrukcija;

    public Instruction(String label, String naziv, String odredisni, String izvorni1, String izvorni2, String imm, String tip, String cijelaInstrukcija) {
        this.label = label;
        this.naziv = naziv;
        this.odredisni = odredisni;
        this.izvorni1 = izvorni1;
        this.izvorni2 = izvorni2;
        this.imm = imm;
        this.tip = tip;
        this.cijelaInstrukcija = cijelaInstrukcija;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOdredisni() {
        return odredisni;
    }

    public void setOdredisni(String odredisni) {
        this.odredisni = odredisni;
    }

    public String getIzvorni1() {
        return izvorni1;
    }

    public void setIzvorni1(String izvorni1) {
        this.izvorni1 = izvorni1;
    }

    public String getIzvorni2() {
        return izvorni2;
    }

    public void setIzvorni2(String izvorni2) {
        this.izvorni2 = izvorni2;
    }

    public String getImm() {
        return imm;
    }

    public void setImm(String imm) {
        this.imm = imm;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }


    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public boolean isStore() {
        return naziv.equals("SW") || naziv.equals("SB") || naziv.equals("SWC1");
    }
}
