package com.johnbryce.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.johnbryce.beans.Company;
import com.johnbryce.beans.Customer;
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
	@Path("createCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCompany(@QueryParam("name") String compName, @QueryParam("pass") String password,
			@QueryParam("email") String email) {

		System.out.println(compName+" "+password +" "+email);
		AdminFacad admin = getFacade();
		String failMsg = "FAILED TO ADD A NEW COMPANY: " + "There is already a company with the same name: " + compName
				+ " - please change the company name"; // debug

		Company company = new Company(compName, password, email);

		try {
			if (admin.createCompany(company) != null) {
				return "SUCCEED TO ADD A NEW COMPANY: name = " + compName + ", id = " + company.getCompanyId();
			}
		} catch (DbdaoException | FacadeException e) {
			e.printStackTrace();
		}

		return failMsg;

	}

	// REMOVE a Company
	@GET
	@Path("removeCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(@QueryParam("compId") long id) {

		String failMsg = "FAILED TO REMOVE A COMPANY: there is no such id! " + id
				+ " - please enter another company id";

		AdminFacad admin = getFacade();
		Company company = null;
		try {
			company = admin.getCompany(id);
			if (company != null) {
				admin.removeCompany(company);
				return "SUCCEED TO REMOVE A COMPANY: name = " + company.getCompName() + ", id = " + id;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return failMsg;
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
				admin.updateCompany(company, password, email);
				return "SUCCEED TO UPDATE A COMPANY: pass = " + company.getPassword() + ",e-mail = "
						+ company.getEmail() + ", id = " + id;
			}
		} catch (DbdaoException e) {
			e.printStackTrace();
		}

		return "FAILED TO UPDATE A COMPANY: there is no such id! " + id + " - please enter another company id";

	}

	@GET
	@Path("getAllCompanies")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllCompanies() {

		// Getting the session and the logged in facade object
		AdminFacad admin = getFacade();

		// Get the List of all the Companies from the Table in the DataBase

		try {
			Collection<Company> companies = admin.getAllCompanies();
			//TODO Collection<CompanyInfo> companiesInfo = new ArrayList<>();

			if (!companies.isEmpty()) {
				for (Company company : companies) {
					//TODO CompanyInfo comapnyInfo = new CompanyInfo(company);
					//TODO companiesInfo.add(comapnyInfo);
				}
			}

			return new Gson().toJson(companies);

		} catch (CouponException e) {
			e.printStackTrace();
		}

		System.out.println("AdminService: FAILED GET ALL COMOPANIES: there are no companies in the DB table!"); // for
		return null;
	}

	@GET
	@Path("getCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCompany(@QueryParam("compId") long id) {

		AdminFacad admin = getFacade();

		try {
			Company company = admin.getCompany(id);
			if (company != null) {
				return new Gson().toJson(company);				
			}
		} catch (DbdaoException e) {
			e.printStackTrace();
		}

		System.err
				.println("FAILED GET COMPANY BY ID: there is no such id!" + id + " - please enter another company id"); // for

		return null;
	}

	// CREATE a new Customer - add a customer to the Customer Table in DB
	@GET
	@Path("createCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String Customer(@QueryParam("name") String custName, @QueryParam("pass") String password) {

		AdminFacad admin = getFacade();

		Customer customer = new Customer(0,custName, password);
		

			if (admin.createCustomer(customer) != null) {
				return "SUCCEED TO ADD A NEW CUSTOMER: name = " + custName + ", id = " + customer.getCustomerId();
			}
		
		return "FAILED TO ADD A NEW CUSTOMER: " + "There is already a customer	 with the same name: " + custName
				+ " - please change the customer name";

	}

	// REMOVE Customer
	@GET
	@Path("removeCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCustomer(@QueryParam("custId") long id) {

		AdminFacad admin = getFacade();

		try {
			Customer customer = admin.getCustomer(id);
			if (customer != null) {
				admin.removeCustomer(customer);
				return "SUCCEED TO REMOVE A CUSTOMER: name = " + customer.getCustomerName() + ", id = " + id;
			}

		} catch (CouponException e) {
			e.printStackTrace();
		}

		return "FAILED TO REMOVE A CUSTOMER: there is no such id! " + id + " - please enter another customer id";
	}

	// UPDATE a customer
	@GET
	@Path("updateCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCustomer(@QueryParam("custId") long id, @QueryParam("pass") String password) {

		AdminFacad admin = getFacade();

		Customer customer;
		try {
			customer = admin.getCustomer(id);
			if (customer != null) {
				admin.updateCustomer(customer, password);
				return "SUCCEED TO UPDATE A CUSTOMER: pass = " + customer.getPassword() + ", id = " + id;
			}
		} catch (DbdaoException e) {
			e.printStackTrace();
		}

		return "FAILED TO UPDATE A CUSTOMER: there is no such id! " + id + " - please enter another customer id";
	}

	@GET
	@Path("getAllCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllCustomers() {

		AdminFacad admin = getFacade();

		// Provide a List of all the Customers from the Table in the DataBase

		try {
			Collection<Customer> customers = admin.getAllCustomers();
			// TODO  Collection<CustomerInfo> customersInfo = new ArrayList<>();

			if (!customers.isEmpty()) {
				for (Customer customer : customers) {
					System.out.println(customer.getCustomerName() + ", id = " + customer.getCustomerId()); // for
					//TODO CustomerInfo webCustomer = new CustomerInfo(customer);
					//TODO customersInfo.add(webCustomer);
				}
				return new Gson().toJson(customers);
			}

		} catch (CouponException e) {
			e.printStackTrace();
		}

		System.out.println("AdminService: FAILED GET ALL CUSTOMERS: there are no customers in the DB table!");
		return null;
	}

	@GET
	@Path("getCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCustomer(@QueryParam("custId") long id) {

		// Getting the session and the logged in facade object
		AdminFacad admin = getFacade();

		try {
			Customer customer = admin.getCustomer(id);
			if (customer != null) {
				System.out.println(customer.getCustomerName() + ", id = " + customer.getCustomerId()); // for
				return new Gson().toJson(customer);
			}
		} catch (CouponException e) {
			e.printStackTrace();
		}

		System.out.println(
				"FAILED GET CUSTOMER BY ID: there is no such id!" + id + " - please enter another customer id");

		return null;
	}

}