package com.android.clockwork.controller;

public class MainController {
    private SessionController sessionController;
    private ServiceController serviceController;
    private AccountController accountController;

    public MainController() {
        sessionController = new SessionController();
        serviceController = new ServiceController();
        accountController = new AccountController();
    }
}
