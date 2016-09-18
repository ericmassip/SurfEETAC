package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Comment;
import models.Imagen;
import models.Spot;
import models.User;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.Before;

public class SurfEETAC extends Application {
	
	@Before
    static void checkUser() {               //Antes de cada petición HTTP, comprueba si existe una sesión de un usuario 
        if(connected() == null) {
            flash.error("Please log in first");
            login();
        }
    }
	
	public static void index() 
	
	{            //Página principal, pasa al index.html la última imagen subida y una lista de las diez últimas imágenes subidas
		Imagen frontImagen = Imagen.find("order by postedAt desc").first();
        List<Imagen> olderImagenes = Imagen.find(
            "order by postedAt desc"
        ).from(1).fetch(10);
        
        render(frontImagen, olderImagenes);
    }
	
	public static void GoToSpots() {        //Muestra la página GoToSpots.html y le pasa la lista con los spots del administrador (que son todos)
		User admin = User.find("byUsername", "admin").first();
		List<Spot> AllSpots = Spot.find("bySurfer", admin).fetch();
		render(AllSpots);	
	}
	
	public static void GoToSpotsAndroid() {
		User admin = User.find("byUsername", "admin").first();
		List<Spot> spots = Spot.find("bySurfer", admin).fetch();
		renderXml(spots);
	}
	
	public static void GoToSelectedSpots(@Required String place) {    //Recibe el valor de place, un string escogido por el usuario
		if (validation.hasErrors()) {
			flash.error("All fields are required!");
			GoToSpots();
	    }
		else {
			User admin = User.find("byUsername", "admin").first();         
			List<Spot> AllSpots = Spot.find("bySurfer", admin).fetch();
			List<Spot> selectedSpots = new ArrayList<Spot>();
			for(int i=0; i<AllSpots.size(); i++) {                    //Recorre todos los spots y añade a la lista creada selectedSpots, 
				if (AllSpots.get(i).place.equals(place)) {            //los spots que contienen el place pasado al método
					selectedSpots.add(AllSpots.get(i));
				}
			}
		render(selectedSpots);                              //Muestra la página GoToSelectedSpots y le pasa la lista selectedSpots
		}
	}
    
    public static void imagenPhoto(long id) {           //Muestra la imagen con el id pasado al método
 	   final Imagen imagen = Imagen.findById(id);
 	   notFoundIfNull(imagen);
 	   response.setContentTypeIfNotSet(imagen.photo.type());
 	   renderBinary(imagen.photo.get());
 	}

	public static void deleteImagen(String title) {              
		User currentUser = connected();
		List <Imagen> imagenes = Imagen.find("author", currentUser).fetch();
		boolean encontrado = false;
		for(int i=0; i<imagenes.size() && encontrado == false; i++){     //Recorre la lista de imágenes del user y si el título de alguna 
			if(imagenes.get(i).title.equals(title)){                     //coincide con el string pasado al método, la borra
				   encontrado = true;
				   Imagen imagen = imagenes.get(i);
				   imagen.photo.getFile().delete();
				   imagen.delete();
				   GoToMyProfile();
			}
		}
		if(encontrado == false){
			GoToMyProfile();
		}
	}
	
	public static void addImagenWithFileName(@Required File photo, 
			@Required String titleImg, 
			@Required String content) throws FileNotFoundException {
		
		if(validation.hasErrors()) {
            flash.error("All fields are required!");
			GoToMyProfile();
        }
		
		User currentUser = connected();                    
		final Imagen imagen = new Imagen();                   //Crea una nueva imagen y le pone los mismos valores pasados 
		   imagen.author = currentUser;                       //al método además de la fecha y de la foto 
		   imagen.photoFileName = photo.getName();
		   imagen.title = titleImg;
		   imagen.content = content;
		   imagen.postedAt = new Date();
		   imagen.photo = new Blob();
		   imagen.photo.set(new FileInputStream(photo), MimeTypes.getContentType(photo.getName()));
		   imagen.save();
		   GoToMyProfile();
		}
	
	public static void openImagenPhoto(long id) {            //Si el usuario clica en la foto, se le abre la URL de la imagen en el navegador
		   final Imagen imagen = Imagen.findById(id);
		   notFoundIfNull(imagen);
		   response.setContentTypeIfNotSet(imagen.photo.type());
		   renderBinary(imagen.photo.get());
		}
	
	public static void downloadImagen(String title) {        
		   Imagen imagen = Imagen.find("byTitle", title).first();
		   //notFoundIfNull(imagen);
		   if (imagen == null) {
			   flash.error("Image not found");
			   index();
		   }
		   response.setContentTypeIfNotSet(imagen.photo.type());
		   renderBinary(imagen.photo.get(), imagen.photoFileName);
		}
	
	public static void setSpot (String path) throws FileNotFoundException{      
		Spot spot = Spot.find("byPath",path).first();
		File photo = new File(path);                      //Busca el fichero con el path pasado al método
		spot.foto = new Blob();                           
		
		spot.foto.set(new FileInputStream(photo), MimeTypes.getContentType(photo.getName()));   //Le asigna el fichero anterior a spot.foto que es de tipo Blob
		response.setContentTypeIfNotSet(spot.foto.type());
		
		if(spot.foto.getFile().exists()){                     //Si el fichero utilizado existe, lo muestra en la web
		renderBinary(spot.foto.get());
		}
		else
		{
			System.out.println("fail");
		}
	}
	
	public static void anadirSpot (Spot spot){           //Al clicar en la imagen de un spot, se activa este método para añadirlo a MySpots
		User currentUser = connected();
			List <Spot> spots = Spot.find("bySurfer", currentUser).fetch();
		    boolean encontrado = false;
		    for(int i=0; i<spots.size() && encontrado == false; i++){      //Recorre todos los spots para ver si el usuario ya tiene este spot en MySpots,
		    	if(spots.get(i).name.equals(spot.name)){                   //si es así, se para la búsqueda y se muestran de nuevo todos los spots
		    		encontrado = true;
		    	}
		    }
		    if(encontrado == false){                          //Si no es así, crea un newSpot y le asigna los mismos valores que tenía
			final Spot newSpot = new Spot();                  //el spot pasado al método, pero le asigna el user actual a newSpot.surfer
			newSpot.surfer = currentUser;
			newSpot.name = spot.name;
			newSpot.description = spot.description;
			newSpot.place = spot.place;
			newSpot.foto = spot.foto;
			newSpot.path = spot.path;
			newSpot.save();
		    }
		GoToSpots();
	}
	
	public static void anadirSpotAndroid (){           //Al clicar en la imagen de un spot, se activa este método para añadirlo a MySpots
		String spotname = params.get("spotname");
		
		User admin = User.find("byUsername", "admin").first();
		@SuppressWarnings("unused")
		List<Spot> AllSpots = Spot.find("bySurfer", admin).fetch();
		//Spot spot = AllSpots.find("byName",spotname);
		
		User currentUser = connected();
			List <Spot> spots = Spot.find("bySurfer", currentUser).fetch();
		    boolean encontrado = false;
		    for(int i=0; i<spots.size() && encontrado == false; i++){      //Recorre todos los spots para ver si el usuario ya tiene este spot en MySpots,
		    	if(spots.get(i).name.equals(spotname)){                   //si es así, se para la búsqueda y se muestran de nuevo todos los spots
		    		encontrado = true;
		    	}
		    }
		    if(encontrado == false){                          //Si no es así, crea un newSpot y le asigna los mismos valores que tenía
			final Spot newSpot = new Spot();                  //el spot pasado al método, pero le asigna el user actual a newSpot.surfer
			newSpot.surfer = currentUser;
			//newSpot.name = spot.name;
			//newSpot.description = spot.description;
			newSpot.save();
			renderText("OK");
	    	}
	    	else {
				renderText("FAIL");
	    	}
		    
	}
	
	public static void anadirSpotselected(Spot spot) {              //Este método es igual que el anterior anadirSpot pero en este caso, 
			User currentUser = connected();                                    //añades el spot desde la página GoToSelectedSpots.html
			List <Spot> spots = Spot.find("bySurfer", currentUser).fetch();
			boolean encontrado = false;
			for(int i=0; i<spots.size() && encontrado == false; i++){
				if(spots.get(i).name.equals(spot.name)){
					encontrado = true;
				}
			}
			if(encontrado == false){
				final Spot newSpot = new Spot();
				newSpot.surfer = currentUser;
				newSpot.name = spot.name;
				newSpot.description = spot.description;
				newSpot.place = spot.place;
				newSpot.foto = spot.foto;
				newSpot.path = spot.path;
				newSpot.save();
    		}
    	GoToSelectedSpots(spot.place);
	}
	
	public static void borrarSpot (String name) {
		User currentUser = connected();
		List <Spot> spots = Spot.find("bySurfer", currentUser).fetch();
	    boolean encontrado = false;
	    for(int i=0; i<spots.size() && encontrado == false; i++){      //Busca si el name de alguno de los spots del usuario coincide con el name
	    	if(spots.get(i).name.equals(name)){                        //pasado al método
	    		encontrado = true;
	    		Spot spot = spots.get(i);
	    		spot.delete();
	    		GoToMySpots();
	    	}
		}
	    if(encontrado == false){
			GoToMySpots();
		}
	}
	
	public static void borrarSpotAndroid () {
		String name = params.get("spotname");
		User currentUser = connected();
		List <Spot> spots = Spot.find("bySurfer", currentUser).fetch();
	    boolean encontrado = false;
	    for(int i=0; i<spots.size() && encontrado == false; i++){      //Busca si el name de alguno de los spots del usuario coincide con el name
	    	if(spots.get(i).name.equals(name)){                        //pasado al método
	    		encontrado = true;
	    		Spot spot = spots.get(i);
	    		spot.delete();
	    		GoToMySpots();
	    		renderText("OK");
	    	}
	    	else {
				renderText("FAIL");
	    	}
	    }
	}
	
	public static void GoToMySpots (){
		User currentUser = connected();
			List<Spot> MySpots = Spot.find("bySurfer", currentUser).fetch();     //Crea la lista de spots del usuario actual y la pasa
			if(MySpots != null){                                                 //a la página GoToMySpots.html
				render(MySpots);	
			}
	}
	
	public static void GoToMySpotsAndroid (){
		User currentUser = connected();
			List<Spot> MySpots = Spot.find("bySurfer", currentUser).fetch();
			if(MySpots != null){
				renderXml(MySpots);	
			}
	}
	
	public static void GoToMyProfile (){                                             //Crea la lista de imágenes del usuario actual y la pasa
		User currentUser = connected();                                             //a la página GoToMySpots.html
			List<Imagen> MyImages = Imagen.find("byAuthor", currentUser).fetch();
			if(MyImages != null){
				render(MyImages);	
			}
			else{
				index();
			}
		}
	
	public static void GoToAProfile (Long id){            //Al clicar encima de una de las fotos de la página principal se abre este método,
		User currentUser = connected();                   //se le pasa el id de la foto clicada y éste busca qué usuario es el propietario de esa imagen
		Imagen frontImagen = Imagen.findById(id);
		User ThatUser = frontImagen.author;
		if(currentUser == ThatUser) {    //Si el usuario clica en una de sus fotos, se abre la página de su mismo perfil
			GoToMyProfile();
		}
		else{
			List<Imagen> HisImages = Imagen.find("byAuthor", ThatUser).fetch();  //Busca los spots y las imágenes del usuario en cuestión y,
			List<Spot> HisSpots = Spot.find("bySurfer", ThatUser).fetch();       //si no son nulas las listas, las muestra en la página GoToAProfile.html
			if(HisImages != null){
				render(HisImages, HisSpots, ThatUser);	
			}
			else{
				index();
			}
		}
	}
	
	public static void GoToHisProfile (@Required String username) {      //Éste metodo es igual que el anterior GoToAProfile, pero éste
		User currentUser = connected();                                  //recibe el username del usuario por medio de un textbox del index.html
		if (validation.hasErrors()) {
			flash.error("All fields are required!");
			index();
	    }
		else {
			User ThatUser = User.find("byUsername", username).first();
			if(ThatUser == null) {
				flash.error("The User you're looking for doesn't exist");
				index();
			}
			else if (ThatUser == currentUser) {
				GoToMyProfile();
			}
			else {
				List<Imagen> HisImages = Imagen.find("byAuthor", ThatUser).fetch();
				List<Spot> HisSpots = Spot.find("bySurfer", ThatUser).fetch();
				if(HisImages != null){
					render(HisImages, HisSpots, ThatUser);	
				}
				else{
					index();
				}
			}
		}
	}
	
	public static void GoToPrevisiones(){
		render();
	}
	
	public static void GoToVideos(){
		render();
	}
	
	public static void comment(Long id){
		Imagen frontImagen = Imagen.findById(id);
		
		render(frontImagen, id);
	}
	
	public static void postComment(@Required Long id, @Required String content) {   //Cuando clicamos Submit a Comment, 
		User currentUser = connected();                                             //si no hay errores, se añade el comentario
		if(id != null){                                                             //a la imagen encontrada gracias al id pasado
			Imagen frontImagen = Imagen.findById(id);
			if (validation.hasErrors()) {
		        render("SurfEETAC/comment.html", frontImagen);
		    }
		    frontImagen.addComment(currentUser.username, content);
		    flash.success("Thanks for posting %s", currentUser.username);
		    index();
		}
		else{
			flash.error("An error ocurred, click on Post a Comment again if you'd like to");
			index();
		}
	}
}

