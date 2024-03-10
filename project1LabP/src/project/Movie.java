package project;

public class Movie {
	private String title;
	private int year;
	private int quantity;
	private int[][] rentals;
	private double price;
	private double tax;
	private String code;
	
	public Movie(String title, int year, int quantity, String rentals, double price, double tax) {
		this.title = title;
		this.year = year;
		this.quantity = quantity;
		this.rentals = rentalsParse(rentals);
		this.tax = tax;
		this.code = codeParse(title);
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public int[][] getRentals() {
		return rentals;
	}
	
	public double getPrice() {
		return price;
	}
	
	public double getTax() {
		return tax;
	}
	
	private int[][] rentalsParse(String rentals) {
		return new int[2][2];
	}
	
	private String codeParse(String title) {
		StringBuilder code = new StringBuilder(title.toUpperCase());
		int index = 0;
		
		while(index != -1) {
			index = code.indexOf(" ");
			if(index >= 0) {
				code.deleteCharAt(index);
			}
		}
		code.trimToSize();
		
		
		
		return code.toString();
	}
}
