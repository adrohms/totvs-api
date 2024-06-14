package com.totvs.domain.crm.repository;

import com.totvs.application.account.dto.UserMngtVM;
import com.totvs.application.crm.dto.ClientRec;
import com.totvs.application.crm.dto.ClientVMParameters;
import com.totvs.domain.account.model.User;
import com.totvs.domain.crm.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Client} entity.
 */
@Repository
public interface CrmRepository extends JpaRepository<Client, Long> {

    @Query("SELECT DISTINCT c FROM Client c " +
            "JOIN FETCH c.person p " +
            "WHERE (:#{#filter.id} IS NULL OR c.id = :#{#filter.id}) " +
            "AND (:#{#filter.name} IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#filter.name}, '%'))) " +
            "AND (:#{#filter.email} IS NULL OR p.email LIKE %:#{#filter.email}%) ")
    Page<Client> findAllByFilter(@Param("filter") ClientVMParameters filter, Pageable pageable);
}
