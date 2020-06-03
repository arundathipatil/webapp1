package neu.edu.csye6225.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String isbn;
    private String title;
    private String authors;
    private Date  publicationDate;
    private int quantity;
    private double price;
    private Date createdDate;
    private Date updatedDate;
    private String userEmail;

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

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int id;
//
//    @Column(name = "ISBN", unique = true)
//    private String ISBN;
//    @Column(name = "title")
//    private String title;
//    @Column(name = "authors")
//    private String authors;
//    @Column(name = "publication_date")
//    private Date  publicationDate;
//    @Column(name = "quantity")
//    private int quantity;
//    @Column(name = "price")
//    private double price;
//    @Column(name= "createdDate")
//    private Date createdDate;
//    @Column(name = "updatedDate")
//    private Date updatedDate;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId", referencedColumnName = "id")
//    private User user;
////    private int userId;
//
//    public User getUser() {
//        return user;
//    }
//db_webapp
//    public void setUser(User user) {
//        this.user = user;
//        user.getBooks().add(this);
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getISBN() {
//        return ISBN;
//    }
//
//    public void setISBN(String ISBN) {
//        this.ISBN = ISBN;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getAuthors() {
//        return authors;
//    }
//
//    public void setAuthors(String authors) {
//        this.authors = authors;
//    }
//
//    public Date getPublicationDate() {
//        return publicationDate;
//    }
//
//    public void setPublicationDate(Date publicationDate) {
//        this.publicationDate = publicationDate;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public Date getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public Date getUpdatedDate() {
//        return updatedDate;
//    }
//
//    public void setUpdatedDate(Date updatedDate) {
//        this.updatedDate = updatedDate;
//    }
}
