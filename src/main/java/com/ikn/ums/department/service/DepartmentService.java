package com.ikn.ums.department.service;

import java.util.List;

import com.ikn.ums.department.entity.Department;

public interface DepartmentService {
	
	Department saveDepartment(Department department);
	Department findDepartmentById(Long departmentId);
	List<Department> getAllDepartments();
	void deleteDepartment(Long department);
	Department updateDepartment(Department department);
	void deleteSelectedDepartmentsByIds(List<Long> ids);
	List<Department> getAllActiveDepartments();

}
