# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped 
with data. The application contains information about all employees at a company. On application start-up, an in-memory 
Mongo database is bootstrapped with a serialized snapshot of the database. While the application runs, the data may be
accessed and mutated in the database without impacting the snapshot.

### How to Run
The application may be executed by running `gradlew bootRun`.

### How to Use
The following endpoints are available to use:
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
For all endpoints that require an "id" in the URL, this is the "employeeId" field.

## What to Implement
Clone or download the repository, do not fork it.

### Task 1
Create a new type, ReportingStructure, that has two properties: employee and numberOfReports.

For the field "numberOfReports", this should equal the total number of reports under a given employee. The number of 
reports is determined to be the number of directReports for an employee and all of their distinct reports. For example, 
given the following employee structure:
```
                    John Lennon
                /               \
         Paul McCartney         Ringo Starr
                               /        \
                          Pete Best     George Harrison
```
The numberOfReports for employee John Lennon (employeeId: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4. 

This new type should have a new REST endpoint created for it. This new endpoint should accept an employeeId and return 
the fully filled out ReportingStructure for the specified employeeId. The values should be computed on the fly and will 
not be persisted.

#### Solution
For my solution, I created a ReportingStructure type and made it a field of every employee instance.

Since this ReportingStructure was to be calculated on the fly with each call, I designed it so that calling the getReportingStructure from the employee would create a new reporting structure to be filled out for each request. 

The actual filling out of the reporting structure (navigating the tree and getting number of nodes) occurs in the EmployeeController class. This is because only this class should hold the employee service which was necessary for retrieving the actual employee objects connected to the employee ids under the direct reporting of each employee.

As for the implementation of determining the number of reports, I treated the problem as a tree graph and used recursion in order to retrieve the number of nodes under a given employee. This would include the root node, so before setting the numberOfReports, I would decrease the number by 1.

### Task 2
Create a new type, Compensation. A Compensation has the following fields: employee, salary, and effectiveDate. Create 
two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
Compensation from the persistence layer.

#### Solution
For my solution, I treated the compensation as a new table connected to the employee's via the foreign key (and primary key) of employee id. It made sense for me to normalize the database in this way for a couple of reasons. 

One was that it did not make sense for an employee to have an effectiveDate associated with it. Since only a compensation would have this field, I felt that it made sense to separate them.

Another reason for this decision is that I am more familiar with relational databases, and that this is a decision I would make in such an environment. Although as I have been looking into it more, I am a bit less confident in this choice. 

I am curious how somebody more versed in mongoDB would handle this problem and if my decision is as viable as I think it is.

## Delivery
Please upload your results to a publicly accessible Git repo. Free ones are provided by Github and Bitbucket.
