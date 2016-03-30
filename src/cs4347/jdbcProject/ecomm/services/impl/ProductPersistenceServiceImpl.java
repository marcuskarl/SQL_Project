package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.dao.impl.ProductDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.services.ProductPersistenceService;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	private DataSource dataSource;

	public ProductPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public Product create(Product product) throws SQLException, DAOException {
		
		ProductDAO productDAO = new ProductDaoImpl();		
		Connection connection = dataSource.getConnection();

		try {
			connection.setAutoCommit(false);
			Product prod = productDAO.create(connection, product);

			connection.commit();
			return prod;
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
	public Product retrieve(Long id) throws SQLException, DAOException {
		
		ProductDAO productDAO = new ProductDaoImpl();		
		Connection connection = dataSource.getConnection();

		try {
			Product prod = productDAO.retrieve(connection, id);

			return prod;
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
	public int update(Product product) throws SQLException, DAOException {
		
		ProductDAO productDAO = new ProductDaoImpl();		
		Connection connection = dataSource.getConnection();
		
		// Updates product details
		try {
			connection.setAutoCommit(false);
			
			productDAO.update(connection, product);
			
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
		
		ProductDAO productDAO = new ProductDaoImpl();		
		Connection connection = dataSource.getConnection();
		
		try {
			connection.setAutoCommit(false);
			
			// Performs product delete
			productDAO.delete(connection, id);
			
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
	public Product retrieveByUPC(String upc) throws SQLException, DAOException {
		
		ProductDAO productDAO = new ProductDaoImpl();		
		Connection connection = dataSource.getConnection();
		
		Product product = null;
		
		try {
			product = productDAO.retrieveByUPC(connection, upc);
			
			// Calls retrievebyZipCode method and sets customerList to the returns List
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		
		// Returns product
		return product;
	}

	@Override
	public List<Product> retrieveByCategory(int category) throws SQLException, DAOException {
		
		ProductDAO productDAO = new ProductDaoImpl();		
		Connection connection = dataSource.getConnection();
		
		List<Product> productList = null;
		
		try {
			// Calls method to create List of matching entries
			productList = productDAO.retrieveByCategory(connection, category);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		
		// Returns product
		return productList;
	}
}