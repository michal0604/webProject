package com.johnbryce.service;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.johnbryce.beans.Company;
import com.johnbryce.dao.CompanyDAO;
import com.johnbryce.exception.CouponException;
import com.johnbryce.facad.AdminFacad;

@Path("admin")
public class AdminService {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private AdminFacad getFacade() {

		AdminFacad admin = null;
		admin = (AdminFacad) request.getSession(false).getAttribute("facade");
		return admin;
	}
	
	
	
	// Create a new company in the db
	@GET
	@Consumes({MediaType.TEXT_PLAIN}) 
	@Produces({MediaType.APPLICATION_JSON})
	@Path("createCompany")
	public String createCompany(@QueryParam("name") String name,
								@QueryParam("password") String password,
								@QueryParam("email") String email) throws Exception {
		
		AdminFacad admin = getFacade();
		
		Company company = new Company();
		company.setCompName(name);
		company.setPassword(password);
		company.setEmail(email);
		try {
		admin.createCompany(company);
		return "Company " + name + " Succesfully Created. ";

		}
		 catch (Exception e2) {
			 
				try {
					throw new Exception("create company failed by admin");
				} catch (Exception e) {
					throw new Exception("create company failed by admin");
				}
			 }
		 
		 }
	
	@GET
	@Consumes({MediaType.TEXT_PLAIN})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("removeCompany")
	
	public String removeCompany(@QueryParam("compId") long compId) throws CouponException {
		AdminFacad admin = getFacade();
		Company company = null;
		String name;
		try {
			name = admin.getCompany(compId).getCompName();
		} catch (CouponException e1) {
			throw new CouponException("didnt success to get company");
		}
		try {
			admin.removeCompany(company);
			return "Company " + name + " Succesfully Removed. ";
		} catch (Exception e) {
			e.printStackTrace();
			return "Remove Company Failed";
		}
	}


	// UPDATE a company
		@GET
		@Path("updateCompany")
		@Produces(MediaType.TEXT_PLAIN)
		public String updateCompany(@QueryParam("compId") long id, @QueryParam("pass") String password,
				@QueryParam("email") String email) {

			AdminFacad admin = getFacade();

			try {
				Company company = admin.getCompany(id);

				if (company != null) {
					
					company.setPassword(password);
					company.setEmail(email);
					admin.updateCompany(company,password,email);
					return "SUCCEED TO UPDATE A COMPANY: pass = " + company.getPassword() + ",e-mail = "
							+ company.getEmail() + ", id = " + id;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "FAILED TO UPDATE A COMPANY";

		}

	/**
	 * This method get company id and return the line from Company table as Company object.
	 * @param id
	 * @return
	 * @throws CouponException
	 */
		@GET
		@Path("getCompany")
		@Produces(MediaType.TEXT_PLAIN)
		public String getCompany(@QueryParam("compId") long id) {

			AdminFacad admin = getFacade();

			try {
				Company company = admin.getCompany(id);
				if (company != null) {
					return new Gson().toJson(new CompanyInfo(company));				
				}
			} catch (DbdaoException e) {
				e.printStackTrace();
			}

			System.err
					.println("FAILED GET COMPANY BY ID: there is no such id!" + id + " - please enter another company id"); // for

			return null;
		}

	/**
	 * This method return all the Companies as a list.
	 * @return
	 * @throws CouponException
	 */
		@GET
		@Path("getAllCompanies")
		@Produces(MediaType.TEXT_PLAIN)
		public String getAllCompanies() {

			// Getting the session and the logged in facade object
			AdminFacad admin = getFacade();

			// Get the List of all the Companies from the Table in the DataBase

			try {
				Set<Company> companies = admin.getAllCompanies();
				Set<CompanyInfo> companiesInfo = new HashSet<>();

				if (!companies.isEmpty()) {
					for (Company company : companies) {
						CompanyInfo comapnyInfo = new CompanyInfo(company);
						companiesInfo.add(comapnyInfo);
					}
				}

				return new Gson().toJson(companiesInfo);

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("AdminService: FAILED GET ALL COMOPANIES"); 
			return null;
		}

	/**
	 * This method get Customer as object and send it to create line in Customer table.
	 * first we check that the name not exist.
	 * @param customer
	 * @throws CouponException
	 */
		@GET
		@Path("createCustomer")
		@Produces(MediaType.TEXT_PLAIN)
		public String Customer(@QueryParam("name") String custName, @QueryParam("pass") String password) {

			AdminFacad admin = getFacade();
			Customer customer = new Customer(custName, password);
		if (customer != null) {
			String custName = customer.getCustomerName();
			if (custName != null) {
				if (customer.getPassword() != null) {
					try {
						if (!customerDAO.isCustomerNameExists(custName)) {
							customerDAO.insertCustomer(customer);
						}
					} catch (CustomerException e) {
						throw new CouponException("create customer by admin failed");

					} catch (CreateException e) {
						throw new CouponException("create customer by admin failed");
					}
				}
			}
		}
	}

	/**
	 *  This method get Customer as object and take all its coupons and delete all the lines with those coupons
	 * from Customer_Coupon table and the line from Customer table
	 * @param customer
	 * @throws CouponException
	 */
	public void removeCustomer(Customer customer) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		try {
			customer_CouponDAO.removeCustomer_Coupon(customer);
		} catch (RemoveException e) {
			throw new CouponException("remove customer_coupon by admin failed");
		}
		try {
			customerDAO.removeCustomer(customer);
		} catch (RemoveException e) {
			throw new CouponException("remove customer by admin failed");
		}

	}

	/**
	 *  This method get Customer,and new password as object and send it to method that update line in the Customer table.
	 * The method don't update the name
	 * @param customer
	 * @param newpassword
	 * @throws CouponException
	 */
	public void updateCustomer(Customer customer, String newpassword) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}

		customer.setPassword(newpassword);
		try {
			customerDAO.updateCustomer(customer);
		} catch (UpdateException e) {
			throw new CouponException("update customer by admin failed");
		}

	}

	/**
	 *  This method return all the Customers as a list.
	 * @return
	 * @throws CouponException
	 */
	public Set<Customer> getAllCustomers() throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		try {
			return customerDAO.getAllCustomers();
		} catch (CustomerException e) {
			throw new CouponException("get all customers by admin failed");
		}
	}

	/**
	 * This method get Customer id and return the line from Customer table as Customer object.
	 * @param id
	 * @return
	 * @throws CouponException
	 */
	public Customer getCustomer(long id) throws CouponException {
		if (!isLogedIn) {
			throw new CouponException("the operation was canceled due to not being loged in");
		}
		try {
			return customerDAO.getCustomer(id);
		} catch (CustomerException e) {
			throw new CouponException("get customer by admin failed");
		}
	}

}