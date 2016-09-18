 package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model{
	
	@Required
    @MaxSize(15)
    @MinSize(4)
    @Match(value="^\\w*$", message="Not a valid username")
    public String username;
	
	@Required
    @MaxSize(15)
    @MinSize(5)
    public String password;
	
	public boolean isAdmin;
	
	public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
	
	public String toString()  {
        return "User(" + username + ")";
    }
	
	public static User connect(String username, String password) {
	    return find("byUsernameAndPassword", username, password).first();
	}
}
