package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
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
		
		// Begins try block
		try {
			
			// Creates string for inserting into prepared statement
			String insertStmt = "INSERT INTO customers (firstName, lastName, gender, dob, email)"
					+ " values (" + customer.getFirstName() + ", " + customer.getLastName() + ", " + customer.getGender()
					+ ", " + customer.getDob() + ", " + customer.getEmail() + ")";
			
			// Sends prepared statement object to the PreparedStatement variable, with the string inserted
			pstmt = connection.prepareStatement( insertStmt );
			
			// Performs the insert command
			pstmt.executeUpdate();
			
			// Prepares string in order to get the id that was just created in the insert operation
			String getInsertedID = "SELECT SCOPE_IDENTITY()";
			
			// Places string to PreparedStatement variable as object
			pstmt = connection.prepareStatement( getInsertedID );
			
			// Performs the query
			ResultSet rs = pstmt.executeQuery();
			
			// Checks for a non null return in the result set
			if (rs.next())
				customer.setId( rs.getLong("id") );
		}
		finally {
			if (pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer retrieve(Connection connection, Long id) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Connection connection, Customer customer) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Customer> retrieveByZipCode(Connection connection, String zipCode) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> retrieveByDOB(Connection connection, Date startDate, Date endDate)
			throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
