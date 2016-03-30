package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.dao.impl.PurchaseDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchasePersistenceService;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	private DataSource dataSource;

	public PurchasePersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public Purchase create(Purchase purchase) throws SQLException, DAOException {
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		try {
			connection.setAutoCommit(false);
			Purchase purch = purchaseDAO.create(connection, purchase);

			connection.commit();
			return purch;
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
	public Purchase retrieve(Long id) throws SQLException, DAOException {
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();

		try {
			Purchase purch = purchaseDAO.retrieve(connection, id);

			return purch;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public int update(Purchase purchase) throws SQLException, DAOException {
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		// Updates product details
		try {
			connection.setAutoCommit(false);
			
			purchaseDAO.update(connection, purchase);
			
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
		
		return 0;
	}

	@Override
	public int delete(Long id) throws SQLException, DAOException {
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		try {
			connection.setAutoCommit(false);
			
			// Performs product delete
			purchaseDAO.delete(connection, id);
			
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
		
		return 0;
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException {
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		List<Purchase> purchaseList = null;
		
		try {
			// Calls method to create List of matching entries
			purchaseList = purchaseDAO.retrieveForCustomerID(connection, customerID);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		
		// Returns product
		return purchaseList;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException {
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		List<Purchase> purchaseList = null;
		
		try {
			// Calls method to create List of matching entries
			purchaseList = purchaseDAO.retrieveForProductID(connection, productID);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		
		// Returns product
		return purchaseList;
	}
}
