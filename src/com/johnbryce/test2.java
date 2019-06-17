package com.johnbryce;

import java.util.HashSet;
import java.util.Set;

import com.johnbryce.beans.Company;
import com.johnbryce.beans.Coupon;
import com.johnbryce.beans.CouponType;
import com.johnbryce.beans.Customer;
import com.johnbryce.facad.AdminFacad;
import com.johnbryce.facad.CompanyFacade;
import com.johnbryce.facad.CouponClientFacade;
import com.johnbryce.facad.CustomerFacad;
import com.johnbryce.utils.ClientType;
import com.johnbryce.utils.CouponSystem;
import com.johnbryce.utils.Utile;

public class test2 {

	public static void main(String[] args) throws Exception {
		Set<Company> companies = new HashSet<Company>();
		Set<Customer> customers = new HashSet<Customer>();
		Set<Coupon> coupons = new HashSet<Coupon>();
		CouponClientFacade facade;

		CouponSystem couponSystem = CouponSystem.getInstance();

		String line = "---------------------------------------------------------------\n";

		/*****************************
		 * Administrator Facade test *
		 *****************************/

		// Login as Admin
		facade = CouponSystem.login("admin", "1234", ClientType.ADMIN);
		if (facade instanceof AdminFacad) {

			System.out.println("========  Login(admin, 1234, AdminFacade) ========\n");
			Database.dropTableifNeeded();
			Database.createTables();

			// Create new 5 companies.
			Company company01 = new Company(1, "PIZZAHUT", "111", "pizzaHut.com");
			Company company02 = new Company(2, "HOLMCE-PLACE", "112", "holmesplace@gmail.com");
			Company company03 = new Company(3, "ELECTRA", "113", "electra@electra.com");
			Company company04 = new Company(4, "WALLA-TOURS", "114", "tours@walla.com");
			Company company05 = new Company(5, "FOX", "115", "FOX@yahoo.com");

			((AdminFacad) facade).createCompany(company01);
			((AdminFacad) facade).createCompany(company02);
			((AdminFacad) facade).createCompany(company03);
			((AdminFacad) facade).createCompany(company04);
			((AdminFacad) facade).createCompany(company05);

			// Show all companies (After creating new companies - before update).
			companies = ((AdminFacad) facade).getAllCompanies();
			System.out.println("After creating new companies - Show all new companies\n" + line + companies.toString());

			((AdminFacad) facade).updateCompany(company03, "3333", "farm@gmail.com");

			// Show One updated company id=5
			Company company07 = ((AdminFacad) facade).getCompany(5);
			System.out.println("Show One company" + company07.toString());

			// Show all companies (after update).
			companies = ((AdminFacad) facade).getAllCompanies();
			System.out.println("Show all companies (after update)" + companies.toString());

			// Add new 5 customers
			Customer customer01 = new Customer(1, "Michal", "2001");
			Customer customer02 = new Customer(2, "Eli", "2002");
			Customer customer03 = new Customer(3, "Hana", "2003");
			Customer customer04 = new Customer(4, "Karin", "2004");
			Customer customer05 = new Customer(5, "Gila", "2005");

			((AdminFacad) facade).createCustomer(customer01);
			((AdminFacad) facade).createCustomer(customer02);
			((AdminFacad) facade).createCustomer(customer03);
			((AdminFacad) facade).createCustomer(customer04);
			((AdminFacad) facade).createCustomer(customer05);

			// Show all customers (After creating new customers - before update).
			customers = ((AdminFacad) facade).getAllCustomers();
			System.out.println(
					"After creating new customers - Show all customers (before update)\n" + customers.toString());

			// Update Customer
			((AdminFacad) facade).updateCustomer(customer02, "444");

			// Show One customer.
			Customer customer07 = ((AdminFacad) facade).getCustomer(4);
			System.out.println("Show One customer" + customer07.toString());

			// Show all customers (after update).
			customers = ((AdminFacad) facade).getAllCustomers();
			System.out.println("Show all customers(after update)" + customers.toString());

			/*****************************************************************
			 * Company Facade TEST: id: 1, user: "PIZZAHUT", password:"111" *
			 *****************************************************************/
			facade = CouponSystem.login("PIZZAHUT", "111", ClientType.COMPANY);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(PIZZAHUT, 111, CompanyFacade) ========\n");

				Coupon coupon01 = new Coupon(1, "Deal-01", Utile.getCurrentDate(), Utile.getDateAfter(10), 5,
						CouponType.Food, "Best Deal", 69.90, "http//www.pizzahatCoupon.co.il/?1");
				Coupon coupon02 = new Coupon(2, "Deal-02", Utile.getCurrentDate(), Utile.getDateAfter(12), 4,
						CouponType.Resturans, "Best Deal", 79.90, "http//www.pizzahatCoupon.co.il/?2");
				Coupon coupon03 = new Coupon(3, "Deal-03", Utile.getCurrentDate(), Utile.getDateAfter(2), 10,
						CouponType.Food, "Best Deal", 111.90, "http//www.pizzahatCoupon.co.il/?3");

				((CompanyFacade) facade).createCoupon(coupon01);
				((CompanyFacade) facade).createCoupon(coupon02);
				((CompanyFacade) facade).createCoupon(coupon03);

				// Show all new PIZZAHUT coupons (before update).
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println("After creating 3 new PIZZAHUT coupons - Show all company coupons (before update) \n"
						+ coupons.toString());

				// Update Coupons
				Coupon coupon04 = new Coupon(3, "Deal-03", Utile.getCurrentDate(), Utile.getDateAfter(2), 10,
						CouponType.Food, "Best Deal", 117.99, "http//www.pizzahatCoupon.co.il/?3");
				((CompanyFacade) facade).updateCoupon(coupon04);

				// get coupon(3)
				Coupon coupon05 = ((CompanyFacade) facade).getCoupon(3);
				System.out.println("Show One PIZZAHUT updated coupon (3)" + coupon05.toString());

				// getCouponsByType(CouponType.FOOD)

				coupons = ((CompanyFacade) facade).getAllCouponsByType(CouponType.Food);
				System.out.println("View all PIZZAHUT company coupons by Type FOOD" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100);
				System.out.println("View all PIZZAHUT company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(10 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(Utile.getDateAfter(10));
				System.out.println("View all PIZZAHUT company Coupons by Max date 10 days from today (Expiration Date= "
						+ Utile.getDateAfter(10).toString() + "):\n" + coupons.toString());

			}

			/*********************************************************************
			 * Company Facade TEST: id: 2, user: "HOLMESPLACE", password: "112" *
			 *********************************************************************/
			facade = CouponSystem.login("HOLMCE-PLACE", "112", ClientType.COMPANY);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(HOLMESPLACE, 112, CompanyFacade) ========\n");

				// Add new 3 coupons
				Coupon coupon01 = new Coupon(4, "sportDeal-01", Utile.getCurrentDate(), Utile.getDateAfter(4), 6,
						CouponType.Health, "Deal Of The year-1", 90.90, "http//www.holmesplace.co.il/?1");
				Coupon coupon02 = new Coupon(5, "sportDeal-02", Utile.getCurrentDate(), Utile.getDateAfter(6), 6,
						CouponType.Sports, "Deal Of The year-1", 130.90, "http//www.holmesplace.co.il/?2");
				Coupon coupon03 = new Coupon(6, "sportDeal-03", Utile.getCurrentDate(), Utile.getDateAfter(10), 6,
						CouponType.Sports, "Deal Of The year-1", 140.90, "http//www.holmesplace.co.il/?3");

				((CompanyFacade) facade).createCoupon(coupon01);
				((CompanyFacade) facade).createCoupon(coupon02);
				((CompanyFacade) facade).createCoupon(coupon03);

				// show all coupons of holmesplace
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println(
						"After creating 3 new HOLMESPLACE coupons-show all company coupons\n" + coupons.toString());

				// getCouponsByType(CouponType.SPORTS)
				coupons = ((CompanyFacade) facade).getAllCouponsByType(CouponType.Sports);
				System.out.println("View all HOLMESPLACE company coupons by Type SPORTS\n" + coupons.toString());

				// getCouponsByType(CouponType.HEALTH)
				coupons = ((CompanyFacade) facade).getAllCouponsByType(CouponType.Health);
				System.out.println("View all HOLMESPLACE company coupons by Type HEALTH\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100);
				System.out.println("View all HOLMESPLACE company Coupons by Max Price 100 nis:\n" + coupons.toString());
				// getCouponsByMaxCouponDate(5 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(Utile.getDateAfter(5));
				System.out
						.println("View all HOLMESPLACE company Coupons by Max date 5 days from today (Expiration Date= "
								+ Utile.getDateAfter(5).toString() + "):\n" + coupons.toString());

			}

			/*************************************************************
			 * Company Facade TEST: user: id: 3, "ELECTRA", password: "113" *
			 *************************************************************/
			facade = CouponSystem.login("ELECTRA", "3333", ClientType.COMPANY);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(ELECTRA, 3333, CompanyFacade) ========\n");

				// Add new 3 coupons
				Coupon coupon01 = new Coupon(7, "ELECTRA-Deal-01", Utile.getCurrentDate(), Utile.getDateAfter(10), 2,
						CouponType.Electricity, "Deal Of The year-1", 30.00, "http//www.electra.co.il/?1");
				Coupon coupon02 = new Coupon(8, "ELECTRA-Deal-02", Utile.getCurrentDate(), Utile.getDateAfter(12), 2,
						CouponType.Electricity, "Deal Of The year-1", 40.00, "http//www.electra.co.il/?2");
				Coupon coupon03 = new Coupon(9, "ELECTRA-Deal-03", Utile.getCurrentDate(), Utile.getDateAfter(11), 2,
						CouponType.Electricity, "Deal Of The year-1", 50.00, "http//www.electra.co.il/?3");

				((CompanyFacade) facade).createCoupon(coupon01);
				((CompanyFacade) facade).createCoupon(coupon02);
				((CompanyFacade) facade).createCoupon(coupon03);

				// Show all new BUG coupons.
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println(
						"After creating 3 new ELECTRA coupons - Show all company coupons\n" + coupons.toString());

				// getCouponsByType(CouponType.ELECTRICITY)
				coupons = ((CompanyFacade) facade).getAllCouponsByType(CouponType.Electricity);
				System.out.println("View all ELECTRA company coupons by Type ELECTRICITY\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100);
				System.out.println("View all ELECTRA company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(100 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(Utile.getDateAfter(11));
				System.out.println("View all ELECTRA company Coupons by Max 100 days from today (Expiration Date= "
						+ Utile.getDateAfter(100).toString() + "):\n" + coupons.toString());
			}

			/***************************************************************
			 * Company Facade TEST: id: 4, user: "WALLA-TOURS", password: "114" *
			 ***************************************************************/
			facade = CouponSystem.login("WALLA-TOURS", "114", ClientType.COMPANY);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(WALLA-TOURS, 114, CompanyFacade) ========\n");

				// Add new 3 coupons
				Coupon coupon01 = new Coupon(10, "deal-01", Utile.getCurrentDate(), Utile.getDateAfter(10), 2,
						CouponType.Camping, "Deal Of The year-1", 35.00, "http//www.wallatours.co.il/?1");
				Coupon coupon02 = new Coupon(11, "deal-02", Utile.getCurrentDate(), Utile.getDateAfter(20), 3,
						CouponType.Traveling, "Deal Of The year-1", 45.00, "http//www.wallatours.co.il/?2");
				Coupon coupon03 = new Coupon(12, "deal-03", Utile.getCurrentDate(), Utile.getDateAfter(30), 3,
						CouponType.Camping, "Deal Of The year-1", 55.00, "http//www.wallatours.co.il/?3");

				((CompanyFacade) facade).createCoupon(coupon01);
				((CompanyFacade) facade).createCoupon(coupon02);
				((CompanyFacade) facade).createCoupon(coupon03);

				// Show all new wallaTours coupons.
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println(
						"After creating 3 new wallaTours coupons - Show all company coupons\n" + coupons.toString());

				// getCouponsByType(CouponType.CAMPING)
				coupons = ((CompanyFacade) facade).getAllCouponsByType(CouponType.Camping);
				System.out.println("View all wallaTours company coupons by Type CAMPING\n" + coupons.toString());

				// getCouponsByType(CouponType.TRAVELING)
				coupons = ((CompanyFacade) facade).getAllCouponsByType(CouponType.Traveling);
				System.out.println("View all wallaTours company coupons by Type TRAVELING\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100);
				System.out.println("View all wallaTours company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(100 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(Utile.getDateAfter(15));
				System.out.println("View all wallaTours company Coupons by Max 15 days from today (Expiration Date= "
						+ Utile.getDateAfter(15).toString() + "):\n" + coupons.toString());

			}

			/***************************************************************
			 * Company Facade TEST: id: 5, user: "FOX", password: "115" *
			 ***************************************************************/
			facade = CouponSystem.login("FOX", "115", ClientType.COMPANY);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(FOX, 115, CompanyFacade) ========\n");

				// Add new 3 coupons
				Coupon coupon01 = new Coupon(13, "FOX-deal-01", Utile.getCurrentDate(), Utile.getDateAfter(15), 2,
						CouponType.Sports, "Deal Of The year-1", 44.00, "http//www.fox.co.il/?1");
				Coupon coupon02 = new Coupon(14, "FOX-deal-02", Utile.getCurrentDate(), Utile.getDateAfter(25), 3,
						CouponType.Sports, "Deal Of The year-1", 47.00, "http//www.fox.co.il/?2");
				Coupon coupon03 = new Coupon(15, "FOX-deal-03", Utile.getCurrentDate(), Utile.getDateAfter(20), 3,
						CouponType.Sports, "Deal Of The year-1", 51.00, "http//www.fox.co.il/?3");

				((CompanyFacade) facade).createCoupon(coupon01);
				((CompanyFacade) facade).createCoupon(coupon02);
				((CompanyFacade) facade).createCoupon(coupon03);

				// Show all new fox coupons.
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out
						.println("After creating 3 new FOX coupons - Show all company coupons\n" + coupons.toString());

				// getCouponsByType(CouponType.sports)
				coupons = ((CompanyFacade) facade).getAllCouponsByType(CouponType.Sports);
				System.out.println("View all FOX company coupons by Type SPORTS\n" + coupons.toString());

				// getCouponsByMaxCouponPrice(100 nis)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponPrice(100);
				System.out.println("View all FOX company Coupons by Max Price 100 nis:\n" + coupons.toString());

				// getCouponsByMaxCouponDate(100 days from today)
				coupons = ((CompanyFacade) facade).getCouponsByMaxCouponDate(Utile.getDateAfter(30));
				System.out.println("View all fox company Coupons by Max 30 days from today (Expiration Date= "
						+ Utile.getDateAfter(30).toString() + "):\n" + coupons.toString());
			}

			/************************
			 * Customer Facade TEST *
			 ************************/

			/**************************************************************************
			 * Customer Facade TEST: id: 1, user: "Michal", password: "2001" *
			 **************************************************************************/

			facade = CouponSystem.login("Michal", "2001", ClientType.CUSTOMER);
			if (facade instanceof CustomerFacad) {
				System.out.println("========  Login(Michal, 2001, Customer) ========\n");
				// CustomerFacad customerFacad = (CustomerFacad) facade;
				// getCouponsByType(CouponType.RESTURANTS)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Resturans);
				System.out.println("List of All RESTURANTS Coupons type: \n" + coupons.toString());
				// getCouponsByType(CouponType.FOOD)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Camping);
				System.out.println("List of All Camping Coupons type: \n" + coupons.toString());

				// Purchase Coupons
				((CustomerFacad) facade).purchaseCoupon(2); // Add new 2-PIZZAHUT Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(4); // Add new 4-HOLMESPLACE Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(12); // Add new 5-WALLATOURS Coupon to customer

				// CustomerFacade Reports

				coupons = ((CustomerFacad) facade).getAllPurchasedCoupons();
				System.out.println("List of All Michal's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Resturans);
				System.out
						.println("List of All Michal's Purchased Coupons by RESTURANTS type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Camping);
				System.out.println("List of All Michal's Purchased Coupons by Camping type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByPrice(80);
				System.out.println(
						"List of All Michal's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());

			}

			/**************************************************************************
			 * Customer Facade TEST: id: 2, user: "Eli", password: "2002" *
			 **************************************************************************/
			facade = CouponSystem.login("Eli", "444", ClientType.CUSTOMER);

			if (facade instanceof CustomerFacad) {
				System.out.println("========  Login(Eli, 444, Customer) ========\n");

				// getCouponsByType(CouponType.Electricity)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Electricity);
				System.out.println("List of All Electricity Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.FOOD)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Food);
				System.out.println("List of All FOOD Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.sports)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Sports);
				System.out.println("List of All sports Coupons type: \n" + coupons.toString());

				// Purchase Coupons
				((CustomerFacad) facade).purchaseCoupon(3); // Add new 3-PIZZAHUT Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(7); // Add new 7-ELECTRA Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(14); // Add new 14-FOX Coupon to customer

				// CustomerFacade Reports

				coupons = ((CustomerFacad) facade).getAllPurchasedCoupons();
				System.out.println("List of All Eli's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Food);
				System.out.println("List of All Eli's Purchased Coupons by Food type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Electricity);
				System.out.println("List of All Eli's Purchased Coupons by Electricity type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Sports);
				System.out.println("List of All Eli's Purchased Coupons by sports type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByPrice(80);
				System.out
						.println("List of All Eli's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());

			}

			/**************************************************************************
			 * Customer Facade TEST: id: 3, user: "Hana", password: "2003" *
			 **************************************************************************/
			facade = CouponSystem.login("Hana", "2003", ClientType.CUSTOMER);
			if (facade instanceof CustomerFacad) {
				System.out.println("========  Login(Hana, 2003, Customer) ========\n");

				// getCouponsByType(CouponType.food)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Food);
				System.out.println("List of All FOOD Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.Electricity)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Electricity);
				System.out.println("List of All Electricity Coupons type: \n" + coupons.toString());

				// Purchase Coupons
				((CustomerFacad) facade).purchaseCoupon(1); // Add new 1-PIZZAHUT Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(2); // Add new 2-PIZZAHUT Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(8); // Add new 8-BUG Coupon to customer

				// CustomerFacade Reports

				coupons = ((CustomerFacad) facade).getAllPurchasedCoupons();
				System.out.println("List of All Hana's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Food);
				System.out.println("List of All Hana's Purchased Coupons by food type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Electricity);
				System.out.println("List of All Hana's Purchased Coupons by Electricity type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByPrice(80);
				System.out.println(
						"List of All Hana's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());

			}

			/**************************************************************************
			 * Customer Facade TEST: id: 4, user: "Karin", password: "2004" *
			 **************************************************************************/
			facade = CouponSystem.login("Karin", "2004", ClientType.CUSTOMER);
			if (facade instanceof CustomerFacad) {
				System.out.println("========  Login(Karin, 2004, Customer) ========\n");

				// getCouponsByType(CouponType.camping)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Camping);
				System.out.println("List of All camping Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.sports)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Sports);
				System.out.println("List of All sports Coupons type: \n" + coupons.toString());

				// Purchase Coupons
				((CustomerFacad) facade).purchaseCoupon(6); // Add new 6-HOLMESPLACE Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(10); // Add new 10-WALLATOURS Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(11); // Add new 11-WALLATOURS Coupon to customer

				// CustomerFacade Reports

				coupons = ((CustomerFacad) facade).getAllPurchasedCoupons();
				System.out.println("List of All Karin's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Sports);
				System.out.println("List of All Karin's Purchased Coupons by Sports type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Camping);
				System.out.println("List of All Karin's Purchased Coupons by Camping type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByPrice(80);
				System.out.println(
						"List of All Karin's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());

			}

			/**************************************************************************
			 * Customer Facade TEST: id: 5, user: "Gila", password: "2005" *
			 **************************************************************************/
			facade = CouponSystem.login("Gila", "2005", ClientType.CUSTOMER);
			if (facade instanceof CustomerFacad) {
				System.out.println("========  Login(Gila, 2005, Customer) ========\n");

				// getCouponsByType(CouponType.camping)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Camping);
				System.out.println("List of All camping Coupons type: \n" + coupons.toString());

				// getCouponsByType(CouponType.Electricity)
				coupons = ((CustomerFacad) facade).getAllCouponsByType(CouponType.Electricity);
				System.out.println("List of All Electricity Coupons type: \n" + coupons.toString());

				// Purchase Coupons
				((CustomerFacad) facade).purchaseCoupon(7); // Add new 7-ELECTRA Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(10); // Add new 10-WALLATOURS Coupon to customer
				((CustomerFacad) facade).purchaseCoupon(11); // Add new 11-WALLATOURS Coupon to customer

				// CustomerFacade Reports

				coupons = ((CustomerFacad) facade).getAllPurchasedCoupons();
				System.out.println("List of All Gila's Purchased Coupons: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Electricity);
				System.out.println("List of All Gila's Purchased Coupons by Electricity type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByType(CouponType.Camping);
				System.out.println("List of All Gila's Purchased Coupons by Camping type: \n" + coupons.toString());

				coupons = ((CustomerFacad) facade).getAllPurchasedCouponsByPrice(80);
				System.out.println(
						"List of All Gila's Purchased Coupons by max price of 80 nis: \n" + coupons.toString());

			}

			/*****************************************
			 * Company Facade test - DELETE (Coupon) *
			 *****************************************/

			// Login as Company PIZZAHUT
			facade = CouponSystem.login("PIZZAHUT", "111", ClientType.COMPANY);
			if (facade instanceof CompanyFacade) {
				System.out.println("========  Login(PIZZAHUT, 111, CompanyFacade) ========\n");

				// Show PIZZAHUT company coupons 1 (before removing coupon 1).

				Coupon coupon = ((CompanyFacade) facade).getCoupon(1);
				System.out.println("View company coupons 1 (before removing coupon 1)\n" + coupon.toString());

				// Remove Coupon 1 from company 1-PIZZAHUT
				((CompanyFacade) facade).removeCouponID(1);
				System.out.println("coupons 1 Deleted.\n");

				// Show PIZZAHUT company coupons 1 (after removing coupon 1).
				coupons = ((CompanyFacade) facade).getCoupons();
				System.out.println("View company coupons 1 (after removing coupon 1)\n" + coupons.toString());
			}

			/*************************
			 * Customer Facade teat *
			 *************************/

			facade = CouponSystem.login("Michal", "2001", ClientType.CUSTOMER);
			if (facade instanceof CustomerFacad) {
				System.out.println("========  Login(Michal, 2001, Customer) ========\n");

				/**
				 * get all Michal Coupons Purchased.
				 */

				coupons = ((CustomerFacad) facade).getAllPurchasedCoupons();
				System.out.println("View all  Michal Coupons Purchased (before removing customer 1-Michal)\n"
						+ coupons.toString());
			}

			/*****************************
			 * Administrator Facade test *
			 *****************************/

			// Login as Admin
			facade = CouponSystem.login("admin", "1234", ClientType.ADMIN);
			if (facade instanceof AdminFacad) {
				System.out.println("========  Login(admin, 1234, AdminFacade) ========\n");

				// Remove customer 1-Michal
				((AdminFacad) facade).removeCustomer(customer01);

				System.out.println("========  DELETE Customer 1-Michal ========\n");

				// Show all customers (after removing customer 1-Michal).
				customers = ((AdminFacad) facade).getAllCustomers();
				System.out
						.println("View customer 1-Michal (after removing customer 1-Michal)\n" + customers.toString());

				/**
				 * Remove Company.
				 */

				// Show all companies (before removing customer 5-fox).
				companies = ((AdminFacad) facade).getAllCompanies();
				System.out.println("View all companies (before removing company 5-Fox)\n" + companies.toString());

				// Remove Company 5-fox
				((AdminFacad) facade).removeCompany(company05);
				System.out.println("========  DELETE Company 5-fox ========\n");

				// Show all companies (after removing customer 5-FOX).
				companies = ((AdminFacad) facade).getAllCompanies();
				System.out.println("View all companies (after removing company 5-FOX)\n" + companies.toString());

			}

			System.out.println("========  shut down system ========\n");

			/**
			 * End of program - Close thread all connections, thread
			 */
			couponSystem.shutdown();
			System.out.println("Disconnected!");
			//
		}
	}

}
