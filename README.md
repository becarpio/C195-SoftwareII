# C195-SoftwareII
Java/SQL program for WGU's Software II

This program was created using JetBranes IntelliJ Community Edition, MySQL Workbench, and SceneBuilder.
The database connection information has been removed from DBController for information security reasons.

Main:
    1) Application supports Spanish, English and Japanese in all screens
        - Attempting any other languages triggers a notice in the console

AptInfoController:
    1) Single click on the Customer's name to bring up their full details

LoginController:
    1) After verification appends user timestamps to userLogins.txt

MainMenuController:
    1) Lambda used in public void initialize to populate TableColumn cell data
    2) Double click on an appointment to bring up the full details of the appointment
    3) Filters are based on the computer's default date
        - Monthly:  Appointments in the current month
        - Weekly:   Appointments within 7 days
        - Daily:    Appointments for the current day

ReportController:
    1) Reports should generate to src/Files
        - AppointmentByConsultant.txt
        - AppointmentByCustomer.txt
        - AppointmentTypeByMonth.txt
    2) Lambda used for Appointments by Customer to cycle through each customer and appointment and print to the txt file
