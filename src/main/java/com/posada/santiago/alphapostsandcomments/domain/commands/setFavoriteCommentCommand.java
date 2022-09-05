package com.posada.santiago.alphapostsandcomments.domain.commands;

import co.com.sofka.domain.generic.Command;

public class setFavoriteCommentCommand extends Command {

    private String postId;
    private String commentId;
    private String favorite;

    public setFavoriteCommentCommand() {
    }

    public String getPostId() {
        return postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getFavorite(){return favorite;}

}
