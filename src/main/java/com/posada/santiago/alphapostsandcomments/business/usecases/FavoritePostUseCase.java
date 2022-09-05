package com.posada.santiago.alphapostsandcomments.business.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.business.generic.UseCaseForCommand;
import com.posada.santiago.alphapostsandcomments.domain.Post;
import com.posada.santiago.alphapostsandcomments.domain.values.Favorite;
import com.posada.santiago.alphapostsandcomments.domain.values.PostId;
import com.posada.santiago.alphapostsandcomments.domain.commands.setFavoritePostCommand;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FavoritePostUseCase extends UseCaseForCommand<setFavoritePostCommand> {

    private final DomainEventRepository repository;
    private final EventBus bus;

    public FavoritePostUseCase(DomainEventRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<setFavoritePostCommand> setFavoritePostCommandMono) {
        return setFavoritePostCommandMono.flatMapMany(command -> repository.findById(command.getPostId())
                .collectList()
                .flatMapIterable(events -> {
                    Post post = Post.from(PostId.of(command.getPostId()), events);
                    post.setFavPost(new Favorite(command.getFavorite().equalsIgnoreCase("true")));
                    return post.getUncommittedChanges();
                }).map(event -> {
                    bus.publish(event);
                    return event;
                }).flatMap(event -> repository.saveEvent(event))
        );

    }
}
