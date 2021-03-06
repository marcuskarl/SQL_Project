package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.util.DAOException;


public class CreditCardDaoImpl implements CreditCardDAO
{

	@Override
	public CreditCard create(Connection connection, CreditCard creditCard, Long customerID)
			throws SQLException, DAOException {

		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for updating with prepared statement
		String updateStmt = "UPDATE CreditCard SET"
				+ " name = ?"
				+ ", ccNumber = ?"
				+ ", expDate = ?"
				+ ", securityCode = ?"
				+ " WHERE customerID = ?";
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( updateStmt );
		
		pstmt.setString(1, creditCard.getName() );
		pstmt.setString(2, creditCard.getCcNumber() );
		pstmt.setString(3, creditCard.getExpDate() );
		pstmt.setString(4, creditCard.getSecurityCode() );
		pstmt.setLong(5, customerID );
		try
		{
			pstmt.executeUpdate();
			return creditCard;
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
	public CreditCard retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for returning row that matches id
		String retrieveCC = "SELECT * FROM creditCard WHERE customerID = ?";
		
		// Places string to PreparedStatement variable as object
		pstmt = connection.prepareStatement( retrieveCC );
		
		pstmt.setLong(1, customerID );
		
		try
		{
			// Executes the query
			ResultSet rs = pstmt.executeQuery();
			
			// Creates CreditCard object to pass information back in method return
			CreditCard creditCard = new CreditCard();
			if (!rs.next())
				return null;
			rs.previous();
			// Checks if results were found
			if ( rs.next() ) {			
				//rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
				
				// Loads data into creditCard object
				creditCard.setName( rs.getString("name") );
				creditCard.setCcNumber( rs.getString("ccNumber") );
				creditCard.setExpDate( rs.getString("expDate") );
				creditCard.setSecurityCode( rs.getString("securityCode") );			
			}
			
			// Returns CreditCard object
			return creditCard;
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
	public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for updating with prepared statement, where all field are set to null
		String updateStmt = "DELETE FROM creditcard"
				+ " WHERE customerID = ?";
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( updateStmt );
		
		pstmt.setLong(1, customerID );
		try
		{
			pstmt.executeUpdate();
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
