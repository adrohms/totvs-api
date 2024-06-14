package com.totvs.domain.account.repository;

import com.totvs.application.account.dto.UserMngtVM;
import com.totvs.domain.account.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneById(Long userId);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByPerson_EmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String login);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.person p " +
            "JOIN FETCH u.authorities auth WHERE " +
            "(:#{#filter.id} IS NULL OR u.id = :#{#filter.id}) " +
            "AND (:#{#filter.name} IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#filter.name}, '%'))) " +
            "AND (:#{#filter.email} IS NULL OR u.email LIKE %:#{#filter.email}%) " +
            "AND (:#{#filter.authorities} IS NULL OR auth.permissionName IN :#{#filter.authorities})")
    Page<User> findUsersByFilter(@Param("filter") UserMngtVM filter, Pageable pageable);
}
