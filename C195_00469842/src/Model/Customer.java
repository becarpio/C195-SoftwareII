package Model;

public class Customer {
    private int customerID;
    private String customer;
    private Boolean active;
    private int addressID;
    private String address;
    private String address2;
    private int postalCode;
    private String phone;
    private String city;

    //Pull any other city info from the City module
    //Constructors
    public Customer() {
        customerID = 0;
        customer = null;
        active = false;
        addressID = 0;
        address = null;
        address2 = null;
        postalCode = 0;
        phone = null;
        city = null;
    }

    public Customer(int customerID, String customer, Boolean active, int addressID, String address, String address2, int postalCode, String phone, String city) {
        this.customerID = customerID;
        this.customer = customer;
        this.active = active;
        this.addressID = addressID;
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city;
    }

    //Getters and Setters
    public int getCustomerID() {
        return customerID;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getAddressID() {
        return addressID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
