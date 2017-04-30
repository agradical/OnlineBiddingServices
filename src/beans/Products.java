package beans;
import java.util.ArrayList;
import java.util.List;

public class Products {
	
	ArrayList<Product> products = new ArrayList<Product>();
	
	public List<Product> getProducts() {
		return products;
	}
	
	public void addProducts(Product product) {
		products.add(product);
	}
}
	
	




