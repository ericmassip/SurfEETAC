package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import models.Comment;
import models.Imagen;
import models.Spot;
import models.User;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.Controller;

public class Application extends Controller {
	
	static User connected() {  //Si existe una sesión abierta, devuelve el username del usuario de la sesión, si no, devuelve nulo
		String username = session.get("user");
		
		if(username != null) {
		return User.find("byUsername", username).first();
		}
		return null;
	}
	
    public static void Start() {  //Cada vez que se reabre la aplicación, comprueba si hay una sesión abierta, si no, muestra la pantalla de login
    	
    	if(connected() != null) {
            SurfEETAC.index();
        }
    	render("@login");
    }
    
    public static void register() {
    	render();
    }
    
    public static void saveUser (
	        @Required @MinSize(6) String username, 
	        @Required @MinSize(6) String password,
	        @Required @Equals("password") String verifyPassword) {
	        
	        // Handle errors
	        if(validation.hasErrors()) {
	            render("@register");
	        }
	        List <User> allUsers = User.findAll();
	        boolean encontrado = false;
	        for(int i=0; i<allUsers.size() && encontrado == false; i++) {   //Recorre todos los usuarios y busca si ya existe un usuario con ese username, 
	        	if(allUsers.get(i).username.equals(username)){              //si es así devuelve un error
	        		encontrado = true;
	        		flash.error("This username already exists");
	        		render("@register");
	        	}
	        }
	        if(encontrado == false){ 
	        User newUser = new User(username,password).save();
	        session.put("user", newUser.username);
	        flash.success("Welcome, " + newUser.username);
	        SurfEETAC.index();
	        // Ok, display the created user
	        render(username, password);
	        }
	    }
    
    public static void SaveUserAndroid() {
    	String username = params.get("username");
    	String password = params.get("password");
        
    	User newUser = User.find("byUsername", username).first();
    	
    	if (newUser != null) {
    	if (newUser.username.equals(username)){
    		renderText("FAIL");
    	}
    	}
    	else {
    		@SuppressWarnings("unused")
			User UserNew = new User(username,password).save();
			renderText("OK");
    	}
    }
    
    public static void login() {                     //Recibe los parámetros del HTML y comprueba si existen en la base de datos, si es así, abre la página principal, si no, da un error y muestra la página de login de nuevo
    	String username = params.get("username");
    	String password = params.get("password");
    	User newUser = User.find("byUsernameAndPassword", username, password).first();
    	
    	if (newUser != null) {
    	if (newUser.username.equals(username) && newUser.password.equals(password)) {
    		session.put("user", newUser.username);
    		flash.success("Welcome, " + newUser.username);
    		SurfEETAC.index();
    	}
    	}
    	else {    		
    		flash.put("username", username);
    		flash.error("Login failed");
			render("@login");
    	}
    }
    
    public static void LoginAndroid() {
    	String username = params.get("username");
    	String password = params.get("password");
    	User newUser = User.find("byUsernameAndPassword", username, password).first();
    	
    	if (newUser != null) {
    	if (newUser.username.equals(username) && newUser.password.equals(password)) {
    		session.put("user", newUser.username);
    		renderText("OK");
    	}
    	}
    	else {
			renderText("FAIL");
    	}
    }
	
	public static void logout() {      //Cierra la sesión
        session.clear();
        Start();
    }
}