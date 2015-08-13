package com.android.clockwork.controller;

import com.android.clockwork.model.Post;

public class MainController {
    private static SessionController sessionController;
    private static ServiceController serviceController;
    private static AccountController accountController;
    private static Post selectedPost;

    public MainController() {
        sessionController = new SessionController();
        serviceController = new ServiceController();
        accountController = new AccountController();
    }

    public static Post getSelectedPost() {
        return selectedPost;
    }

    public static void setSelectedPost(Post selectedPost) {
        MainController.selectedPost = selectedPost;
    }
}
