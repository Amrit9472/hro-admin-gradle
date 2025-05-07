package com.eos.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eos.admin.dto.NameTypeDTO;
import com.eos.admin.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	@Query(value = """
			SELECT DISTINCT d.department_name AS name, 'Department' AS type
			FROM department d
			WHERE d.location_id IN (
			    SELECT DISTINCT l.location_id
			    FROM locations l
			    WHERE l.loaction_name = :location
			)
			UNION
			SELECT DISTINCT ds.designation_name AS name, 'Designation' AS type
			FROM designations ds
			WHERE ds.location_id IN (
			    SELECT DISTINCT l.location_id
			    FROM locations l
			    WHERE l.loaction_name = :location
			)
			""", nativeQuery = true)
	List<NameTypeDTO> findNamesByLocationName(@Param("location") String location);

}
