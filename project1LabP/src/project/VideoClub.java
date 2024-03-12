package project;

import java.io.FileNotFoundException;
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
		int filmesTotais = 0;
		
		for(Movie filme : filmes)
			filmesTotais += filme.getQuantity();
		return filmesTotais;
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
			if(filme.getYear() == year)
				yearFilter.append(String.format("Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return yearFilter.toString();
	}
	
	public String filterByPrice(double price) {
		StringBuilder priceFilter = new StringBuilder();
		
		for(Movie filme : filmes) {
			if(filme.getPrice() <= price)
				priceFilter.append(String.format("Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return priceFilter.toString();
	}
	
	public String filterAvailableMovies() {
		StringBuilder availableMovies = new StringBuilder();
		
		for(Movie filme : filmes) {
			if(filme.getQuantity() >= 1)
				availableMovies.append(String.format("Title:" + filme.getTitle() + ",Price:$%.2f" + System.lineSeparator(), filme.getPrice()));
		}
		
		return availableMovies.toString();
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
			
			if(action[0] == "rent") {
				if(!doesMovieExist(title)) {
					sbRentals.append("Movie not found: client " + userId + " asked for " + title + System.lineSeparator());
				}else {
					Movie currentFilme = findMovie(title);
					
					if(!isMovieAvailable(currentFilme)) {
						sbRentals.append("Movie currently not available: client " + userId + " asked for " + currentFilme.getTitle() + System.lineSeparator());
					}else {
						afterTax = currentFilme.getPrice() * (currentFilme.getTax() / 100);
						sbRentals.append(String.format("Rental sucessful: client " + userId + " rented " + currentFilme.getTitle() + "for $%.2f" + System.lineSeparator() + 
						"Total: $%.2f" + "[" + afterTax + "]" , currentFilme.getPrice()));
						
						currentFilme.rentalRegister(Integer.parseInt(userId)); // regista um novo rental
						
						totalRevenue += currentFilme.getPrice(); // adiciona ao total de revenue o preço do filme
						totalProfit += afterTax; // adiciona ao profit o total apos as taxas de estudio
					}
				}
			}else {
				Movie currentFilme = findMovie(title);
				
				int numOfLateDays = Math.abs(currentFilme.getRentals()[currentFilme.findUserIndex(Integer.parseInt(userId))][1]);
				
				if(numOfLateDays > -1) { // verificar se o user esta OU nao com dias em atraso
					sbRentals.append("Movie returned: client " + userId + " returned " + currentFilme.getTitle() + System.lineSeparator());
					currentFilme.rentalUnregister(Integer.parseInt(userId));
					
				}else {
					sbRentals.append("Movie returned with " + numOfLateDays + " days of delay: client " + userId + " returned " + currentFilme.getTitle() + System.lineSeparator());
					currentFilme.rentalUnregister(Integer.parseInt(userId));
					
					totalProfit += numOfLateDays * 2.0;
				}
			}
			
			sbRentals.append("Revenue: " + totalRevenue + System.lineSeparator());
			sbRentals.append("Profit: " + totalProfit + System.lineSeparator());
		}
		return sbRentals.toString();
	}
	
	public void updateStock(String fileName) throws
	FileNotFoundException {
		
	}
	
	private String[][] readStock(String fileName, int numberOfMovies)throws
	FileNotFoundException {
		Scanner ler = new Scanner(new File(fileName));
		ler.nextLine(); //skippar cabealho
		int nLinhas = 0;
		
		while(ler.hasNext()) {
			nLinhas++; // contar linhas
			ler.nextLine(); 
		}
		
		ler.close();
		
		Scanner ler2 = new Scanner(new File(fileName));
		ler2.nextLine(); //skippar cabealho
		
		String[][] stock = new String[numberOfMovies <= nLinhas ? numberOfMovies : nLinhas][6]; // se o n de linhas for menor que numberOfMovies utiliza-se isso
		
		for(int i = 0; i < nLinhas && i < numberOfMovies; i++) { // acaba quando chegar ao maximo, seja ele o n de linhas ou o n de filmes pedidos
			stock[i] = ler2.nextLine().split(",");
		}
		
		ler2.close();
		return stock;
	}
	
	private String[][] readRentals(String rentalsFileName)throws
	FileNotFoundException {
		Scanner ler = new Scanner(new File(rentalsFileName));
		ler.nextLine(); //skippar cabealho
		int nLinhas = 0;
		
		while(ler.hasNext()) {
			nLinhas++; // contar linhas
			ler.nextLine(); 
		}
		
		ler.close();
		
		Scanner ler2 = new Scanner(new File(rentalsFileName));
		ler2.nextLine(); //skippar cabealho
		
		String[][] rentals = new String[nLinhas][3];
		
		for(int i = 0; i < nLinhas; i++) {
			rentals[i] = ler2.nextLine().split(",");
		}
		
		ler2.close();
		
		return rentals;
	}
	
	private int[][] rentalsParse(String rentals) {
		rentals.replace("[()]", ""); // remove os parenteses da string
		
		String[] lines = rentals.split(" ");
		
		String[][] rentalsArString = new String[lines.length][2];
		int[][] rentalsAr = new int[lines.length][2];
		
		if(!(rentals != null)) {
			for(int i = 0; i < rentalsAr.length; i++) {
				rentalsArString[i] = lines[i].split(";");
				for(int j = 0; j < 2; j++) {
					rentalsAr[i][j] = Integer.parseInt(rentalsArString[i][j]);
				}
			}
		}
		return rentalsAr;
	}
	
	private boolean doesMovieExist(String name) {
		for(Movie filme : filmes) {
			if(filme.getTitle() == name || filme.getCode() == name) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isMovieAvailable(Movie movie) {
		return movie.getQuantity() >= 1;
	}
	
	private Movie findMovie(String name) {
		for(Movie filme : filmes) {
			if (filme.getTitle() == name || filme.getCode() == name)
				return filme;
		}
		return filmes[0]; //nao sera alcancado
	}
 }
