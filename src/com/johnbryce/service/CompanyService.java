package com.johnbryce.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.johnbryce.beans.Company;
import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.exception.CouponException;
import com.johnbryce.facad.AdminFacad;
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

	@GET
	@Path("createCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCompany(@QueryParam("title") String title, @QueryParam("start_date") long start_date,
			@QueryParam("end_date") long end_date, @QueryParam("amount") int amount,
			@QueryParam("type") CouponType type, @QueryParam("message") String message,
			@QueryParam("price") double price, @QueryParam("image") String image) {

		CompanyFacade company = getFacade();

		Coupon coupon = new Coupon(title, new Date(start_date), new Date(end_date), amount, type, message, price,
				image);
		try {
			company.createCoupon(coupon);
			return "Succeded to add a new coupon: Title = " + title;
		} catch (Exception e) {
			return "Failed to Add a new coupon:" + e.getMessage();
		}

	}

	@GET
	@Path("removeCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(@QueryParam("compId") long id) {

		CompanyFacade company = getFacade();
		try {
			company.removeCouponID(id);
			return "Succeded to remove a coupon:  id = " + id;
		} catch (Exception e) {
			return "Failed to remove a coupon: " + e.getMessage();
		}

	}
	
	@GET
	@Path("updateCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCoupon(@QueryParam("couponId") long id,@QueryParam("title") String title, @QueryParam("start_date") long start_date,
			@QueryParam("end_date") long end_date, @QueryParam("amount") int amount,
			@QueryParam("type") CouponType type, @QueryParam("message") String message,
			@QueryParam("price") double price, @QueryParam("image") String image) {

		CompanyFacade company = getFacade();
		try {
			Coupon coupon = company.getCoupon(id);
			if (coupon != null) {
				coupon.setTitle(title);
				coupon.setStart_date(new Date(start_date));
				coupon.setEnd_date(new Date(end_date));
				coupon.setAmount(amount);
				coupon.setType(type);
				coupon.setMessage(message);
				coupon.setPrice(price);
				coupon.setImage(image);
				company.updateCoupon(coupon);
				return "Succeded to update a company: title = " + coupon.getTitle();
			} else {
				return "Failed to update a company: the provided company id is invalid";
			}
		} catch (Exception e) {
			return "Failed to update a company: " + e.getMessage();
		}

	}

}