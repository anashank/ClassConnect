package com.example.application.views.list;
import java.time.LocalDate;

public class Profile {
    String firstName, lastName, grade, email, password, school;
    int age;
    LocalDate birthday;
    LocalDate currentDate = LocalDate.now();
    Schedule schedule = new Schedule();

    // Default constructor
    public Profile() {
        firstName = null;
        lastName = null;
        grade = null;
        email = null;
        password = null;
        school = null;
        age = 0;
        birthday = LocalDate.of(2000, 1, 1); // Default birthday to avoid null
    }

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

    // Method to calculate age based on birthday and current date
    public int calculateAge() {
        if (birthday == null) {
            return 0; // Handle case where birthday is not set
        }

        int calculatedAge = currentDate.getYear() - birthday.getYear();
        if (currentDate.getDayOfYear() < birthday.getDayOfYear()) {
            calculatedAge -= 1;
        }
        return calculatedAge;
    }

    // Getter and setter methods
    public int getAge() {
        return age;
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
        this.age = calculateAge(); // Recalculate age when birthday changes
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
