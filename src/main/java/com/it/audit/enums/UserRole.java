package com.it.audit.enums;

import java.util.ArrayList;
import java.util.List;

public enum UserRole {
	ADMIN(1 << 0, "管理员"),
	EXECUTIVE(1 << 1, "高管"),
	MANAGER(1 << 2, "项目经理"),
	REVIEWER(1 << 3, "项目质量复核"),
	AUDITOR(1 << 4, "审计师");
	
	private int index;
	private String text;
	
	private UserRole(int index, String text) {
		this.index = index;
		this.text = text;
	}
	
	/**
	 * 拆分用户权限
	 * @param indexs
	 * @return
	 */
	public List<UserRole> splitRoles(int indexs){
		List<UserRole> result = new ArrayList<>();
		UserRole[] roles = UserRole.values();
		for(UserRole role: roles){
			if(role.getIndex() == (indexs & role.getIndex())){
				result.add(role);
			}
		}
		return result.isEmpty()? null : result;
	}
	
	/**
	 * 合并用户权限
	 * @param roles
	 * @return
	 */
	public int mergeRoles(List<UserRole> roles){
		int indexs = 0;
		for(UserRole role: roles){
			indexs = indexs | role.getIndex();
		}
		return indexs;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
