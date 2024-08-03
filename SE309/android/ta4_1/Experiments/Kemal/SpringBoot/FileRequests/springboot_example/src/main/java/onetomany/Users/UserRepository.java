package onetomany.Users;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Kemal Yavuz
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    void deleteById(int id);
}
