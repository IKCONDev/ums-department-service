package com.ikn.ums.department.service;

import com.ikn.ums.department.entity.Department;

public interface DepartmentService {
	
	Department saveDepartment(Department department);
	Department findDepartmentById(Long departmentId);

}
