package com.it.audit.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.it.audit.enums.UserRole;
import com.it.audit.enums.UserStatus;

import lombok.Data;


@javax.persistence.Entity
@javax.persistence.Table(name = "it_audit_user", uniqueConstraints = {
      }, indexes = {
      })
@javax.persistence.EntityListeners(AuditingEntityListener.class)
@Data
public class ItAuditUser {

	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, insertable = false, nullable = false)
	private Long id;
	
	//用户id
	@Column(name = "user_id")
	private Long userId;
	
	//用户姓名
	@Column(name = "user_name")
	private String userName;
	
	//用户名
	@Column(name = "login_name", unique = true, nullable = false)
	private String loginName;
	
	//密码
	@Column(name = "password")
	private String password;
	
	//用户角色
	@Column(name = "user_role")
	private UserRole userRole;
	
	//用户状态
	@Column(name = "status")
	private UserStatus status;
	
	//创建时间
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat
	@Column(name = "create_time")
	private DateTime createTime;
	
	//更新时间
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat
	@Column(name = "update_time")
	private DateTime updateTime;
}
