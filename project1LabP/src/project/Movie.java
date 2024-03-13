/**
 * Classe que representa um Movie.
 * 
 * @author Rodrigo Afonso (61839)
 * @version 1.0
 */
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
	
	/**
	 * Representa um filme.
	 * Contém características de um filme.
	 * 
	 * @param title - título
	 * @param year - ano de lançamento
	 * @param quantity - quantidade de cópias disponíveis
	 * @param rentals - array dos rentals ativos
	 * @param price - preço
	 * @param tax - taxa do estúdio
	 */
	public Movie(String title, int year, int quantity, int[][] rentals, double price, double tax) {
		this.title = title;
		this.year = year;
		this.quantity = quantity;
		this.rentals = rentals;
		this.price = price;
		this.tax = tax;
		this.code = codeParse(title);
	}
	

	/**
	 * Retorna o título do filme.
	 *
	 * @return o título do filme
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Retorna o ano de lançamento do filme.
	 *
	 * @return o ano de lançamento do filme
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Retorna a quantidade de cópias disponíveis do filme.
	 *
	 * @return quantidade de cópias disponíveis do filme
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Retorna a array que representa todos os rentals ativos do filme.
	 *
	 * @return a array que representa todos os rentals ativos do filme
	 */
	public int[][] getRentals() {
		return rentals;
	}
	
	/**
	 * Retorna o preço do filme.
	 *
	 * @return o preço do filme
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Retorna a taxa do estúdio aplicada ao filme.
	 *
	 * @return a taxa do estúdio aplicada ao filme
	 */
	public double getTax() {
		return tax;
	}
	
	/**
	 * Retorna a cota do filme.
	 * É um código de identificaçao de 6 caractéres gerado a partir do título do filme.
	 *
	 * @return cota do filme
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Método utilizado para registar um rental novo deste filme.
	 * 
	 * @param userId - id do usuário que está a alugar o filme
	 */
	public void rentalRegister(int userId) {
		rentals = Arrays.copyOf(rentals,rentals.length + 1);
		rentals[rentals.length - 1] = new int[]{userId, 7};
		quantity--;
	}
	
	/**
	 * Método utilizado devolver uma cópia deste filme.
	 * 
	 * @throws ArrayIndexOutOfBoundsException - se o usuário não tiver alugado o filme
	 * @param userId - id do usuário que alugou o filme
	 */
	public void rentalUnregister(int userId) {
		int userIndex = findUserIndex(userId);
    
    	try {
        for(int i = userIndex; i < rentals.length - 1; i++) {
            rentals[i][0] = rentals[i + 1][0];
            rentals[i][1] = rentals[i + 1][1];
        }
        
        rentals = Arrays.copyOf(rentals, rentals.length - 1);
        quantity++;
    	} catch(ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException();
	  	}
	}
	
	/**
	 * Método utilizado encontrar o index no array de rentals de um user.
	 * 
	 * @param userId - id do usuário a procurar
	 * @return o index do usuário no array de rentals
	 */
	public int findUserIndex(int userId) {
		for(int i = 0; i < rentals.length; i++) { // procura o usuario
			if(rentals[i][0] == userId) {
				return i; // retorna o index do usuario
			}
		}
		return -1;
	}
	
	/**
	 * Transforma o título do filme numa cota indentificadora de 6 caractéres.
	 * 
	 * @param title - título do filme
	 * @return cota do filme
	 */
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
	
	/**
	 * Apaga todos os caracteres que nao sao letras.
	 * 
	 * @param sb - StringBuilder a ser modificado
	 */
	private void delNonLetters(StringBuilder sb) {
		for(int i = 0; i < sb.length(); i++) { // apaga todos os caracteres que nao sao letras
			if(!Character.isLetter(sb.charAt(i))) {
				sb.deleteCharAt(i);
				i--; // decrementar o index para nao saltar caracteres
			}
		}
	}
}