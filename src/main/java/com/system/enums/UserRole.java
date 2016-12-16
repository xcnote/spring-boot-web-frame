package com.system.enums;

import java.util.ArrayList;
import java.util.List;

public enum UserRole {
	ADMIN(1 << 0, "超级管理员"),
	ROLEA(1 << 1, "用户角色A"),
	ROLEB(1 << 2, "用户角色B"),
	ROLEC(1 << 3, "用户角色C"),
	ROLED(1 << 4, "用户角色D");
	
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
