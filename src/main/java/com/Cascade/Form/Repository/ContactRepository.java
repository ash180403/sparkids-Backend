package com.Cascade.Form.Repository;

import com.Cascade.Form.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends MongoRepository<User, String> {
	boolean existsByContact(String contact);
	boolean existsByEmail(String email);

    List<User> findByName(String name);
    
    Optional<User> findByEmail(String email);
    Optional<User> findByNameAndContactAndAddress(String name, String contact, String address);
    
    void deleteByName(String name);

    boolean existsByName(String name);
    List<User> findByReason(String reason);


    List<User> findAllByEmailIsNotNull();

    List<User> findAllByContactIsNotNull();
}
