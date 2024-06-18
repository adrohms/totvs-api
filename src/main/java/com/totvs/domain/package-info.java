/**
 * This is the heart of the business software, the layer where the business domain is implemented.
 * It contains entities, value objects, aggregates, domain events, and domain services.
 * It encapsulates the business rules and business logic.
 *
 * It is divided in modules. Each one with his own responsibility:
 * account: Responsible for managing user account;
 * common: Common domain business that can be reused across other modules;
 * crm: Responsible to manage customers;
 */
package com.totvs.domain;