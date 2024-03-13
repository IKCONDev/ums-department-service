package com.ikn.ums.department.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ikn.ums.department.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDepartmentId(Long departmentId);
    Optional<Department> findByDepartmentName(String deptName);
    
    @Query(value = "SELECT COUNT(*) FROM employee_tab WHERE department_id =:departmentId",nativeQuery = true )
	Long findDepartmentIdCount(Long departmentId);
    
    @Query("FROM Department WHERE active=:status")
    List<Department> findAllDepartmentByStatus(String status);
    
    @Query("FROM Department WHERE active=Active AND departmentHead=:emailId")
	List<Department> getDepartmentBydepartmentHead(String emailId);
    
}
