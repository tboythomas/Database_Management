import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileInputStream;

/**
 * Runs queries against a back-end database
 */
public class Query {
	private String configFilename;
	private Properties configProps = new Properties();

	private String jSQLDriver;
	private String jSQLUrl;
	private String jSQLUser;
	private String jSQLPassword;

	// DB Connection
	private Connection conn;
    private Connection customerConn;

	// Canned queries

	// LIKE does a case-insensitive match
	private static final String SEARCH_SQL =
		"SELECT * FROM movie "
		+ "WHERE name LIKE ('%' + ? + '%') "
		+ "ORDER BY id";
	private PreparedStatement searchmovieStatement;
	
	private static final String DIRECTOR_MID_SQL = "SELECT y.* "
					 + "FROM movie_directors x, directors y "
					 + "WHERE x.mid = ? and x.did = y.id";
	private PreparedStatement directorMidStatement;
	
	/* uncomment, and edit, after your create your own customer database */
	
	private static final String CUSTOMER_LOGIN_SQL = 
		"SELECT * FROM customer WHERE login = ? and password = ?";
	private PreparedStatement customerLoginStatement;

	private static final String BEGIN_TRANSACTION_SQL = 
		"SET TRANSACTION ISOLATION LEVEL SERIALIZABLE; BEGIN TRANSACTION;";
	private PreparedStatement beginTransactionStatement;

	private static final String COMMIT_SQL = "COMMIT TRANSACTION";
	private PreparedStatement commitTransactionStatement;

	private static final String ROLLBACK_SQL = "ROLLBACK TRANSACTION";
	private PreparedStatement rollbackTransactionStatement;
	
	//Query for searching actor
	private static final String Actor_movie = 
		"SELECT * "
		+ "FROM actor a, casts c "
		+ "WHERE a.id = c.pid and c.mid =?";
	private PreparedStatement actormovieStatement;
	
	
	//query for method get renter id
	private static final String Customer_Rent = 
			"SELECT cid FROM rental WHERE status = 'open' AND mid = ?";
	private PreparedStatement customerrentStatement;
	
	//query needed for fast search, return movie info
	private static final String Moviefast = 
			"SELECT id, name, year FROM movie WHERE name LIKE ? "
			+ "ORDER BY id";
	private PreparedStatement moviefastStatement;
	
	//query for fast search, actors
	private static final String Actorfast =
			"SELECT a.lname, a.fname, m.id "
			+ "FROM movie m, casts c, actor a "
			+ "WHERE m.id = c.mid AND c.pid = a.id and m.name LIKE ? "
			+ "ORDER BY m.id";
	private PreparedStatement actorfastStatement;
	
	//query for fast search directors
	private static final String Directorfast =
			"SELECT d.lname, d.fname, m.id "
			+ "FROM movie m, directors d, movie_directors md "
			+ "WHERE md.did = d.id AND md.mid = m.id AND m.name LIKE ? "
			+ "ORDER BY m.id";
	private PreparedStatement directorfastStatement;
	
	//query for printing personal data
	private static final String Personinfo = 
			"SELECT fname, lname FROM Customer WHERE cid = ?";
	private PreparedStatement personStatement;
	
	//get the customer plan return the max of numbers of movies
	private static final String Planinfo = 
			"SELECT r.max FROM Rental_Plan r, Customer c WHERE c.cid = ? AND c.pid = r.pid";
	private PreparedStatement planStatement;
	
	//return the movies the customer has rent
	private static final String Numrent =
			"SELECT count(*) FROM Rental WHERE cid = ? AND status = 'open'";
	private PreparedStatement numrentStatement;
	
	//query for the plan list
	private static final String Planlist = 
			"SELECT * FROM Rental_Plan";
	private PreparedStatement planlistStatement;
	
	//query for update the customer's plan
	private static final String Changeplan =
			"UPDATE Customer SET pid = ? WHERE cid = ?";
	private PreparedStatement changeStatement;
	
	//query for check plan id
	private static final String Planid =
			"SELECT pid FROM Rental_Plan";
	private PreparedStatement pidStatement;
	
	//query for check movie available
	private static final String Movieid = 
			"SELECT id FROM movie WHERE id = ? ";
	private PreparedStatement movieidStatement;
	
	//query for renting
	private static final String rent = 
			"INSERT INTO Rental(cid, mid, time, status) VALUES (?, ?, CURRENT_TIMESTAMP, 'open')";
	private PreparedStatement rentStatement;
	
	//query for returning
	private static final String returning =
			"UPDATE Rental SET status = 'closed' WHERE cid = ? AND mid = ?";
	private PreparedStatement returnStatement;
	
	public Query(String configFilename) {
		this.configFilename = configFilename;
	}

    /**********************************************************/
    /* Connection code to SQL Azure. Example code below will connect to the imdb database on Azure
       IMPORTANT NOTE:  You will need to create (and connect to) your new customer database before 
       uncommenting and running the query statements in this file .
     */

	public void openConnection() throws Exception {
		configProps.load(new FileInputStream(configFilename));

		jSQLDriver   = configProps.getProperty("videostore.jdbc_driver");
		jSQLUrl	   = configProps.getProperty("videostore.imdb_url");
		jSQLUser	   = configProps.getProperty("videostore.sqlazure_username");
		jSQLPassword = configProps.getProperty("videostore.sqlazure_password");
		
		/* load jdbc drivers */
		Class.forName(jSQLDriver).newInstance();

		/* open connections to the imdb database */

		conn = DriverManager.getConnection(jSQLUrl, // database
						   jSQLUser, // user
						   jSQLPassword); // password
                
		conn.setAutoCommit(true); //by default automatically commit after each statement 

		/* You will also want to appropriately set the 
                   transaction's isolation level through:  
		   conn.setTransactionIsolation(...) */

		customerConn = DriverManager.getConnection(configProps.getProperty("videostore.customer_url"),jSQLUser,jSQLPassword);
		customerConn.setAutoCommit(true);
		customerConn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	        
	}

	public void closeConnection() throws Exception {
		conn.close();
		customerConn.close();
	}

    /**********************************************************/
    /* prepare all the SQL statements in this method.
      "preparing" a statement is almost like compiling it.  Note
       that the parameters (with ?) are still not filled in */

	public void prepareStatements() throws Exception {

		directorMidStatement = conn.prepareStatement(DIRECTOR_MID_SQL);

		/* uncomment after you create your customers database */
		
		customerLoginStatement = customerConn.prepareStatement(CUSTOMER_LOGIN_SQL);
		beginTransactionStatement = customerConn.prepareStatement(BEGIN_TRANSACTION_SQL);
		commitTransactionStatement = customerConn.prepareStatement(COMMIT_SQL);
		rollbackTransactionStatement = customerConn.prepareStatement(ROLLBACK_SQL);
		

		/* add here more prepare statements for all the other queries you need */
		actormovieStatement = conn.prepareStatement(Actor_movie);
		searchmovieStatement = conn.prepareStatement(SEARCH_SQL);
		//statements for fast search
		moviefastStatement = conn.prepareStatement(Moviefast);
		actorfastStatement = conn.prepareStatement(Actorfast);
		directorfastStatement = conn.prepareStatement(Directorfast);
		
		customerrentStatement = customerConn.prepareStatement(Customer_Rent);
		personStatement = customerConn.prepareStatement(Personinfo);
		planStatement = customerConn.prepareStatement(Planinfo);
		numrentStatement = customerConn.prepareCall(Numrent);
		planlistStatement = customerConn.prepareStatement(Planlist);
		changeStatement = customerConn.prepareStatement(Changeplan);
		pidStatement = customerConn.prepareStatement(Planid);
		movieidStatement = conn.prepareStatement(Movieid);
		rentStatement = customerConn.prepareStatement(rent);
		returnStatement = customerConn.prepareStatement(returning);
	}


    /**********************************************************/
    /* Suggested helper functions; you can complete these, or write your own
       (but remember to delete the ones you are not using!) */

	public int getRemainingRentals(int cid) throws Exception {
		/* How many movies can she/he still rent?
		   You have to compute and return the difference between the customer's plan
		   and the count of outstanding rentals */
		int max = planmax(cid);
		numrentStatement.clearParameters();
		numrentStatement.setInt(1, cid);
		ResultSet temp = numrentStatement.executeQuery();
		temp.next();
		int rent = temp.getInt(1);
		return (max - rent);
	}

	public String getCustomerName(int cid) throws Exception {
		/* Find the first and last name of the current customer. */
		personStatement.clearParameters();
		personStatement.setInt(1, cid);
		ResultSet temp = personStatement.executeQuery();
		temp.next();
		String name = temp.getString(1) + " " + temp.getString(2);
		temp.close();
		return (name);
	}

	public boolean isValidPlan(int planid) throws Exception {
		/* Is planid a valid plan ID?  You have to figure it out */
		ResultSet temp = pidStatement.executeQuery();
		while(temp.next()){
			if(temp.getInt(1) == planid){
				return true;
			}
		}
		temp.close();
		return false;
	}
	
	public int planmax(int cid) throws Exception{
		planStatement.clearParameters();	
		planStatement.setInt(1, cid);
		ResultSet temp = planStatement.executeQuery();
		temp.next();
		int max = temp.getInt(1);
		temp.close();
		return max;
	}
	
	public boolean isValidMovie(int mid) throws Exception {
		/* is mid a valid movie ID?  You have to figure it out */
		movieidStatement.clearParameters();
		movieidStatement.setInt(1, mid);
		ResultSet temp = movieidStatement.executeQuery();
		boolean id = temp.next();
		temp.close();
		return id;
	}

	private int getRenterID(int mid) throws Exception {
		/* Find the customer id (cid) of whoever currently rents the movie mid; return -1 if none */
		customerrentStatement.clearParameters();
		customerrentStatement.setInt(1, mid);
		ResultSet temp = customerrentStatement.executeQuery();
		int cid = temp.next()? temp.getInt("cid"): -1;
		temp.close();
		return cid;
	}

    /**********************************************************/
    /* login transaction: invoked only once, when the app is started  */
	public int transaction_login(String name, String password) throws Exception {
		/* authenticates the user, and returns the user id, or -1 if authentication fails */

		/* Uncomment after you create your own customers database */
		int cid;
		customerLoginStatement.clearParameters();
		customerLoginStatement.setString(1,name);
		customerLoginStatement.setString(2,password);
		ResultSet cid_set = customerLoginStatement.executeQuery();
		if (cid_set.next()) cid = cid_set.getInt(1);
		else cid = -1;
		cid_set.close();
		return(cid);
	}

	public void transaction_printPersonalData(int cid) throws Exception {
		/* println the customer's personal data: name, and plan number */
		beginTransaction();
		String name = getCustomerName(cid);
		int max = planmax(cid);
		int dif = getRemainingRentals(cid);
		System.out.println("Dear Customer: " + name );
		System.out.println("You can rent at most " + max + " movies");
		System.out.println("You can still rent " + dif + " movies");
		commitTransaction();
	}


    /**********************************************************/
    /* main functions in this project: */

	public void transaction_search(int cid, String movie_title)
			throws Exception {
		/* searches for movies with matching titles: SELECT * FROM movie WHERE name LIKE movie_title */
		/* prints the movies, directors, actors, and the availability status:
		   AVAILABLE, or UNAVAILABLE, or YOU CURRENTLY RENT IT */

		/* Interpolate the movie title into the SQL string */
		searchmovieStatement.clearParameters();
		searchmovieStatement.setString(1, movie_title);
		ResultSet movie_set = searchmovieStatement.executeQuery();
		while (movie_set.next()) {
			int mid = movie_set.getInt(1);
			System.out.println("ID: " + mid + " NAME: "
					+ movie_set.getString(2) + " YEAR: "
					+ movie_set.getString(3));
			/* do a dependent join with directors */
			directorMidStatement.clearParameters();
			directorMidStatement.setInt(1, mid);
			ResultSet director_set = directorMidStatement.executeQuery();
			while (director_set.next()) {
				System.out.println("\t\tDirector: " + director_set.getString(3)
						+ " " + director_set.getString(2));
			}
			director_set.close();
			/* now you need to retrieve the actors, in the same manner */
			actormovieStatement.clearParameters();
			actormovieStatement.setInt(1,mid);
			ResultSet actors = actormovieStatement.executeQuery();
			while(actors.next()){
				System.out.println("\t\tActor: " + actors.getString(2) + " " +  actors.getString(3));
			}
			actors.close();
			/* then you have to find the status: of "AVAILABLE" "YOU HAVE IT", "UNAVAILABLE" */
			int customerid = getRenterID(mid);
			if(customerid == cid){
				System.out.println("\t\tYOU HAVE IT");
			}else if (customerid == -1){
				System.out.println("\t\tAVAILABLE");
			}else{
				System.out.println("\t\tUNAVAILABLE");
			}
		}
		movie_set.close();
		System.out.println();
	}

	public void transaction_choosePlan(int cid, int pid) throws Exception {
	    /* updates the customer's plan to pid: UPDATE customer SET plid = pid */
	    /* remember to enforce consistency ! */
		beginTransaction();
		//check if valid plan however in the videostore.java it has been checked for us
		if(!isValidPlan(pid)){
			rollbackTransaction();
		}else{
			changeStatement.clearParameters();
			changeStatement.setInt(1, pid);
			changeStatement.setInt(2, cid);
			changeStatement.executeUpdate();
			//check if the customer having too many movies
			int dif = getRemainingRentals(cid);
			if(dif < 0){
				rollbackTransaction();
				System.out.println("Don't cheat! You have to rent some movies before downgrading your membership!");
			}else{
				commitTransaction();
			}
		}
	}

	public void transaction_listPlans() throws Exception {
	    /* println all available plans: SELECT * FROM plan */
		ResultSet list = planlistStatement.executeQuery();
		while(list.next()){
			System.out.println("Plan id: " + list.getInt(1) + " Plan name: " + list.getString(2) 
					+ " Max numbers of movies: " + list.getInt(3) + " Monthly Fee: " + list.getInt(3));
		}
		list.close();
	}

	public void transaction_rent(int cid, int mid) throws Exception {
	    /* rent the movie mid to the customer cid */
	    /* remember to enforce consistency ! */
		if(!isValidMovie(mid)){
			System.out.println("Invalid movie id");
		}else{
			beginTransaction();
			int dif = getRemainingRentals(cid);
			if(dif <= 0){
				rollbackTransaction();
				System.out.println("You have reached the limit!");
			}else{
				int temp = getRenterID(mid);
				if(temp == -1){
					rentStatement.clearParameters();
					rentStatement.setInt(1, cid);
					rentStatement.setInt(2, mid);
					rentStatement.executeUpdate();
					commitTransaction();
					System.out.println("Have fun!");
				}else{
					rollbackTransaction();
					if(temp == cid){
						System.out.println("Check your pocket! You have this one!");
					}else{
						System.out.println("Too bad. Out of Stock.");
					}
				}
			}
		}
	}

	public void transaction_return(int cid, int mid) throws Exception {
	    /* return the movie mid by the customer cid */
		beginTransaction();
		if(!isValidMovie(mid)){
			rollbackTransaction();
			System.out.println("Invalid Movie id");
		}else{
			int temp = getRenterID(mid);
			if(temp != cid){
				rollbackTransaction();
				System.out.println("You didn't rent this movie!");
			}else{
				returnStatement.clearParameters();
				returnStatement.setInt(1, cid);
				returnStatement.setInt(2, mid);
				returnStatement.executeUpdate();
				commitTransaction();
				System.out.println("Thanks for returning!");
			}
		}
	}

	public void transaction_fastSearch(int cid, String movie_title)
			throws Exception {
		/* like transaction_search, but uses joins instead of dependent joins
		   Needs to run three SQL queries: (a) movies, (b) movies join directors, (c) movies join actors
		   Answers are sorted by mid.
		   Then merge-joins the three answer sets */
		
		//clear parameters
		moviefastStatement.clearParameters();
		moviefastStatement.setString(1, "%" + movie_title + "%");
		ResultSet movies = moviefastStatement.executeQuery();

		directorfastStatement.clearParameters();
		directorfastStatement.setString(1, "%" + movie_title + "%");
		ResultSet directors = directorfastStatement.executeQuery();

		actorfastStatement.clearParameters();
		actorfastStatement.setString(1, "%" + movie_title + "%");
		ResultSet actors = actorfastStatement.executeQuery();

		
		boolean temp1 = directors.next();
		boolean temp2 = actors.next();
		
		//perform a sort-merge join method
		while(movies.next()){
			int mid = movies.getInt(1);
			System.out.println("ID: " + mid + " Name: " + movies.getString(2) +" Year: " + movies.getString(3));
					
			while(temp1 && directors.getInt(3) < mid){
				temp1 = directors.next();
			}
			while(temp1 && directors.getInt(3) == mid){
				System.out.println("\t\tDirector: " + directors.getString(2) + " " + directors.getString(1));
				temp1 = directors.next();			
			}
			
			while(temp2 && actors.getInt(3) < mid){
				temp2 = actors.next();
			}
			while(temp2 && actors.getInt(3) == mid){
				System.out.println("\t\tActors: " + actors.getString(2) + " " + actors.getString(1));
				temp2 = actors.next();
			}
		}
		actors.close();
		directors.close();
		movies.close();
	}


       /*Uncomment helpers below once you've got beginTransactionStatement,
       commitTransactionStatement, and rollbackTransactionStatement setup from
       prepareStatements():*/
    
       public void beginTransaction() throws Exception {
	    customerConn.setAutoCommit(false);
	    beginTransactionStatement.executeUpdate();	
        }

        public void commitTransaction() throws Exception {
	    commitTransactionStatement.executeUpdate();	
	    customerConn.setAutoCommit(true);
        }
        
        public void rollbackTransaction() throws Exception {
	    rollbackTransactionStatement.executeUpdate();
	    customerConn.setAutoCommit(true);
	    } 
 

}
