package org.tweetter.listener.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

@Component
public class Subscription implements GraphQLSubscriptionResolver {

    public Tweet tweets(DataFetchingEnvironment environment) {
        return new Tweet("Hello");
    }
}
