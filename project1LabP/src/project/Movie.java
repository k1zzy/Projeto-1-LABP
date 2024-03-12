package project;

import java.util.Arrays;

public class Movie {
	private final String title;
	private final int year;
	private int quantity;
	private int[][] rentals;
	private final double price;
	private final double tax;
	private final String code;
	
	public Movie(String title, int year, int quantity, int[][] rentals, double price, double tax) {
		this.title = title;
		this.year = year;
		this.quantity = quantity;
		this.rentals = rentals;
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
	
	public void rentalRegister(int userId) {
		rentals = Arrays.copyOf(rentals,rentals.length + 1);
		rentals[rentals.length - 2][0] = userId;
		rentals[rentals.length - 2][1] = 7;
	}
	
	public void rentalUnregister(int userId) {		
		for(int i = findUserIndex(userId); i < rentals.length - 1; i++) {
			rentals[i][0] = rentals[i + 1][0];
			rentals[i][1] = rentals[i + 1][1];
		}
		
		rentals = Arrays.copyOf(rentals, rentals.length - 1);
	}
	
	public int findUserIndex(int userId) {
		for(int i = 0; i < rentals.length; i++) {
			if(rentals[i][0] == userId) {
				return i;
			}
		}
		return -1;
	}
	
	private String codeParse(String title) {
		StringBuilder sbTitle = new StringBuilder(title.toUpperCase()); // colocar tudo em maiusculas
		StringBuilder code = new StringBuilder();
		
		delNonLetters(sbTitle); // apaga todas os caracteres que nao sao letras
		
		int index = 0; // comecar na primeira posicao
		while(index < sbTitle.length()) { // a cada 4 posicoes, da append do caracter ate acabar
			code.append(sbTitle.charAt(index));
			index += 4;
		}
		
		index = 1; // colocar o index na segunda posicao
		while(index < sbTitle.length()) { // a cada 1 posicoes, da append do caracter ate acabar
			code.append(sbTitle.charAt(index));
			index += 2;
		}
		
		index = 2; // colocar o index na terceira posicao
		while(index < sbTitle.length()) { // a cada 4 posicoes, da append do caracter ate acabar
			code.append(sbTitle.charAt(index));
			index += 4;
		}
		
		code.delete(3, (code.length() - 3)); //  mantem apenas os 3 primeiros caracteres e os ultimos 3
		
		return code.toString();
	}
	
	private void delNonLetters(StringBuilder sb) {
		for(int i = 0; i < sb.length(); i++) {
			if(!Character.isLetter(sb.charAt(i))) {
				sb.deleteCharAt(i);
				i--;
			}
		}
	}
}