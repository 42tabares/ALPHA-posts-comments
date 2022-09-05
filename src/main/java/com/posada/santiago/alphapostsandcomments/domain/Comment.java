package com.posada.santiago.alphapostsandcomments.domain;

import co.com.sofka.domain.generic.Entity;
import com.posada.santiago.alphapostsandcomments.domain.values.Author;
import com.posada.santiago.alphapostsandcomments.domain.values.CommentId;
import com.posada.santiago.alphapostsandcomments.domain.values.Content;
import com.posada.santiago.alphapostsandcomments.domain.values.Favorite;
import com.posada.santiago.alphapostsandcomments.domain.values.PostId;

public class Comment extends Entity<CommentId> {

    private Author author;
    private Content content;

    private Favorite favorite;

    public Comment(CommentId entityId, Author author, Content content, Favorite favorite) {
        super(entityId);
        this.author = author;
        this.content = content;
        this.favorite = favorite;
    }

    public Author author() {
        return author;
    }

    public Content content() {
        return content;
    }

    public Favorite favorite(){return  favorite;}

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }
}
