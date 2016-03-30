package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerDaoImpl implements CustomerDAO
{
	@Override
	public Customer create(Connection connection, Customer customer) throws SQLException, DAOException {
		
		// Checks that customer ID is null, throws exception is a value is found
		if (customer.getId() != null)
			throw new DAOException("Trying to insert Customer with NON-NULL ID");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for inserting into prepared statement
		String insertStmt = "INSERT INTO customer (firstName, lastName, gender, dob, email)"
				+ " VALUES (?, ? ?, ?, ?)";
		
		// Sends prepared statement object to the PreparedStatement variable, with the string inserted
		pstmt = connection.prepareStatement( insertStmt );
		
		pstmt.setString(1, customer.getFirstName() );
		pstmt.setString(2, customer.getLastName() );
		pstmt.setString(3, customer.getGender().toString() );
		pstmt.setDate(4, customer.getDob() );
		pstmt.setString(5, customer.getEmail() );
		
		// Performs the insert command
		pstmt.executeUpdate();
		
		// Prepares string in order to get the id that was just created in the insert operation
		String getInsertedID = "SELECT LAST_INSERT_ID()";
		
		// Places string to PreparedStatement variable as object
		pstmt = connection.prepareStatement( getInsertedID );
		
		// Executes the query
		ResultSet rs = pstmt.executeQuery();
		
		// Checks for a non null return in the result set
		if ( rs.next() ) {			
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			customer.setId( rs.getLong("id") ); // Sets the customer ID to what was created in the database
		}
			
		// Returns the customer with the updated id
		return customer;
	}

	@Override
	public Customer retrieve(Connection connection, Long id) throws SQLException, DAOException {
		
		Customer customer = null;
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for returning row that matches id
		String retrieveStmt = "SELECT * FROM customer WHERE id = ?";
		
		// Places string to PreparedStatement variable as object
		pstmt = connection.prepareStatement( retrieveStmt );
		
		pstmt.setLong(1, id);
		
		// Executes the query
		ResultSet rs = pstmt.executeQuery();
		
		// Checks for a non null return in the result set
		if ( rs.next() ) {
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			// Initializes customer variable
			customer = new Customer();
			
			// Loads ResultSet returns to customer fields
			customer.setId( rs.getLong("id") );
			customer.setFirstName( rs.getString("firstName") );
			customer.setLastName( rs.getString("lastName") );
			customer.setGender( rs.getString("gender").charAt(0) );
			customer.setDob( rs.getDate("dob") );
			customer.setEmail( rs.getString("email") );
		}
		
		// Returns customer information
		return customer;
	}

	@Override
	public int update(Connection connection, Customer customer) throws SQLException, DAOException {

		// Checks that customer ID is positive number, throws exception is a value is found
		if (customer.getId() < 1)
			throw new DAOException("Trying to update Customer with invalid ID.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for updating with prepared statement
		String updateStmt = "UPDATE customer SET"
				+ " firstName = ?"
				+ ", lastName = ?"
				+ ", gender = ?"
				+ ", dob = ?"
				+ ", email = ?"
				+ " WHERE id = ?";
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( updateStmt );
		
		pstmt.setString(1, customer.getFirstName() );
		pstmt.setString(2, customer.getLastName() );
		pstmt.setString(3, customer.getGender().toString() );
		pstmt.setDate(4, customer.getDob() );
		pstmt.setString(5, customer.getEmail() );
		pstmt.setLong(6, customer.getId() );
		
		pstmt.executeUpdate();		
		
		return 0;
	}

	@Override
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
		
		// Checks that customer ID is positive number, throws exception is a value is found
		if (id < 1)
			throw new DAOException("Trying to delete Customer with invalid ID.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for updating with prepared statement
		String deleteStmt = "DELETE FROM customer WHERE id = ?";
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( deleteStmt );
		
		pstmt.setLong(1, id);
		
		pstmt.executeUpdate();		
		
		return 0;
	}

	@Override
	public List<Customer> retrieveByZipCode(Connection connection, String zipCode) throws SQLException, DAOException {
		
		if (zipCode == null)
			throw new DAOException("Trying to search for customers with invalid Zip Code.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for searching table
		String zipCodeSearch = "SELECT * FROM customer, creditCard, address "
				+ "WHERE customer.id = address.customerID " 
				+ "AND address.customerID = creditCard.customerID " 
				+ "AND address.zipcode = ?";
		
		// Loads string to PreparedStatement
		pstmt = connection.prepareStatement( zipCodeSearch );
		
		pstmt.setString(1, zipCode);
		
		// Performs query and sends data to ResultSet variable
		ResultSet rs = pstmt.executeQuery();
		
		// Creates a List for returning search results
		List<Customer> customerList = null;
		
		// Checks if results were found
		if ( rs.next() ) {			
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			// If results were found, customerList is initialized (otherwise null is returned)
			customerList = new ArrayList<Customer>();
						
			do
			{
				// Creates new Customer object for adding to List array
				Customer customer = new Customer();
				Address address = new Address();
				CreditCard creditCard = new CreditCard();
				
				// Updates values of customer from Result Set
				customer.setId( rs.getLong("id") );
				customer.setFirstName( rs.getString("firstName") );
				customer.setLastName( rs.getString("lastName") );
				customer.setGender( rs.getString("gender").charAt(0) );
				customer.setDob( rs.getDate("dob") );
				customer.setEmail( rs.getString("email") );
				
				address.setAddress1( rs.getString("address1") );
				address.setAddress2( rs.getString("address2") );
				address.setCity( rs.getString("city") );
				address.setState( rs.getString("state") );
				address.setZipcode( rs.getString("zipcode") );
				
				customer.setAddress( address );
				
				creditCard.setName( rs.getString("name") );
				creditCard.setCcNumber( rs.getString("ccNumber") );
				creditCard.setExpDate( rs.getString("expDate") );
				creditCard.setSecurityCode( rs.getString("securityCode") );
				
				customer.setCreditCard( creditCard );
				
				// Adds customer to List
				customerList.add(customer);			
			} while ( rs.next() ); // Loops until all results are added to list
		}
		
		// Returns generated List
		return customerList;
	}

	@Override
	public List<Customer> retrieveByDOB(Connection connection, Date startDate, Date endDate)
			throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for use in PreparedStatement
		String searchByDOB = "SELECT * FROM customer "
				+ "WHERE dob >= ? "
				+ "AND dob <= ?";
		
		// Loads string to PreparedStatement
		pstmt = connection.prepareStatement( searchByDOB );
		
		pstmt.setDate(1, startDate);
		pstmt.setDate(2, endDate);		
		
		// Performs query and sends data to ResultSet variable
		ResultSet rs = pstmt.executeQuery();
		
		// Creates a List for returning search results
		List<Customer> customerList = null;
		
		// Checks if results were found
		if ( rs.next() ) {
			
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			// If results were found, customerList is initializes (otherwise null is returned)
			customerList = new ArrayList<Customer>();
						
			do
			{
				// Creates new Customer object for adding to List array
				Customer customer = new Customer();
				Address address = new Address();
				CreditCard creditCard = new CreditCard();
				
				// Updates values of customer from Result Set
				customer.setId( rs.getLong("id") );
				customer.setFirstName( rs.getString("firstName") );
				customer.setLastName( rs.getString("lastName") );
				customer.setGender( rs.getString("gender").charAt(0) );
				customer.setDob( rs.getDate("dob") );
				customer.setEmail( rs.getString("email") );
				
				address.setAddress1( rs.getString("address1") );
				address.setAddress2( rs.getString("address2") );
				address.setCity( rs.getString("city") );
				address.setState( rs.getString("state") );
				address.setZipcode( rs.getString("zipcode") );
				
				customer.setAddress( address );
				
				creditCard.setName( rs.getString("name") );
				creditCard.setCcNumber( rs.getString("ccNumber") );
				creditCard.setExpDate( rs.getString("expDate") );
				creditCard.setSecurityCode( rs.getString("securityCode") );
				
				customer.setCreditCard( creditCard );
				
				// Adds customer to List
				customerList.add(customer);			
			} while ( rs.next() ); // Loops until all results are added to list
		}
		
		// Returns generated List
		return customerList;
	}	
}