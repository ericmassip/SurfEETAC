package models;

import java.util.List;

import javax.persistence.*;

import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Spot extends Model {
	
	public String name;
	
	public String place;
	
	@ManyToOne
	public User surfer;
    
    @Lob
    public String description;
    
    public Blob foto;
    
    public String path;
    
    public boolean isAdmin;
    
    public Spot() {}
    
    public Spot(String name, String description, Blob foto, String path) {
    	this.name = name;
        this.description = description;
        this.foto = foto;
        this.path = path;
    }
}