package ma.ensa.model;

import java.time.LocalDate;

public class Membre implements Comparable<Membre> {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    LocalDate dateInscription;

    @Override
    public int compareTo(Membre o) {
        if (nom.compareTo(o.nom)==0) {
            return prenom.compareTo(o.prenom);
        }
        return nom.compareTo(o.nom);
    }

    @Override
    public boolean equals(Object obj){
        if(! obj.getClass().equals(this.getClass())){
            return false;
        }
        Membre m = (Membre) obj;
        return nom.equals(m.nom);
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }
}
