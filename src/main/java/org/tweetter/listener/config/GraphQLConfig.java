package org.tweetter.listener.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import graphql.servlet.ObjectMapperConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


@Configuration
public class GraphQLConfig {

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(5);
    }


    @Bean
    public Client horseBirdClient(ExecutorService executorService, @Value("conumser.key") String consumerKey, @Value("conumser.secret") String consumerSecret, @Value("access.token") String accessToken, @Value("access.token.secret") String accessTokenSecret) {
        /* Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = messageQueue();
        BlockingQueue<Event> eventQueue = eventQueue();

        /* Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        // Optional: set up some followings and track terms
        List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList("twitter", "api");
        hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();

        // Attempts to establish a connection.
        hosebirdClient.connect();
        return hosebirdClient;
    }

    @Bean
    public LinkedBlockingQueue<Event> eventQueue() {
        return new LinkedBlockingQueue<>(1000);
    }

    @Bean
    public LinkedBlockingQueue<String> messageQueue() {
        return new LinkedBlockingQueue<>(100000);
    }


    @Bean
    public ObjectMapperConfigurer objectMapperConfigurer() {
        return (mapper -> mapper.registerModule(new JavaTimeModule()));
    }
}
