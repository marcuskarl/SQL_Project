package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductDaoImpl implements ProductDAO
{
	@Override
	public Product create(Connection connection, Product product) throws SQLException, DAOException {
		
		// Checks that customer ID is null, throws exception is a value is found
		if (product.getId() != null)
			throw new DAOException("Trying to insert Product with NON-NULL ID.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for inserting into prepared statement
		String insertStmt = "INSERT INTO product (prodName, prodDescription, prodCategory, prodUPC)"
				+ " VALUES (?, ?, ?, ?)";
		
		// Sends prepared statement object to the PreparedStatement variable, with the string inserted
		pstmt = connection.prepareStatement( insertStmt );
		
		pstmt.setString(1, product.getProdName() );
		pstmt.setString(2, product.getProdDescription() );
		pstmt.setInt(3, product.getProdCategory() );
		pstmt.setString(4, product.getProdUPC() );
		
		try
		{
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
				
				product.setId( rs.getLong("id") ); // Sets the product ID to what was created in the database
			}
				
			// Returns the product with the updated id
			return product;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			pstmt.close();
		}
	}

	@Override
	public Product retrieve(Connection connection, Long id) throws SQLException, DAOException {
		
		Product product = null;

		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for returning row that matches id
		String retrieveStmt = "SELECT * FROM product WHERE id = ?";
		
		// Places string to PreparedStatement variable as object
		pstmt = connection.prepareStatement( retrieveStmt );
		
		pstmt.setLong(1, id );
		
		try
		{
			// Executes the query
			ResultSet rs = pstmt.executeQuery();
			
			// Checks for a non null return in the result set
			if ( rs.next() ) {
				rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
				
				// Initializes customer variable
				product = new Product();
				
				// Loads ResultSet returns to product fields
				product.setId( rs.getLong("id") );
				product.setProdName( rs.getString("prodName") );
				product.setProdDescription( rs.getString("prodDescription") );
				product.setProdCategory( rs.getInt("prodCategory") );
				product.setProdUPC( rs.getString("prodUPC") );			
			}
			
			// Returns product information
			return product;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			pstmt.close();
		}
	}

	@Override
	public int update(Connection connection, Product product) throws SQLException, DAOException {
		
		// Checks that product ID is positive number, throws exception is a value is found
		if (product.getId() < 1)
			throw new DAOException("Trying to update product with invalid ID.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for updating with prepared statement
		String updateStmt = "UPDATE customer SET "
				+ "prodName = ?"
				+ ", prodDescription = ?"
				+ ", prodCategory = ?"
				+ ", prodUPC = ?"
				+ " WHERE id = ?";
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( updateStmt );
		
		pstmt.setString(1, product.getProdName() );
		pstmt.setString(2, product.getProdDescription() );
		pstmt.setInt(3, product.getProdCategory() );
		pstmt.setString(4, product.getProdUPC() );
		pstmt.setLong(5, product.getId() );
		try
		{
			return pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			pstmt.close();
		}
	}

	@Override
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
		
		// Checks that product ID is positive number, throws exception is a value is found
		if (id < 1)
			throw new DAOException("Trying to delete Product with invalid ID.");
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for updating with prepared statement
		String deleteStmt = "DELETE FROM product WHERE id = ?";
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( deleteStmt );
		
		pstmt.setLong(1, id );
		
		try
		{
			return pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			pstmt.close();
		}
		
	}

	@Override
	public List<Product> retrieveByCategory(Connection connection, int category) throws SQLException, DAOException {
				
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for searching table
		String categorySearch = "SELECT * FROM product WHERE prodCategory = ?";
		
		// Loads string to PreparedStatement
		pstmt = connection.prepareStatement( categorySearch );
		
		pstmt.setInt(1, category );
		
		
		try
		{
			// Performs query and sends data to ResultSet variable
			ResultSet rs = pstmt.executeQuery();
			
			// Creates a List for returning search results
			List<Product> productList = null;
			
			// Checks if results were found
			if ( rs.next() ) {			
				rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
				
				// If results were found, productList is initialized (otherwise null is returned)
				productList = new ArrayList<Product>();
				
				do
				{
					// Creates new Product object for adding to List array
					Product product = new Product();
					
					// Updates values of product from Result Set
					product.setId( rs.getLong("id") );
					product.setProdName( rs.getString("prodName") );
					product.setProdDescription( rs.getString("prodDescription") );
					product.setProdCategory( rs.getInt("prodCategory") );
					product.setProdUPC( rs.getString("prodUPC") );	
					
					// Adds customer to List
					productList.add(product);			
				} while ( rs.next() ); // Loops until all results are added to list
			}
			
			// Returns generated List
			return productList;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			pstmt.close();
		}
	}

	@Override
	public Product retrieveByUPC(Connection connection, String upc) throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for searching table
		String categorySearch = "SELECT * FROM product WHERE prodUPC = ?";
		
		// Loads string to PreparedStatement
		pstmt = connection.prepareStatement( categorySearch );
		
		pstmt.setString(1, upc );
		
		try
		{
			// Performs query and sends data to ResultSet variable
			ResultSet rs = pstmt.executeQuery();
			
			// Creates a List for returning search results
			Product product = null;
			
			// Checks if results were found
			if ( rs.next() ) {			
				rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
	
				// Creates new Customer object for adding to List array
				product = new Product();
				
				// Updates values of product from Result Set
				product.setId( rs.getLong("id") );
				product.setProdName( rs.getString("prodName") );
				product.setProdDescription( rs.getString("prodDescription") );
				product.setProdCategory( rs.getInt("prodCategory") );
				product.setProdUPC( rs.getString("prodUPC") );	
			}
			
			// Returns product
			return product;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			pstmt.close();
		}
	}
}
