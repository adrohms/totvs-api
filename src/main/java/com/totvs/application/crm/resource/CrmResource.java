package com.totvs.application.crm.resource;

import com.totvs.infrastructure.exception.BadRequestAlertException;
import com.totvs.application.crm.dto.ClientRec;
import com.totvs.application.crm.dto.ClientVMParameters;
import com.totvs.application.util.HeaderUtil;
import com.totvs.application.util.ResponseUtil;
import com.totvs.domain.crm.repository.CrmRepository;
import com.totvs.domain.crm.service.impl.CrmServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/crm")
public class CrmResource {

    private final Logger log = LoggerFactory.getLogger(CrmResource.class);

    private static final String ENTITY_NAME = "client";

    @Value("${totvs.clientApp.name}")
    private String applicationName;

    private final CrmServiceImpl crmService;

    private final CrmRepository clientRepository;

    public CrmResource(CrmServiceImpl crmService, CrmRepository crmRepository) {
        this.crmService = crmService;
        this.clientRepository = crmRepository;
    }

    /**
     * {@code POST  /client/create} : Create a new client.
     *
     * @param clientRec the clientRec to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientRec, or with status {@code 400 (Bad Request)} if the client has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/client/create")
    public ResponseEntity<ClientRec> createClient(@Valid @RequestBody ClientRec clientRec) throws URISyntaxException {
        log.debug("REST request to save Client : {}", clientRec);
        if (clientRec.id() != null) {
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ClientRec result = crmService.save(clientRec);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /client/:id} : Updates an existing client.
     *
     * @param id the id of the clientRec to save.
     * @param clientRec the clientRec to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientRec,
     * or with status {@code 400 (Bad Request)} if the clientRec is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientRec couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/client/{id}")
    public ResponseEntity<ClientRec> updateClient(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody ClientRec clientRec
    ) throws URISyntaxException {
        log.debug("REST request to update Client : {}, {}", id, clientRec);
        if (clientRec.id() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientRec.id())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClientRec result = crmService.update(clientRec);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientRec.id().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /client/:id} : Partial updates given fields of an existing client, field will ignore if it is null
     *
     * @param id the id of the clientRec to save.
     * @param clientRec the clientRec to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientRec,
     * or with status {@code 400 (Bad Request)} if the clientRec is not valid,
     * or with status {@code 404 (Not Found)} if the clientRec is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientRec couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/client/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClientRec> partialUpdateClient(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody ClientRec clientRec
    ) throws URISyntaxException {
        log.debug("REST request to partial update Client partially : {}, {}", id, clientRec);
        if (clientRec.id() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientRec.id())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientRec> result = crmService.partialUpdate(clientRec);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientRec.id().toString())
        );
    }

    /**
     * {@code GET  /client/all} : get all the client.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of client in body.
     */
    @GetMapping("/client/all")
    public List<ClientRec> getAllClients() {
        log.debug("REST request to get all People");
        return crmService.findAll();
    }

    @GetMapping("/client/filter")
    public ResponseEntity<Page<ClientRec>> findAllByFilter(
            @ModelAttribute ClientVMParameters clientVMParameters,
            Pageable pageable
    ) {
        Page<ClientRec> users = this.crmService.findAllByFilter(clientVMParameters, pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * {@code GET  /client/:id} : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/client/{id}")
    public ResponseEntity<ClientRec> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Optional<ClientRec> clientRec = crmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientRec);
    }

    /**
     * {@code DELETE  /client/:id} : delete the "id" client.
     *
     * @param id the id of the clientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/client/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        crmService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}