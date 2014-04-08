package org.neo4j.extensions.spring.repository;

import org.neo4j.extensions.spring.domain.SmallUser;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Defines a complete data access contract for a
 * {@link org.neo4j.extensions.spring.domain.SmallUser}.
 *
 * @author bradnussbaum
 * @version 1.0.0
 * @see org.neo4j.extensions.spring.domain.SmallUser
 * @since 1.0.0
 */
@Repository
public interface SmallUserRepository extends GraphRepository<SmallUser> {

    @Query("START n=node:user_fulltext(name={0}) RETURN n;")
    SmallUser findByName(String name);

    @Query("START n=node:user_fulltext(email={email}) RETURN count(n) > 0;")
    boolean hasByEmail(@Param("email") final String email);

    @Query("START n=node:user_fulltext(email={0}) RETURN n;")
    SmallUser findByEmail(String email);

    @Query("START n=node:user_fulltext(username={0}) RETURN n;")
    SmallUser findByUsername(String username);

}
