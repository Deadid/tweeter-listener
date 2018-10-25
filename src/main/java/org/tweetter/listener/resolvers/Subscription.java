package org.tweetter.listener.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Component
public class Subscription implements GraphQLSubscriptionResolver {

    public Publisher<Tweet> tweets(DataFetchingEnvironment environment) {
        return Flux.create(fluxSink -> fluxSink.next(new Tweet(LocalDateTime.now().toString())));
    }
}
