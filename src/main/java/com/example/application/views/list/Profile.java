package com.example.application.views.list;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String grade;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String school;

    @Column(nullable = true)
    private LocalDate birthday;

    @Column(nullable = true)
    private int age;

    @OneToMany(mappedBy = "profile")
    private List<Schedule> schedules; // Link to schedules

    // Default constructor
    public Profile() {}

    // Parameterized constructor
    public Profile(String firstName, String lastName, String grade, String email, String password, String school, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
        this.email = email;
        this.password = password;
        this.school = school;
        this.birthday = birthday;
        this.age = calculateAge();
    }

    // Methods for age calculation
    public int calculateAge() {
        if (birthday != null) {
            LocalDate currentDate = LocalDate.now();
            int age = currentDate.getYear() - birthday.getYear();
            if (currentDate.getDayOfYear() < birthday.getDayOfYear()) {
                age -= 1;
            }
            return age;
        }
        return 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
