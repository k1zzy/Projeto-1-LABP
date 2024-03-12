package project;

import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;

public class VideoClub {
	private int totalProfit;
	private int totalRevenue;
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
									stock[i][3], // rentals
									Double.parseDouble(stock[i][4]), // price
									Double.parseDouble(stock[i][5].replace("%", "")) // tax, delete a %
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
				yearFilter.append(String.format("Title:" + filme.getTitle() + ",Price:$%.2f \n", filme.getPrice()));
		}
		
		return yearFilter.toString();
	}
	
	public String filterByPrice(double price) {
		StringBuilder priceFilter = new StringBuilder();
		
		for(Movie filme : filmes) {
			if(filme.getPrice() <= price)
				priceFilter.append(String.format("Title:" + filme.getTitle() + ",Price:$%.2f \n", filme.getPrice()));
		}
		
		return priceFilter.toString();
	}
	
	public String filterAvailableMovies() {
		StringBuilder availableMovies = new StringBuilder();
		
		for(Movie filme : filmes) {
			if(filme.getQuantity() >= 1)
				availableMovies.append(String.format("Title:" + filme.getTitle() + ",Price:$%.2f \n", filme.getPrice()));
		}
		
		return availableMovies.toString();
	}
	
	public String activityLog(String rentalsFileName) throws
	FileNotFoundException{
		String[][] rentalsRead = readRentals(rentalsFileName);
		StringBuilder sbRentals = new StringBuilder();
		
		for(String[] action : rentalsRead) {
			if(action[0] == "rent") {
				switch(isMovieAvailable(action[0])) {
				case -1:
					sbRentals.append("Movie not found: client " + action[1] + " asked for " + action[2]);
					break;
				case 0:
					Movie currentFilme = findMovie(action[2]);
					sbRentals.append("Movie currently not available: client " + action[1] + " asked for " + currentFilme.getTitle());
					break;
				case 1:
					Movie currentFilme2 = findMovie(action[2]);
					sbRentals.append(String.format("Rental sucessful: client " + action[1] + " rented " + currentFilme2.getTitle() + "for $%.2f \n", currentFilme.getPrice()));
					break;
				}
			}
			else {
				
			}
		}
		
		return "blah";
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
	
	private int isMovieAvailable(String name) {
		for(Movie filme : filmes) {
			if(filme.getTitle() == name || filme.getCode() == name)
				if(filme.getQuantity() >= 1)
					return 1; // rental successful
				else
					return 0; // not in stock
		}
		return -1; // not found in database
	}
	
	private Movie findMovie(String name) {
		for(Movie filme : filmes) {
			if (filme.getTitle() == name || filme.getCode() == name)
				return filme;
		}
		return filmes[0]; //nao sera alcancado
	}
 }
