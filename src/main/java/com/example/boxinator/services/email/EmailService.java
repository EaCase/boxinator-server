package com.example.boxinator.services.email;

public interface EmailService {
    /**
     * Sends an email with a link which can be used to fully register an account with the provided email.
     *
     * @param email              address to send the mail to
     * @param temporaryUserToken temporary token for the user.
     */
    void sendAccountRegistration(String email, String temporaryUserToken);

    /**
     * Sends an email confirmation message of an order.
     */
    void sendOrderConfirmation(String email, Long orderId);
}
