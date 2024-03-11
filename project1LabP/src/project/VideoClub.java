package project;

import java.io.FileNotFoundException;
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
		return "pixa";
	}
	
	public String filterByPrice(double price) {
		return "";
	}
	
	public String filterAvailableMovies() {
		return "";
	}
	
	public String activityLog(String rentalsFileName) throws
	FileNotFoundException{
		return "";
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
			nLinhas++;
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
}
