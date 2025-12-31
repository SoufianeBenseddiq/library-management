package ma.ensa.model;

import java.time.LocalDate;

public class Empreinte {
    private int id;
    private Livre livre;
    private Membre membre;
    private LocalDate dateEmpreinte;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourReelle;

    public Empreinte(Livre livre, Membre membre, LocalDate dateRetourPrevue) {
        this.livre = livre;
        this.membre = membre;
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Empreinte(int id, Livre livre, Membre membre, LocalDate dateEmpreinte, LocalDate dateRetourPrevue, LocalDate dateRetourReelle) {
        this.id = id;
        this.livre = livre;
        this.membre = membre;
        this.dateEmpreinte = dateEmpreinte;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourReelle = dateRetourReelle;
    }

    @Override
    public String toString() {
        return this.livre.toString() + " " + this.membre.toString() + " " + this.dateRetourPrevue.toString();
    }

    @Override
    public boolean equals (Object obj){
        if(!obj.getClass().equals(this.getClass())) {
            return false;
        }
        Empreinte empreinte = (Empreinte) obj;
        return livre.equals(empreinte.livre) && dateRetourPrevue.equals(empreinte.dateRetourPrevue);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public LocalDate getDateEmpreinte() {
        return dateEmpreinte;
    }

    public void setDateEmpreinte(LocalDate dateEmpreinte) {
        this.dateEmpreinte = dateEmpreinte;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourReelle() {
        return dateRetourReelle;
    }

    public void setDateRetourReelle(LocalDate dateRetourReelle) {
        this.dateRetourReelle = dateRetourReelle;
    }
}
