package com.ikn.ums.department.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.department.entity.Department;
import com.ikn.ums.department.exception.BusinessException;
import com.ikn.ums.department.exception.EntityNotFoundException;
import com.ikn.ums.department.repository.DepartmentRepository;
import com.ikn.ums.department.service.DepartmentService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

	   @Autowired
	    private DepartmentRepository departmentRepository;

	   @Override
	    public Department saveDepartment(Department department) {
		   Department dbDepartment = null;
	    	log.info("DepartmentService.saveDepartment() Entered");
	    	try {
	    		if(department == null) {
		    		throw new EntityNotFoundException("errorcode","Department Object is null");
		    	}
	    		dbDepartment = departmentRepository.save(department);
	    		log.info("DepartmentService.saveDepartment() Exited sucessfully");
			}catch (Exception e) {
				log.info("DepartmentService.saveDepartment() exited with exception :"+e.getStackTrace().toString());
				throw new BusinessException("<error code>","An error occured in Department Controller /r/n"+ e.getStackTrace().toString());
			}
	        return dbDepartment;
	    }

	   @Override
	    public Department findDepartmentById(Long departmentId) {
	    	log.info("DepartmentService.findDepartmentById() Entered");
	    	Department dbDepartment = null;
	        try {
	        	if(departmentId < 1) {
	        		log.info("DepartmentService.findDepartmentById() exited with exception : "+"Invalid department id "+departmentId);
	        		throw new IllegalArgumentException("Invalid department id "+departmentId);
	        	}
	        	dbDepartment = departmentRepository.findByDepartmentId(departmentId);
	        	if(dbDepartment == null) {
	        		log.info("DepartmentService.findDepartmentById() exited with exception : "+"Department with id "+departmentId+" does not exists");
	        		throw new EntityNotFoundException("errorcode","Department with id "+departmentId+" does not exists");
	        	}
	        	log.info("DepartmentService.findDepartmentById() has found the department with id :"+departmentId+" and exited sucessfully");
	        	return dbDepartment;
	        }catch (IllegalArgumentException iae) {
	        	log.info("DepartmentService.findDepartmentById() exited with business exception :"+iae.getMessage());
				throw new BusinessException("<error code>", iae.getMessage());
			}catch (Exception e) {
				log.info("DepartmentService.findDepartmentById() exited with business exception :"+e.getStackTrace().toString());
				throw new BusinessException("<error code>", e.getStackTrace().toString());
			}
	    }
}
