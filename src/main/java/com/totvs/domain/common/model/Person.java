package com.totvs.domain.common.model;

import com.totvs.domain.common.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.totvs.domain.common.enumeration.PersonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "totvs_person")
public class Person extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 10, max = 254)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tax_id", nullable = false)
    private String taxId;

    @Enumerated(EnumType.STRING)
    @Column(name = "person_type")
    private PersonType personType;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Phone> phones = new HashSet<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Address> addresses = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Person firstName(String firstName) {
        this.setName(firstName);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return this.taxId;
    }

    public Person taxId(String taxId) {
        this.setTaxId(taxId);
        return this;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public PersonType getPersonType() {
        return this.personType;
    }

    public Person personType(PersonType personType) {
        this.setPersonType(personType);
        return this;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setPerson(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setPerson(this));
        }
        this.addresses = addresses;
    }

    public Person addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Person addAddress(Address address) {
        this.addresses.add(address);
        address.setPerson(this);
        return this;
    }

    public Person removeAddress(Address address) {
        this.addresses.remove(address);
        address.setPerson(null);
        return this;
    }

    public Set<Phone> getPhones() {
        return this.phones;
    }

    public void setPhones(Set<Phone> phones) {
        if (this.phones != null) {
            this.phones.forEach(i -> i.setPerson(null));
        }
        if (phones != null) {
            phones.forEach(i -> i.setPerson(this));
        }
        this.phones = phones;
    }

    public Person phones(Set<Phone> phones) {
        this.setPhones(phones);
        return this;
    }

    public Person addPhone(Phone phone) {
        this.phones.add(phone);
        phone.setPerson(this);
        return this;
    }

    public Person removePhone(Phone phone) {
        this.phones.remove(phone);
        phone.setPerson(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + getId() +
                ", firstName='" + getName() + "'" +
                ", taxId='" + getTaxId() + "'" +
                ", personType='" + getPersonType() + "'" +
                "}";
    }
}
