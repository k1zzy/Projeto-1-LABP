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
		this.price = price;
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
	
	public String getCode() {
		return code;
	}
	
	private int[][] rentalsParse(String rentals) {
		return new int[2][2];
	}
	
	private String codeParse(String title) {
		StringBuilder titleUppercase = new StringBuilder(title.toUpperCase());
		StringBuilder code = new StringBuilder();
		
		for(int i = 0; i < titleUppercase.length(); i++) {
			if(!Character.isLetter(titleUppercase.charAt(i))) {
				titleUppercase.deleteCharAt(i);
				i--;
			}
		}
		
		int index = 0; // comecar na primeira posicao
		while(index < titleUppercase.length()) { // a cada 4 posicoes, da append do caracter ate acabar
			code.append(titleUppercase.charAt(index));
			index += 4;
		}
		
		index = 1; // colocar o index na segunda posicao
		while(index < titleUppercase.length()) { // a cada 1 posicoes, da append do caracter ate acabar
			code.append(titleUppercase.charAt(index));
			index += 2;
		}
		
		index = 2; // colocar o index na terceira posicao
		while(index < titleUppercase.length()) { // a cada 4 posicoes, da append do caracter ate acabar
			code.append(titleUppercase.charAt(index));
			index += 4;
		}
		
		code.delete(3, (code.length() - 3)); //  mantem apenas os 3 primeiros caracteres e os ultimos 3
		
		return code.toString();
	}
}