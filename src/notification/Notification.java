package notification;

import DAO.BidsDao;
import DAO.ProductsDao;
import beans.Bid;
import beans.Product;
import beans.Products;
import email.Email;

public class Notification implements Runnable {

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(10000);
				BidsDao bidsdao = new BidsDao();
				ProductsDao dao = new ProductsDao();
				Products products  = dao.getExpiredProducts();
				for(Product p : products.getProducts()) {
					dao.markSold(p);
					Bid b = bidsdao.getHighestBidder(p);
					if(b!=null) {
						String buyer_email = b.getBuyerEmail();
						String seller_email = b.getSellerEmail();
						new Email(buyer_email, "Product is Sold to you");
						new Email(seller_email, "Ypur product has been sold");
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
