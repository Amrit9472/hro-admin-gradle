package com.eos.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.OurUsers;

@Repository
public interface UsersRepository extends JpaRepository<OurUsers,Integer> {

	Optional<OurUsers> findByEmail(String email);

}
