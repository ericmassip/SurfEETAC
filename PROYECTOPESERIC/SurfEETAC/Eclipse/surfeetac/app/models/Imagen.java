package models;

import java.util.*;

import javax.persistence.*;

import play.data.validation.Required;
import play.db.jpa.*;

@Entity
public class Imagen extends Model {
	
	@Required
    @ManyToOne
    public User author;
	
	public String title;
	
	public String photoFileName;
	
	public Date postedAt;
	
	public Blob photo;
	
	@Lob
    public String content;
	
	public Imagen() {}
	
	@OneToMany(mappedBy="foto", cascade=CascadeType.ALL)
    public List<Comment> comments;
	
	public Imagen (User author, String title, String photoFileName, Blob photo, String content){
		this.comments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.photoFileName = photoFileName;
		this.postedAt = new Date();
		this.photo = photo;
		this.content = content;
	}
	
	public Imagen addComment(String author, String content) {
        Comment newComment = new Comment(this, author, content).save();
        this.comments.add(newComment);
        this.save();
        return this;
	}
}
