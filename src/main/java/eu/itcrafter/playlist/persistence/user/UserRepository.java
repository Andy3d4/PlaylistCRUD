package eu.itcrafter.playlist.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

}