package com.posada.santiago.alphapostsandcomments.domain.events;

import co.com.sofka.domain.generic.DomainEvent;

public class FavoriteCommentUpdated extends DomainEvent {

    private String commentId;
    private Boolean favorite;


    public FavoriteCommentUpdated(String commentId, Boolean favorite) {
        super("posada.santiago.CommentFavoriteUpdated");
        this.commentId = commentId;
        this.favorite = favorite;
    }

    public String getCommentId() {
        return commentId;
    }

    public Boolean getFavorite() {
        return favorite;
    }
}
