package project;

public class Movie {
	private final String title;
	private final int year;
	private int quantity;
	private int[][] rentals;
	private double price;
	private double tax;
	private final String code;
	
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
	
	//TODO
	private int[][] rentalsParse(String rentals) {
		rentals.replace("()", ""); // remove os parenteses da string
		int counter = 1; // numero de 
		
		for(int i = 0; i < rentals.length(); i++) {
			counter++;
		}
		
		String[] lines = new String[counter];
		
		int[][] rentalsAr = new int[counter][2];
		
//		for(int j = 0; j < counter; j++) {
//			rentalsAr[j] = Integer.parseInt(lines[j].split(";"));
//		}
		
		return rentalsAr;
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