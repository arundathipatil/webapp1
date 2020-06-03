package neu.edu.csye6225.model;

import javax.persistence.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String isbn;
    private String buyersemail;
    private String title;
    private int quantity;
    private String sellersemail;
    private double price;

    public String getBuyersemail() {
        return buyersemail;
    }

    public void setBuyersemail(String buyersemail) {
        this.buyersemail = buyersemail;
    }

    public String getSellersemail() {
        return sellersemail;
    }

    public void setSellersemail(String sellersemail) {
        this.sellersemail = sellersemail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
