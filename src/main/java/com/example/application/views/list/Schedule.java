package com.example.application.views.list;

public class Schedule {
        String className, teacherName;
        int period;
        public Schedule(){//
            period = 0;
            className = "Empty";
            teacherName = "Empty";
        }

        public Schedule(int period, String className, String teacherName){
            this.period = period;
            this.className = className;
            this.teacherName = teacherName;
        }
        public String getClassName(){
            return className;
        }
        public String getTeacherName() {
            return teacherName;
        }
        public int getPeriod(){
            return period;
        }
        public void setClassName(String className){
            this.className = className;
        }
        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }
        public void setPeriod(int period){
            this.period = period;
        }

    }

