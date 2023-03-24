package com.example.boxinator.services.email;

/**
 * Send email on different actions.
 */
public interface EmailService {
    /**
     * Sends an email with a link which can be used to fully register an account with the provided email.
     *
     * @param email              address to send the mail to
     * @param temporaryUserToken temporary token for the user, which can be used when registering the account
     *                           fully to link the created shipments with the full account.
     */
    void sendAccountRegistration(String email, String temporaryUserToken);

    /**
     * Sends an email confirmation message of an order.
     */
    void sendOrderConfirmation(String email, Long shipmentId);
}
