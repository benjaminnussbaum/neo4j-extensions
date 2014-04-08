package org.neo4j.extensions.spring.rest;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.neo4j.extensions.client.UserClient;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.SmallUser;
import org.neo4j.extensions.spring.repository.SmallUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The User controller.
 *
 * @author bradnussbaum
 * @author joemoore
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/user")
public class UserController implements UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Context
    private SmallUserRepository userRepository;

    /**
     * @return Status 201 on success.
     */
    public Response create(Boolean indexingOn) {
        LOGGER.debug(String.format("POST /user/create?indexingOn=%s", indexingOn));
        long startTimeTx = System.currentTimeMillis();

        // create user
        SmallUser user = new SmallUser();
        // create friend 1
        SmallUser friend1 = new SmallUser();
        // create friend 2
        SmallUser friend2 = new SmallUser();

        // establish friends for user
        Set<SmallUser> friends = new LinkedHashSet<SmallUser>();
        friends.add(friend1);
        friends.add(friend2);
        user.setFriends(friends);

        // establish friends for friend 1
        Set<SmallUser> friend1Friends = new LinkedHashSet<>();
        friend1Friends.add(user);
        friend1.setFriends(friend1Friends);

        // establish friends for friend 2
        Set<SmallUser> friend2Friends = new LinkedHashSet<>();
        friend2Friends.add(user);
        friend2.setFriends(friend2Friends);

        // save the user and cascade friends
        user = userRepository.save(user);

        // lookup to validate being saved
        SmallUser userActual = userRepository.findOne(user.getId());
        Set<SmallUser> friendsActual = userActual.getFriends();

        // assemble result package
        FriendResult result = new FriendResult();
        result.setUser(userActual);
        result.setFriends(friendsActual);

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.debug(String.format("UserController: TX: userId=%s, processTime=%dms", user.getId(), processTimeTx));

        return Response.status(Response.Status.CREATED).entity(result).build();
    }

}
