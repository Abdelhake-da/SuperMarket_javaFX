package sample;

public class Prodect_Type {
    int Id;
    String BarCode;
    int Quantity;
    String Name;
    String Category;
    Double Price;

    public Prodect_Type(int id, String barCode, int quantity, String name, String category, Double price) {
        Id = id;
        BarCode = barCode;
        Quantity = quantity;
        Name = name;
        Category = category;
        Price = price;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
}
