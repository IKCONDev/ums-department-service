package com.ikn.ums.department.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.department.entity.Department;
import com.ikn.ums.department.repository.DepartmentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentService {

	   @Autowired
	    private DepartmentRepository departmentRepository;

	    public Department saveDepartment(Department department) {
	    	log.info("DepartmentService.saveDepartment() Entered");
	    	
	    	System.out.println("DepartmentService.saveDepartment() LLLL : " + department.getDepartmentId());
	        return departmentRepository.save(department);
	    }

	    public Department findDepartmentById(Long departmentId) {
	    	log.info("DepartmentService.findDepartmentById() Entered");
	        return departmentRepository.findByDepartmentId(departmentId);
	    }

}
