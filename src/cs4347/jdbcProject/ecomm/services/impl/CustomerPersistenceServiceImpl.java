package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.dao.impl.AddressDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CreditCardDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CustomerDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.services.CustomerPersistenceService;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	private DataSource dataSource;

	public CustomerPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	/**
	 * This method provided as an example of transaction support across multiple inserts.
	 * 
	 * Persists a new Customer instance by inserting new Customer, Address, 
	 * and CreditCard instances. Notice the transactional nature of this 
	 * method which includes turning off autocommit at the start of the 
	 * process, and rolling back the transaction if an exception 
	 * is caught. 
	 */
	@Override
	public Customer create(Customer customer) throws SQLException, DAOException
	{
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			Customer cust = customerDAO.create(connection, customer);
			Long custID = cust.getId();

			if (cust.getAddress() == null) {
				throw new DAOException("Customers must include an Address instance.");
			}
			Address address = cust.getAddress();
			addressDAO.create(connection, address, custID);

			if (cust.getCreditCard() == null) {
				throw new DAOException("Customers must include an CreditCard instance.");
			}
			CreditCard creditCard = cust.getCreditCard();
			creditCardDAO.create(connection, creditCard, custID);

			connection.commit();
			return cust;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public Customer retrieve(Long id) throws SQLException, DAOException {
		
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		
		try {
			// Retrieves customer information and assigns to Customer variable
			Customer cust = null;
			cust = customerDAO.retrieve(connection, id);
			
			if (cust == null) // Throws error message if id does not exist
				throw new DAOException("Customer not found.");
			
			// Retrieves address information and assigns to Address variable
			Address address = null;
			address = addressDAO.retrieveForCustomerID(connection, id);
			
			if (address == null) // Throws error message if id does not exist
				throw new DAOException("Customers address not found.");
			else // Else assigns address information to customer information
				cust.setAddress(address);
			
			// Retrieves credit card information and assigns to CreditCard variable
			CreditCard creditCard = null;
			creditCard = creditCardDAO.retrieveForCustomerID(connection, id);
			
			if (creditCard == null) // Throws error message if id is not found
				throw new DAOException("Customers creditcard not found.");
			else // Else assigns credit card information to customer information
				cust.setCreditCard(creditCard);
			
			return cust; // Returns collected information
			
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public int update(Customer customer) throws SQLException, DAOException {
		
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();
		
		Connection connection = dataSource.getConnection();
		int numRowsAffected = 0;
		// Updates customer details, address, and credit card information
		try {
			connection.setAutoCommit(false);
			
			numRowsAffected = customerDAO.update(connection, customer);
			addressDAO.create(connection, customer.getAddress(), customer.getId());
			creditCardDAO.create(connection, customer.getCreditCard(), customer.getId());			
			connection.commit();
			return numRowsAffected;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		
	
	}

	@Override
	public int delete(Long id) throws SQLException, DAOException {
		
		CustomerDAO customerDAO = new CustomerDaoImpl();
		Connection connection = dataSource.getConnection();
		int numRowsAffected = 0;
		try {
			connection.setAutoCommit(false);
			
			// Performs delete from customer and cascade deletes to credit card and address
			numRowsAffected = customerDAO.delete(connection, id);
			
			connection.commit();
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		
		return numRowsAffected;
	}

	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException {
		
		CustomerDAO customerDAO = new CustomerDaoImpl();
		Connection connection = dataSource.getConnection();
		
		List<Customer> customerList = null;
		
		try {
			// Calls retrievebyZipCode method and sets customerList to the returns List
			customerList = customerDAO.retrieveByZipCode(connection, zipCode);			
			// Returns list
			return customerList;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}

	}

	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException {
		
		CustomerDAO customerDAO = new CustomerDaoImpl();
		Connection connection = dataSource.getConnection();
		
		List<Customer> customerList = null;
		
		try {
			// Calls retrievebyDOB method and sets customerList to the returns List
			customerList = customerDAO.retrieveByDOB(connection, startDate, endDate);
			
			// Returns list
			return customerList;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}

	}
}
