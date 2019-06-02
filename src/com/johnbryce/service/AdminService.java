package com.johnbryce.service;

import java.util.HashSet;
import java.util.Set;

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

		AdminFacad admin = getFacade();
		Company company = new Company(compName, password, email);
		try {
			admin.createCompany(company);
			return "Succeded to add a new company: name = " + compName;
		} catch (CouponException e) {
			return "Failed to Add a new Company:" + e.getMessage();
		}

	}

	// REMOVE a Company
	@GET
	@Path("removeCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(@QueryParam("compId") long id) {

		AdminFacad admin = getFacade();
		try {
			Company company = admin.getCompany(id);
			if (company != null) {
				admin.removeCompany(company);
				return "Succeded to remove a company: name = " + company.getCompName() + ", id = " + id;
			} else {
				return "Failed to remove a company: the provided company id is invalid";
			}
		} catch (CouponException e) {
			return "Failed to remove a company: " + e.getMessage();
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
				admin.updateCompany(company, password, email);
				return "Succeded to update a company: pass = " + company.getPassword() + ",e-mail = "
						+ company.getEmail() + ", id = " + id;
			} else {
				return "Failed to update a company: the provided company id is invalid";
			}
		} catch (CouponException e) {
			return "Failed to update a company: " + e.getMessage();
		}

	}

	@GET
	@Path("getAllCompanies")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllCompanies() {
		AdminFacad admin = getFacade();
		Set<Company> companies;
		try {
			companies = admin.getAllCompanies();
		} catch (CouponException e) {
			System.err.println("Get all Companies failed: " + e.getMessage());
			companies = new HashSet<Company>();
		}
		return new Gson().toJson(companies);
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
			} else {
				return null;
			}
		} catch (CouponException e) {
			System.err.println("get company by id failed " + e.getMessage());
			return null;
		}
	}

	@GET
	@Path("createCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String Customer(@QueryParam("name") String custName, @QueryParam("pass") String password) {

		AdminFacad admin = getFacade();
		Customer customer = new Customer(custName, password);
		try {
			admin.createCustomer(customer);
			return "Succeded to add a new customer: name = " + custName;
		} catch (CouponException e) {
			return "Failed to Add a new customer:" + e.getMessage();
		}
	}

	@GET
	@Path("removeCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCustomer(@QueryParam("custId") long id) {

		AdminFacad admin = getFacade();

		try {
			Customer customer = admin.getCustomer(id);
			if (customer != null) {
				admin.removeCustomer(customer);
				return "Succeded to remove a customer: name = " + customer.getCustomerName();
			} else {
				return "Failed to remove a customer: the provided customer id is invalid";
			}
		} catch (CouponException e) {
			return "Failed to remove a customer: " + e.getMessage();
		}

	}

	// UPDATE a customer
	@GET
	@Path("updateCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCustomer(@QueryParam("custId") long id, @QueryParam("pass") String password) {
		AdminFacad admin = getFacade();
		try {
			Customer customer = admin.getCustomer(id);
			if (customer != null) {
				admin.updateCustomer(customer, password);
				return "Succeded to update a customer:: pass = " + customer.getPassword();
			} else {
				return "Failed to update a customer: the provided customer id is invalid";
			}
		} catch (CouponException e) {
			return "Failed to update a customer: " + e.getMessage();
		}
	}

	@GET
	@Path("getAllCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllCustomers() {
		AdminFacad admin = getFacade();
		Set<Customer> customers;
		try {
			customers = admin.getAllCustomers();
		} catch (CouponException e) {
			System.err.println("Get all customers failed: " + e.getMessage());
			customers = new HashSet<Customer>();
		}
		return new Gson().toJson(customers);
	}

	@GET
	@Path("getCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCustomer(@QueryParam("custId") long id) {
		AdminFacad admin = getFacade();
		try {
			Customer customer = admin.getCustomer(id);
			if (customer != null) {
				return new Gson().toJson(customer);
			} else {
				return null;
			}
		} catch (CouponException e) {
			System.err.println("get customer by id failed " + e.getMessage());
			return null;
		}
	}

}