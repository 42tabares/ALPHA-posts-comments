package com.sofka.alphapostcomments;

import co.com.sofka.domain.generic.DomainEvent;
import com.posada.santiago.alphapostsandcomments.business.gateways.DomainEventRepository;
import com.posada.santiago.alphapostsandcomments.business.gateways.EventBus;
import com.posada.santiago.alphapostsandcomments.domain.commands.CreatePostCommand;
import com.posada.santiago.alphapostsandcomments.domain.events.PostCreated;
import com.posada.santiago.alphapostsandcomments.business.usecases.CreatePostUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest
class AlphaPostCommentsApplicationTests {

	@Mock
	private DomainEventRepository repository;

	@Mock
	private EventBus bus;

	@Test
	void PostCreatedTest(){

		String postID = "271";

		//Arrange
		var postCreated = new PostCreated("Lorem Ipsum Sic Dolor", "Solaris Leo");
		postCreated.setAggregateRootId(postID);

		var useCase = new CreatePostUseCase(repository,bus);

		Mockito.when(repository.findById(postID)).thenReturn((Flux<DomainEvent>) List.of(postCreated));
		useCase.apply();



	}
}
