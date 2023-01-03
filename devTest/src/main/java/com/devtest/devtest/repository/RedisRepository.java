package com.devtest.devtest.repository;

import com.devtest.devtest.model.User_RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisRepository extends CrudRepository<User_RefreshToken, String> {


}
