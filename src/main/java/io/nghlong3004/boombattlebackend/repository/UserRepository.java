package io.nghlong3004.boombattlebackend.repository;

import io.nghlong3004.boombattlebackend.model.Boomber;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserRepository {

    @Select("""
            SELECT *
            FROM boomber
            WHERE username = #{username}
            """)
    Optional<Boomber> findByUsername(String username);

    @Select("""
            SELECT 1
            FROM boomber
            WHERE username = #{username}
            """)
    Optional<Boolean> existsByUsername(String username);

    @Insert("""
             INSERT INTO
             boomber(username, password)
             VALUES(#{username}, #{password})
            """)
    void save(Boomber boomber);

}
