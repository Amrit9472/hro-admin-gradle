package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.Language;
@Repository
public interface LanguagesRepository extends JpaRepository<Language, Long> {

}
