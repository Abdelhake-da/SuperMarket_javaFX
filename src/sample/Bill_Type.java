package sample;

public class Bill_Type {
    int Id;
    String Name;
    int Quantity;
    Double Total;

    public Bill_Type(int id, String name, int quantity, Double total) {
        Id = id;
        Name = name;
        Quantity = quantity;
        Total = total;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }
}
