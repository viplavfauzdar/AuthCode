package com.authcode.grps;

import java.util.List;

import lombok.Data;

@Data
public class UserInfo {
	
	public String id;
	public Meta meta;
	public String last_login;
	public String name;
	public String first_name;
	public String last_name;
	public String email;
	public String title;
	public List<Group> groups;
	public Integer department;
	public Manager manager;
	public List<DirectReport> direct_reports;
	public Links links;

}

@Data
class Group{	
	public String dn;
	public String cn;
}

@Data
class Meta{
	public String dn;
	public String cn;
}

@Data
class Manager{
	public Links links;
}

@Data
class DirectReport {
	public Links links;
}

@Data
class Links {
	public String rel;
	public String href;
}


