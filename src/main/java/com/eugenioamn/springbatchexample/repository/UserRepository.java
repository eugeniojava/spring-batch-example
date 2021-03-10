package com.eugenioamn.springbatchexample.repository;

import com.eugenioamn.springbatchexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
