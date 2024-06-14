package com.totvs.domain.crm.model;


import com.totvs.domain.common.AbstractAuditingEntity;
import com.totvs.domain.common.model.Person;
import com.totvs.domain.crm.enumeration.InterestType;
import com.totvs.domain.crm.enumeration.OriginType;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "totvs_client")
public class Client extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin")
    private OriginType origin;


    @Enumerated(EnumType.STRING)
    @Column(name = "interest")
    private InterestType interest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OriginType getOrigin() {
        return origin;
    }

    public void setOrigin(OriginType origin) {
        this.origin = origin;
    }

    public InterestType getInterest() {
        return interest;
    }

    public void setInterest(InterestType interest) {
        this.interest = interest;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getUserEmail(Client user){
        if(user.getPerson()!=null){
            return user.getPerson().getEmail();
        } else {
            return null; // or some default value
        }
    }

    public String getUserName(Client user){
        if(user.getPerson()!=null){
            return user.getPerson().getName();
        } else {
            return null; // or some default value
        }
    }

    public String getUserName(){
        if(this.getPerson()!=null){
            return this.getPerson().getName();
        } else {
            return null; // or some default value
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

}