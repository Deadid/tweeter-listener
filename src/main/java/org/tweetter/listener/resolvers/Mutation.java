package org.tweetter.listener.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class Mutation implements GraphQLMutationResolver {
    public Publisher<Tweet> tweets(DataFetchingEnvironment environment) {
        return Flux.create(sink -> sink.next(new Tweet("Hello")));
    }
}
