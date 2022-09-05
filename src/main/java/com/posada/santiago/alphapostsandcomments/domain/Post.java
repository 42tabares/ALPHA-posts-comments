package com.posada.santiago.alphapostsandcomments.domain;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.domain.events.FavoriteCommentUpdated;
import com.posada.santiago.alphapostsandcomments.domain.events.FavoritePostUpdated;
import com.posada.santiago.alphapostsandcomments.domain.events.PostCreated;
import com.posada.santiago.alphapostsandcomments.domain.values.*;
import com.posada.santiago.alphapostsandcomments.domain.events.CommentAdded;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Post extends AggregateEvent<PostId> {



    protected Title title;

    protected Author author;

    protected Favorite favorite;

    protected List<Comment> comments;

    public Post(PostId entityId, Title title, Author author) {
        super(entityId);
        subscribe(new PostChange(this));
        appendChange(new PostCreated(title.value(), author.value())).apply();
    }


    private Post(PostId id){
        super(id);
        subscribe(new PostChange(this));
    }

    public static Post from(PostId id, List<DomainEvent> events){
        Post post = new Post(id);
        events.forEach(event -> post.applyEvent(event));
        return post;
    }

    public void addAComment(CommentId id, Author author, Content content){
        Objects.requireNonNull(id);
        Objects.requireNonNull(author);
        Objects.requireNonNull(content);
        appendChange(new CommentAdded(id.value(), author.value(), content.value())).apply();
    }

    public void setFavPost(Favorite favorite){
        Objects.requireNonNull(favorite);
        appendChange(new FavoritePostUpdated(favorite.value())).apply();
    }

    public void setFavComment(CommentId id, Favorite favorite){
        Objects.requireNonNull(id);
        Objects.requireNonNull(favorite);
        appendChange(new FavoriteCommentUpdated(id.value(), favorite.value())).apply();
    }

    public Optional<Comment> getCommentById(CommentId commentId){
        return comments.stream().filter((comment -> comment.identity().equals(commentId))).findFirst();
    }
}
