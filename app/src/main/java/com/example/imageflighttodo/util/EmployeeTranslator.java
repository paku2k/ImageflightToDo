package com.example.imageflighttodo.util;

public class EmployeeTranslator {
    public static final int CHRISTOPH = 1;
    public static final int WILLY = 2;
    public static final int SIMON = 3;
    public static String translate(int i){
        switch (i){
            case 1:
                return "Christoph";

            case 2:
                return "Willy";

            case 3:
                return "Simon";

        }
return "Not Assigned";
    }


}
