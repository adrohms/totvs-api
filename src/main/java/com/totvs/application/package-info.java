/**
 * This layer is responsible for coordinating application-specific tasks and orchestrating the flow of data between the
 * domain and the outside world.
 *
 * It is divided in "modules". Each one with his own responsibility:
 * account: Responsible for managing user account;
 * common: Common application functionality that can be reused across other modules;
 * crm: Responsible to manage customers;
 * util: Unrelated to business specific logic. Can be use by any.
*/
package com.totvs.application;