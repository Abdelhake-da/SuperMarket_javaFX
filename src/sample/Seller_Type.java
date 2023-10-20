package sample;

public class Seller_Type {
    int Id;
    String Name ;
    String Password;
    String Gender;

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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    String Role;

    public Seller_Type(int id, String name, String password, String gender, String role) {
        Id = id;
        Name = name;
        Password = password;
        Gender = gender;
        Role = role;
    }

}
