package com.ikn.ums.department.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeVO {
	
		private Integer id;
		
		private String employeeOrgId;

		private String teamsUserId;

		private String firstName;

		private String lastName;

		private String email;
		
		private String reportingManager;
		
		private String gender;

		//private String userRole;

		private String designation;
		
		private Designation empDesignation;
		
		private Long departmentId;
		
		private DepartmentVO department;
		
		private String dateOfJoining;
		
		private String employeeStatus;


}
