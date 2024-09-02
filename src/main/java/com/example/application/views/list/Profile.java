package com.example.application.views.list;
import java.time.LocalDate;
import java.util.ArrayList;

public class Profile extends UserForm{
    String firstName, lastName,grade, email, password, school;
    int age;
    LocalDate birthday;
    LocalDate currentDate = LocalDate.now();
    Schedule schedule = new Schedule();
    ArrayList <Friends> friends;

   public Profile(){//default
        firstName = null;
        lastName = null;
        grade = null;
        email = null;
        password = null;
        school = null;
        age = 0;
        birthday = null;
    }
   public Profile(String firstName, String lastName, String grade, String email, String password, String school, LocalDate birthday){//will put more stuff in later
       this.firstName = firstName;
       this.lastName = lastName;
       this.grade = grade;
       this.email = email;
       this.password = password;
       this.school = school;
       this.age = calculateAge();
       this.birthday = birthday;
    }
    //making up methods first
    public int calculateAge(){
       age = currentDate.getYear() - birthday.getYear();
        if (currentDate.getDayOfYear() < birthday.getDayOfYear()) {
            age = age-1;
        }
        return age;
    }
    public int getAge(){
       return age;
    }

    public String getFirstName(){
       return firstName;
    }
    public void setFirstName(String firstName){
       this.firstName = firstName;
    }

    public String getLastName(){
       return lastName;
    }
    public void setLastName(String lastName){
       this.lastName = lastName;
    }

    public String getEmail(){
       return email;
    }
    public void setEmail(String email){
       this.email = email;
    }

    public String getPassword(){
       return password;
    }
    public void setPassword(String password){
       this.password = password;
    }

    public void setCurrentGrade(String grade){
       this.grade = grade;
    }

    public String getCurrentGrade(){
       return grade;
    }

    public String getSchool(){
       return school;
    }
    public void setSchool(String school){
       this.school = school;
    }

    public LocalDate getBirthday() {
       return birthday;
    }
    public void setBirthday(LocalDate birthday){
       this.birthday = birthday;
    }

    public Schedule getSchedule(){
       return schedule;
    }


}
