package project;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;

public class VideoClub {
	private double totalProfit;
	private double totalRevenue;
	private Movie[] filmes;
	
	public VideoClub(String fileName, int numberOfMovies)throws
	FileNotFoundException {
		String[][] stock = readStock(fileName, numberOfMovies);
		filmes = new Movie[stock.length];
		
		for(int i = 0; i < stock.length; i++) {
			filmes[i] = new Movie(
									stock[i][0], // title
									Integer.parseInt(stock[i][1]), // year
									Integer.parseInt(stock[i][2]), // quantity
									rentalsParse(stock[i][3]), // rentals
									Double.parseDouble(stock[i][4]), // price
									Double.parseDouble(stock[i][5].replace("%", "")) // tax, deletes %
								 );
		}
	}
	
	public int getNumberOfMovies() {
		return filmes.length;
	}
	
	public int numberAvailableMovies() {
		int filmesAvailable = 0;
		
		for(Movie filme : filmes) {
			if(filme.getQuantity() >= 1)
				filmesAvailable++;
		}
		return filmesAvailable;
	}
	
	public double getTotalRevenue() {
		return totalRevenue;
	}
	
	public double getTotalProfit() {
		return totalProfit;
	}
	
	public String filterByYear(int year) {
		StringBuilder yearFilter = new StringBuilder();
		
		for(Movie filme : filmes) {
			if(filme.getYear() == year) // se o ano do filme for igual ao ano dado
				yearFilter.append(String.format(Locale.US,"Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return yearFilter.toString(); // retorna a lista de filmes com o ano igual ao ano dado
	}
	
	public String filterByPrice(double price) {
		StringBuilder priceFilter = new StringBuilder();
		
		for(Movie filme : filmes) { 
			if(filme.getPrice() <= price) // se o preço do filme for menor ou igual ao preço dado
				priceFilter.append(String.format(Locale.US,"Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return priceFilter.toString(); // retorna a lista de filmes com o preço menor ou igual ao preço dado
	}
	
	public String filterAvailableMovies() {
		StringBuilder availableMovies = new StringBuilder();
		
		for(Movie filme : filmes) { 
			if(filme.getQuantity() >= 1) // se a quantidade do filme for maior ou igual a 1
				availableMovies.append(String.format(Locale.US,"Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return availableMovies.toString(); // retorna a lista de filmes disponiveis
	}
	
	public String activityLog(String rentalsFileName) throws
	FileNotFoundException{
		String[][] rentalsRead = readRentals(rentalsFileName);
		StringBuilder sbRentals = new StringBuilder();
		double afterTax = 0;
		String userId = "";
		String title = "";
		
		for(String[] action : rentalsRead) {
			userId = action[1];
			title = action[2];
			
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
						
						currentFilme.rentalRegister(Integer.parseInt(userId)); // regista um novo rental
						
						totalRevenue += currentFilme.getPrice(); // adiciona ao total de revenue o preço do filme
						totalProfit += afterTax; // adiciona ao profit o total apos as taxas de estudio
						
						// revenue += currentFilme.getPrice(); // adiciona ao total de revenue o preço do filme
						// profit += afterTax; // adiciona ao profit o total apos as taxas de estudio
					}
				}
			}else {
				Movie currentFilme = findMovie(title);
				
				int numOfLateDays = 0;
				
				try {
					numOfLateDays = currentFilme.getRentals()[currentFilme.findUserIndex(Integer.parseInt(userId))][1]; // numero de dias em atraso
				} catch (Exception e) {
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
					
					String s = numOfLateDays == 1 ? "" : "s";
					
					sbRentals.append("Movie returned with " + numOfLateDays + " day" + s + " of delay: client " + userId);
					sbRentals.append(" returned " + currentFilme.getTitle());
					sbRentals.append(System.lineSeparator());
					sbRentals.append(String.format(Locale.US,"Total: $%.2f [$%.2f]", numOfLateDays * 2.0, afterTax));
					sbRentals.append(System.lineSeparator());
					
					currentFilme.rentalUnregister(Integer.parseInt(userId));
					
					totalProfit += afterTax; // adiciona ao profit o total apos as taxas de estudio
					totalRevenue += numOfLateDays * 2.0; // numero de dias * 2 (taxa por dia atrasado)
					
					// revenue += numOfLateDays * 2.0;; 
					// profit += numOfLateDays * 2.0; 	// numero de dias * 2 (taxa por dia atrasado)
				}
			}
		}
		//sbRentals.append(String.format(Locale.US,"Revenue: $%.2f" + System.lineSeparator(), revenue)); //pelos vistos nao e preciso
		//sbRentals.append(String.format(Locale.US,"Profit: $%.2f" + System.lineSeparator(), profit));
		
		return sbRentals.toString();
	}
	
	public void updateStock(String fileName) throws
	FileNotFoundException {
		
	}
	
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
	
	private String[][] readRentals(String rentalsFileName)throws
	FileNotFoundException {
		Scanner ler = new Scanner(new File(rentalsFileName));
		ler.nextLine(); // skippar cabealho
		int nLinhas = 0;
		
		while(ler.hasNext()) {
			nLinhas++; // contar linhas
			ler.nextLine(); 
		}
		
		ler.close();
		
		Scanner ler2 = new Scanner(new File(rentalsFileName));
		ler2.nextLine(); // skippar cabealho
		
		String[][] rentals = new String[nLinhas][3];
		
		for(int i = 0; i < nLinhas; i++) {
			rentals[i] = ler2.nextLine().split(",");
		}
		
		ler2.close();
		
		return rentals;
	}
	
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
	
	private boolean doesMovieExist(String name) {
		for(Movie filme : filmes) { // procura o filme pelo nome ou pelo codigo
			if(filme.getTitle().equals(name) || filme.getCode().equals(name)) {
				return true; // retorna true se o filme existir
			}
		}
		return false;  // retorna false se o filme nao existir
	}
	
	private boolean isMovieAvailable(Movie movie) {
		return movie.getQuantity() >= 1; // retorna true se a quantidade do filme for maior ou igual a 1
	}
	
	private Movie findMovie(String name) {
		for(Movie filme : filmes) { // procura o filme pelo nome ou pelo codigo
			if (filme.getTitle().equals(name) || filme.getCode().equals(name))
				return filme;
		}
		return filmes[0]; //nao sera alcancado
	}
 }
