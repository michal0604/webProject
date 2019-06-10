package com.johnbryce.service;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.johnbryce.beans.Company;
import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.facad.CompanyFacade;

@Path("company")
public class CompanyService {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private CompanyFacade getFacade() {

		CompanyFacade company = (CompanyFacade) request.getSession(false).getAttribute("facade");
		return company;
	}

	@POST
	@Path("createCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCompany(@QueryParam("title") String title, @QueryParam("start_date") long start_date,
			@QueryParam("end_date") long end_date, @QueryParam("amount") int amount,
			@QueryParam("type") CouponType type, @QueryParam("message") String message,
			@QueryParam("price") double price, @QueryParam("image") String image) {

		CompanyFacade companyFacade = getFacade();

		Coupon coupon = new Coupon(title, new Date(start_date), new Date(end_date), amount, type, message, price,
				image);
		try {
			companyFacade.createCoupon(coupon);
			return "Succeded to add a new coupon: Title = " + title;
		} catch (Exception e) {
			return "Failed to Add a new coupon:" + e.getMessage();
		}

	}

	@GET
	@Path("removeCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(@PathParam("compId") long id) {

		CompanyFacade companyFacade = getFacade();
		try {
			companyFacade.removeCouponID(id);
			return "Succeded to remove a coupon:  id = " + id;
		} catch (Exception e) {
			return "Failed to remove a coupon: " + e.getMessage();
		}

	}

	@POST
	@Path("updateCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCoupon(@QueryParam("couponId") long id, @QueryParam("title") String title,
			@QueryParam("start_date") long start_date, @QueryParam("end_date") long end_date,
			@QueryParam("amount") int amount, @QueryParam("type") CouponType type,
			@QueryParam("message") String message, @QueryParam("price") double price,
			@QueryParam("image") String image) {

		CompanyFacade companyFacade = getFacade();
		try {
			Coupon coupon = companyFacade.getCoupon(id);
			if (coupon != null) {
				coupon.setTitle(title);
				coupon.setStart_date(new Date(start_date));
				coupon.setEnd_date(new Date(end_date));
				coupon.setAmount(amount);
				coupon.setType(type);
				coupon.setMessage(message);
				coupon.setPrice(price);
				coupon.setImage(image);
				companyFacade.updateCoupon(coupon);
				return "Succeded to update a company: title = " + coupon.getTitle();
			} else {
				return "Failed to update a company: the provided company id is invalid";
			}
		} catch (Exception e) {
			return "Failed to update a company: " + e.getMessage();
		}

	}

	@GET
	@Path("getCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCompany() {
		CompanyFacade companyFacade = getFacade();
		Company company;
		try {
			company = companyFacade.getCompany();
		} catch (Exception e) {
			System.err.println("Get Company failed: " + e.getMessage());
			company = new Company();
		}

		return new Gson().toJson(company);
	}

	@GET
	@Path("getCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCoupon(@PathParam("compId") long id) {
		CompanyFacade companyFacade = getFacade();
		try {
			Coupon coupon = companyFacade.getCoupon(id);
			if (coupon != null) {
				return new Gson().toJson(coupon);
			} else {
				return null;
			}
		} catch (Exception e) {
			System.err.println("get coupon by id failed " + e.getMessage());
			return null;
		}
	}

	@GET
	@Path("getCoupons")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCoupons() {
		CompanyFacade companyFacade = getFacade();
		Set<Coupon> coupons;
		try {
			coupons = companyFacade.getCoupons();
		} catch (Exception e) {
			System.err.println("Get Coupons failed: " + e.getMessage());
			coupons = new HashSet<Coupon>();
		}
		return new Gson().toJson(coupons);
	}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/getAllCouponsByType")
	public Set<Coupon> getAllCouponsByType(@PathParam("type")CouponType type) {
		CompanyFacade companyFacade = getFacade();
		Set<Coupon>  allCouponsByType=new HashSet<>();
		try {
			allCouponsByType=companyFacade.getAllCouponsByType(type);
		} catch (Exception e) {
			System.err.println("Get Coupons by type failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return allCouponsByType;
	}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/getCouponsByMaxCouponPrice")
	public Set<Coupon> getCouponsByMaxCouponPrice(@PathParam("price") double price) {
		CompanyFacade companyFacade = getFacade();
		Set<Coupon>  allCouponsByType=new HashSet<>();
		try {
			allCouponsByType=companyFacade.getCouponsByMaxCouponPrice(price);
		} catch (Exception e) {
			System.err.println("Get Coupons by max price failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return allCouponsByType;
	}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/getCouponsByMaxCouponDate")
	public Set<Coupon> getCouponsByMaxCouponDate(@PathParam("date") Date date) {
		CompanyFacade companyFacade = getFacade();
		Set<Coupon>  allCouponsByType=new HashSet<>();
		try {
			allCouponsByType=companyFacade.getCouponsByMaxCouponDate(date);
		} catch (Exception e) {
			System.err.println("Get Coupons by max dare failed: " + e.getMessage());
			allCouponsByType = new HashSet<Coupon>();
		}
		return allCouponsByType;
	}


}