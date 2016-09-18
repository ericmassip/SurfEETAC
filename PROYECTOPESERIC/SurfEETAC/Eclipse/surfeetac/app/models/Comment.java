package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
 
@Entity
public class Comment extends Model {
 
    public String author;
    
    public Date postedAt;
     
    @Lob
    public String content;
    
    @ManyToOne
    public Imagen foto;
    
    public Comment(Imagen foto, String author, String content) {
        this.foto = foto;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }
 
}