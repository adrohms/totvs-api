package com.totvs.domain.common.repository;

import com.totvs.domain.common.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Address entity.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByZipCode(String zipCode);
}
