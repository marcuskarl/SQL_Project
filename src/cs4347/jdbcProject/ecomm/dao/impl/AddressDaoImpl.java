package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class AddressDaoImpl implements AddressDAO
{

	@Override
	public Address create(Connection connection, Address address, Long customerID) throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for updating with prepared statement
		String updateStmt = "UPDATE customer SET"
				+ " address1 = " + address.getAddress1()
				+ ", address2 = " + address.getAddress2()
				+ ", city = " + address.getCity()
				+ ", state = " + address.getState()
				+ ", zipcode = " + address.getZipcode()
				+ " WHERE customerID = " + customerID;
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( updateStmt );
		pstmt.executeUpdate();	
		
		return null;
	}

	@Override
	public Address retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
					
		// Creates string for returning row that matches id
		String retrieveAddress = "SELECT * FROM address WHERE customerID = " + customerID;
		
		// Places string to PreparedStatement variable as object
		pstmt = connection.prepareStatement( retrieveAddress );
		
		// Executes the query
		ResultSet rs = pstmt.executeQuery();
		
		// Creates object for holding to pass in method return
		Address address = new Address();
		
		// Checks if results were found
		if ( rs.next() ) {			
			rs.previous(); // Moves pointer back to first entry (from rs.next() in if statement check)
			
			address.setAddress1( rs.getString("address1") );
			address.setAddress2( rs.getString("address2") );
			address.setCity( rs.getString("city") );
			address.setState( rs.getString("state") );
			address.setZipcode( rs.getString("zipcode") );
		}
		
		// Returns Address object with customer information
		return address;
	}

	@Override
	public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		
		// Creates PreparedStatement variable
		PreparedStatement pstmt = null;
		
		// Creates string for updating with prepared statement, where all field are set to null
		String updateStmt = "UPDATE customer SET"
				+ " address1 = null"
				+ ", address2 = null"
				+ ", city = null"
				+ ", state = null"
				+ ", zipcode = null"
				+ " WHERE customerID = " + customerID;
		
		// Inserts string into PreparedStatement and then performs the operation
		pstmt = connection.prepareStatement( updateStmt );
		pstmt.executeUpdate();		
	}
}
