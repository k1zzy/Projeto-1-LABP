/**
 * Classe que representa um VideoClub.
 * 
 * @author Rodrigo Afonso (61839)
 * @version 1.0
 */

package project;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;

public class VideoClub {
	private double totalProfit;
	private double totalRevenue;
	private Movie[] filmes;
	
	/**
	 * Representa um VideoClub.
	 * Contém características de um VideoClub.
	 * 
	 * @param fileName - nome do ficheiro do stock
	 * @param numberOfMovies - número de filmes a retirar do stock
	 * @throws FileNotFoundException - exceção para ficheiro não encontrado
	 * @throws NumberFormatException - exceção para erros na conversão de tipos
	 * @throws ArrayIndexOutOfBoundsException - exceção para argumentos insuficientes
	 */
	public VideoClub(String fileName, int numberOfMovies)throws
	FileNotFoundException {
		String[][] stock = readStock(fileName, numberOfMovies);
		filmes = new Movie[stock.length];
		int line = 1;
		
		try {
			for(int i = 0; i < stock.length; i++) {
				line++;
				filmes[i] = new Movie(
										stock[i][0], // title
										Integer.parseInt(stock[i][1]), // year
										Integer.parseInt(stock[i][2]), // quantity
										rentalsParse(stock[i][3]), // rentals
										Double.parseDouble(stock[i][4]), // price
										Double.parseDouble(stock[i][5].replace("%", "")) // tax, deletes %
									 );
		}
		}catch(ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Erro na leitura, argumentos insuficientes: linha " + line + " do ficheiro " + fileName);
		}catch(NumberFormatException e) {
			throw new NumberFormatException("Erro na conversão de tipos: linha " + line + " do ficheiro " + fileName);
		}
	}
	/**
	 * Retorna o número de filmes.
	 *
	 * @return o número de filmes
	 */
	public int getNumberOfMovies() {
		return filmes.length;
	}
	
	/**
	 * Retorna o número de filmes disponíveis.
	 *
	 * @return o número de filmes disponíveis
	 */
	public int numberAvailableMovies() {
		int filmesAvailable = 0;
		
		for(Movie filme : filmes) {
			if(filme.getQuantity() >= 1)
				filmesAvailable++;
		}
		return filmesAvailable;
	}
	
	/**
	 * Retorna o valor total da receita.
	 *
	 * @return o valor total da receita
	 */
	public double getTotalRevenue() {
		return totalRevenue;
	}
	
	/**
	 * Retorna o valor total do profit.
	 *
	 * @return o valor total do profit
	 */
	public double getTotalProfit() {
		return totalProfit;
	}
	
	/**
	 * Retorna uma lista de filmes com o ano igual ao ano dado.
	 *
	 * @param year - ano a filtrar
	 * @return uma String de filmes com o ano igual ao ano dado
	 */
	public String filterByYear(int year) {
		StringBuilder yearFilter = new StringBuilder();
		
		for(Movie filme : filmes) {
			if(filme.getYear() == year) // se o ano do filme for igual ao ano dado
				yearFilter.append(String.format(Locale.US,"Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return yearFilter.toString(); // retorna a lista de filmes com o ano igual ao ano dado
	}
	
	/**
	 * Retorna uma lista de filmes com o preço menor ou igual ao preço dado.
	 *
	 * @param price - preço a filtrar
	 * @return uma String de filmes com o preço menor ou igual ao preço dado
	 */
	public String filterByPrice(double price) {
		StringBuilder priceFilter = new StringBuilder();
		
		for(Movie filme : filmes) { 
			if(filme.getPrice() <= price) // se o preço do filme for menor ou igual ao preço dado
				priceFilter.append(String.format(Locale.US,"Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return priceFilter.toString(); // retorna a lista de filmes com o preço menor ou igual ao preço dado
	}
	
	/**
	 * Retorna uma lista de filmes que contenham em stock uma ou mais cópias.
	 *
	 * @return uma String de filmes disponíveis para alugar
	 */
	public String filterAvailableMovies() {
		StringBuilder availableMovies = new StringBuilder();
		
		for(Movie filme : filmes) { 
			if(filme.getQuantity() >= 1) // se a quantidade do filme for maior ou igual a 1
				availableMovies.append(String.format(Locale.US,"Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return availableMovies.toString(); // retorna a lista de filmes disponiveis
	}
	
	/**
	 * Um log da atividade durante um dia do VideoClub.
	 *
	 * @param rentalsFileName - nome do ficheiro de rentals feitos durante o dia
	 * @throws FileNotFoundException - exceção para ficheiro não encontrado
	 * @throws NumberFormatException - exceção para formato de user inválido
	 * @throws ArrayIndexOutOfBoundsException - exceção para argumentos insuficientes
	 * @return uma String de todas as ações feitas durante o dia
	 */
	public String activityLog(String rentalsFileName) throws
	FileNotFoundException{
		String[][] rentalsRead = readRentalsFile(rentalsFileName);
		StringBuilder sbRentals = new StringBuilder();
		double afterTax = 0;
		String userId = "";
		String title = "";
		int line = 1;
		
		for(String[] action : rentalsRead) {
			line++;
			
			try {
				userId = action[1];
				title = action[2];
			}catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException("Erro na leitura, argumentos insuficientes: linha " + line + " do ficheiro " + rentalsFileName);
			}
			
			try {
				if(action[0].equals("rent")) { // se o user esta a alugar um filme
					if(!doesMovieExist(title)) { // se o filme nao existir
						sbRentals.append("Movie not found: client " + userId); 
						sbRentals.append(" asked for " + title);
						sbRentals.append(System.lineSeparator());
						
					}else { // se o filme existir
						Movie currentFilme = findMovie(title);
						
						if(!isMovieAvailable(currentFilme)) { // se o filme nao estiver disponivel mas existir
							sbRentals.append("Movie currently not available: client " + userId);
							sbRentals.append(" asked for " + currentFilme.getTitle());
							sbRentals.append(System.lineSeparator());
							
						}else { // se o filme estiver disponivel e existir
							afterTax = currentFilme.getPrice() * (1 - currentFilme.getTax() / 100); // calcula o preço apos as taxas de estudio
							
							sbRentals.append("Rental successful: client " + userId); 
							sbRentals.append(" rented " + currentFilme.getTitle()); 
							sbRentals.append(String.format(Locale.US," for $%.2f", currentFilme.getPrice()));
							sbRentals.append(System.lineSeparator());
							sbRentals.append(String.format(Locale.US,"Total: $%.2f [$%.2f]", currentFilme.getPrice(), afterTax));
							sbRentals.append(System.lineSeparator());
							
							try {
								currentFilme.rentalRegister(Integer.parseInt(userId)); // regista um novo rental
							}catch (NumberFormatException e) {
								throw new NumberFormatException("Erro, formato de user inválido: linha " + line + " do ficheiro " + rentalsFileName);
							}
							
							totalRevenue += currentFilme.getPrice(); // adiciona ao total de revenue o preço do filme
							totalProfit += afterTax; // adiciona ao profit o total apos as taxas de estudio
						}
					}
				}else if(action[0].equals("return")) { // se o user esta a devolver um filme
					int numOfLateDays = 0;
					
					if (!doesMovieExist(title)) { // se o filme nao existir
						sbRentals.append("Movie not in videoclub: client " + userId);
						sbRentals.append(" tried to return " + title);
						sbRentals.append(System.lineSeparator());
						
					}else { // se o filme existir
						Movie currentFilme = findMovie(title);
	
						try {
							numOfLateDays = currentFilme.getRentals()[currentFilme.findUserIndex(Integer.parseInt(userId))][1]; // numero de dias em atraso
						}catch (Exception e) {
							sbRentals.append("Movie not rented before by user: client " + userId);
							sbRentals.append(" asked for " + currentFilme.getTitle());
							sbRentals.append(System.lineSeparator());
							continue;
						}
						if(numOfLateDays >= 0) { // se nao houver atraso na devolucao
							sbRentals.append("Movie returned: client " + userId);
							sbRentals.append(" returned " + currentFilme.getTitle());
							sbRentals.append(System.lineSeparator());
							sbRentals.append("Total: $0.00 [$0.00]");
							sbRentals.append(System.lineSeparator());
							
							currentFilme.rentalUnregister(Integer.parseInt(userId));
							
						}else { // se houver atraso na devolucao
							numOfLateDays = Math.abs(numOfLateDays);
							afterTax = numOfLateDays * 2.0 * (1 - currentFilme.getTax() / 100); // numero de dias * 2 (taxa por dia atrasado) menos a taxa de estudio
							
							String s = numOfLateDays == 1 ? "" : "s"; // se o numero de dias for 1, nao adiciona o "s"
							
							sbRentals.append("Movie returned with " + numOfLateDays + " day" + s + " of delay: client " + userId);
							sbRentals.append(" returned " + currentFilme.getTitle());
							sbRentals.append(System.lineSeparator());
							sbRentals.append(String.format(Locale.US,"Total: $%.2f [$%.2f]", numOfLateDays * 2.0, afterTax));
							sbRentals.append(System.lineSeparator());
							
							currentFilme.rentalUnregister(Integer.parseInt(userId));
							
							totalProfit += afterTax; // adiciona ao profit o total apos as taxas de estudio
							totalRevenue += numOfLateDays * 2.0; // numero de dias * 2 (taxa por dia atrasado)
						}
					}
				}else { // se a açao nao for rent ou return
					sbRentals.append("Invalid action: client " + userId);
					sbRentals.append(" tried to " + action[0]);
					sbRentals.append(System.lineSeparator());
				}
			}catch (NullPointerException e) {
				sbRentals.append("No rentals made today");
				sbRentals.append(System.lineSeparator());
			}
		}
		
		return sbRentals.toString();
	}
	
	/**
	 * Atualiza o stock do VideoClub após todas as ações feitas durante o dia.
	 *
	 * @throws FileNotFoundException - exceção para ficheiro não encontrado
	 * @param fileName - nome do ficheiro onde escrever o novo stock
	 */
	public void updateStock(String fileName) throws
	FileNotFoundException { 
		StringBuilder newStock = new StringBuilder("Title,Year,Quantity,Rentals,Price,Tax"); // cabecalho do ficheiro
		newStock.append(System.lineSeparator());
		
		PrintWriter escrever = new PrintWriter((fileName)); // cria um novo ficheiro
		for (Movie filme : filmes) { // escreve o novo stock no ficheiro
			filme.updateRentalsTime(); // atualiza o tempo de aluguer
			
		    newStock.append(filme.getTitle() + ",");
		    newStock.append(filme.getYear() + ",");
		    newStock.append(filme.getQuantity() + ",");
		    newStock.append(rentalsArrayToString(filme.getRentals()) + ",");
		    newStock.append(filme.getPrice() + ",");
		    newStock.append(filme.getTax() + "%");
			newStock.append(System.lineSeparator());
		}
		
		escrever.write(newStock.toString());
		escrever.close();
	}
	
	/**
	 * Função auxiliar para converter um array de rentals para uma string 2D.
	 * 
	 * @param rentals - array de rentals a converter
	 * @return uma string 2D dos rentals do filme
	 */
	private String rentalsArrayToString(int[][] rentals) {
		StringBuilder rentalsString = new StringBuilder();

		for (int i = 0; i < rentals.length; i++) {
			if(i != rentals.length - 1)
				rentalsString.append("(" + rentals[i][0] + ";" + rentals[i][1] + ") ");
			else
				rentalsString.append("(" + rentals[i][0] + ";" + rentals[i][1] + ")");		
		}
		return rentalsString.toString();
	}
	
	/**
	 * Função auxiliar para ler o stock do VideoClub.
	 * 
	 * @param fileName - nome do ficheiro do stock
	 * @param numberOfMovies - número de filmes a retirar do stock
	 * @throws FileNotFoundException - exceção para ficheiro não encontrado
	 * @return uma array com todos os parametros de cada filme em stock em cada linha
	 */
	private String[][] readStock(String fileName, int numberOfMovies)throws
	FileNotFoundException {
		Scanner ler = new Scanner(new File(fileName));
		ler.nextLine(); // skippar cabealho
		int nLinhas = 0;
		
		String[][] stock = new String[numberOfMovies][6]; // cria um array de strings com o tamanho do numero de filmes	
		
		for(int i = 0; i < numberOfMovies && ler.hasNext(); i++) {
			stock[i] = ler.nextLine().split(","); // separa a string por virgula
			nLinhas++;
		}
		
		if (nLinhas < numberOfMovies) {
			stock = Arrays.copyOf(stock, nLinhas); // se o numero de linhas for menor que o numero de filmes, copia o array para um novo array com o tamanho do numero de linhas
		}
		
		ler.close();
		return stock;
	}
	
	/**
	 * Função auxiliar para ler o ficheiro de rentals.
	 * 
	 * @param rentalsFileName - nome do ficheiro de rentals feitos durante o dia
	 * @throws FileNotFoundException - exceção para ficheiro não encontrado
	 * @return uma array com todos os rentals feitos durante o dia
	 */
	private String[][] readRentalsFile(String rentalsFileName)throws
	FileNotFoundException {
		Scanner ler = new Scanner(new File(rentalsFileName));
		ler.nextLine(); // skippar cabeçalho
		int nLinhas = 0;
		
		String[][] rentals = new String[1][3];

		while(ler.hasNext()) {
			rentals = Arrays.copyOf(rentals, nLinhas + 1);
			rentals[nLinhas] = ler.nextLine().split(",");
			nLinhas++;
		}
		
		ler.close();
		return rentals;
	}
	
	/**
	 * Função auxiliar para converter a String representativa de rentals do filme numa array de inteiros.
	 * 
	 * @param rentals - string representativa de rentals do filme
	 * @return uma array de inteiros representativa de rentals do filme
	 */
	private int[][] rentalsParse(String rentals) {
		if (rentals.equals("")) { // se a string estiver vazia
			return new int[0][0]; // retorna um array vazio
		}
		
		rentals = rentals.replace("(", ""); // remove os parenteses da string
		rentals = rentals.replace(")", ""); // remove os parenteses da string
		
		String[] lines = rentals.split(" "); // separa a string por espacos
		
		String[][] rentalsArString = new String[lines.length][2]; // cria um array de strings com o tamanho do numero de linhas
		int[][] rentalsAr = new int[lines.length][2]; // cria um array de inteiros com o tamanho do numero de linhas
		
		for(int i = 0; i < rentalsAr.length; i++) { // separa a string por ponto e virgula
			rentalsArString[i] = lines[i].split(";"); 
			for(int j = 0; j < 2; j++) { // converte os valores de string para int
				rentalsAr[i][j] = Integer.parseInt(rentalsArString[i][j]);
			}
		}
		return rentalsAr;
	}
	
	/**
	 * Verifica se um filme existe no VideoClub.
	 * 
	 * @param name - nome do filme a procurar
	 * @return true se o filme existir, false se o filme nao existir
	 */
	private boolean doesMovieExist(String name) {
		for(Movie filme : filmes) { // procura o filme pelo nome ou pelo codigo
			if(filme.getTitle().equals(name) || filme.getCode().equals(name)) {
				return true; // retorna true se o filme existir
			}
		}
		return false;  // retorna false se o filme nao existir
	}
	
	/**
	 * Verifica se existe pelo menos uma cópia de dado filme.
	 * 
	 * @param movie - filme a verificar
	 * @return true se o filme estiver disponível, false se o filme nao estiver disponível
	 */
	private boolean isMovieAvailable(Movie movie) {
		return movie.getQuantity() >= 1; // retorna true se a quantidade do filme for maior ou igual a 1
	}
	
	/**
	 * Procura um filme pelo nome ou pelo código.
	 * 
	 * @param name - nome ou código do filme a procurar
	 * @return o filme encontrado
	 */
	private Movie findMovie(String name) {
		for(Movie filme : filmes) { // procura o filme pelo nome ou pelo codigo
			if (filme.getTitle().equals(name) || filme.getCode().equals(name))
				return filme;
		}
		return filmes[0]; //nao sera alcancado
	}
 }
