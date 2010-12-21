# Loading and connecting multiple data sets

The HR example demonstrates how to use the basic CSV connector to quickly load various CSV files into GoodData and join them together.

## Departments Data Set

The departments data set is a minimalistic CSV that contains only a unique department identifier and a department name.

Run `gdi.sh` script with the `1-department.txt` file to create a new GoodData project and the _Department_ data set containing fields as described in the `department.xml` file. 

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\hr\1-department.txt
    Project id = 'nfh4zj3itxkxhevu4a22t4xgeg41d5h0' created.
    Data successfully loaded.


_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/hr/1-department.txt
    Project id = 'nfh4zj3itxkxhevu4a22t4xgeg41d5h0' created.
    Data successfully loaded.

### What's Inside

This data set will be populated with records from the `department.csv` CSV file. The structure of the `department.csv` file is described in the `department.xml` configuration file: one unique _connection point_ (with `ldmType` set to `CONNECTION_POINT`) and its corresponding label (`ldmType` set to `LABEL` and the `reference` set to the `name` of the primary field - `department`).

The model is described by the _logical data model (LDM) diagram_ below:

![Department LDM Diagram](http://developer.gooddata.com/images/gdcl/examples/hr/hr_1_department_ldm.png "Department LDM Diagram")

The data model is simple so far, containing one _Department_ box  that represents so called _attribute_ in GoodData jargon.

## Employee Data Set

The `employee.csv` data set contains employee names and departments.


Run `gdi.sh` with the `2-employee.txt` file to add the 'Employee' dataset, connect it to 'Deparment' and populate it with data from the `employee.csv` file.

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\hr\2-employee.txt
    Data successfully loaded.


_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/hr/2-employee.txt
    Data successfully loaded.


### What's Inside?

The `employee.csv` file includes four columns: `ID` which is described as a `CONNECTION_POINT` in the `employee.xml` configuration file, `FIRSTNAME` and `LASTNAME` described as `LABEL`s and `DEPARTMENT` which is a `REFERENCE` to the `Department` schema by the `schemaReference` element (compare to the `name` element at the begining of the `department.xml` configuration file).

The `REFERENCE` field always connects to the target data set using the value of its connection point. For example, _Sheri Nowmer_ from the `employee.csv` belongs to the department _d1_ which refers to _HQ General Management_.

The logical model diagram of these two connected data sets will be as follows:

![Employee and Department LDM Diagram](http://developer.gooddata.com/images/gdcl/examples/hr/hr_2_employee_ldm.png "Employee and Deparment LDM Diagram")
 
### Salary Data Set

The Salary data set will enhance our project with information about who received a payment and when.

It will be added, connected and populated by running the following command:

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\hr\3-salary.txt
    Data successfully loaded.


_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/hr/3-salary.txt
    Data successfully loaded.

       
### What's Inside?

As described in the `salary.xml` configuration file, this data set connnects to the `employee.csv` using the `EMPLOYEE_ID` field (`ldmType` is `REFERENCE`) and the date of payment is available in the `DATE` field (`ldmType` is `DATE`). Note the `DATE` field has the `schemaReference` set to `payment` which refers to the date dimension defined and created in the lines 9, 12 and 15 of the `3-salary.txt` script.

The complete logical data model can be illustrated by the following diagram:

![Full HR Diagram](http://developer.gooddata.com/images/gdcl/examples/hr/hr_3_salary_ldm.png "Full HR Diagram")

For the sake of simplicity, only some of the date related attributes are displayed.

## What Next?

Now you can log into the [GoodData user interface](https://secure.gooddata.com/) and select the _HR_ project. When you switch to the _Data_ section and click _Model_ in the left menu bar you can see a data model visualization similar to what's outlined above.

Then you can switch to the _Reports_ section and start building your first reports - how about showing cross-department salary trend as a stacked bar chart?
