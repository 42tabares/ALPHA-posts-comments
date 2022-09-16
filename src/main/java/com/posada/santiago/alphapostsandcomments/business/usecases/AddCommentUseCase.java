package com.posada.santiago.alphapostsandcomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.application.handlers.CommandHandle;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.business.generic.UseCaseForCommand;
import com.posada.santiago.alphapostsandcomments.domain.Post;
import com.posada.santiago.alphapostsandcomments.domain.commands.AddCommentCommand;
import com.posada.santiago.alphapostsandcomments.domain.values.Author;
import com.posada.santiago.alphapostsandcomments.domain.values.CommentId;
import com.posada.santiago.alphapostsandcomments.domain.values.Content;
import com.posada.santiago.alphapostsandcomments.domain.values.PostId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class AddCommentUseCase extends UseCaseForCommand<AddCommentCommand> {

    private final DomainEventRepository repository;
    private final EventBus bus;

    private final Logger logger = Logger.getLogger(AddCommentUseCase.class.getName());

    public AddCommentUseCase(DomainEventRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<AddCommentCommand> addCommentCommandMono) {
        logger.info("Engaging comment creation use case");
        logger.info("Searching for post...");
        return addCommentCommandMono.flatMapMany(command -> repository.findById(command.getPostId())
                .collectList()
                .flatMapIterable(events -> {
                    logger.info("Post found");
                    Post post = Post.from(PostId.of(command.getPostId()), events);
                    logger.info("Adding comment to post " + command.getPostId());
                    post.addAComment(CommentId.of(command.getCommentId()), new Author(command.getAuthor()), new Content(command.getContent()));
                    logger.info("Comment " + command.getCommentId() + " created on post " + command.getPostId());
                    logger.info("Saving domain event repository");
                    return post.getUncommittedChanges();
                }).map(event -> {
                    logger.info("Publishing event via RabbitMQ");
                    bus.publish(event);
                    return event;
                }).flatMap(event -> repository.saveEvent(event))
        );

    }
}
