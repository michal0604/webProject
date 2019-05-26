package com.johnbryce.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Company;
import com.johnbryce.beans.Coupon;
import com.johnbryce.dao.CompanyDAO;
import com.johnbryce.exception.CompanyException;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.utils.ConnectionPool;

/**
 * this class implement the DB operations associated with the Company's data
 * access requirements.
 * 
 * @author Eivy & Michal
 *
 */

public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool pool;

	/**
	 * cTor for the DBDAO that initiate the resource required for the class
	 * 
	 * @throws CouponException
	 *             for problems from creation.
	 */
	public CompanyDBDAO() throws CouponException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (Exception e) {
			throw new CouponException("connection failed");
		}
	}

	/**
	 * Inserts a company data set to the Database
	 * 
	 * @throws CouponException
	 *             for problems in inserting the company to the DB
	 * @throws SQLException
	 *             for DB related failures
	 * 
	 * 
	 * @see com.johnbryce.dao.CompanyDAO#insertCompany
	 */
	@Override
	public void insertCompany(Company Company) throws CouponException, SQLException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "insert into Company(ID, COMP_NAME, PASSWORD, EMAIL) values (?,?,?,?)";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, Company.getCompanyId());
			pstmt.setString(2, Company.getCompName());
			pstmt.setString(3, Company.getPassword());
			pstmt.setString(4, Company.getEmail());
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			throw new CouponException("Company creation failed " + ex.getMessage());
		} finally {
			connection.close();
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * remove a company from the Database
	 * 
	 * @param company
	 *            company to be remove
	 * @throws CouponException
	 * @throws CompanyRemovalException
	 *             for problems regarding the removal of company from DB
	 * @throws SQLException
	 *             SQLException for DB related failures
	 * 
	 * @see com.johnbryce.dao.CompanyDAO#removeCompany
	 */
	@Override
	public void removeCompany(Company Company) throws CouponException, SQLException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sql = "delete from Company where ID = ?";
		try {
			PreparedStatement pstm1 = connection.prepareStatement(sql);
			connection.setAutoCommit(false);
			pstm1.setLong(1, Company.getCompanyId());
			pstm1.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException("failed to remove Company " + e.getMessage());
		} finally {
			connection.close();
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * updates a company into the Database
	 * 
	 * @param company
	 *            company to update
	 * @throws CompanyException
	 * @throws Exception.CouponException 
	 * @throws CouponException
	 *             regarding the connection problem
	 * @throws CompanyUpdateException
	 *             or problems in updating the company to the DB
	 * @throws SQLException
	 *             for DB related failures
	 * 
	 * @see com.johnbryce.dao.CompanyDAO#updateCompany
	 */
	@Override
	public void updateCompany(Company Company) throws CompanyException{
		try {
			try {
				pool = ConnectionPool.getInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw new CompanyException("connection failed " + e.getMessage());
		}
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CompanyException("connection failed " + e.getMessage());
		}
		try {
			String sql = "update Company set COMP_NAME= ?,PASSWORD = ?, EMAIL= ? where ID = ?";
			PreparedStatement stm1 = connection.prepareStatement(sql);
			stm1.setString(1, Company.getCompName());
			stm1.setString(2, Company.getPassword());
			stm1.setString(3, Company.getEmail());
			stm1.setLong(4, Company.getCompanyId());
			stm1.executeUpdate();
		} catch (SQLException e) {
			throw new CompanyException("update company failed " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CompanyException("connection close failed " + e.getMessage());
			}
			try {
				try {
					pool.returnConnection(connection);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				throw new CompanyException("return connection failed " + e.getMessage());
			}
		}

	}

	/**
	 * get a company data set by the company's id.
	 * 
	 * @param id
	 *            representing the id of the required company
	 * @throws CouponException
	 *             for errors happing due to trying to get a company from DB
	 * @throws SQLException
	 *             for DB related failures
	 * 
	 * @see com.johnbryce.dao.CompanyDAO#getCompany
	 */
	
	@Override
	public Company getCompany(long companyId) throws CouponException, SQLException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Company company = null;

			String sql = "SELECT * FROM Company WHERE ID=" + companyId;
			try {
				PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			if(rs.next()) {
				company =new Company();
				company.setCompanyId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
			}
		} catch (SQLException e) {
			throw new CouponException("unable to get Company data " + e.getMessage());
		} finally {
			connection.close();
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return company;
	}

	/**
	 * get all the Companies from the Database.
	 * 
	 * @throws CouponException
	 *             for errors occurring due to trying to get all companies from DB
	 * @throws SQLException
	 *             for DB related failures
	 * @throws ConnectionException
	 *             error occurring due to connection problems
	 * 
	 * @see com.johnbryce.dao.CompanyDAO#getAllCompanys
	 */
	
	@Override
	public Set<Company> getAllCompanys() throws CouponException, SQLException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Set<Company> set = new HashSet<Company>();
		String sql = "SELECT * FROM Company";
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				long id = rs.getLong(1);
				String compName = rs.getString(2);
				String password = rs.getString(3);
				String email = rs.getString(4);
				set.add(new Company(id, compName, password, email));
			}
		} catch (SQLException e) {
			throw new CouponException("cannot get Company data " + e.getMessage());
		} finally {
			connection.close();
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return set;
	}

	/**
	 * this method returns a company iff the user password is correct.
	 * 
	 * @param name
	 *            company's name of the logged in company
	 * @param password
	 *            company's password of the logged in company
	 * @throws CouponException
	 *             for problem retrieving the company data.
	 * @throws SQLException
	 *             for DB related failures
	 * @throws CompanyException
	 * @throws ConnectionException
	 *             error occurring due to connection problems
	 * 
	 * @see com.johnbryce.dao.CompanyDAO#login(java.lang.String, java.lang.String)
	 */
	@Override
	public Company login(String compName, String password) throws CouponException, SQLException, CompanyException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Company company =null;
		try {
			String sql = "SELECT * FROM Company WHERE COMP_NAME = ? ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, compName);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				company = new Company();
				company.setCompanyId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
				if (company.getPassword().equals(password)) {
					return company;
				}
			}
			return null;
		} catch (SQLException e) {
			throw new CouponException(" Failed to get the company data. " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CompanyException("connection close failed"+e.getMessage());
			}
			try {
				try {
					pool.returnConnection(connection);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				throw new CompanyException("return connection failed"+e.getMessage());
			}
		}
	}

	/**
	 * returns if a company identified by the name exist in the DB records.
	 * 
	 * @param compName
	 *            name that should be checked for existing
	 * @throws CouponException
	 *             for error related to the retrieval of the company
	 * @throws SQLException
	 *             for DB related failures
	 * @throws ConnectionException
	 *             error occurring due to connection problems
	 * 
	 * @see com.johnbryce.dao.CompanyDAO#getAllCompanys
	 */

	@Override
	public boolean isCompanyNameExists(String compName) throws CouponException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String sql = "SELECT ID FROM Company WHERE COMP_NAME = ? ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, compName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;

		} catch (SQLException e) {
			throw new CouponException("Failed to checking if Company name already exists.");
		
		} finally {
			
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed"+e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	
	/** 
	 * this method get company ID, and return list of coupons from data base.
	 * 
	 */
	
	@Override
	public Set<Coupon> getAllCoupons(long companyId) throws CouponException, CompanyException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Set<Coupon> coupons = new HashSet<Coupon>();
		CouponDBDAO couponDB = new CouponDBDAO();
		try  {
			
			String sql = "SELECT COUPON_ID FROM Company_Coupon WHERE COMPANY_ID=?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1,companyId) ;
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				try {
					coupons.add(couponDB.getCoupon(rs.getLong("COUPON_ID")));
				} catch (CreateException e) {
					throw new CompanyException("get coupon failed"+e.getMessage());
				}
			}
		} catch (SQLException e) {
			throw new CouponException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed"+e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return coupons;
		
	}
}
	

	
