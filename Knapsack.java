import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
* This program solves the Knapsack problem with one additional factor: the weight
* In order to select the subset of products that has the most values but lowest weight,
* we need to sort the product list by weight in ascending order before applying the knapsack algorithm
*
* @author  Hung Chu - hungcq83@gmail.com
* @version 1.0
* @since   2018-01-19 
*/
public class Knapsack {
	static int[] price = new int[20001];
	static int[] volume = new int[20001];;
	static int[] productID = new int[20001];
	static int[] weight = new int[20001];
	static final int length = 45;
	static final int width = 30;
	static final int height = 35;
	static int count = 0;
	static final int totalVolume = length * width * height;

	// A utility function that returns maximum of two integers
	static int max(int a, int b) {
		return (a > b) ? a : b;
	}

	// Returns the maximum value that can be put in a knapsack of capacity W
	static int knapSack(int maxVolume, int volume[], int price[], int numberOfProducts) {
		int i, w;
		int K[][] = new int[numberOfProducts + 1][maxVolume + 1];

		// Build table K[][] in bottom up manner
		for (i = 0; i <= numberOfProducts; i++) {
			for (w = 0; w <= maxVolume; w++) {
				if (i == 0 || w == 0)
					K[i][w] = 0;
				else if (volume[i - 1] <= w)
					K[i][w] = max(price[i - 1] + K[i - 1][w - volume[i - 1]], K[i - 1][w]);
				else
					K[i][w] = K[i - 1][w];
			}
		}

		//pick products
		int maxValue = K[numberOfProducts][maxVolume];
		List<Result> results = new ArrayList<Result>();
		for (int idx=maxVolume; idx>0; idx--) {
			if (K[numberOfProducts][idx] < maxValue) {
				break;
			}
			i = numberOfProducts;
			int j = idx;
			List<Integer> productIDs = new ArrayList<Integer>();
			int totalPrice = 0;
			int totalVol = 0;
			int productIDSum = 0;
			int totalWeight = 0;
			count = 0;
			while (i > 0 && j > 0 ) {
				if (K[i][j] != K[i-1][j]) {
					i--;
					count++;
					j = j - volume[i];
					productIDs.add(productID[i]);
					totalPrice += price[i];
					totalVol += volume[i];
					productIDSum += productID[i];
					totalWeight += weight[i];
				}
				else {
					i--;
				}
			}
			results.add(new Result(productIDSum, totalVol, totalWeight, totalPrice, productIDs));
	
		}
		Collections.sort(results);

		System.out.println("Total price = " + results.get(0).totalPrice);
		System.out.println("Total volume = " + results.get(0).totalVolume);
		System.out.println("Total weight = " + results.get(0).totalWeight);
		System.out.println("ProductIDSum = " + results.get(0).productIDSum);
		System.out.println("ProductIDs = " + results.get(0).productIDs);

		return K[numberOfProducts][maxVolume];
	}

	static void readFile(String fileName) {
		List<ProductInfo> productInfos = new ArrayList<ProductInfo>();
		// read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach(line -> {
				String[] arr = line.split(",");
				int pProductID = Integer.valueOf(arr[0]);
				int pPrice = Integer.valueOf(arr[1]);
				int pLength = Integer.valueOf(arr[2]);
				int pWidth = Integer.valueOf(arr[3]);
				int pHeight = Integer.valueOf(arr[4]);
				int pWeight = Integer.valueOf(arr[5]);
				int pVolume = pLength * pWidth * pHeight;
				if (pLength <= length && pWidth <= width && pHeight <= height) {
					productInfos.add(new ProductInfo(pProductID, pVolume, pWeight, pPrice));
					count++;
				}
			});
			// sort products by weight in ascending order
			Collections.sort(productInfos);
			count = 0;
			productInfos.forEach(productInfo -> {
				productID[count] = productInfo.getProductID();
				price[count] = productInfo.getPrice();
				volume[count] = productInfo.getVolume();
				weight[count] = productInfo.getWeight();
				count++;
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Driver program to test above function
	public static void main(String args[]) {
		readFile("products.csv");
		System.out.println(knapSack(totalVolume, volume, price, 20000));
	}
}

class ProductInfo implements Comparable {
	int productID;
	int volume;
	int weight;
	int price;	
	
	public ProductInfo(int productID, int volume, int weight, int price) {
		this.productID = productID;
		this.volume = volume;
		this.weight = weight;
		this.price = price;
	}
	
	
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}


	@Override
	public int compareTo(Object o) {
		ProductInfo product = (ProductInfo) o;
		return this.weight - product.weight;
	}
}

class Result implements Comparable {
	int productIDSum;
	int totalVolume;
	int totalWeight;
	int totalPrice;
	List<Integer> productIDs;
	
	public Result(int productIDSum, int totalVolume, int totalWeight, int totalPrice, List productIDs) {
		this.productIDSum = productIDSum;
		this.totalVolume = totalVolume;
		this.totalWeight = totalWeight;
		this.totalPrice = totalPrice;
		this.productIDs = productIDs;
	}
	
	public int getProductIDSum() {
		return productIDSum;
	}
	public void setProductIDSum(int productIDSum) {
		this.productIDSum = productIDSum;
	}
	public int getTotalVolume() {
		return totalVolume;
	}
	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}
	public int getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public int compareTo(Object arg0) {
		Result result = (Result) arg0;
		return this.totalWeight - result.totalWeight;
	}
	
}