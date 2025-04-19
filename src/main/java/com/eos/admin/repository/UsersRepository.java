package com.eos.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.OurUsers;

@Repository
public interface UsersRepository extends JpaRepository<OurUsers,Integer> {

	Optional<OurUsers> findByEmail(String email);

	@Query("SELECT DISTINCT u.uniqueCode FROM OurUsers u WHERE u.uniqueCode IS NOT NULL AND u.uniqueCode <> 'Admin'")
	List<String> findDistinctProcesses();

}
