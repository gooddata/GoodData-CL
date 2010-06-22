The HR example demonstrates how to use the basic CSV connector to quickly load various CSV files into GoodData and join them together.

1. Run `gdi.sh` script with the `1-department.txt` file to create a new GoodData project and the 'Department' dataset containing fields as described in the `department.xml` file. This dataset will be populated with records from the `department.csv` CSV file:

Windows:

        c:> bin\gdi.bat -u <username> -p <password> examples\hr\1-department.txt
        Project id = 'nfh4zj3itxkxhevu4a22t4xgeg41d5h0' created.
        Data successfully loaded.


Unix like OS (Linux, Mac OS X and others):

        $ ./bin/gdi.sh -u <username> -p <password> examples/hr/1-department.txt
        Project id = 'nfh4zj3itxkxhevu4a22t4xgeg41d5h0' created.
        Data successfully loaded.

        
2. Run `gdi.sh` with the `2-employee.txt` file to add the 'Employee' dataset, connect it to 'Deparment' and populate it with data from the `employee.csv` file.

Windows:

        c:> bin\gdi.bat -u <username> -p <password> examples\hr\2-employee.txt
        Data successfully loaded.


Unix like OS (Linux, Mac OS X and others):

        $ ./bin/gdi.sh -u <username> -p <password> examples/hr/2-employee.txt
        Data successfully loaded.

3. Add, connect and and populate the 'Salary' dataset.

Windows:

        c:> bin\gdi.bat -u <username> -p <password> examples\hr\3-salary.txt
        Data successfully loaded.


Unix like OS (Linux, Mac OS X and others):

        $ ./bin/gdi.sh -u <username> -p <password> examples/hr/3-salary.txt
        Data successfully loaded.

