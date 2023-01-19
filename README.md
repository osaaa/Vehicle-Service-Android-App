Important notes:

###Regarding Employees
1-One employee account has been created, they must login with credentials:
-email: employee@employee.com
-password: employee

2- When you first create an employee(branch) account. The profile is blank.
The employee can go to the EDIT PROFILE/SERVICES to add the different info
or if already added, can change them. After each change, the changes must be
saved with the designated button.

3- In the edit page, services created by admin is listed and emplyee
can choose any service by name to add to the branch list of services.

---

####Regarding Admin:
1-Admin must login with credentials:
-email: admin@admin.com (must use this email only)
-password: admin1

If for some reasons the data is wiped out
-Admin must register with these credentials:
-account type: Branch (because admin is the main Branch)
-username: admin (or anything else)
-email: admin@admin.com (must use this email only)
-password: admin1 (or anything else more that 6 characters)

This is because of the FirebaseAuth setup and the app setup

- Always logout when you login with a user credential and are done with a user task, if not, you will be sent back in your session when you try to sign up new users

- Due to security reasons (google policy) the admin can't remove users from firebaseAuth but he can remove users from the realtime database, so when a deleted user trys to login, the program will display message that the account is deleted.
