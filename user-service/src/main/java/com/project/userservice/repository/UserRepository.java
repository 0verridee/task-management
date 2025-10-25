package com.project.userservice.repository;


import com.project.userservice.entity.UserEntity;
import com.project.userservice.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    @Deprecated
    @Modifying
    @Query("""
            update UserEntity u
                        set u.userStatus = :status
                        where u.id = :id
            """
    )
    void setStatus(
            @Param("id") Long id,
            @Param("status") UserStatus status);
}
