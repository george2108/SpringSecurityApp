package com.app;

import com.app.persistance.entities.PermissionEntity;
import com.app.persistance.entities.RoleEnum;
import com.app.persistance.entities.RolesEntity;
import com.app.persistance.entities.UserEntity;
import com.app.persistance.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringSecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository){
		return args -> {
			// Crear permisos
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();
			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();
			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();
			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();
			PermissionEntity refactorPermission = PermissionEntity.builder()
					.name("REFACTOR")
					.build();

			// Crear roles
			RolesEntity adminRole = RolesEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permission(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();
			RolesEntity userRole = RolesEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permission(Set.of(createPermission, readPermission))
					.build();
			RolesEntity invitedRole = RolesEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permission(Set.of(readPermission))
					.build();
			RolesEntity devRole = RolesEntity.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permission(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
					.build();

			// crear usuarios
			UserEntity jorgeUser = UserEntity.builder()
					.username("jorge")
					.password(new BCryptPasswordEncoder().encode("password"))
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(adminRole))
					.build();
			UserEntity barbaraUser = UserEntity.builder()
					.username("barbi")
					.password(new BCryptPasswordEncoder().encode("password"))
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(userRole))
					.build();
			UserEntity vicenteUser = UserEntity.builder()
					.username("vicente")
					.password(new BCryptPasswordEncoder().encode("password"))
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(invitedRole))
					.build();
			UserEntity lupitaUser = UserEntity.builder()
					.username("lupita")
					.password(new BCryptPasswordEncoder().encode("password"))
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(devRole))
					.build();

			userRepository.saveAll(List.of(jorgeUser,barbaraUser,vicenteUser,lupitaUser));
		};
	}
}
