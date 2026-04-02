package com.example.ecommerce.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User 
{
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		@Column(unique = true, nullable = false)
		private String username;
		
		@Column(nullable = false)
		private String password;
		
		@Column(unique = true, nullable =false)
		private String email;
		
		@ElementCollection(fetch = FetchType.EAGER)
		@Enumerated(EnumType.STRING)
		@CollectionTable(name = "user_role",joinColumns =@JoinColumn(name = "user_id"))
		@Column(name = "roles")
		private Set<String> roles = new HashSet<>();
		
		public Long getId()
		{
			return id;
		}
		public void setId(Long id)
		{
			this.id = id;
		}
		public String getUsername()
		{
			return username;
			
		}
		public void setUsername(String username)
		{
			this.username = username;
		}
		public String getPassword()
		{
			return password;
		}
		public void setPassword(String password)
		{
			this.password=password;
		}
		public String getEmail()
		{
			return email;
		}
		public void setEmail(String email)
		{
			this.email = email;
		}
		public Set<String> getRoles()
		{
			return roles;
		}
		public void setRoles(Set<String> roles)
		{
			this.roles=roles;
		}
}


