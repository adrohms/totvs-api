package com.totvs.domain.common.service.impl;

import com.totvs.application.common.dto.PersonRec;
import com.totvs.domain.common.model.Person;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Person}.
 */
public interface PersonImpl {

    /**
     * Save a person.
     *
     * @param personRec the entity to save.
     * @return the persisted entity.
     */
    PersonRec save(PersonRec personRec);

    /**
     * Updates a person.
     *
     * @param personRec the entity to update.
     * @return the persisted entity.
     */
    PersonRec update(PersonRec personRec);

    /**
     * Partially updates a person.
     *
     * @param personRec the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PersonRec> partialUpdate(PersonRec personRec);

    /**
     * Get all the people.
     *
     * @return the list of entities.
     */
    List<PersonRec> findAll();

    /**
     * Get the "id" person.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonRec> findOne(Long id);

    /**
     * Delete the "id" person.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
