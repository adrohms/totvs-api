package com.totvs.domain.common.repository;

import com.totvs.domain.common.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Phone entity.
 */
@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    List<Phone> findPhoneByNumber(String number);
}
