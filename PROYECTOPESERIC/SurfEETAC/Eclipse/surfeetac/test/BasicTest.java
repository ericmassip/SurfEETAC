import java.util.List;

import models.Comment;
import models.Imagen;
import models.Spot;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.db.jpa.Blob;
import play.test.Fixtures;
import play.test.UnitTest;


public class BasicTest extends UnitTest {
		 
	@Before
	public void setup() {
	    Fixtures.deleteDatabase();
	}

    @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
        new User( "bob54", "secret").save();
        
        // Retrieve the user with username: bob54
        User bob = User.find("byUsername", "bob54").first();
        
        // Test 
        assertNotNull(bob);
        assertEquals("secret", bob.password);
    }
    
    @Test
    public void tryConnectAsUser() {
        // Create a new user and save it
        new User("bob54", "secret").save();
        
        // Test 
        assertNotNull(User.connect("bob54", "secret"));
        assertNull(User.connect("bob54", "badpassword"));
        assertNull(User.connect("tom54", "secret"));
    }
    
    @Test
    public void userImagenes() {
        // Create a new user and save it
    	 User bob = new User("bob54", "secret").save();
     
        // Create a new image
        new Imagen(bob, "My first image", "First_image.jpg", new Blob(), "My first image").save();
        new Imagen(bob, "My second image", "Second_image.jpg", new Blob(), "My second image").save();
     
        // Retrieve all images
        List<Imagen> bobImages = Imagen.find("byAuthor", bob).fetch();
     
        // Tests
        assertEquals(2, bobImages.size());
     
        Imagen firstImage = bobImages.get(0);
        assertNotNull(firstImage);
        assertEquals(bob, firstImage.author);
        assertEquals("My first image", firstImage.title);
        assertNotNull(firstImage.postedAt);
     
        Imagen secondImage = bobImages.get(1);
        assertNotNull(secondImage);
        assertEquals(bob, secondImage.author);
        assertEquals("My second image", secondImage.title);
        assertNotNull(secondImage.postedAt);
    }
    
    @Test
    public void userImagenes2() {
        // Create a new user and save it
    	User bob = new User("bob54", "secret").save();
    	User erick = new User("massip04", "massip").save();
    	
        // Create new images
    	Imagen firstImage = new Imagen(bob, "My first image", "First_image.jpg", new Blob(), "My first image").save();
        Imagen secondImage = new Imagen(bob, "My second image", "Second_image.jpg", new Blob(), "My second image").save();
        Imagen Hossegor = new Imagen(erick, "Hossegor", "Hossegor.jpg", new Blob(), "Hossegor").save();
        Imagen LaPiste = new Imagen(erick, "La Piste", "LaPiste.jpg", new Blob(), "La Piste").save();
        Imagen Capbreton = new Imagen(erick, "Capbreton", "Capbreton.jpg", new Blob(), "Capbreton").save();
    
        // Count things
        assertEquals(2, User.count());
        assertEquals(5, Imagen.count());
     
        // Retrieve Bob's first image
        firstImage = Imagen.find("byAuthor", bob).first();
        assertNotNull(firstImage);
        assertEquals("First_image.jpg", firstImage.photoFileName);
        Hossegor = Imagen.find("byAuthor", erick).first();
        assertNotNull(Hossegor);
        
        // Delete images
        Capbreton.delete();
        LaPiste.delete();
        secondImage.delete();
        
        // Check
        assertEquals(2, User.count());
        assertEquals(0, Spot.count());
        assertEquals(2, Imagen.count());
    }
    
    @Test
    public void useTheCommentsRelation() {
    	
    	//Create a new user and save it
    	User erick = new User("massip04", "massip").save();
    	
    	//Create an image
    	Imagen LaPiste = new Imagen(erick, "La Piste", "LaPiste.jpg", new Blob(), "Foto de La Piste").save();
     
        // Post a first comment
        LaPiste.addComment("Jeff", "Nice picture");
        LaPiste.addComment("Tom", "Pumping!!!");
     
        // Count things
        assertEquals(1, User.count());
        assertEquals(2, Comment.count());
     
        // Retrieve Bob's post
        LaPiste = Imagen.find("byAuthor", erick).first();
        assertNotNull(LaPiste);
     
        // Navigate to comments
        assertEquals(2, LaPiste.comments.size());
        assertEquals("Jeff", LaPiste.comments.get(0).author);
        
        // Delete the post
        LaPiste.delete();
        
        // Check that all comments have been deleted
        assertEquals(1, User.count());
        assertEquals(0, Imagen.count());
        assertEquals(0, Comment.count());
    }
    
    @Test
    public void fullTest() {
        Fixtures.loadModels("TestData.yml");
     
        // Count things
        assertEquals(3, User.count());
        assertEquals(4, Spot.count());
        assertEquals(2, Imagen.count());
        assertEquals(3, Comment.count());
     
        // Try to connect as users
        assertNotNull(User.connect("Bob", "secret"));
        assertNotNull(User.connect("Jeff", "secret"));
        assertNull(User.connect("Jeff", "badpassword"));
        assertNull(User.connect("tom@gmail.com", "secret"));
     
        // Find all comments related to Port Ginesta's image
        List<Comment> portginestaComments = Comment.find("foto.title", "Erick Surfing").fetch();
        assertEquals(2, portginestaComments.size());
     
        // Find the most recent image
        Imagen lastImagen = Imagen.find("order by postedAt desc").first();
        assertNotNull(lastImagen);
        assertEquals("Guerrero Posturetis", lastImagen.title);
     
        // Check that this post has two comments
        assertEquals(1, lastImagen.comments.size());
     
        // Post a new comment
        lastImagen.addComment("Jim", "Hello guys");
        assertEquals(2, lastImagen.comments.size());
        assertEquals(4, Comment.count());
    }
}
