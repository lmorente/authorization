package com.security.book.service;

import javax.annotation.PostConstruct;

import com.security.book.repository.BookRepository;
import com.security.book.repository.entity.*;
import com.security.book.repository.RoleRepository;
import com.security.book.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SampleDataLoader {

    private final BookRepository bookRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public SampleDataLoader(BookRepository bookRepository, RoleRepository roleRepository, UserRepository userRepository,
                            PasswordEncoder encoder) {
        this.bookRepository = bookRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostConstruct
    public void init() {
        this.saveRolesAndNewUser();
        this.saveNewBooks();
    }

    private void saveNewBooks() {
        if (!this.bookRepository.findAll().isEmpty()) {
            final Book book1 = new Book("Don Quijote", "En un lugar de la Mancha",
                    "Cervantes", "Desconocido", 1605);
            book1.addComment(new Comment(5, "un clásico"));
            book1.addComment(new Comment(0, "no me ha gustado"));
            this.bookRepository.save(book1);

            final Book book2 = new Book("El principito", "Un piloto se pierde en el Sáhara",
                    "Antoine de Saint-Exupéry", "Gallimard", 1943);
            book2.addComment(new Comment(0, "este tampoco"));
            this.bookRepository.save(book2);

            this.bookRepository.save(new Book("Lazarillo de Tormes", "La vida de Lazarillo de Tormes y de " +
                    "sus fortunas y adversidades", "Desconocido", "Acceso público", 1554));
        }
    }

    private void saveRolesAndNewUser() {
        if (!(this.roleRepository.findAll().isEmpty() && this.userRepository.findAll().isEmpty())) {
            //Save Roles
            final Role admin = this.roleRepository.save(new Role(1, ERole.ROLE_ADMIN));
            final Role user = this.roleRepository.save(new Role(2, ERole.ROLE_USER));

            //Save default user
            final Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(admin);
            final Set<Role> userRoles = new HashSet<>();
            userRoles.add(user);

            this.userRepository.save(new User(1L, "admin", "pepe@gmail.com",
                    encoder.encode("6QemJAKuvQN"), adminRoles));
            this.userRepository.save(new User(2L, "juan", "juan@hotmail.com",
                    encoder.encode("urdzeBGCtDYQ"), userRoles));
        }
    }
}
