package com.totvs.domain.crm.service.impl;

import com.totvs.application.crm.dto.ClientRec;
import com.totvs.application.crm.dto.ClientVMParameters;
import com.totvs.domain.crm.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Client}.
 */
public interface CrmServiceImpl {

    /**
     * Save a client.
     *
     * @param clientRec the entity to save.
     * @return the persisted entity.
     */
    ClientRec save(ClientRec clientRec);

    /**
     * Updates a client.
     *
     * @param clientRec the entity to update.
     * @return the persisted entity.
     */
    ClientRec update(ClientRec clientRec);

    /**
     * Partially updates a client.
     *
     * @param clientRec the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClientRec> partialUpdate(ClientRec clientRec);

    /**
     * Get all the client.
     *
     * @return the list of entities.
     */
    List<ClientRec> findAll();

    /**
     * Get all the client by filter.
     *
     * @param clientVMParameters
     * @return the list of entities.
     */
    Page<ClientRec> findAllByFilter(ClientVMParameters clientVMParameters, Pageable pageable);

    /**
     * Get the "id" client.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClientRec> findOne(Long id);

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
