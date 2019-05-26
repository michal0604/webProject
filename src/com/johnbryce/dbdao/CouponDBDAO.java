package com.johnbryce.dbdao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.dao.CouponDAO;
import com.johnbryce.exception.CouponException;
import com.johnbryce.exception.CreateException;
import com.johnbryce.exception.RemoveException;
import com.johnbryce.exception.UpdateException;
import com.johnbryce.utils.ConnectionPool;

/**
 * this class implements data base operations for Coupon's requirements.
 * 
 * @author Eivy & Michal
 *
 */
public class CouponDBDAO implements CouponDAO {
	private ConnectionPool pool;

	/**
	 * cTor for the coupon object.
	 * @throws Exception.CouponException 
	 * 
	 * @throws CouponException
	 * @throws CouponException 
	 */
	public CouponDBDAO() throws CouponException  {
		try {
			try {
				pool = ConnectionPool.getInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw new CouponException("connection failed");
		}
	}

	/**
	 * Inserts a coupon data set to the Database
	 * 
	 * @param coupon
	 *            coupon to be inserted
	 * @throws CreateException
	 *             for problems in inserting the coupon to the DB
	 * @throws SQLException
	 *             for DB related failures
	 */
	@Override
	public void insertCoupon(Coupon coupon) throws CreateException {

		Connection connection = null;
		try {
			try {
				connection = pool.getConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw new CreateException("didnt success to connect " + e.getMessage());
		}
		String sql = "INSERT INTO Coupon (ID,title,start_date,end_date,amount,type,message,price,image) VALUES(?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, coupon.getCouponId());
			pstmt.setString(2, coupon.getTitle());
			pstmt.setDate(3, (Date) coupon.getStart_date());
			pstmt.setDate(4, (Date) coupon.getEnd_date());
			pstmt.setInt(5, coupon.getAmount());
			pstmt.setString(6, coupon.getType().name());
			pstmt.setString(7, coupon.getMessage());
			pstmt.setDouble(8, coupon.getPrice());
			pstmt.setString(9, coupon.getImage());
			pstmt.executeUpdate();

		} catch (SQLException ex) {
			throw new CreateException("Coupon creation failed " + ex.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e1) {
				throw new CreateException("close connection was failed " + e1.getMessage());
			}
			try {
				try {
					pool.returnConnection(connection);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				throw new CreateException("return connection was failed " + e.getMessage());
			}

		}
	}

	/**
	 * this function removed a given coupon from the coupon data
	 * 
	 * @param Coupon
	 *            to be removed.
	 * 
	 * @throws RemoveException
	 * @throws CreateException
	 */
	@Override
	public void removeCoupon(Coupon Coupon) throws CreateException, RemoveException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CreateException("didnt success to connect " + e.getMessage());
		}
		String sql = "DELETE FROM Coupon WHERE ID=?";
		try {
			PreparedStatement pstm1 = connection.prepareStatement(sql);
			connection.setAutoCommit(false);
			pstm1.setLong(1, Coupon.getCouponId());
			pstm1.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RemoveException("Database error " + e1.getMessage());
			}
			throw new RemoveException("failed to remove Coupon " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RemoveException("connection close failed"+e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new RemoveException("return connection failed"+e.getMessage());
			}

		}
	}

	/**
	 * updates a coupon into the Database
	 * 
	 * @param coupon
	 *            coupon to update
	 * @throws UpdateException
	 *             for problems in updating the coupon to the DB
	 * @throws CreateException
	 *             for problems in inserting the coupon to the DB
	 */
	@Override
	public void updateCoupon(Coupon Coupon) throws UpdateException, CreateException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CreateException("didnt success to connect " + e.getMessage());
		}
		try {
			String sql = "UPDATE Coupon SET TITLE=?, START_DATE=?, END_DATE=?, AMOUNT=?,"
					+ " TYPE=?, MESSAGE=?, PRICE=?, IMAGE=? WHERE ID=?";
			PreparedStatement stm1 = connection.prepareStatement(sql);
			stm1.setString(1, Coupon.getTitle());
			stm1.setDate(2, (Date) Coupon.getStart_date());
			stm1.setDate(3, (Date) Coupon.getEnd_date());
			stm1.setInt(4, Coupon.getAmount());
			stm1.setString(5, Coupon.getType().toString());
			stm1.setString(6, Coupon.getMessage());
			stm1.setDouble(7, Coupon.getPrice());
			stm1.setString(8, Coupon.getImage());
			stm1.setLong(9, Coupon.getCouponId());
			stm1.executeUpdate();
		} catch (SQLException e) {
			throw new UpdateException("update Coupon failed " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new UpdateException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CreateException(" return connection failed " + e.getMessage());
			}
		}
	}

	/**
	 * get a coupon data set by the coupon's id.
	 * 
	 * @param couponId
	 *            representing the id of the required coupon
	 * @return a coupon which her id is couponId.
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 * @throws CreateException
	 */
	@Override
	public Coupon getCoupon(long couponId) throws CreateException, CouponException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CreateException("didnt success to connect " + e.getMessage());
		}
		Coupon coupon = null;
			String sql = "SELECT * FROM Coupon WHERE ID=" + couponId;
			try {
				PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			if (rs.next()) {
				coupon = new Coupon();
				coupon.setCouponId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_date(rs.getDate(3));
				coupon.setEnd_date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				switch (rs.getString(6)) {
				case "Food":
					coupon.setType(CouponType.Food);
					break;
				case "Resturans":
					coupon.setType(CouponType.Resturans);
					break;
				case "Electricity":
					coupon.setType(CouponType.Electricity);
					break;
				case "Health":
					coupon.setType(CouponType.Health);
					break;
				case "Sports":
					coupon.setType(CouponType.Sports);
					break;
				case "Camping":
					coupon.setType(CouponType.Camping);
					break;
				case "Traveling":
					coupon.setType(CouponType.Traveling);
					break;
				default:
					System.out.println("Coupon not existent");
					break;
				}
			}
		} catch (SQLException e) {
			throw new CouponException("unable to get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return coupon;
	}

	/**
	 * get a list of all available coupons
	 * 
	 * @return a list of available coupons
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 */
	@Override
	public Set<Coupon> getAllCoupons() throws CouponException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("connection failed" + e.getMessage());
		}
		Set<Coupon> set = new HashSet<Coupon>();
		Coupon coupon;
		String sql = "SELECT * FROM Coupon";
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setCouponId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_date(rs.getDate(3));
				coupon.setEnd_date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				switch (rs.getString(6)) {
				case "Food":
					coupon.setType(CouponType.Food);
					break;
				case "Resturans":
					coupon.setType(CouponType.Resturans);
					break;
				case "Electricity":
					coupon.setType(CouponType.Electricity);
					break;
				case "Health":
					coupon.setType(CouponType.Health);
					break;
				case "Sports":
					coupon.setType(CouponType.Sports);
					break;
				case "Camping":
					coupon.setType(CouponType.Camping);
					break;
				case "Traveling":
					coupon.setType(CouponType.Traveling);
					break;
				default:
					System.out.println("Coupon not existent");
					break;
				}
				set.add(coupon);
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new CouponException("cannot get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed" + e.getMessage());
			}
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
	 * this function remove a coupon by it's id
	 * 
	 * @param couponId
	 *            of the coupon to be deleted
	 * 
	 * @throws CouponException
	 */
	@Override
	public void removeCouponID(long couponId) throws CouponException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("Database error " + e.getMessage());
		}
		String sql = "DELETE FROM Coupon WHERE ID=?";
		try {
			PreparedStatement pstm1 = connection.prepareStatement(sql);
			connection.setAutoCommit(false);
			pstm1.setLong(1, couponId);
			pstm1.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();

			} catch (SQLException e1) {
				throw new CouponException("Database error " + e1.getMessage());
			}
			throw new CouponException("failed to remove Coupon " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CouponException("return connection failed " + e.getMessage());
			}
		}

	}

	/**
	 * this function returns all available coupons by specific max coupon
	 * exploration Date
	 * 
	 * @param untilDate
	 *            the date that a coupon's expire Date cannot exceed
	 * @return a list of available coupon that expire before or the same date as
	 *         untilDate
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 */
	@Override
	public Set<Coupon> getAllCouponsByDate(String untilDate) throws CouponException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("Database error " + e.getMessage());
		}
		Set<Coupon> set = new HashSet<Coupon>();
		Coupon coupon;
		String sql = "select * from Coupon where end_date=" + untilDate;
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setCouponId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_date(rs.getDate(3));
				coupon.setEnd_date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				switch (rs.getString(6)) {
				case "food":
					coupon.setType(CouponType.Food);
					break;
				case "Resturans":
					coupon.setType(CouponType.Resturans);
					break;
				case "Electricity":
					coupon.setType(CouponType.Electricity);
					break;
				case "Health":
					coupon.setType(CouponType.Health);
					break;
				case "Sports":
					coupon.setType(CouponType.Sports);
					break;
				case "Camping":
					coupon.setType(CouponType.Camping);
					break;
				case "Traveling":
					coupon.setType(CouponType.Traveling);
					break;
				default:
					System.out.println("Coupon not existent");
					break;
				}
				set.add(coupon);
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new CouponException("cannot get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CouponException("return connection failed " + e.getMessage());
			}
		}
		return set;
	}

	
	/**
	 * this function returns all available coupons by specific type
	 * 
	 * @param coupType
	 *            the required coupon type
	 * @return all available coupons that much the type
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 */

	public Set<Coupon> getAllCouponsByType(CouponType couponType) throws CouponException {

		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Set<Coupon> list = new HashSet<>();
			String sql = String.format("select * from Coupon where TYPE = '%s'", couponType);
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet resultSet = stm1.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = new Coupon();
				coupon.setCouponId(resultSet.getLong(1));
				coupon.setTitle(resultSet.getString(2));
				coupon.setStart_date(resultSet.getDate(3));
				coupon.setEnd_date(resultSet.getDate(4));
				coupon.setAmount(resultSet.getInt(5));
				switch (resultSet.getString(6)) {
				case "Food":
					coupon.setType(CouponType.Food);
					break;
				case "Resturans":
					coupon.setType(CouponType.Resturans);
					break;
				case "Electricity":
					coupon.setType(CouponType.Electricity);
					break;
				case "Health":
					coupon.setType(CouponType.Health);
					break;
				case "Sports":
					coupon.setType(CouponType.Sports);
					break;
				case "Camping":
					coupon.setType(CouponType.Camping);
					break;
				case "Traveling":
					coupon.setType(CouponType.Traveling);
					break;
				default:
					break;
				}
				coupon.setMessage(resultSet.getString(7));
				coupon.setPrice(resultSet.getDouble(8));
				coupon.setImage(resultSet.getString(9));

				list.add(coupon);
			}
			return list;
		} catch (SQLException e) {
			System.out.println(e);
			throw new CouponException("cannot get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed " + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CouponException("return connection failed " + e.getMessage());
			}
		}

	}

	/**
	 * this function returns all available coupons by specific max coupon price
	 * 
	 * @param priceMax
	 *            the price that a coupon's price cannot exceed
	 * @return a list of available coupon that are lower or equal to PriceMax
	 * @throws CouponException
	 *             for error related to the retrieval of the coupon
	 */
	
	@Override
	public Set<Coupon> getAllCouponsByPrice(double priceMax) throws CouponException {
		Connection connection;
		try {
			connection = pool.getConnection();
		} catch (Exception e) {
			throw new CouponException("Database error " + e.getMessage());
		}
		Set<Coupon> set = new HashSet<Coupon>();
		Coupon coupon;
		String sql = "select * from Coupon where Price = " + priceMax;
		try {
			PreparedStatement stm1 = connection.prepareStatement(sql);
			ResultSet rs = stm1.executeQuery();
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setCouponId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStart_date(rs.getDate(3));
				coupon.setEnd_date(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				switch (rs.getString(6)) {
				case "food":
					coupon.setType(CouponType.Food);
					break;
				case "Resturans":
					coupon.setType(CouponType.Resturans);
					break;
				case "Electricity":
					coupon.setType(CouponType.Electricity);
					break;
				case "Health":
					coupon.setType(CouponType.Health);
					break;
				case "Sports":
					coupon.setType(CouponType.Sports);
					break;
				case "Camping":
					coupon.setType(CouponType.Camping);
					break;
				case "Traveling":
					coupon.setType(CouponType.Traveling);
					break;
				default:
					System.out.println("Coupon not existent");
					break;
				}
				set.add(coupon);
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new CouponException("cannot get Coupon data " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponException("connection close failed" + e.getMessage());
			}
			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
				throw new CouponException("return connection failed " + e.getMessage());
			}
		}
		return set;
	}

	/**
	 * this function search if a given string exist as a title of one of the
	 * coupons.
	 * 
	 * @param coupTitle
	 *            a string representing a coupon title
	 * 
	 * @return true if there is such a coupon title, false otherwise.
	 * @throws CouponException
	 */
	@Override
	public boolean isCouponTitleExists(String Title) throws CouponException {
		Connection connection = null;
		try {
			connection = pool.getConnection();
		} catch (Exception e1) {
			throw new CouponException("connection failed");
		}
		try {
			String sql = "SELECT ID FROM Coupon WHERE title = ? ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, Title);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;

		} catch (SQLException e) {

			throw new CouponException("  Checking if Coupon Title Exists Failed."+e.getMessage());

		} finally {
			try {
				connection.close();
			} catch (SQLException e1) {
				throw new CouponException("connection close failed"+e1.getMessage());
			}

			try {
				pool.returnConnection(connection);
			} catch (Exception e) {
			}

		}
	}

	
		
	}
