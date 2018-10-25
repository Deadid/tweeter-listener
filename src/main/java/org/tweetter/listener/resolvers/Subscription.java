package org.tweetter.listener.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.event.Event;
import graphql.schema.DataFetchingEnvironment;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;

@Component
public class Subscription implements GraphQLSubscriptionResolver {

    private final Client horsebirdClient;
    private final BlockingQueue<String> messageQueue;
    private final BlockingQueue<Event> events;

    public Subscription(Client horsebirdClient, BlockingQueue<String> messageQueue, BlockingQueue<Event> events) {
        this.horsebirdClient = horsebirdClient;
        this.messageQueue = messageQueue;
        this.events = events;
    }

    public Publisher<Tweet> tweets(DataFetchingEnvironment environment) {
        return Flux.create(fluxSink -> fluxSink.next(new Tweet(exctractMessage())));
    }


    private String exctractMessage() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PreDestroy
    public void destroy() {
        horsebirdClient.stop();
    }
}
