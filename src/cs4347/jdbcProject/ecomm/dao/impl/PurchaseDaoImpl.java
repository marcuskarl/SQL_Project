package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchaseDaoImpl implements PurchaseDAO
{

	@Override
	public Purchase create(Connection connection, Purchase purchase) throws SQLException, DAOException {
		
		// Checks that customer ID is null, throws exception is a value is found
		if (purchase.getId() != null)
			throw new DAOException("Trying to insert Purchase with NON-NULL ID.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for inserting into prepared statement
		String insertStmt = "INSERT INTO purchase (productID, customerID, purchaseDate, purchaseAmount)"
				+ " VALUES (?, ?, ?, ?)";
				
		// Sends prepared statement object to the PreparedStatement variable, with the string inserted
		pstmt = connection.prepareStatement( insertStmt );
		
		pstmt.setLong(1, purchase.getProductID() );
		pstmt.setLong(2, purchase.getCustomerID() );
		pstmt.setDate(3, purchase.getPurchaseDate() );
		pstmt.setDouble(4, purchase.getPurchaseAmount());
				
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
			
			purchase.setId( rs.getLong("id") ); // Sets the purchase ID to what was created in the database
		}
			
		// Returns the purchase with the updated id
		return purchase;
	}

	@Override
	public Purchase retrieve(Connection connection, Long id) throws SQLException, DAOException {
		
		Purchase purchase = null;

		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for returning row that matches id
		String retrieveStmt = "SELECT * FROM purchase WHERE id = ?";
		
		// Places string to PreparedStatement variable as object
		pstmt = connection.prepareStatement( retrieveStmt );
		
		pstmt.setLong(1, id );
		
		// Executes the query
		ResultSet rs = pstmt.executeQuery();
		
		// Checks for a non null return in the result set
		if ( rs.next() ) {
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			// Initializes Purchase variable
			purchase = new Purchase();
			
			// Loads ResultSet returns to purchase fields
			purchase.setId( rs.getLong("id") );
			purchase.setCustomerID( rs.getLong("customerID") );
			purchase.setProductID( rs.getLong("productID") );
			purchase.setPurchaseDate( rs.getDate("purchaseDate") );
			purchase.setPurchaseAmount( rs.getDouble("purchaseAmount") );
		}
		
		// Returns purchase information (or null if not found)
		return purchase;
	}

	@Override
	public int update(Connection connection, Purchase purchase) throws SQLException, DAOException {
		
		// Checks that purchase ID is positive number, throws exception is a value is found
		if (purchase.getId() < 1)
			throw new DAOException("Trying to update purchase with invalid ID.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for updating with prepared statement
		String updateStmt = "UPDATE purchase SET"
				+ " customerID = ?"
				+ ", productID = ?"
				+ ", purchaseDate = ?"
				+ ", purchaseAmount = ?"
				+ " WHERE id = ?";
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( updateStmt );
		
		pstmt.setLong(1, purchase.getCustomerID() );
		pstmt.setLong(2, purchase.getProductID() );
		pstmt.setDate(3, purchase.getPurchaseDate() );
		pstmt.setDouble(4, purchase.getPurchaseAmount() );
		pstmt.setLong(5, purchase.getCustomerID() );
		
		pstmt.executeUpdate();
		
		return 0;
	}

	@Override
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
		
		// Checks that purchase ID is positive number, throws exception is a value is found
		if (id < 1)
			throw new DAOException("Trying to delete Purchase with invalid ID.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for updating with prepared statement
		String deleteStmt = "DELETE FROM purchase WHERE id = ?";
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( deleteStmt );
		pstmt.setLong(1, id );
		
		pstmt.executeUpdate();
		
		return 0;
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Connection connection, Long customerID)
			throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for searching table
		String customerSearch = "SELECT * FROM purchase WHERE customerID = ?";
		
		// Loads string to PreparedStatement
		pstmt = connection.prepareStatement( customerSearch );
		
		pstmt.setLong(1, customerID );
		
		// Performs query and sends data to ResultSet variable
		ResultSet rs = pstmt.executeQuery();
		
		// Creates a List for returning search results
		List<Purchase> purchaseList = null;
		
		// Checks if results were found
		if ( rs.next() ) {			
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			// If results were found, purchaseList is initialized (otherwise null is returned)
			purchaseList = new ArrayList<Purchase>();
			
			do
			{
				// Creates new Purchase object for adding to List array
				Purchase purchase = new Purchase();
				
				// Loads ResultSet returns to purchase fields
				purchase.setId( rs.getLong("id") );
				purchase.setCustomerID( rs.getLong("customerID") );
				purchase.setProductID( rs.getLong("productID") );
				purchase.setPurchaseDate( rs.getDate("purchaseDate") );
				purchase.setPurchaseAmount( rs.getDouble("purchaseAmount") );
				
				// Adds Purchase to List
				purchaseList.add(purchase);		
			} while ( rs.next() ); // Loops until all results are added to list
		}
		
		// Returns generated List
		return purchaseList;
	}

	@Override
	public List<Purchase> retrieveForProductID(Connection connection, Long productID)
			throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for searching table
		String productSearch = "SELECT * FROM purchase WHERE productID = ?";
		
		// Loads string to PreparedStatement
		pstmt = connection.prepareStatement( productSearch );
		
		pstmt.setLong(1,  productID );
		
		// Performs query and sends data to ResultSet variable
		ResultSet rs = pstmt.executeQuery();
		
		// Creates a List for returning search results
		List<Purchase> purchaseList = null;
		
		// Checks if results were found
		if ( rs.next() ) {			
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			// If results were found, productList is initialized (otherwise null is returned)
			purchaseList = new ArrayList<Purchase>();
			
			do
			{
				// Creates new Purchase object for adding to List array
				Purchase purchase = new Purchase();
				
				// Loads ResultSet returns to purchase fields
				purchase.setId( rs.getLong("id") );
				purchase.setCustomerID( rs.getLong("customerID") );
				purchase.setProductID( rs.getLong("productID") );
				purchase.setPurchaseDate( rs.getDate("purchaseDate") );
				purchase.setPurchaseAmount( rs.getDouble("purchaseAmount") );
				
				// Adds Purchase to List
				purchaseList.add(purchase);		
			} while ( rs.next() ); // Loops until all results are added to list
		}
		
		// Returns generated List
		return purchaseList;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Connection connection, Long customerID)
			throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for searching table
		String purchaseSearch = "SELECT MIN(purchaseAmount), MAX(purchaseAmount), AVG(purchaseAmount)"
								+ " FROM purchase WHERE customerID = ?";
		
		// Loads string to PreparedStatement
		pstmt = connection.prepareStatement( purchaseSearch );
		
		pstmt.setLong(1, customerID );
		
		// Performs query and sends data to ResultSet variable
		ResultSet rs = pstmt.executeQuery();
		
		// Creates a purchaseSummary for returning search results
		PurchaseSummary purchaseSummary = null;
		
		// Checks if results were found
		if ( rs.next() ) {			
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			// Initializes variable
			purchaseSummary = new PurchaseSummary();
			
			// If results were found, purchaseSummary is initialized (otherwise null is returned)
			purchaseSummary.minPurchase = rs.getFloat( "MIN(purchaseAmount)" );
			purchaseSummary.maxPurchase = rs.getFloat( "MAX(purchaseAmount)" );
			purchaseSummary.avgPurchase = rs.getFloat( "AVG(purchaseAmount)" );
		}
		
		return purchaseSummary;
	}
}