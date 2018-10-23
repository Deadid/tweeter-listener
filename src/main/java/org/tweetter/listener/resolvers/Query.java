package org.tweetter.listener.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {
    public Tweet tweets(DataFetchingEnvironment environment) {
        return new Tweet("Hello");
    }
}
