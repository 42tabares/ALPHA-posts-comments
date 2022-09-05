package com.posada.santiago.alphapostsandcomments.domain.commands;

import co.com.sofka.domain.generic.Command;

public class setFavoritePostCommand extends Command {

    private String postId;
    private String favorite;

    public setFavoritePostCommand() {
    }

    public String getPostId() {
        return postId;
    }

    public String getFavorite(){return favorite;}

}
