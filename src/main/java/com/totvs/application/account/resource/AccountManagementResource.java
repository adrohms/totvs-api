package com.totvs.application.account.resource;

import com.totvs.domain.account.model.User;
import com.totvs.application.account.dto.UserMngtVM;
import com.totvs.domain.account.service.AccountManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/account")
public class AccountManagementResource {

    private static class AccountAdminResourceException extends RuntimeException {
        private AccountAdminResourceException(String message) {
            super(message);
        }
    }

    private final AccountManagementService accountManagementService;

    public AccountManagementResource(AccountManagementService accountManagementService) {
        this.accountManagementService = accountManagementService;
    }


    /**
     * {@code GET  /activate/${id}} : activate the registered user.
     *
     * @param id the User id.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate/{id}")
    public ResponseEntity activateAccount(@PathVariable(value = "id", required = false) final Long id) {
        Optional<User> user = this.accountManagementService.activateUserById(id);
        if (!user.isPresent()) {
            throw new AccountManagementResource.AccountAdminResourceException("No user was found for this activation key");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserMngtVM>> getAllUsers(
            @ModelAttribute UserMngtVM user,
            Pageable pageable
    ) {
        Page<UserMngtVM> users = this.accountManagementService.getAllUsers(user, pageable);
        return ResponseEntity.ok(users);
    }

}