package com.posada.santiago.alphapostsandcomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.business.generic.UseCaseForCommand;
import com.posada.santiago.alphapostsandcomments.domain.Post;
import com.posada.santiago.alphapostsandcomments.domain.commands.CreatePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.values.Author;
import com.posada.santiago.alphapostsandcomments.domain.values.PostId;
import com.posada.santiago.alphapostsandcomments.domain.values.Title;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class CreatePostUseCase extends UseCaseForCommand<CreatePostCommand> {
    private final DomainEventRepository repository;
    private final EventBus bus;

    private final Logger logger = Logger.getLogger(AddCommentUseCase.class.getName());

    public CreatePostUseCase(DomainEventRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<CreatePostCommand> createPostCommandMono) {
        logger.info("Engaging new post creation...");
        return createPostCommandMono.flatMapIterable(command -> {
            logger.info("Creating post model...");
            Post post = new Post(
                    PostId.of(command.getPostId()),
                    new Title(command.getTitle()),
                    new Author(command.getAuthor()));
            logger.info("Post " + command.getPostId() + " created with title: " + command.getTitle());
            logger.info("Saving event & Publishing event via Rabbit MQ...");
            return post.getUncommittedChanges();
        }).flatMap(event -> repository.saveEvent(event).thenReturn(event))
                .doOnNext(bus::publish)
                .doOnNext(end -> logger.info("Published via Rabbit MQ"));
    }
}
