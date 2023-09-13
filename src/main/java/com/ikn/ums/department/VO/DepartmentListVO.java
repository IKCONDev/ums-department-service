package com.ikn.ums.department.VO;

import java.util.List;

import com.ikn.ums.department.entity.Department;

import lombok.Data;

@Data
public class DepartmentListVO {

	private List<Department> department;
}
