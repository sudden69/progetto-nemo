package com.example.nemo.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user",schema = "public")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "code", nullable = true, length = 70)
    private String code;
    @Basic
    @Column(name = "first_name", nullable = true, length = 50)
    private String firstName;
    @Basic
    @Column(name = "last_name", nullable = true, length = 50)
    private String lastName;
    @Basic
    @Column(name = "telephone_number", nullable = true, length = 20)
    private String telephoneNumber;
    @Basic
    @Column(name = "email", nullable = true, length = 90)
    private String email;
    @Basic
    @Column(name = "address", nullable = true, length = 150)
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(code, that.code) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(telephoneNumber, that.telephoneNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, firstName, lastName, telephoneNumber, email, address);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }


}