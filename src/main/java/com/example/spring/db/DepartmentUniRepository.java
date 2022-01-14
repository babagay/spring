package com.example.spring.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface DepartmentUniRepository extends JpaRepository<DepartmentUni, Long> {
    @Query("select d from DepartmentUni d where d.id = ?1")
    @Nullable
    DepartmentUni byId(Long id);
    
}
