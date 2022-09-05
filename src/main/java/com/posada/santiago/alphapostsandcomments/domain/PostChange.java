package com.posada.santiago.alphapostsandcomments.domain;

import co.com.sofka.domain.generic.EventChange;
import com.posada.santiago.alphapostsandcomments.domain.events.FavoriteCommentUpdated;
import com.posada.santiago.alphapostsandcomments.domain.events.FavoritePostUpdated;
import com.posada.santiago.alphapostsandcomments.domain.events.PostCreated;
import com.posada.santiago.alphapostsandcomments.domain.values.Favorite;
import com.posada.santiago.alphapostsandcomments.domain.values.Title;
import com.posada.santiago.alphapostsandcomments.domain.events.CommentAdded;
import com.posada.santiago.alphapostsandcomments.domain.values.Author;
import com.posada.santiago.alphapostsandcomments.domain.values.CommentId;
import com.posada.santiago.alphapostsandcomments.domain.values.Content;

import java.util.ArrayList;

public class PostChange extends EventChange {

    public PostChange(Post post){
        apply((PostCreated event)-> {
            post.title = new Title(event.getTitle());
            post.author = new Author(event.getAuthor());
            post.comments = new ArrayList<>();
            post.favorite = new Favorite(false);
        });

        apply((CommentAdded event)-> {
            Comment comment = new Comment(
                    CommentId.of(event.getId()),
                    new Author(event.getAuthor()),
                    new Content(event.getContent()),
                    new Favorite(false));

            post.comments.add(comment);
        });

        apply((FavoritePostUpdated event) -> {
            post.favorite = new Favorite(event.getFavorite());
        });

        apply((FavoriteCommentUpdated event) -> {
            post.getCommentById(CommentId.of(event.getCommentId())).get()
                    .setFavorite(new Favorite(event.getFavorite()));
        });
    }
}
